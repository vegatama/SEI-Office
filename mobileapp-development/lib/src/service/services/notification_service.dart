import 'dart:async';
import 'dart:convert';
import 'dart:ui';

import 'package:awesome_notifications/awesome_notifications.dart';
import 'package:flutter/foundation.dart';
import 'package:http/http.dart' as http;
import 'package:seioffice/src/service/environment.dart';
import 'package:seioffice/src/service/request/notification.dart';
import 'package:seioffice/src/service/service.dart';
import 'package:seioffice/src/service/services/user_service.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:workmanager/workmanager.dart';

import '../api.dart';
import '../response/notification.dart';
import 'app_service.dart';

@pragma('vm:entry-point')
void _callbackDispatcher() {
  Workmanager().executeTask((task, inputData) async {
    try {
      if (task == 'notificationTask') {
        var prefs = await SharedPreferences.getInstance();
        // uses dedicated notification id for background notification
        // to avoid conflict with foreground notification
        int? lastNotificationId = prefs.getInt('lastBackgroundNotificationId');
        String? token = prefs.getString('token');
        String? employeeCode = prefs.getString('employee_code');
        if (employeeCode != null && token != null) {
          Future<NotificationListResponse> getResponse(
              APIRequest<NotificationListResponse> request) async {
            Uri uri = Uri.parse(kAPIBaseUrl + request.path);
            uri = uri.replace(queryParameters: request.queryParameters);
            var response = await http.get(uri, headers: {
              'Authorization': 'Bearer $token',
              'Content-Type': 'application/json',
            });
            if (response.statusCode == 200) {
              return NotificationListResponse.fromJson(
                  jsonDecode(response.body));
            } else {
              throw Exception('Failed to load notification');
            }
          }

          if (lastNotificationId == null) {
            NotificationListResponse response = await getResponse(
                GetNotificationHistoryRequest(empCode: employeeCode));
            for (var notification in response.notifications) {
              if (lastNotificationId == null ||
                  notification.id > lastNotificationId) {
                lastNotificationId = notification.id;
              }
            }
            if (lastNotificationId != null) {
              await prefs.setInt(
                  'lastBackgroundNotificationId', lastNotificationId);
            }
          } else {
            NotificationListResponse response = await getResponse(
                GetNotificationListRequest(
                    empCode: employeeCode, after: lastNotificationId));
            for (var notification in response.notifications) {
              await AwesomeNotifications().createNotification(
                content: NotificationContent(
                  id: notification.id,
                  channelKey: 'basic_channel',
                  body: notification.message,
                ),
              );
              if (lastNotificationId == null ||
                  notification.id > lastNotificationId) {
                lastNotificationId = notification.id;
              }
            }
            if (lastNotificationId != null) {
              await prefs.setInt(
                  'lastBackgroundNotificationId', lastNotificationId);
            }
          }
        }
      }
    } catch (e, s) {
      debugPrintStack(stackTrace: s, label: e.toString());
    }
    return true;
  });
}

class NotificationService extends Service {
  late Timer _timer;
  int? _lastNotificationId;
  final ValueNotifier<List<NotificationDto>> unreadNotifications =
      ValueNotifier([]);
  final List<NotificationDto> _notified = [];
  @override
  Future<void> init() async {
    var prefs = await SharedPreferences.getInstance();
    _lastNotificationId = prefs.getInt('lastNotificationId');
    await AwesomeNotifications().initialize(
      // 'resource://drawable/res_app_icon',
      null,
      [
        NotificationChannel(
          channelKey: 'basic_channel',
          channelName: 'Basic notifications',
          channelDescription: 'Notification channel for basic notifications',
          defaultColor: const Color(0xFF50B33F),
          ledColor: const Color(0xFF50B33F),
          playSound: true,
          enableVibration: true,
        ),
      ],
      debug: kDebugMode,
    );
    if (!await AwesomeNotifications().isNotificationAllowed()) {
      await AwesomeNotifications().requestPermissionToSendNotifications();
    }
    var userController = get<UserService>();
    await Workmanager().initialize(
      _callbackDispatcher,
      isInDebugMode: false,
    );
    // Runs every 15 minutes when the app is in the background
    await Workmanager().registerPeriodicTask(
      'notification-task',
      'notificationTask',
      // make sure it only runs when the app is in the background
      constraints: Constraints(
        networkType: NetworkType.connected,
        requiresBatteryNotLow: true,
        requiresCharging: false,
        requiresDeviceIdle: false,
        requiresStorageNotLow: false,
      ),
      inputData: <String, dynamic>{
        if (userController.user.value != null)
          'employee_code': userController.user.value!.employee_code,
      },
    );
    // Runs every 15 minutes when the app is in the foreground
    _timer = Timer.periodic(const Duration(seconds: 5), (timer) {
      _onTimer();
    });
  }

  @override
  void dispose() {
    _timer.cancel();
    super.dispose();
  }

  Future<void> markAsRead(int id) async {
    var userController = get<UserService>();
    var appController = get<AppService>();
    var response = await appController.request(NotificationMarkAsReadRequest(
        empCode: userController.currentUser.employee_code, id: id));
    print('Mark as read $id response: ${response.msg}');
  }

  void _onTimer() async {
    var userController = get<UserService>();
    var appController = get<AppService>();
    if (userController.user.value == null) return;
    if (_lastNotificationId == null) {
      NotificationListResponse response = await appController.request(
          GetNotificationHistoryRequest(
              empCode: userController.user.value!.employee_code));
      // update the _lastNotificationId for the first time
      for (var notification in response.notifications) {
        if (_lastNotificationId == null ||
            notification.id > _lastNotificationId!) {
          _lastNotificationId = notification.id;
        }
      }
      var prefs = await SharedPreferences.getInstance();
      if (_lastNotificationId != null) {
        await prefs.setInt('lastNotificationId', _lastNotificationId!);
      }
      _notified.clear();
    } else {
      NotificationListResponse response = await appController.request(
          GetNotificationListRequest(
              empCode: userController.user.value!.employee_code,
              after: _lastNotificationId));
      List<NotificationDto> notifications = response.notifications;
      unreadNotifications.value = notifications;
      for (var notification in notifications) {
        if (_notified.contains(notification)) continue;
        _notified.add(notification);
        await AwesomeNotifications().createNotification(
          content: NotificationContent(
            id: notification.id,
            channelKey: 'basic_channel',
            body: notification.message,
          ),
        );
      }
    }
  }

  Future<void> markAsReadNotificationCount() async {
    bool changed = false;
    for (var notification in unreadNotifications.value) {
      if (_lastNotificationId == null ||
          notification.id > _lastNotificationId!) {
        _lastNotificationId = notification.id;
        changed = true;
      }
    }
    _notified.clear();
    if (changed) {
      var prefs = await SharedPreferences.getInstance();
      await prefs.setInt('lastNotificationId', _lastNotificationId!);
    }
  }
}
