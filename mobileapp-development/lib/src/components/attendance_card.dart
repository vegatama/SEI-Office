import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'package:seioffice/src/components/card.dart';
import 'package:seioffice/src/components/shimmer.dart';
import 'package:seioffice/src/service/localization.dart';
import 'package:seioffice/src/service/routes.dart';
import 'package:seioffice/src/theme/theme.dart';
import 'package:seioffice/src/util.dart';

import '../service/response/attendance.dart';

class PlaceholderAttendanceCard extends StatelessWidget {
  const PlaceholderAttendanceCard({super.key});

  @override
  Widget build(BuildContext context) {
    return const ContentPlaceholder(
      showContent: false,
      child: AlertCard(
        content: Text('BELUM ABSEN'),
        icon: SizedBox(),
        color: Colors.transparent,
        foregroundColor: Colors.transparent,
      ),
    );
  }
}

class AttendanceCard extends StatefulWidget {
  final AttendanceData data;
  final VoidCallback onRefresh;

  const AttendanceCard({
    required this.data,
    super.key,
    required this.onRefresh,
  });

  @override
  State<AttendanceCard> createState() => _AttendanceCardState();
}

AttendanceFilterStatus? parseAttendanceStatus(String? status) {
  if (status != null) {
    AttendanceStatus? attendanceStatus = AttendanceStatus.parse(status);
    if (attendanceStatus != null) {
      AttendanceFilterStatus? filterStatus =
          AttendanceFilterStatus.match(attendanceStatus);
      if (filterStatus != null) {
        return filterStatus;
      }
    }
  }
  return null;
}

class _AttendanceCardState extends State<AttendanceCard> {
  AttendanceFilterStatus get status {
    AttendanceFilterStatus? s = parseAttendanceStatus(widget.data.status);
    return s ?? AttendanceFilterStatus.absent;
    // if (s == AttendanceFilterStatus.absent) {
    //   if (widget.data.firstCheckIn != null) {
    //     if (widget.data.approved == kApprovalPending) {
    //       return AttendanceFilterStatus.present;
    //     }
    //   }
    // }
    // if (s != null) return s;
    // if (widget.data.firstCheckIn == null) {
    //   return AttendanceFilterStatus.absent;
    // }
    // return AttendanceFilterStatus.present;
  }

  AttendanceStatus get detailedStatus {
    var attendanceStatus = AttendanceStatus.parse(widget.data.status ?? '') ??
        AttendanceStatus.absent;
    return attendanceStatus;
  }

  Duration get late {
    LocalTime? checkInTime = widget.data.firstCheckIn;
    if (checkInTime == null) {
      return Duration.zero;
    }
    LocalTime? jamMasuk = widget.data.jamMasuk;
    if (jamMasuk == null) {
      return Duration.zero;
    }
    return checkInTime.difference(jamMasuk);
  }

  bool get isToday {
    int day = widget.data.day;
    int month = widget.data.month;
    int year = widget.data.year;
    final now = DateTime.now();
    return now.day == day && now.month == month && now.year == year;
  }

  Color get foregroundColor {
    switch (status) {
      case AttendanceFilterStatus.absent:
        if (isToday && widget.data.approved != kApprovalRejected) {
          return SEITheme.of(context).colorScheme.surfaceUnknownForeground;
        }
        return SEITheme.of(context).colorScheme.surfaceDangerForeground;
      case AttendanceFilterStatus.present:
        if (widget.data.approved == kApprovalPending) {
          return SEITheme.of(context).colorScheme.surfaceWarningForeground;
        } else if (widget.data.approved == kApprovalRejected) {
          return SEITheme.of(context).colorScheme.surfaceDangerForeground;
        }
        Duration l = late;
        if (l.inMinutes > 0) {
          return SEITheme.of(context).colorScheme.surfaceFineForeground;
        }
        return SEITheme.of(context).colorScheme.surfaceSuccessForeground;
      case AttendanceFilterStatus.permitted:
        return SEITheme.of(context).colorScheme.surfaceWarningForeground;
      case AttendanceFilterStatus.holiday:
        return SEITheme.of(context).colorScheme.surfaceMutedForeground;
    }
  }

