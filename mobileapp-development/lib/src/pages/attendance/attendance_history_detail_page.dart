import 'package:flutter/material.dart';
import 'package:geolocator/geolocator.dart';
import 'package:seioffice/src/components/common.dart';
import 'package:seioffice/src/components/countdown.dart';
import 'package:seioffice/src/components/header.dart';
import 'package:seioffice/src/components/map/adaptive_map.dart';
import 'package:seioffice/src/components/page.dart';
import 'package:seioffice/src/components/section.dart';
import 'package:seioffice/src/service/localization.dart';
import 'package:seioffice/src/service/response/attendance.dart';
import 'package:seioffice/src/service/service.dart';
import 'package:seioffice/src/theme/theme.dart';

import '../../components/animation.dart';
import '../../components/image.dart';
import '../../service/services/attendance_service.dart';

class AttendanceHistoryDetailPage extends StatefulWidget {
  final int year;
  final int month;
  final int day;

  const AttendanceHistoryDetailPage({
    required this.year,
    required this.month,
    required this.day,
  });

  @override
  State<AttendanceHistoryDetailPage> createState() =>
      _AttendanceHistoryDetailPageState();
}

class _AttendanceHistoryDetailPageState
    extends State<AttendanceHistoryDetailPage> {
  Future<TodayAttendanceResponse>? _future;

  @override
  void didChangeDependencies() {
    super.didChangeDependencies();
    final attendanceController = getService<AttendanceService>();
    _future ??= attendanceController
        .getAttendanceData(DateTime(widget.year, widget.month, widget.day));
  }

  Future<void> _onRefresh() async {
    final attendanceController = getService<AttendanceService>();
    setState(() {
      _future = attendanceController
          .getAttendanceData(DateTime(widget.year, widget.month, widget.day));
    });
    await _future;
  }

  @override
  Widget build(BuildContext context) {
    return FutureBuilder(
      future: _future,
      builder: (context, snapshot) {
        return TabbedPage(
          onRefresh: _onRefresh,
          appBar: Header(
            subtitle: Text(context.intl.attendanceHistoryTitle),
            title: Text(
              context.intl.formatDate(
                DateTime(widget.year, widget.month, widget.day),
              ),
            ),
            leading: IconButton(
              onPressed: () {
                Navigator.of(context).pop();
              },
              icon: const Icon(Icons.arrow_back),
            ),
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
              AttendanceHistoryDetailPageContent(
                data: snapshot.data,
                showHolidayMessage: true,
                showLiteMap: true,
                checkOut: false,
              ),
            ],
            [
              AttendanceHistoryDetailPageContent(
                data: snapshot.data,
                showHolidayMessage: true,
                showLiteMap: true,
                checkOut: true,
              ),
            ],
          ],
        );
      },
    );
  }
}

class AttendanceHistoryDetailPageContent extends StatefulWidget {
  final TodayAttendanceResponse? data;
  final bool showHolidayMessage;
  final bool showLiteMap;
  final bool checkOut;

  const AttendanceHistoryDetailPageContent({
    super.key,
    required this.data,
    this.showHolidayMessage = false,
    this.showLiteMap = false,
    required this.checkOut,
  });

  @override
  State<AttendanceHistoryDetailPageContent> createState() =>
      _AttendanceHistoryDetailPageContentState();
}

