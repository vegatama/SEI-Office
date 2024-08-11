import 'package:flutter/material.dart';
import 'package:seioffice/src/components/card.dart';
import 'package:seioffice/src/components/data.dart';
import 'package:seioffice/src/components/page.dart';
import 'package:seioffice/src/service/services/notification_service.dart';
import 'package:seioffice/src/service/localization.dart';
import 'package:seioffice/src/service/response/notification.dart';
import 'package:seioffice/src/service/service.dart';
import 'package:seioffice/src/theme/theme.dart';

import 'notifications.dart';

class DataList {
  final List<dynamic> data;

  DataList(this.data);

  String? getString(int index) {
    return data[index]?.toString();
  }

  int? getInt(int index) {
    var value = data[index];
    return value == null ? null : int.tryParse(value.toString());
  }

  double? getDouble(int index) {
    var value = data[index];
    return value == null ? null : double.tryParse(value.toString());
  }

  bool? getBool(int index) {
    return data[index]?.toString() == 'true';
  }

  DateTime? getDateTime(int index) {
    var value = data[index];
    return value == null ? null : DateTime.parse(value as String);
  }

  dynamic operator [](int index) => data[index];

  int get length => data.length;
}

class NotificationProperty<T> {
  final int index;

  const NotificationProperty(this.index);
}

abstract class NotificationType {
  final String Function(BuildContext context, DataList data) message;

  NotificationType(this.message);

  String format(DataList data, BuildContext context) {
    String message = this.message(context, data);
    for (int i = 0; i < data.length; i++) {
      message = message.replaceAll('{$i}', data[i].toString());
    }
    return message;
  }

  Widget? buildIcon(BuildContext context) {
    return null;
  }

  Widget? buildTrailing(BuildContext context) {
    return null;
  }

  Widget build(
      BuildContext context, DataList data, NotificationDto notification) {
    final theme = SEITheme.of(context);
    return NotificationCard(
      backgroundColor: notification.isRead ? theme.colorScheme.muted : null,
      foregroundColor:
          notification.isRead ? theme.colorScheme.mutedForeground : null,
      trailing: buildTrailing(context),
      subtitle: Text(context.intl.formatDate(notification.timestamp)),
      icon: buildIcon(context),
      child: Text(
        format(data, context),
      ),
    );
  }
}

mixin InteractableNotificationType on NotificationType {
  void onTap(BuildContext context, DataList data);

  @override
  Widget build(
      BuildContext context, DataList data, NotificationDto notification) {
    return GestureDetector(
      onTap: () {
        var controller = context.getService<NotificationService>();
        showLoading(context, controller.markAsRead(notification.id))
            .then((value) async {
          await RefreshableParent.dispatchRefresh(context);
          if (context.mounted) {
            onTap(context, data);
          }
        });
      },
      child: super.build(context, data, notification),
    );
  }

  @override
  Widget? buildTrailing(BuildContext context) {
    return const Icon(Icons.arrow_forward);
  }
}

final Map<String, NotificationType> notificationTypes = {
  'AttendanceSwipeRequest': NotificationAttendanceSwipeRequest(),
  'AttendanceSwipeResponseAccepted':
      NotificationAttendanceSwipeResponseAccepted(),
  'AttendanceSwipeResponseRejected':
      NotificationAttendanceSwipeResponseRejected(),
  'IzinCutiRequest': NotificationIzinCutiRequest(),
  'IzinCutiResponseAccepted': NotificationIzinCutiResponseAccepted(),
  'IzinCutiResponseRejected': NotificationIzinCutiResponseRejected(),
};
