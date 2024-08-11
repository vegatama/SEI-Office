import 'dart:async';

import 'package:badges/badges.dart' as badges;
import 'package:flag/flag_enum.dart';
import 'package:flag/flag_widget.dart';
import 'package:flutter/gestures.dart';
import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'package:seioffice/src/components/animation.dart';
import 'package:seioffice/src/components/avatar.dart';
import 'package:seioffice/src/components/button.dart';
import 'package:seioffice/src/components/header.dart';
import 'package:seioffice/src/components/page.dart';
import 'package:seioffice/src/components/shimmer.dart';
import 'package:seioffice/src/pages/dashboard/service_list_page.dart';
import 'package:seioffice/src/service/localization.dart';
import 'package:seioffice/src/service/request/employee.dart';
import 'package:seioffice/src/service/routes.dart';
import 'package:seioffice/src/service/service.dart';
import 'package:seioffice/src/service/services/app_service.dart';
import 'package:seioffice/src/service/services/notification_service.dart';
import 'package:seioffice/src/service/services/user_service.dart';
import 'package:seioffice/src/theme/theme.dart';
import 'package:seioffice/src/util.dart';

import '../../service/feature.dart';

class DashboardPage extends StatefulWidget {
  const DashboardPage({super.key});

  @override
  State<DashboardPage> createState() => _DashboardPageState();
}

class _DashboardPageState extends State<DashboardPage> {
  Future<DashboardData>? employeeDetail;
  final ScrollController _scrollController = ScrollController();
  String get greetingBasedOnTime {
    final hour = DateTime.now().hour;
    if (hour < 12) {
      return intl.greetingMorning;
    } else if (hour < 15) {
      return intl.greetingAfternoon;
    } else if (hour < 18) {
      return intl.greetingEvening;
    } else {
      return intl.greetingNight;
    }
  }

  @override
  void didChangeDependencies() {
    super.didChangeDependencies();
    employeeDetail ??= DashboardData.fetch(context).notifyError(context);
  }

  Future<void> _onRefresh() async {
    employeeDetail = DashboardData.fetch(context).notifyError(context);
    if (!mounted) return;
    setState(() {});
  }