class _AttendanceHistoryDetailPageContentState
    extends State<AttendanceHistoryDetailPageContent> {
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
    if (widget.data == null ||
        widget.data!.latitude == null ||
        widget.data!.longitude == null) {
      return 0;
    }
    LatLng center = LatLng(
      widget.data!.centerLatitude,
      widget.data!.centerLongitude,
    );
    LatLng user = LatLng(
      widget.data!.latitude!,
      widget.data!.longitude!,
    );
    return Geolocator.distanceBetween(
      center.latitude,
      center.longitude,
      user.latitude,
      user.longitude,
    );
  }

  Duration? get kurangWaktu {
    // = (jamKeluar - jamMasuk) - (jamKeluar - checkOut)
    if (widget.data == null) {
      return null;
    }
    if (widget.data!.checkOut == null) {
      return null;
    }
    if (widget.data!.jamKeluar == null) {
      return null;
    }
    if (widget.data!.jamMasuk == null) {
      return null;
    }
    return widget.data!.jamKeluar!
            .toToday()
            .difference(widget.data!.jamMasuk!.toToday()) -
        widget.data!.jamKeluar!
            .toToday()
            .difference(widget.data!.checkOut!.toToday());
  }

  @override
  Widget build(BuildContext context) {
    var data = widget.data;
    if (data == null) {
      return Container();
    }
    Duration? kurangWaktu = this.kurangWaktu;
    if (data.message == 'LIBUR') {
      if (widget.showHolidayMessage) {
        final theme = SEITheme.of(context);
        return Container(
          height: MediaQuery.of(context).size.height / 2,
          child: Center(
            child: Text(
              intl.attendanceStatusHoliday,
              style: theme.typography.lg.copyWith(
                color: theme.colorScheme.muted,
              ),
            ),
          ),
        );
      }
      return Container();
    }
    final theme = SEITheme.of(context);

    double? latitude =
        widget.checkOut ? widget.data?.latitudeCheckOut : widget.data?.latitude;
    double? longitude = widget.checkOut
        ? widget.data?.longitudeCheckOut
        : widget.data?.longitude;

    return Container(
      padding: const EdgeInsets.all(24),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.stretch,
        mainAxisSize: MainAxisSize.min,
        children: [
          if (widget.showLiteMap && widget.data != null) ...[
            AspectRatio(
              aspectRatio: 1,
              child: ClipRRect(
                borderRadius: BorderRadius.circular(12),
                child: StaticLiteMap(
                  title: widget.checkOut
                      ? Text('Lokasi Check Out')
                      : Text('Lokasi Check In'),
                  center: LatLng(
                    widget.data!.centerLatitude,
                    widget.data!.centerLongitude,
                  ),
                  radius: widget.data!.radius,
                  userLocation: latitude != null &&
                          longitude != null &&
                          latitude != 0 &&
                          longitude != 0
                      ? LatLng(
                          latitude,
                          longitude,
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
          CountdownBuilder(
            until: data.jamMasuk!.toToday(),
            builder: (context, time) {
              if (time.isNegative) {
                return const SizedBox();
              }
              return Padding(
                padding: const EdgeInsets.only(bottom: 16),
                child: ParagraphSection(
                  title: Text('Sisa Waktu'),
                  content: Text(intl.formatTime(time)),
                ),
              );
            },
          ),
          row([
            ParagraphSection(
              title: Text(intl.entryTime),
              content: Text(intl.formatTimeOfDay(data.jamMasuk!.toTimeOfDay())),
            ),
            ParagraphSection(
              title: Text(intl.exitTime),
              content:
                  Text(intl.formatTimeOfDay(data.jamKeluar!.toTimeOfDay())),
            ),
          ]),
          const SizedBox(height: 16),
          row([
            if (!widget.checkOut)
              ParagraphSection(
                title: Text(intl.attendanceCheckInTime),
                content: Text(data.checkIn != null
                    ? intl.formatTimeOfDay(data.checkIn!.toTimeOfDay())
                    : '-'),
              ),
            if (!widget.checkOut)
              ConstantRebuild(
                builder: (context) {
                  if (data.checkIn == null) {
                    return const SizedBox();
                  }
                  Duration? lateTime;
                  if (data.jamMasuk != null) {
                    final now = data.checkIn?.toToday() ?? DateTime.now();
                    final jamMasuk = data.jamMasuk!.toToday();
                    if (now.isAfter(jamMasuk)) {
                      lateTime = now.difference(jamMasuk);
                    }
                  }
                  if (lateTime != null) {
                    return Container(
                      child: ParagraphSection(
                        title: Text(intl.attendanceLateTime),
                        content: Text(intl.formatTime(lateTime)),
                      ),
                    );
                  }
                  return const SizedBox();
                },
              ),
            if (widget.checkOut)
              ParagraphSection(
                title: Text(intl.attendanceCheckOutTime),
                content: Text(data.checkOut != null
                    ? intl.formatTimeOfDay(data.checkOut!.toTimeOfDay())
                    : '-'),
              ),
            if (widget.checkOut)
              ParagraphSection(
                title: Text('Kurang Jam'),
                content: Text(
                    kurangWaktu == null ? '-' : intl.formatTime(kurangWaktu)),
              ),
          ]),
          if (data.keterangan != null && !widget.checkOut) ...[
            const SizedBox(height: 16),
            row([
              ParagraphSection(
                title: Text(intl.attendanceReason),
                content: Text(data.keterangan!),
              ),
              ParagraphSection(
                title: Text('Peninjau'),
                content: Text(data.reviewerEmployeeName ?? 'TIDAK ADA'),
              ),
            ]),
          ],
          if (data.keteranganCheckOut != null && widget.checkOut) ...[
            const SizedBox(height: 16),
            row([
              ParagraphSection(
                title: Text(intl.attendanceReason),
                content: Text(data.keteranganCheckOut!),
              ),
              ParagraphSection(
                title: Text('Peninjau'),
                content: Text(data.reviewerCheckOutEmployeeName ?? 'TIDAK ADA'),
              ),
            ]),
          ],
          const SizedBox(height: 16),
          if (!widget.checkOut)
            ParagraphSection(
              title: Text(intl.attendanceStatus),
              content: data.checkIn == null
                  ? Text('-')
                  : data.approved == kApprovalApproved
                      ? LabeledIcon(
                          icon: Icon(Icons.verified),
                          leading: Text(intl.approved),
                        )
                      : data.approved == kApprovalRejected
                          ? LabeledIcon(
                              icon: Icon(Icons.cancel),
                              leading: Text('Ditolak'),
                              iconTheme: IconThemeData(
                                  color: theme.colorScheme.destructive),
                            )
                          : LabeledIcon(
                              icon: Icon(Icons.watch_later),
                              leading: Text(intl.pending),
                            ),
            ),
          if (widget.checkOut)
            ParagraphSection(
              title: Text(intl.attendanceStatus),
              content: data.checkOut == null
                  ? Text('-')
                  : data.approvedCheckOut == kApprovalApproved
                      ? LabeledIcon(
                          icon: Icon(Icons.verified),
                          leading: Text(intl.approved),
                        )
                      : data.approvedCheckOut == kApprovalRejected
                          ? LabeledIcon(
                              icon: Icon(Icons.cancel),
                              leading: Text('Ditolak'),
                              iconTheme: IconThemeData(
                                  color: theme.colorScheme.destructive),
                            )
                          : LabeledIcon(
                              icon: Icon(Icons.watch_later),
                              leading: Text(intl.pending),
                            ),
            ),
          const SizedBox(height: 16),
          if (!widget.checkOut && data.imageUrl != null)
            ParagraphSection(
              title: Text(intl.attendanceAttachedPhoto),
              // content: Text('-'),
              content: data.imageUrl == null
                  ? Text('-')
                  : Container(
                      margin: const EdgeInsets.only(top: 8),
                      height: 200,
                      child: PhotoBox(image: NetworkImage(data.imageUrl!))),
            ),
          if (widget.checkOut && data.imageUrlCheckOut != null)
            ParagraphSection(
              title: Text(intl.attendanceAttachedPhoto),
              // content: Text('-'),
              content: data.imageUrlCheckOut == null
                  ? Text('-')
                  : Container(
                      height: 200,
                      margin: const EdgeInsets.only(top: 8),
                      child: PhotoBox(
                          image: NetworkImage(data.imageUrlCheckOut!))),
            ),
          const SizedBox(height: 16),
          // for (int i = 0; i < 10; i++)
          //   NotificationCard(child: Text('Lorem ipsum dolor sit amet $i'))
        ],
      ),
    );
  }
}
