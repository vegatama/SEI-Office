import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'package:seioffice/src/components/attendance_card.dart';
import 'package:seioffice/src/components/card.dart';
import 'package:seioffice/src/components/header.dart';
import 'package:seioffice/src/components/page.dart';
import 'package:seioffice/src/service/localization.dart';
import 'package:seioffice/src/service/routes.dart';
import 'package:seioffice/src/service/service.dart';
import 'package:seioffice/src/theme/theme.dart';
import 'package:seioffice/src/util.dart';

import '../../service/services/attendance_service.dart';
import '../dashboard/notification_page.dart';

class AttendanceDashboardPage extends StatefulWidget {
  const AttendanceDashboardPage({Key? key}) : super(key: key);
  @override
  State<AttendanceDashboardPage> createState() =>
      _AttendanceDashboardPageState();
}

class _AttendanceDashboardPageState extends State<AttendanceDashboardPage> {
  late Future<AttendanceDashboardData> _future;

  @override
  void didChangeDependencies() {
    super.didChangeDependencies();
    _refresh();
  }

  void _refresh() {
    // from last 7 days only
    final from = DateTime.now().subtract(const Duration(days: 6));
    _future = getService<AttendanceService>().getDashboardData(from);
  }

  final GlobalKey _key = GlobalKey();

  @override
  Widget build(BuildContext context) {
    final theme = SEITheme.of(context);
    return FutureBuilder(
        key: ValueKey(_future.hashCode),
        future: _future,
        builder: (context, snapshot) {
          return StandardPage(
            key: _key,
            onRefresh: () async {
              setState(() {
                _refresh();
              });
              await _future;
            },
            appBar: Header(
              title: Text(intl.attendance),
              leading: IconButton(
                icon: const Icon(Icons.arrow_back),
                onPressed: () {
                  Navigator.of(context).pop();
                },
              ),
            ),
            padding: const EdgeInsets.only(left: 24, right: 24, top: 12),
            children: [
              Header(
                title: Text(intl.recapAttendanceDashboard),
                small: true,
              ),
              const SizedBox(height: 12),
              IntrinsicHeight(
                child: Row(
                  crossAxisAlignment: CrossAxisAlignment.stretch,
                  children: [
                    Expanded(
                      child: StatsCard(
                        title: Text(intl.totallyLate).placeholder(snapshot),
                        child: Text('60').placeholder(
                            snapshot,
                            () => Text(
                                snapshot.requireData.totalLate.toString())),
                        description:
                            Text(intl.minuteAttendance).placeholder(snapshot),
                        icon: const Icon(Icons.timelapse),
                        backgroundColor: theme.colorScheme
                            .generateSurfaceColor('total_late'.hashCode),
                      ),
                    ),
                    const SizedBox(width: 8),
                    Expanded(
                      child: StatsCard(
                        title:
                            Text(intl.totalShortWorkHour).placeholder(snapshot),
                        child: Text('60').placeholder(
                            snapshot,
                            () => Text(snapshot.requireData.totalShortWorkHour
                                .toString())),
                        description:
                            Text(intl.minuteAttendance).placeholder(snapshot),
                        icon: const Icon(Icons.work_history_outlined),
                        backgroundColor: theme.colorScheme
                            .generateSurfaceColor('short_work_hour'.hashCode),
                      ),
                    ),
                  ],
                ),
              ),
              const SizedBox(height: 8),
              StatsCard(
                title: Text(intl.totalForgotCheckOut).placeholder(snapshot),
                child: Text('60').placeholder(
                    snapshot,
                    () => Text(
                        snapshot.requireData.totalForgotCheckOut.toString())),
                description: Text(intl.times).placeholder(snapshot),
                icon: const Icon(Icons.access_alarm),
                backgroundColor: theme.colorScheme
                    .generateSurfaceColor('total_forget'.hashCode),
              ),
              const SizedBox(height: 8),
              ActionCard(
                child: Text('Check In/Check Out'),
                trailing: Icon(Icons.arrow_forward),
                onPressed: () {
                  // context.pushNamed(kPageLeaveNew);
                  context.pushNamed(kPageLiveAttendance).then((value) {
                    setState(() {
                      _refresh();
                    });
                  });
                },
              ),
              const SizedBox(height: 24),
              Header(
                title: Text('Meminta Persetujuan'),
                small: true,
              ),
              const SizedBox(height: 12),
              Column(
                crossAxisAlignment: CrossAxisAlignment.stretch,
                mainAxisSize: MainAxisSize.min,
                children: [
                  if (!snapshot.hasData)
                    for (int i = 0; i < 2; i++)
                      Container(
                        margin: const EdgeInsets.only(bottom: 4),
                        height: 56,
                      ).placeholder(snapshot),
                  if (snapshot.hasData)
                    for (var request in snapshot.requireData.requests)
                      Container(
                        margin: const EdgeInsets.only(bottom: 4),
                        child: EmployeeCheckInApprovalNotification(
                          request: request,
                          onRefresh: () {
                            setState(() {
                              _refresh();
                            });
                          },
                        ),
                      ),
                  ActionCard(
                    onPressed: () {
                      context.pushNamed(kPageAttendanceRequestsLog).then(
                        (value) {
                          setState(() {
                            _refresh();
                          });
                        },
                      );
                    },
                    trailing: const Icon(Icons.arrow_forward),
                    child: Text('Lihat Semua'),
                  ),
                ],
              ),
              const SizedBox(height: 24),
              Header(
                title: Text(intl.attendanceHistory),
                small: true,
              ),
              const SizedBox(height: 12),
              Column(
                  crossAxisAlignment: CrossAxisAlignment.stretch,
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    for (int i = 0;
                        i <
                            (snapshot.hasData
                                ? snapshot.requireData.history.length
                                : 7);
                        i++)
                      Container(
                        padding: const EdgeInsets.only(bottom: 4),
                        child: snapshot.hasData
                            ? AttendanceCard(
                                data: snapshot.requireData.history[i],
                                onRefresh: () {
                                  setState(() {
                                    _refresh();
                                  });
                                })
                            : PlaceholderAttendanceCard(),
                      ),
                    ActionCard(
                      onPressed: () {
                        context.pushNamed(kPageAttendanceHistory).then((value) {
                          setState(() {
                            _refresh();
                          });
                        });
                      },
                      trailing: const Icon(Icons.arrow_forward),
                      child: Text(intl.seeMore),
                    ),
                  ]),
              const SizedBox(height: 24),
            ],
          );
        });
  }
}