  Color get color {
    switch (status) {
      case AttendanceFilterStatus.absent:
        if (isToday && widget.data.approved != kApprovalRejected) {
          return SEITheme.of(context).colorScheme.surfaceUnknown;
        }
        return SEITheme.of(context).colorScheme.surfaceDanger;
      case AttendanceFilterStatus.present:
        if (widget.data.approved == kApprovalPending) {
          return SEITheme.of(context).colorScheme.surfaceWarning;
        }
        if (widget.data.approved == kApprovalRejected) {
          return SEITheme.of(context).colorScheme.surfaceDanger;
        }
        Duration l = late;
        if (l.inMinutes > 0) {
          return SEITheme.of(context).colorScheme.surfaceFine;
        }
        return SEITheme.of(context).colorScheme.surfaceSuccess;
      case AttendanceFilterStatus.permitted:
        return SEITheme.of(context).colorScheme.surfaceWarning;
      case AttendanceFilterStatus.holiday:
        return SEITheme.of(context).colorScheme.surfaceMuted;
    }
  }

  Widget get icon {
    switch (status) {
      case AttendanceFilterStatus.absent:
        if (isToday && widget.data.approved != kApprovalRejected) {
          return const Icon(Icons.help);
        }
        return const Icon(Icons.event_busy);
      case AttendanceFilterStatus.present:
        if (widget.data.approved == kApprovalPending) {
          return const Icon(Icons.watch_later);
        } else if (widget.data.approved == kApprovalRejected) {
          return const Icon(Icons.cancel);
        }
        Duration l = late;
        if (l.inMinutes > 0) {
          return const Icon(Icons.watch_later);
        }
        return const Icon(Icons.verified);
      case AttendanceFilterStatus.permitted:
        return const Icon(Icons.assignment_late);
      case AttendanceFilterStatus.holiday:
        return const Icon(Icons.calendar_today);
    }
  }

  Widget get content {
    switch (status) {
      case AttendanceFilterStatus.absent:
        if (isToday && widget.data.approved != kApprovalRejected) {
          return Text(intl.attendanceStatusUnknown);
        }
        if (widget.data.approved == kApprovalRejected) {
          return Text(intl.attendanceStatusRejected);
        }
        return Text(intl.attendanceStatusAbsent);
      case AttendanceFilterStatus.present:
        if (widget.data.approved == kApprovalPending) {
          return Text(intl.attendanceStatusPending);
        } else if (widget.data.approved == kApprovalRejected) {
          return Text(intl.attendanceStatusRejected);
        }
        Duration l = late;
        if (l.inMinutes > 0) {
          return Text(intl.attendanceStatusLate);
        }
        return Text(intl.attendanceStatusPresent);
      case AttendanceFilterStatus.permitted:
        return Text(intl.attendanceStatusPermitted);
      case AttendanceFilterStatus.holiday:
        return Text(intl.attendanceStatusHoliday);
    }
  }

  Widget? get details {
    switch (status) {
      case AttendanceFilterStatus.absent:
        if (isToday) {
          return null;
        }
        if (widget.data.keterangan != null &&
            widget.data.keterangan?.toLowerCase() != 'tidak hadir') {
          if (widget.data.keterangan != null && widget.data.keterangan != '-') {
            return Text(widget.data.keterangan!);
          }
        }
        return null;
      case AttendanceFilterStatus.present:
        if (widget.data.approved != kApprovalApproved) {
          return null;
        }
        Duration l = late;
        if (l.inMinutes > 0) {
          return Text(intl.lateBy(l.inMinutes));
        }
        if (widget.data.keterangan != null && widget.data.keterangan != '-') {
          return Text(widget.data.keterangan!);
        }
        return null;
      case AttendanceFilterStatus.permitted:
        if (widget.data.keterangan != null && widget.data.keterangan != '-') {
          return Text(widget.data.keterangan!);
        }
        return null;
      case AttendanceFilterStatus.holiday:
        if (widget.data.keterangan != null &&
            widget.data.keterangan != '-' &&
            widget.data.keterangan != 'Hari Libur') {
          return Text(widget.data.keterangan!);
        }
        return null;
    }
  }

  @override
  Widget build(BuildContext context) {
    int day = widget.data.day;
    int month = widget.data.month;
    int year = widget.data.year;
    return GestureDetector(
      onTap: () {
        if (isToday) {
          context.pushNamed(kPageLiveAttendance).then((value) {
            widget.onRefresh();
          });
        } else {
          context.pushNamed(kPageAttendanceDetail, queryParameters: {
            'day': day.toString(),
            'month': month.toString(),
            'year': year.toString(),
          });
        }
      },
      child: AlertCard(
        title: isToday
            ? Text(intl.today)
            : Text(intl.formatDate(DateTime(year, month, day))),
        content: content,
        icon: icon,
        color: color,
        foregroundColor: foregroundColor,
        details: details,
      ),
    );
  }
}
