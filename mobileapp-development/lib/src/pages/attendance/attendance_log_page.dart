import 'package:flutter/material.dart';
import 'package:seioffice/src/components/attendance_card.dart';
import 'package:seioffice/src/components/divider.dart';
import 'package:seioffice/src/components/header.dart';
import 'package:seioffice/src/components/page.dart';
import 'package:seioffice/src/pages/attendance/attendance_log_filter_page.dart';
import 'package:seioffice/src/service/localization.dart';
import 'package:seioffice/src/service/response/attendance.dart';
import 'package:seioffice/src/service/service.dart';
import 'package:seioffice/src/util.dart';

import '../../service/services/attendance_service.dart';

class AttendanceLogPage extends StatefulWidget {
  const AttendanceLogPage({super.key});

  @override
  State<AttendanceLogPage> createState() => _AttendanceLogPageState();
}

class _AttendanceLogPageState extends State<AttendanceLogPage> {
  AttendanceFilterData filterData = const AttendanceFilterData();
  final List<MonthData> data = [];
  DateTime? lastDate;

  Future<void>? currentRequest;
  bool reachedEnd = false;

  void refresh() {
    reachedEnd = false;
    currentRequest = null;
    lastDate = null;
    data.clear();
  }

  Future<void> createNextRequest(DateTime? lastDate) async {
    if (filterData.statuses.isEmpty) {
      reachedEnd = true;
      return;
    }
    if (lastDate != null) {
      if (filterData.startDate != null &&
          lastDate.isBefore(filterData.startDate!)) {
        reachedEnd = true;
        return;
      }
    } else {
      if (filterData.endDate != null) {
        lastDate = filterData.endDate;
      }
    }
    final attendanceController = getService<AttendanceService>();
    final response = await attendanceController.getHistory(
      to: lastDate,
    );
    DateTime? newLastDate;
    int count = 0;
    response.data.sort((a, b) {
      DateTime aDate = DateTime(a.year, a.month, a.day);
      DateTime bDate = DateTime(b.year, b.month, b.day);
      return bDate.compareTo(aDate);
    });
    for (var item in response.data) {
      final asDate = DateTime(item.year, item.month, item.day);
      if (filterData.startDate != null &&
          asDate.isBefore(filterData.startDate!)) {
        continue;
      }
      AttendanceFilterStatus? status = parseAttendanceStatus(item.status);
      if (status == null) {
        if (item.firstCheckIn == null) {
          status = AttendanceFilterStatus.absent;
        } else {
          status = AttendanceFilterStatus.present;
        }
      }
      if (!filterData.statuses.contains(status)) {
        continue;
      }
      final monthData = data.lastWhere(
        (element) => element.month == item.month && element.year == item.year,
        orElse: () {
          final monthData = MonthData(item.month, item.year);
          data.add(monthData);
          return monthData;
        },
      );
      monthData.data.add(item);
      // sort the data by day, month, and year descending
      monthData.data.sort((a, b) {
        DateTime aDate = DateTime(a.year, a.month, a.day);
        DateTime bDate = DateTime(b.year, b.month, b.day);
        return bDate.compareTo(aDate);
      });

      if (newLastDate == null || asDate.isBefore(newLastDate)) {
        newLastDate = asDate;
      }
      count++;
    }
    if (count == 0) {
      reachedEnd = true;
    }
    setState(() {
      currentRequest = null;
      this.lastDate = newLastDate?.subtract(const Duration(days: 1));
    });
  }

  @override
  Widget build(BuildContext context) {
    return StandardPage(
      appBar: Header(
        title: Text(intl.attendanceHistoryTitle),
        actions: [
          IconButton(
            onPressed: () {
              Navigator.push(context, SEIPageRoute(builder: (context) {
                return AttendanceLogFilterPage.fromFilterData(filterData);
              })).then((value) {
                if (value is AttendanceFilterData) {
                  setState(() {
                    filterData = value;
                    refresh();
                  });
                }
              });
            },
            icon: Icon(Icons.filter_alt),
          ),
        ],
        leading: IconButton(
          onPressed: () {
            Navigator.pop(context);
          },
          icon: Icon(Icons.arrow_back),
        ),
      ),
      onRefresh: () async {
        setState(() {
          refresh();
        });
      },
      padding: const EdgeInsets.only(left: 24, right: 24, bottom: 24),
      itemCount: data.length + (reachedEnd ? 0 : 1),
      builder: (context, index) {
        if (index == data.length) {
          if (reachedEnd) {
            return const SizedBox();
          }
          currentRequest ??= createNextRequest(lastDate);
          return FutureBuilder(
            future: currentRequest,
            builder: (context, snapshot) {
              if (snapshot.connectionState != ConnectionState.done) {
                return Column(
                    mainAxisSize: MainAxisSize.min,
                    crossAxisAlignment: CrossAxisAlignment.stretch,
                    children: [
                      for (int i = 0; i < 7; i++)
                        const PlaceholderAttendanceCard(),
                    ]
                        .joinSeparator(
                          const SizedBox(height: 8),
                        )
                        .toList());
              }
              return const SizedBox();
            },
          );
        }
        if (index < 0 || index >= data.length) {
          return const SizedBox();
        }
        final monthData = data[index];
        return buildMonthData(monthData, monthData.data);
      },
    );
  }

  Widget buildMonthData(MonthData monthData, List<AttendanceData> data) {
    bool thisMonth = DateTime.now().month == monthData.month;
    return Container(
      padding: EdgeInsets.only(top: 18),
      child: Column(
        mainAxisSize: MainAxisSize.min,
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: [
          ContentDivider(
            child: Text(thisMonth
                ? intl.thisMonth
                : '${intl.getMonth(monthData.month)} ${monthData.year}'),
          ),
          const SizedBox(height: 4),
          for (var item in data)
            AttendanceCard(
              data: item,
              onRefresh: refresh,
            ),
        ].joinSeparator(const SizedBox(height: 8)).toList(),
      ),
    );
  }
}

class MonthData {
  final int month;
  final int year;

  final List<AttendanceData> data = [];

  MonthData(this.month, this.year);
}
