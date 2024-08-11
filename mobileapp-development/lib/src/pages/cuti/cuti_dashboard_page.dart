import 'dart:async';

import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'package:seioffice/src/components/animation.dart';
import 'package:seioffice/src/components/card.dart';
import 'package:seioffice/src/components/header.dart';
import 'package:seioffice/src/components/page.dart';
import 'package:seioffice/src/components/shimmer.dart';
import 'package:seioffice/src/pages/cuti/cuti_new_page.dart';
import 'package:seioffice/src/service/localization.dart';
import 'package:seioffice/src/service/request/cuti.dart';
import 'package:seioffice/src/service/response/cuti.dart';
import 'package:seioffice/src/service/routes.dart';
import 'package:seioffice/src/service/service.dart';

import '../../service/services/app_service.dart';
import '../../service/services/user_service.dart';
import '../../theme/theme.dart';
import 'cuti_history_page.dart';

class CutiDashboardPage extends StatefulWidget {
  const CutiDashboardPage({Key? key}) : super(key: key);

  @override
  State<CutiDashboardPage> createState() => _CutiDashboardPageState();
}

class _CutiDashboardPageState extends State<CutiDashboardPage> {
  late Future<CutiDashboardData> future;

  @override
  void didChangeDependencies() {
    super.didChangeDependencies();
    future = CutiDashboardData.fetch(context).onError((error, stackTrace) {
      print('Error: $error');
      print(stackTrace);
      throw error!;
    });
  }

