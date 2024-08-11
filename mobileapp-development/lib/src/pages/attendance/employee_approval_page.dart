import 'package:flutter/material.dart';
import 'package:geolocator/geolocator.dart';
import 'package:seioffice/src/components/animation.dart';
import 'package:seioffice/src/components/common.dart';
import 'package:seioffice/src/components/dialog.dart';
import 'package:seioffice/src/components/header.dart';
import 'package:seioffice/src/components/map/adaptive_map.dart';
import 'package:seioffice/src/components/page.dart';
import 'package:seioffice/src/components/section.dart';
import 'package:seioffice/src/service/localization.dart';
import 'package:seioffice/src/service/request/attendance.dart';
import 'package:seioffice/src/service/response/attendance.dart';
import 'package:seioffice/src/service/service.dart';
import 'package:seioffice/src/theme/theme.dart';

import '../../components/button.dart';
import '../../service/services/app_service.dart';
import '../../service/services/user_service.dart';

class EmployeeCheckInApprovalPage extends StatelessWidget {
  final EmployeeCheckInApproval data;

  const EmployeeCheckInApprovalPage({
    super.key,
    required this.data,
  });

  @override
  Widget build(BuildContext context) {
    return StandardPage(
      appBar: Header(
        subtitle: Text(context.intl.attendanceHistoryTitle),
        title: Text(
          data.checkOut ? 'Check Out Approval' : 'Check In Approval',
        ),
        leading: IconButton(
          onPressed: () {
            Navigator.of(context).pop();
          },
          icon: const Icon(Icons.arrow_back),
        ),
      ),
      children: [
        EmployeeCheckInApprovalPageContent(
          data: data,
          showHolidayMessage: true,
          showLiteMap: true,
        ),
      ],
    );
  }
}

class EmployeeCheckInApprovalPageContent extends StatefulWidget {
  final EmployeeCheckInApproval data;
  final bool showHolidayMessage;
  final bool showLiteMap;

  const EmployeeCheckInApprovalPageContent({
    super.key,
    required this.data,
    this.showHolidayMessage = false,
    this.showLiteMap = false,
  });

  @override
  State<EmployeeCheckInApprovalPageContent> createState() =>
      _EmployeeCheckInApprovalPageContentState();
}

