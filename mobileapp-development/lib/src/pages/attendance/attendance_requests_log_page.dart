import 'package:flutter/material.dart';
import 'package:seioffice/src/components/header.dart';
import 'package:seioffice/src/components/page.dart';
import 'package:seioffice/src/service/response/attendance.dart';
import 'package:seioffice/src/service/service.dart';

import '../../service/services/attendance_service.dart';
import '../dashboard/notification_page.dart';

class AttendanceRequestsLogPage extends StatefulWidget {
  const AttendanceRequestsLogPage({super.key});

  @override
  State<AttendanceRequestsLogPage> createState() =>
      _AttendanceRequestsLogPageState();
}

class _AttendanceRequestsLogPageState extends State<AttendanceRequestsLogPage> {
  final List<EmployeeCheckInApproval> data = [];
  int? lastId;
  Future<void>? currentRequest;
  bool reachedEnd = false;

  void refresh() {
    reachedEnd = false;
    currentRequest = null;
    lastId = null;
    data.clear();
  }

  Future<void> createNextRequest(int? lastId) async {
    final attendanceController = getService<AttendanceService>();
    final response = await attendanceController.getApprovalHistory(lastId);
    int? newLastId;
    for (var item in response.requests) {
      data.add(item);
      if (lastId == null || item.id < lastId) {
        newLastId = item.id;
      }
    }
    if (newLastId == null) {
      reachedEnd = true;
    } else {
      setState(() {
        this.lastId = newLastId;
      });
    }
    setState(() {
      currentRequest = null;
    });
  }

  @override
  Widget build(BuildContext context) {
    return StandardPage(
      appBar: Header(
        title: const Text('Meminta Persetujuan'),
        leading: IconButton(
          onPressed: () {
            Navigator.of(context).pop();
          },
          icon: const Icon(Icons.arrow_back),
        ),
      ),
      onRefresh: () async {
        setState(() {
          refresh();
        });
        await currentRequest;
      },
      padding: const EdgeInsets.only(left: 24, right: 24, bottom: 24, top: 12),
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
              return const SizedBox();
            },
          );
        }
        final item = data[index];
        return Container(
          margin: const EdgeInsets.only(bottom: 4),
          child: EmployeeCheckInApprovalNotification(
            request: item,
            onRefresh: () {
              setState(() {
                refresh();
              });
            },
          ),
        );
      },
    );
  }
}
