import 'dart:math';

import 'package:flutter/material.dart';
import 'package:geolocator/geolocator.dart';
import 'package:go_router/go_router.dart';
import 'package:seioffice/src/components/common.dart';
import 'package:seioffice/src/components/map/adaptive_map.dart';
import 'package:seioffice/src/components/page.dart';
import 'package:seioffice/src/service/localization.dart';
import 'package:seioffice/src/service/request/attendance.dart';
import 'package:seioffice/src/service/response/attendance.dart';
import 'package:seioffice/src/service/service.dart';
import 'package:seioffice/src/theme/theme.dart';
import 'package:seioffice/src/transitions/reveal_page_transition.dart';

import '../../components/button.dart';
import '../../components/card.dart';
import '../../components/dialog.dart';
import '../../components/header.dart';
import '../../components/section.dart';
import '../../components/steps.dart';
import '../../components/swiper.dart';
import '../../service/routes.dart';
import '../../service/services/app_service.dart';
import '../../service/services/user_service.dart';
import '../../util.dart';
import 'attendance_history_detail_page.dart';
import 'employee_checkin_request.dart';

class LiveAttendancePage extends StatefulWidget {
  const LiveAttendancePage({super.key});

  @override
  State<LiveAttendancePage> createState() => _LiveAttendancePageState();
}

class _LiveAttendancePageState extends State<LiveAttendancePage> {
  final StackedBottomSheetController _bottomSheetController =
      StackedBottomSheetController(
    initialState: BottomSheetState.collapsed,
  );
  final LocalMapController _mapController = LocalMapController();
  TodayAttendanceResponse? data;
  LatLng? position;
  bool holiday = false;

  Future<LocalMapData> initialize() async {
    final appController = getService<AppService>();
    final userController = getService<UserService>();
    final response = await appController.request(AttendanceTodayRequest(
        employeeCode: userController.currentUser.employee_code));
    setState(() {
      data = response;
      holiday = response.message == 'LIBUR';
      if (_bottomSheetController.state == BottomSheetState.collapsed) {
        _bottomSheetController.state = BottomSheetState.expanded;
      }
    });
    return LocalMapData(
      centerLatitude: response.centerLatitude,
      centerLongitude: response.centerLongitude,
      radius: response.radius,
    );
  }

  String formatTime(DateTime time) {
    return '${time.hour.toString().padLeft(2, '0')}:${time.minute.toString().padLeft(2, '0')}';
  }

  double? get distance {
    if (position == null ||
        data?.centerLatitude == null ||
        data?.centerLongitude == null ||
        data?.radius == null) {
      return null;
    }
    final distance = Geolocator.distanceBetween(
      data!.centerLatitude,
      data!.centerLongitude,
      position!.latitude,
      position!.longitude,
    );
    // return distance - data!.radius;
    return max(0, distance - data!.radius);
  }

  Widget row(List<Widget> children) {
    return Row(
      children: children.map((e) => Expanded(child: e)).toList(),
    );
  }