class _EmployeeCheckInApprovalPageContentState
    extends State<EmployeeCheckInApprovalPageContent> {
  Widget row(List<Widget> children) {
    return Row(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: children.map((e) => Expanded(child: e)).toList(),
    );
  }

  String formatDistance(double meters) {
    if (meters < 1000) {
      return '${meters.toStringAsFixed(0)} m';
    }
    int km = (meters / 1000).floor();
    double kmRounded = meters / 1000;
    if (km == kmRounded) {
      return '$km km';
    }
    return '${kmRounded.toStringAsFixed(2)} km';
  }

  double get distance {
    if (widget.data.latitude == 0 || widget.data.longitude == 0) {
      return 0;
    }
    LatLng center = LatLng(
      widget.data.centerLatitude,
      widget.data.centerLongitude,
    );
    LatLng user = LatLng(
      widget.data.latitude,
      widget.data.longitude,
    );
    return Geolocator.distanceBetween(
      center.latitude,
      center.longitude,
      user.latitude,
      user.longitude,
    );
  }

  @override
  Widget build(BuildContext context) {
    final appController = context.getService<AppService>();
    var data = widget.data;
    final theme = SEITheme.of(context);
    return Container(
      padding: const EdgeInsets.all(24),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.stretch,
        mainAxisSize: MainAxisSize.min,
        children: [
          if (widget.showLiteMap) ...[
            AspectRatio(
              aspectRatio: 1,
              child: ClipRRect(
                borderRadius: BorderRadius.circular(12),
                child: StaticLiteMap(
                  title: widget.data.checkOut
                      ? Text('Lokasi Check Out')
                      : Text('Lokasi Check In'),
                  center: LatLng(
                    widget.data.centerLatitude,
                    widget.data.centerLongitude,
                  ),
                  radius: widget.data.radius,
                  userLocation:
                      widget.data.latitude != 0 && widget.data.longitude != 0
                          ? LatLng(
                              widget.data.latitude,
                              widget.data.longitude,
                            )
                          : null,
                ),
              ),
            ),
            const SizedBox(height: 32),
            Center(
              child: SizedBox(
                width: 250,
                child:
                    DistanceVisualization(distance: formatDistance(distance)),
              ),
            ),
            const SizedBox(height: 48),
          ],
          row([
            // nama pegawai
            ParagraphSection(
              title: Text('Nama Pegawai'),
              content: Text(data.employeeName),
            ),
            // jabatan
            ParagraphSection(
              title: Text('Jabatan'),
              content: Text(data.employeeJobTitle),
            ),
          ]),
          const SizedBox(height: 16),
          row([
            ParagraphSection(
              title: Text(intl.attendanceCheckInTime),
              content: Text(
                  intl.formatDate(data.time.toDateTime(data.date), time: true)),
            ),
            ParagraphSection(
              title: Text('Jam Masuk'),
              content:
                  Text(intl.formatTimeOfDay(data.targetTime.toTimeOfDay())),
            ),
          ]),
          ConstantRebuild(
            builder: (context) {
              Duration? lateTime;
              final now = data.time.toToday();
              final jamMasuk = data.targetTime.toToday();
              if (now.isAfter(jamMasuk)) {
                lateTime = now.difference(jamMasuk);
              }
              if (lateTime != null) {
                return Container(
                  padding: const EdgeInsets.only(top: 16),
                  child: ParagraphSection(
                    title: Text(intl.attendanceLateTime),
                    content: Text(intl.formatTime(lateTime)),
                  ),
                );
              }
              return const SizedBox();
            },
          ),
          if (data.reason != null) ...[
            const SizedBox(height: 16),
            ParagraphSection(
              title: Text(intl.attendanceReason),
              content: Text(data.reason!),
            ),
          ],
          const SizedBox(height: 16),
          ParagraphSection(
            title: Text(intl.attendanceStatus),
            content: data.approved == kApprovalApproved
                ? LabeledIcon(
                    icon: Icon(Icons.verified),
                    leading: Text('Disetujui'),
                  )
                : data.approved == kApprovalRejected
                    ? LabeledIcon(
                        icon: Icon(Icons.cancel),
                        leading: Text('Ditolak'),
                        iconTheme:
                            IconThemeData(color: theme.colorScheme.destructive),
                      )
                    : LabeledIcon(
                        icon: Icon(Icons.watch_later),
                        leading: Text('Menunggu'),
                      ),
          ),
          const SizedBox(height: 16),
          ParagraphSection(
            title: Text(intl.attendanceAttachedPhoto),
            content: Text('-'),
          ),
          const SizedBox(height: 48),
          PrimaryButton(
            onPressed: () {
              showDialog(
                context: context,
                builder: (context) {
                  return PopupDialog(
                    title: Text('Setujui Permintaan'),
                    content: Text(
                        'Apakah anda yakin ingin menyetujui permintaan check in?'),
                    actions: [
                      SecondaryButton(
                        onPressed: () {
                          Navigator.pop(context);
                        },
                        child: Text('Batal'),
                      ),
                      PrimaryButton(
                        onPressed: () {
                          Navigator.pop(context, true);
                        },
                        child: Text('Setujui'),
                      ),
                    ],
                  );
                },
              ).then((value) {
                if (value == true) {
                  showLoading(
                      context,
                      appController.request(AttendanceConfirmRequest(
                        id: widget.data.id,
                        approve: true,
                        empCode: context
                            .getService<UserService>()
                            .currentUser
                            .employee_code,
                      ))).then((value) {
                    Navigator.pop(context);
                  });
                }
              });
            },
            child: Text('Setujui Permintaan'),
          ),
          const SizedBox(height: 8),
          SecondaryButton(
            onPressed: () {
              showDialog(
                context: context,
                builder: (context) {
                  return PopupDialog(
                    title: Text('Tolak Permintaan'),
                    content: Text(
                        'Apakah anda yakin ingin menolak permintaan check in?'),
                    actions: [
                      SecondaryButton(
                        onPressed: () {
                          Navigator.pop(context);
                        },
                        child: Text('Batal'),
                      ),
                      PrimaryButton(
                        onPressed: () {
                          Navigator.pop(context, true);
                        },
                        child: Text('Tolak'),
                      ),
                    ],
                  );
                },
              ).then((value) {
                if (value == true) {
                  showLoading(
                      context,
                      appController.request(AttendanceConfirmRequest(
                        id: widget.data.id,
                        approve: false,
                        empCode: context
                            .getService<UserService>()
                            .currentUser
                            .employee_code,
                      ))).then((value) {
                    Navigator.pop(context);
                  });
                }
              });
            },
            child: Text('Tolak Permintaan'),
          ),
        ],
      ),
    );
  }
}