  @override
  Widget build(BuildContext context) {
    final appController = getService<AppService>();
    final userController = getService<UserService>();
    final notificationController = getService<NotificationService>();
    final theme = SEITheme.of(context);
    final mediaPadding = MediaQuery.paddingOf(context);
    return FutureBuilder(
        future: employeeDetail,
        builder: (context, snapshot) {
          return RefreshIndicator(
            onRefresh: _onRefresh,
            child: ScrollConfiguration(
              behavior: ScrollConfiguration.of(context).copyWith(
                dragDevices: {
                  PointerDeviceKind.touch,
                  PointerDeviceKind.mouse,
                },
              ),
              child: Scaffold(
                backgroundColor: theme.colorScheme.secondary,
                body: Stack(
                  children: [
                    ListView(
                      clipBehavior: Clip.none,
                      padding: EdgeInsets.only(bottom: 32),
                      children: [
                        Stack(
                          clipBehavior: Clip.none,
                          children: [
                            // end of the visual patch
                            Container(
                              height: 300 + mediaPadding.top,
                              child: Stack(
                                children: [
                                  Positioned.fill(
                                    child: Hero(
                                      tag: #background,
                                      createRectTween: (begin, end) {
                                        return MaterialRectCenterArcTween(
                                            begin: begin, end: end);
                                      },
                                      child: ClipRRect(
                                        borderRadius: const BorderRadius.only(
                                          bottomLeft: Radius.circular(32),
                                          bottomRight: Radius.circular(32),
                                        ),
                                        child: Container(
                                          decoration: BoxDecoration(
                                            color: theme.colorScheme.primary,
                                          ),
                                        ),
                                      ),
                                    ),
                                  ),
                                  const Positioned(
                                    top: -40,
                                    right: -50,
                                    child: Hero(
                                      tag: #backgroundLogo,
                                      child: AnimatedSEILogoBackground(),
                                    ),
                                  ),
                                  Positioned.fill(
                                    top: mediaPadding.top,
                                    child: Padding(
                                      padding: const EdgeInsets.only(
                                        top: 24,
                                        left: 24,
                                        right: 24,
                                        bottom: 32,
                                      ),
                                      child: Column(
                                        crossAxisAlignment:
                                            CrossAxisAlignment.stretch,
                                        mainAxisSize: MainAxisSize.min,
                                        children: [
                                          Row(
                                            mainAxisAlignment:
                                                MainAxisAlignment.end,
                                            children: [
                                              ValueListenableBuilder(
                                                valueListenable:
                                                    notificationController
                                                        .unreadNotifications,
                                                builder:
                                                    (context, value, child) {
                                                  return badges.Badge(
                                                    showBadge: value.isNotEmpty,
                                                    badgeContent: Text(
                                                      value.length.toString(),
                                                      style: theme.typography.xs
                                                          .copyWith(
                                                        color: theme.colorScheme
                                                            .primaryForeground,
                                                        fontWeight:
                                                            FontWeight.bold,
                                                      ),
                                                    ),
                                                    position: badges
                                                        .BadgePosition.topEnd(
                                                      top: -6,
                                                      end: 0,
                                                    ),
                                                    child: IconButton(
                                                      iconSize: 32,
                                                      icon: Icon(
                                                        Icons.notifications,
                                                        color: theme.colorScheme
                                                            .primaryForeground,
                                                      ),
                                                      onPressed: () {
                                                        context.pushNamed(
                                                            kPageNotification);
                                                      },
                                                    ),
                                                  );
                                                },
                                              ),
                                            ],
                                          ),
                                          Spacer(),
                                          GestureDetector(
                                            onTap: () {
                                              context.pushNamed(kPageProfile);
                                            },
                                            child: Column(
                                              mainAxisSize: MainAxisSize.min,
                                              crossAxisAlignment:
                                                  CrossAxisAlignment.start,
                                              children: [
                                                Hero(
                                                  tag: #employeeAvatar,
                                                  child: EmployeeCircleAvatar(
                                                    radius: 48,
                                                    showContent: true,
                                                    url: userController
                                                        .currentUser.avatar,
                                                  ),
                                                ),
                                                const SizedBox(height: 16),
                                                ContentPlaceholder(
                                                  showContent: snapshot.hasData,
                                                  child: Row(
                                                    mainAxisSize:
                                                        MainAxisSize.min,
                                                    children: [
                                                      Text(
                                                        '$greetingBasedOnTime, ',
                                                        style: theme
                                                            .typography.xxl
                                                            .copyWith(
                                                          color: theme
                                                              .colorScheme
                                                              .primaryForeground,
                                                        ),
                                                      ),
                                                      Text(
                                                        snapshot.hasData
                                                            ? snapshot
                                                                .requireData
                                                                .employeeName
                                                            : 'USERNAME',
                                                        style: theme
                                                            .typography.xxl
                                                            .copyWith(
                                                          color: theme
                                                              .colorScheme
                                                              .primaryForeground,
                                                          fontWeight:
                                                              fontSemiBold,
                                                        ),
                                                      ),
                                                    ],
                                                  ),
                                                ),
                                                const SizedBox(height: 2),
                                                ContentPlaceholder(
                                                  showContent: snapshot.hasData,
                                                  child: Text(
                                                    // 'Software Engineer',
                                                    snapshot.hasData
                                                        ? snapshot.requireData
                                                            .employeeJobTitle
                                                        : 'JABATAN',
                                                    style: theme.typography.md
                                                        .copyWith(
                                                      color: theme.colorScheme
                                                          .primaryForeground
                                                          .withOpacity(0.7),
                                                    ),
                                                  ),
                                                ),
                                              ],
                                            ),
                                          ),
                                        ],
                                      ),
                                    ),
                                  ),
                                ],
                              ),
                            ),
                          ],
                        ),
                        const SizedBox(height: 32),
                        Padding(
                          padding: const EdgeInsets.symmetric(horizontal: 24),
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.stretch,
                            children: [
                              Header(
                                title: Text(intl.serviceTitle),
                                actions: [
                                  IconButton(
                                    onPressed: () {
                                      Navigator.push(
                                        context,
                                        SEIPageRoute(
                                          builder: (context) {
                                            return ServiceListPage(
                                              accessMap: userController
                                                  .currentUser.accessMap,
                                            );
                                          },
                                        ),
                                      );
                                    },
                                    icon: Icon(Icons.arrow_forward),
                                  ),
                                ],
                              ),
                              const SizedBox(height: 8),
                              ...kGroupedFeatures
                                  .map((featureList) {
                                    return IntrinsicHeight(
                                      child: Row(
                                        crossAxisAlignment:
                                            CrossAxisAlignment.stretch,
                                        children: featureList
                                            .map(
                                              (feature) => Expanded(
                                                child: PrimaryIconButton(
                                                  icon: Icon(feature.icon),
                                                  label: Text(
                                                      feature.name(context)),
                                                  color: theme.colorScheme[
                                                      feature.colorHash],
                                                  onPressed: () {
                                                    context.pushNamed(
                                                      feature.routeName,
                                                    );
                                                  },
                                                ),
                                              ),
                                            )
                                            .toList()
                                            .joinSeparator(
                                              const SizedBox(width: 8),
                                            )
                                            .toList(),
                                      ),
                                    );
                                  })
                                  .toList()
                                  .joinSeparator(
                                    const SizedBox(height: 8),
                                  ),
                              // IntrinsicHeight(
                              //   child: Row(
                              //     crossAxisAlignment: CrossAxisAlignment.stretch,
                              //     children: [
                              //       Expanded(
                              //         child: PrimaryIconButton(
                              //           icon: Icon(Icons.event_available),
                              //           label: Text(intl.serviceAttendance),
                              //           color: theme.colorScheme[1],
                              //           onPressed: userController
                              //                   .currentUser.accessMap.absensi
                              //               ? () {
                              //                   // Navigator.pushNamed(
                              //                   //     context, kPageAttendance);
                              //                   context
                              //                       .pushNamed(kPageAttendance)
                              //                       .then((value) => _onRefresh());
                              //                 }
                              //               : null,
                              //         ),
                              //       ),
                              //       const SizedBox(width: 8),
                              //       Expanded(
                              //         child: PrimaryIconButton(
                              //           icon: Icon(Icons.list_alt),
                              //           label: Text(intl.serviceEvents),
                              //           color: theme.colorScheme[100],
                              //           onPressed: () {},
                              //         ),
                              //       ),
                              //       const SizedBox(width: 8),
                              //       Expanded(
                              //         child: PrimaryIconButton(
                              //           icon: Icon(Icons.calendar_month),
                              //           label: Text(intl.serviceCalendar),
                              //           color: theme.colorScheme[300],
                              //           onPressed: () {},
                              //         ),
                              //       ),
                              //     ],
                              //   ),
                              // ),
                              // const SizedBox(height: 8),
                              // IntrinsicHeight(
                              //   child: Row(
                              //     crossAxisAlignment: CrossAxisAlignment.stretch,
                              //     children: [
                              //       Expanded(
                              //         child: PrimaryIconButton(
                              //           icon: Icon(Icons.file_copy),
                              //           label: Text(intl.serviceDocuments),
                              //           color: theme.colorScheme[800],
                              //           onPressed: () {},
                              //         ),
                              //       ),
                              //       const SizedBox(width: 8),
                              //       Expanded(
                              //         child: PrimaryIconButton(
                              //           icon: Icon(Icons.directions_car),
                              //           label: Text(intl.serviceVehicles),
                              //           color: theme.colorScheme[1600],
                              //           onPressed: () {},
                              //         ),
                              //       ),
                              //       const SizedBox(width: 8),
                              //       Expanded(
                              //         child: PrimaryIconButton(
                              //           icon: Icon(Icons.work),
                              //           label: Text(intl.serviceProjects),
                              //           color: theme.colorScheme[1800],
                              //           onPressed: () {},
                              //         ),
                              //       ),
                              //     ],
                              //   ),
                              // ),
                              const SizedBox(height: 24),
                              Container(
                                padding: EdgeInsets.symmetric(vertical: 96),
                                child: IntrinsicHeight(
                                  child: Row(
                                    mainAxisAlignment: MainAxisAlignment.center,
                                    crossAxisAlignment:
                                        CrossAxisAlignment.stretch,
                                    children: [
                                      IconButton(
                                        color: theme.colorScheme.primary,
                                        onPressed: () {
                                          appController.toggleLanguage();
                                        },
                                        iconSize: 48,
                                        icon: AspectRatio(
                                          aspectRatio: 1,
                                          child: Center(
                                            child: Container(
                                              decoration: BoxDecoration(
                                                shape: BoxShape.circle,
                                                border: Border.all(
                                                  color:
                                                      theme.colorScheme.muted,
                                                  width: 2,
                                                ),
                                              ),
                                              width: 42,
                                              height: 42,
                                              child: Flag.fromCode(
                                                appController.locale.value
                                                            .countryCode !=
                                                        'ID'
                                                    ? FlagsCode.US
                                                    : FlagsCode.ID,
                                                fit: BoxFit.cover,
                                                borderRadius: 48,
                                              ),
                                            ),
                                          ),
                                        ),
                                      ),
                                      const SizedBox(width: 16),
                                      VerticalDivider(
                                        color: theme.colorScheme.muted,
                                        thickness: 2,
                                      ),
                                      const SizedBox(width: 16),
                                      IconButton(
                                        color: theme.colorScheme.primary,
                                        onPressed: () {
                                          appController.toggleTheme();
                                        },
                                        iconSize: 48,
                                        icon: Icon(appController.themeMode !=
                                                ThemeMode.light
                                            ? Icons.dark_mode
                                            : Icons.light_mode),
                                      ),
                                    ],
                                  ),
                                ),
                              ),
                              Text(
                                'SEI Office v2.0.0',
                                textAlign: TextAlign.center,
                                style: theme.typography.sm.copyWith(
                                  color: theme.colorScheme.muted,
                                ),
                              ),
                            ],
                          ),
                        ),
                      ],
                    ),
                  ],
                ),
              ),
            ),
          );
        });
  }
}

class DashboardData {
  final String employeeName;
  final String employeeJobTitle;
  final String? avatar;

  DashboardData({
    required this.employeeName,
    required this.employeeJobTitle,
    required this.avatar,
  });

  static Future<DashboardData> fetch(BuildContext context) async {
    final appController = context.getService<AppService>();
    final userController = context.getService<UserService>();
    final employeeDetail = await appController.request(EmployeeProfileRequest(
      empcode: userController.currentUser.employee_code,
    ));
    return DashboardData(
      employeeName: employeeDetail.name,
      employeeJobTitle: employeeDetail.job_title,
      avatar: employeeDetail.avatar,
    );
  }
}

const kAttendanceStatusPresent = 'HADIR SESUAI KETENTUAN';
const kAttendanceStatusHoliday = 'HARI LIBUR';
const kAttendanceStatusLate = 'TERLAMBAT';