  @override
  void initState() {
    super.initState();
    _bottomSheetController.addListener(() {
      setState(() {});
      if (_bottomSheetController.state == BottomSheetState.expanded ||
          _bottomSheetController.state == BottomSheetState.collapsed) {
        WidgetsBinding.instance.addPostFrameCallback((timeStamp) {
          if (mounted) {
            _mapController.fit();
          }
        });
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    final now = DateTime.now();
    final theme = SEITheme.of(context);
    double radius = distance ?? double.infinity;
    return StackedBottomSheet(
      appBar: Header(
        title: Text(intl.attendanceTitle),
        leading: IconButton(
          icon: const Icon(Icons.arrow_back),
          onPressed: () {
            Navigator.of(context).pop();
          },
        ),
        actions: [
          // history
          IconButton(
            icon: const Icon(Icons.history),
            onPressed: () {
              // Navigator.of(context).pushNamed(Routes.attendanceHistory);
              context.pushNamed(kPageAttendanceHistory);
            },
          ),
        ],
      ),
      expandedHeight: holiday ? 300 : 340,
      floatingActionButton: FloatingActionButton(
        onPressed: () {
          // Navigator.of(context).pushNamed(Routes.attendanceCheckIn);
          _mapController.fit();
        },
        child: const Icon(Icons.my_location),
      ),
      controller: _bottomSheetController,
      flexibleSpace: radius > 0
          ? NotificationCard(
              subtitle: Text(isCheckOut
                  ? 'Proses Check Out anda akan membutuhkan persetujuan'
                  : intl.attendanceCheckOutOutOfRangeMessage),
              icon: const Icon(Icons.error_outline),
              backgroundColor: theme.colorScheme.surfaceNotificationDestructive,
              dropShadow: true,
              child: Text(intl.attendanceOutOfRangeTitle),
            )
          : null,
      body: LocalMap(
        onPositionChanged: (position) {
          WidgetsBinding.instance.addPostFrameCallback((timeStamp) {
            if (mounted) {
              setState(() {
                this.position = position;
              });
            }
          });
        },
        padding: EdgeInsets.only(
          top: radius > 0 && !isCheckOut ? 180 : 90,
          bottom: _bottomSheetController.state == BottomSheetState.collapsed
              ? 50
              : 350,
          left: 100,
          right: 100,
        ),
        centerLatitude: -6.94760,
        centerLongitude: 107.60090,
        radius: 150,
        controller: _mapController,
        init: initialize,
        onDismissed: () {
          Navigator.pop(context);
        },
      ),
      fullPage: holiday
          ? null
          : TabbedPage(
              appBar: Header(
                title: Text(intl.attendanceTitle),
                leading: IconButton(
                  icon: const Icon(Icons.arrow_back),
                  onPressed: () {
                    _bottomSheetController.state = BottomSheetState.expanded;
                  },
                ),
                actions: [
                  // history
                  IconButton(
                    icon: const Icon(Icons.history),
                    onPressed: () {
                      context.pushNamed(kPageAttendanceHistory);
                    },
                  ),
                ],
              ),
              footer: Container(
                padding: const EdgeInsets.all(32),
                child: bottomSwiper,
              ),
              tabs: [
                Tab(
                  text: 'Check In',
                ),
                Tab(
                  text: 'Check Out',
                ),
              ],
              children: [
                [
                  if (data != null)
                    AttendanceHistoryDetailPageContent(
                        data: data!, checkOut: false),
                ],
                [
                  if (data != null)
                    AttendanceHistoryDetailPageContent(
                        data: data!, checkOut: true),
                ]
              ],
              // body: AttendanceHistoryDetailPageContent(
              //     data: DataAbsenSaya.fromJson({})),
            ),
      bottomSheet: Container(
          padding: const EdgeInsets.symmetric(horizontal: 12),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              Text(
                intl.formatDate(now),
                textAlign: TextAlign.center,
                style: theme.typography.md.copyWith(
                  fontWeight: fontMedium,
                ),
              ),
              const SizedBox(height: 24),
              Steps(
                children: [
                  row([
                    ParagraphSection(
                        title: Text(intl.attendanceCheckIn),
                        content: Text(data?.checkIn == null
                            ? '-'
                            : formatTime(data!.checkIn!.toToday()))),
                    if (data?.checkIn != null)
                      ParagraphSection(
                        title: Text(intl.attendanceStatus),
                        content: data?.approved == kApprovalApproved
                            ? LabeledIcon(
                                icon: Icon(Icons.verified),
                                leading: Text('Disetujui'),
                              )
                            : data?.approved == kApprovalRejected
                                ? LabeledIcon(
                                    icon: Icon(Icons.cancel),
                                    iconTheme: IconThemeData(
                                        color: theme.colorScheme.destructive),
                                    leading: Text('Ditolak'),
                                  )
                                : LabeledIcon(
                                    icon: Icon(Icons.watch_later),
                                    leading: Text('Menunggu'),
                                  ),
                      ),
                  ]),
                  row([
                    ParagraphSection(
                      title: Text(intl.attendanceCheckOut),
                      content: Text(
                        data?.checkOut == null
                            ? '-'
                            : formatTime(data!.checkOut!.toToday()),
                      ),
                    ),
                    if (data?.checkOut != null)
                      ParagraphSection(
                        title: Text(intl.attendanceStatus),
                        content: data?.approvedCheckOut == kApprovalApproved
                            ? LabeledIcon(
                                icon: Icon(Icons.verified),
                                leading: Text('Disetujui'),
                              )
                            : data?.approvedCheckOut == kApprovalRejected
                                ? LabeledIcon(
                                    icon: Icon(Icons.cancel),
                                    iconTheme: IconThemeData(
                                        color: theme.colorScheme.destructive),
                                    leading: Text('Ditolak'),
                                  )
                                : LabeledIcon(
                                    icon: Icon(Icons.watch_later),
                                    leading: Text('Menunggu'),
                                  ),
                      ),
                  ])
                ],
              ),
              const Spacer(),
              const SizedBox(height: 24),
              bottomSwiper,
              const SizedBox(height: 32),
            ],
          )),
    );
  }

  Widget get bottomSwiper {
    bool checkOut = isCheckOut;
    if (holiday) {
      final theme = SEITheme.of(context);
      return Container(
        padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 8),
        child: Text(
          'Hari Libur',
          style: theme.typography.md.copyWith(
            fontWeight: fontMedium,
            color: theme.colorScheme.muted,
          ),
          textAlign: TextAlign.center,
        ),
      );
    }
    if (checkOut && !canCheckOut) {
      final theme = SEITheme.of(context);
      if (data?.checkIn != null && data?.approved != 1) {
        return Container(
          padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 8),
          child: Text(
            data?.approved == 0
                ? 'Kehadiran Belum Disetujui'
                : 'Kehadiran Ditolak',
            style: theme.typography.md.copyWith(
              fontWeight: fontMedium,
              color: theme.colorScheme.muted,
            ),
            textAlign: TextAlign.center,
          ),
        );
      }
      return Container(
        padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 8),
        child: Text(
          'Selamat Beristirahat',
          style: theme.typography.md.copyWith(
            fontWeight: fontMedium,
            color: theme.colorScheme.muted,
          ),
          textAlign: TextAlign.center,
        ),
      );
    }
    return ParagraphSection(
      title: Text(checkOut
          ? data?.checkOut != null
              ? 'Geser untuk memperbaharui Check Out'
              : intl.swipeToCheckOut
          : intl.swipeToCheckIn),
      gap: 8,
      alignment: Alignment.center,
      content: Swiper(
        onSwiped: (swiped) {
          if (swiped) {
            if ((distance ?? double.infinity) > 0 && position != null) {
              Navigator.of(context).push(
                SEIPageRoute(
                  builder: (context) {
                    return EmployeeCheckinRequestPage(
                      distance: distance!,
                      latitude: position!.latitude,
                      longitude: position!.longitude,
                      isCheckOut: checkOut,
                    );
                  },
                ),
              ).then((value) {
                if (mounted) {
                  showLoading(
                    context,
                    initialize(),
                  );
                }
              });
            } else if (position != null) {
              LatLng latLng = position!;
              final appController = getService<AppService>();
              final userController = getService<UserService>();
              showLoading(
                context,
                appController
                    .request(
                  AttendanceSwipeRequest(
                    empcode: userController.currentUser.employee_code,
                    latitude: latLng.latitude,
                    longitude: latLng.longitude,
                  ),
                )
                    .then((response) {
                  if (response.msg == 'SUCCESS') {
                    return Navigator.of(context)
                        .push(RevealPageRoute(
                      alignment: Alignment.bottomCenter,
                      builder: (context) {
                        return ActionFeedbackPage(
                          icon: Icon(Icons.verified),
                          title: Text(checkOut
                              ? intl.checkOutSuccessful
                              : intl.checkInSuccessful),
                          action: [
                            TertiaryButton(
                              child: Text('OK'),
                              onPressed: () {
                                Navigator.of(context).pop();
                              },
                            ),
                          ],
                        );
                      },
                    ))
                        .then((value) {
                      if (mounted) {
                        showLoading(
                          context,
                          initialize(),
                        );
                      }
                    });
                  } else {
                    showDialog(
                        context: context,
                        builder: (context) {
                          return PopupDialog(
                            title: Text(checkOut
                                ? intl.checkOutFailed
                                : intl.checkInFailed),
                            content: Text(response.msg),
                            actions: [
                              PrimaryButton(
                                onPressed: () {
                                  Navigator.of(context).pop();
                                },
                                child: Text('OK'),
                              ),
                            ],
                          );
                        });
                  }
                }).whenComplete(
                  () {
                    initialize();
                  },
                ),
              );
            }
          }
        },
      ),
    );
  }

  bool get isCheckOut {
    return data?.checkIn != null;
  }

  bool get canCheckOut {
    // jika belum waktunya pulang, tetapi sudah check out sebelumnya,
    // user masih bisa tetap check out untuk mengupdate check out terakhir
    // jika sudah lebih dari jam pulang, waktu check out sekarang dijadikan
    // waktu check out terakhir dan final.
    if (data?.checkIn != null && data?.approved != 1) {
      return false;
    }
    DateTime now = DateTime.now();
    if (data?.jamKeluar != null && now.isAfter(data!.jamKeluar!.toToday())) {
      return data?.checkOut == null;
    }
    return true;
  }
}