  @override
  Widget build(BuildContext context) {
    final theme = SEITheme.of(context);
    return FutureBuilder(
        future: future,
        key: ValueKey(future.hashCode),
        builder: (context, snapshot) {
          return StandardPage(
            appBar: Header(
              title: Text(intl.leavesTitle),
              leading: IconButton(
                onPressed: () {
                  Navigator.of(context).pop();
                },
                icon: const Icon(Icons.arrow_back),
              ),
            ),
            onRefresh: () async {
              Future<CutiDashboardData> newFuture =
                  CutiDashboardData.fetch(context);
              setState(() {
                future = newFuture;
              });
              await newFuture;
            },
            padding:
                const EdgeInsets.only(bottom: 24, left: 24, right: 24, top: 12),
            children: [
              Header(
                title: Text(intl.recapPermit),
                small: true,
                actions: [
                  IconButton(
                    onPressed: () {
                      context.pushNamed(kPageLeaveQuota);
                    },
                    icon: Icon(Icons.arrow_forward),
                  ),
                ],
              ),
              const SizedBox(height: 8),
              IntrinsicHeight(
                child: Row(
                  crossAxisAlignment: CrossAxisAlignment.stretch,
                  children: [
                    Expanded(
                      child: StatsCard(
                        title: ContentPlaceholder(
                          child: Text(intl.remainPermission),
                          showContent: snapshot.hasData,
                        ),
                        child: ContentPlaceholder(
                            showContent: snapshot.hasData,
                            child: Text(snapshot.hasData
                                ? snapshot.data!.accumulatedTimeOffMinutes
                                    .toString()
                                : '55')),
                        description: ContentPlaceholder(
                          child: Text(intl.remainMinute),
                          showContent: snapshot.hasData,
                        ),
                        backgroundColor: theme.colorScheme['sei_2'.hashCode],
                        icon: Icon(
                          Icons.timer_outlined,
                        ),
                      ),
                    ),
                    const SizedBox(width: 8),
                    Expanded(
                      child: StatsCard(
                        title: ContentPlaceholder(
                          child: Text(intl.remainDaysOff),
                          showContent: snapshot.hasData,
                        ),
                        child: ContentPlaceholder(
                            showContent: snapshot.hasData,
                            child: Text(snapshot.hasData
                                ? snapshot.data!.remainingTimeOffDays.toString()
                                : '55')),
                        description: ContentPlaceholder(
                          child: Text(intl.remainDay),
                          showContent: snapshot.hasData,
                        ),
                        backgroundColor: theme.colorScheme['sei_7'.hashCode],
                        icon: Icon(
                          Icons.holiday_village,
                        ),
                      ),
                    ),
                  ],
                ),
              ),
              // if (snapshot.hasData &&
              //     snapshot.requireData.activeTimeOff != null)
              //   const SizedBox(height: 8),
              ConstantRebuild(
                builder: (context) {
                  if (snapshot.hasData &&
                      snapshot.requireData.activeTimeOff != null) {
                    var progress = snapshot.requireData.activeCutiProgress!;
                    if (progress <= 0) {
                      return const SizedBox();
                    }
                    return Container(
                      margin: const EdgeInsets.only(top: 8),
                      child: GestureDetector(
                        onTap: () {
                          context.pushNamed(kPageLeaveDetail, queryParameters: {
                            'id': snapshot.requireData.activeTimeOff!.id
                                .toString()
                          });
                          Feedback.forTap(context);
                        },
                        child: ProgressiveCard(
                          title: snapshot.requireData.activeTimeOff!.startDate
                                  .isAfter(DateTime.now())
                              ? Text('Cuti Mendatang')
                              : Text(intl.activeLeave),
                          child: Text(snapshot.requireData
                              .getActiveCutiRemainingDays(context)!
                              .toString()),
                          description: Text(intl.remainingLeave),
                          progress: progress,
                          trailing: Icon(Icons.arrow_forward_ios),
                        ),
                      ),
                    );
                  }
                  return const SizedBox();
                },
              ),
              const SizedBox(height: 8),
              ActionCard(
                child: Text(intl.applyPermissionLeave),
                trailing: Icon(Icons.add),
                onPressed: () {
                  // context.pushNamed(kPageLeaveNew);
                  showLoading(context, CutiMasterData.fetch(context))
                      .then((value) {
                    Navigator.push(context, SEIPageRoute(builder: (context) {
                      return CutiNewPage(
                        data: value,
                      );
                    })).then((value) {
                      setState(() {
                        future = CutiDashboardData.fetch(context);
                      });
                    });
                  });
                },
              ),
              const SizedBox(height: 24),
              Header(
                title: Text('Menunggu Persetujuan'),
                small: true,
              ),
              const SizedBox(height: 8),
              Column(
                crossAxisAlignment: CrossAxisAlignment.stretch,
                mainAxisSize: MainAxisSize.min,
                children: [
                  if (snapshot.hasData)
                    for (var reviewRequest
                        in snapshot.requireData.reviewedTimeOff)
                      Column(
                        children: [
                          IzinCutiRequestDataWidget(
                              data: reviewRequest,
                              onRefresh: () {
                                setState(() {
                                  future = CutiDashboardData.fetch(context);
                                });
                              }),
                          const SizedBox(height: 4),
                        ],
                      ),
                  if (!snapshot.hasData)
                    for (var i = 0; i < 2; i++)
                      Column(
                        children: [
                          ActionCard(
                            title: ContentPlaceholder(
                              child: Text(i % 2 != 0
                                  ? intl.applicationListReject
                                  : intl.applicationListNew),
                            ),
                            child:
                                ContentPlaceholder(child: Text('Fauzan Azmi')),
                            backgroundColor: i % 2 != 0
                                ? theme.colorScheme.destructive
                                : null,
                            leading: ContentPlaceholder(
                              child: ActionIcon(
                                icon: i % 2 != 0 ? Icons.close : Icons.check,
                              ),
                            ),
                          ),
                          const SizedBox(height: 4),
                        ],
                      ),
                  ActionCard(
                    child: Text(intl.seeAll),
                    onPressed: () {
                      context.pushNamed(kPageLeaveReview).then((value) {
                        setState(() {
                          future = CutiDashboardData.fetch(context);
                        });
                      });
                    },
                    trailing: Icon(Icons.arrow_forward),
                  ),
                ],
              ),
              const SizedBox(height: 24),
              Header(
                title: Text('Riwayat Izin/Cuti'),
                small: true,
              ),
              const SizedBox(height: 8),
              Column(
                crossAxisAlignment: CrossAxisAlignment.stretch,
                mainAxisSize: MainAxisSize.min,
                children: [
                  if (snapshot.hasData)
                    for (var history in snapshot.requireData.timeOffHistory)
                      Column(
                        children: [
                          IzinCutiDataWidget(
                              data: history,
                              onRefresh: () {
                                setState(() {
                                  future = CutiDashboardData.fetch(context);
                                });
                              }),
                          const SizedBox(height: 4),
                        ],
                      ),
                  if (!snapshot.hasData)
                    for (var i = 0; i < 5; i++)
                      Column(
                        children: [
                          ActionCard(
                            title: ContentPlaceholder(
                              child: Text('Lorem Ipsum'),
                            ),
                            child: ContentPlaceholder(
                                child: Text('Lorem Ipsum dolor sit amet')),
                            backgroundColor: i % 2 != 0
                                ? theme.colorScheme.destructive
                                : null,
                            leading: ContentPlaceholder(
                              child: ActionIcon(
                                icon: Icons.check_circle,
                              ),
                            ),
                          ),
                          const SizedBox(height: 4),
                        ],
                      ),
                  ActionCard(
                    child: Text(intl.seeAll),
                    onPressed: () {
                      context.pushNamed(kPageLeaveHistory).then((value) {
                        setState(() {
                          future = CutiDashboardData.fetch(context);
                        });
                      });
                    },
                    trailing: Icon(Icons.arrow_forward),
                  ),
                ],
              ),
            ],
          );
        });
  }
}

