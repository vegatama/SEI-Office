import 'package:flutter/material.dart';
import 'package:seioffice/src/components/card.dart';
import 'package:seioffice/src/components/data.dart';
import 'package:seioffice/src/components/header.dart';
import 'package:seioffice/src/components/page.dart';
import 'package:seioffice/src/service/notification.dart';
import 'package:seioffice/src/service/request/notification.dart';
import 'package:seioffice/src/service/response/attendance.dart';
import 'package:seioffice/src/service/response/notification.dart';
import 'package:seioffice/src/service/service.dart';

import '../../service/services/app_service.dart';
import '../../service/services/notification_service.dart';
import '../../service/services/user_service.dart';
import '../attendance/employee_approval_page.dart';

class NotificationPage extends StatefulWidget {
  const NotificationPage({super.key});

  @override
  State<NotificationPage> createState() => _NotificationPageState();
}

class _NotificationPageState extends State<NotificationPage> {
  final List<NotificationDto> data = [];

  int? lastId;
  Future<void>? currentRequest;
  bool reachedEnd = false;

  @override
  void didChangeDependencies() {
    super.didChangeDependencies();
    final notificationController = getService<NotificationService>();
    notificationController.markAsReadNotificationCount();
  }

  void refresh() {
    reachedEnd = false;
    currentRequest = null;
    lastId = null;
    data.clear();
    final notificationController = getService<NotificationService>();
    notificationController.markAsReadNotificationCount();
  }

  Future<void> createNextRequest(int? lastId) async {
    final appController = getService<AppService>();
    final userController = getService<UserService>();
    final response = await appController.request(GetNotificationHistoryRequest(
        empCode: userController.currentUser.employee_code, before: lastId));
    for (var item in response.notifications) {
      data.add(item);
      if (lastId == null || item.id < lastId) {
        lastId = item.id;
      }
    }
    if (this.lastId == lastId) {
      reachedEnd = true;
    } else {
      this.lastId = lastId;
    }
    setState(() {
      currentRequest = null;
    });
  }

  @override
  Widget build(BuildContext context) {
    return RefreshableParent(
      onRefresh: () async {
        setState(() {
          refresh();
        });
        await currentRequest;
      },
      child: StandardPage(
        appBar: Header(
          title: Text('Notifikasi'),
          leading: IconButton(
            onPressed: () {
              Navigator.of(context).pop();
            },
            icon: const Icon(Icons.arrow_back),
          ),
        ),
        // padding: EdgeInsets.all(24),
        padding:
            const EdgeInsets.only(left: 24, right: 24, bottom: 24, top: 12),
        onRefresh: () async {
          setState(() {
            refresh();
          });
          await currentRequest;
        },
        itemCount: data.length + (reachedEnd ? 0 : 1),
        builder: (context, index) {
          if (index == data.length) {
            if (reachedEnd) {
              return const SizedBox();
            }
            currentRequest ??= createNextRequest(lastId);
            return FutureBuilder(
              future: currentRequest,
              builder: (context, snapshot) {
                return const Center(
                  child: CircularProgressIndicator(),
                );
              },
            );
          }
          final item = data[index];
          NotificationType? notificationType = notificationTypes[item.typeName];
          if (notificationType == null) {
            return const SizedBox();
          }
          return Container(
            margin: const EdgeInsets.only(bottom: 4),
            child: notificationType.build(context, DataList(item.data), item),
          );
        },
      ),
    );
  }
}

class EmployeeCheckInApprovalNotification extends StatelessWidget {
  final EmployeeCheckInApproval request;
  final VoidCallback onRefresh;

  const EmployeeCheckInApprovalNotification({
    required this.request,
    required this.onRefresh,
  });

  @override
  Widget build(BuildContext context) {
    return ActionCard(
      onPressed: () {
        Navigator.push(context, SEIPageRoute(builder: (context) {
          return EmployeeCheckInApprovalPage(
            data: request,
          );
        })).then((value) {
          onRefresh();
        });
        Feedback.forTap(context);
      },
      child: Text(request.checkOut
          ? '${request.employeeName} meminta persetujuan check-out'
          : '${request.employeeName} meminta persetujuan check-in'),
      trailing: Icon(Icons.arrow_forward),
      leading: ActionIcon(icon: Icons.verified_outlined),
    );
  }
}