class CutiDashboardData {
  final int remainingTimeOffDays;
  final int accumulatedTimeOffMinutes;
  final IzinCutiData? activeTimeOff;
  final List<IzinCutiData> timeOffHistory;
  final List<IzinCutiRequestData> reviewedTimeOff;

  CutiDashboardData({
    required this.remainingTimeOffDays,
    required this.accumulatedTimeOffMinutes,
    required this.activeTimeOff,
    required this.timeOffHistory,
    required this.reviewedTimeOff,
  });

  String? getActiveCutiRemainingDays(BuildContext context) {
    if (activeTimeOff == null) {
      return null;
    }
    DateTime now = DateTime.now();
    if (activeTimeOff!.startDate.isAfter(now)) {
      // use start date and end date
      return context.intl.formatTime(
          activeTimeOff!.startDate.difference(activeTimeOff!.endDate));
    }
    if (activeTimeOff!.endDate.isAfter(now)) {
      return context.intl.formatTime(activeTimeOff!.endDate.difference(now));
    }
    return context.intl.formatTime(Duration.zero);
  }

  double? get activeCutiProgress {
    if (activeTimeOff == null) {
      return null;
    }
    DateTime now = DateTime.now();
    if (activeTimeOff!.startDate.isAfter(now)) {
      // use start date and end date
      return 1;
    }
    if (activeTimeOff!.endDate.isAfter(now)) {
      Duration total =
          activeTimeOff!.endDate.difference(activeTimeOff!.startDate);
      Duration remaining = activeTimeOff!.endDate.difference(now);
      return (remaining.inMilliseconds / total.inMilliseconds).clamp(0, 1);
    }
    return 0;
  }

  static Future<CutiDashboardData> fetch(BuildContext context) async {
    final appController = context.getService<AppService>();
    final userController = context.getService<UserService>();
    var dashboard = await appController.request(IzinCutiDashboardRequest(
        empcode: userController.currentUser.employee_code));
    if (dashboard.message != 'SUCCESS') {
      throw Exception(dashboard.message);
    }
    var timeOffHistory = await appController.request(
      GetIzinCutiListRequest(
        empcode: userController.currentUser.employee_code,
      ),
    );
    if (timeOffHistory.message != 'SUCCESS') {
      throw Exception(timeOffHistory.message);
    }
    var reviewedTimeOff = await appController.request(
      GetReviewedIzinCutiListRequest(
        empcode: userController.currentUser.employee_code,
        status: [
          // IzinCutiStatus.APPROVED,
          // IzinCutiStatus.REJECTED,
          IzinCutiStatus.PENDING,
        ],
      ),
    );
    if (reviewedTimeOff.message != 'SUCCESS') {
      throw Exception(reviewedTimeOff.message);
    }
    return CutiDashboardData(
      remainingTimeOffDays: dashboard.sisaCuti,
      accumulatedTimeOffMinutes: dashboard.akumulasiIzin,
      activeTimeOff: dashboard.cutiAktif,
      timeOffHistory: timeOffHistory.data.take(5).toList(),
      reviewedTimeOff: reviewedTimeOff.data.take(2).toList(),
    );
  }
}
