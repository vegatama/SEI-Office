import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'package:seioffice/src/service/routes.dart';

import '../util.dart';
import 'notification.dart';

class NotificationAttendanceSwipeRequest extends NotificationType
    with InteractableNotificationType {
  NotificationAttendanceSwipeRequest()
      : super((context, data) {
          if (data.getBool(2) ?? false) {
            return '{0} meminta persetujuan check-out';
          } else {
            return '{0} meminta persetujuan check-in';
          }
        });

  @override
  Widget? buildIcon(BuildContext context) {
    return const Icon(Icons.access_time);
  }

  bool isCheckOut(DataList data) {
    return data.getBool(2) ?? false;
  }

  int getPendingAttendanceId(DataList data) {
    return data.getInt(1)!;
  }

  @override
  void onTap(BuildContext context, DataList data) {
    context.pushNamed(kPageAttendanceReview, queryParameters: {
      'id': data.getInt(1).toString(),
    });
  }
}

class NotificationAttendanceSwipeResponseAccepted extends NotificationType
    with InteractableNotificationType {
  NotificationAttendanceSwipeResponseAccepted()
      : super((context, data) {
          if (data.getBool(2) ?? false) {
            return '{0} telah menyetujui check-out anda';
          } else {
            return '{0} telah menyetujui check-in anda';
          }
        });

  @override
  Widget? buildIcon(BuildContext context) {
    return const Icon(Icons.verified_outlined);
  }

  bool isCheckOut(DataList data) {
    return data.getBool(2) ?? false;
  }

  LocalDate getAttendanceDate(DataList data) {
    return LocalDate.parse(data.getString(1)!);
  }

  @override
  void onTap(BuildContext context, DataList data) {
    LocalDate date = getAttendanceDate(data);
    context.pushNamed(kPageAttendanceDetail, queryParameters: {
      'day': date.day.toString(),
      'month': date.month.toString(),
      'year': date.year.toString(),
    });
  }

  @override
  Widget? buildTrailing(BuildContext context) {
    return const Icon(Icons.arrow_forward);
  }
}

class NotificationAttendanceSwipeResponseRejected extends NotificationType
    with InteractableNotificationType {
  NotificationAttendanceSwipeResponseRejected()
      : super((context, data) {
          if (data.getBool(2) ?? false) {
            return '{0} telah menolak check-out anda';
          } else {
            return '{0} telah menolak check-in anda';
          }
        });

  @override
  Widget? buildIcon(BuildContext context) {
    return const Icon(Icons.cancel_outlined);
  }

  bool isCheckOut(DataList data) {
    return data.getBool(2) ?? false;
  }

  LocalDate getAttendanceDate(DataList data) {
    return LocalDate.parse(data.getString(1)!);
  }

  @override
  void onTap(BuildContext context, DataList data) {
    LocalDate date = getAttendanceDate(data);
    context.pushNamed(kPageAttendanceDetail, queryParameters: {
      'day': date.day.toString(),
      'month': date.month.toString(),
      'year': date.year.toString(),
    });
  }
}

class NotificationIzinCutiRequest extends NotificationType
    with InteractableNotificationType {
  NotificationIzinCutiRequest()
      : super((context, data) {
          return '{0} meminta persetujuan izin cuti: {1}';
        });

  @override
  Widget? buildIcon(BuildContext context) {
    return const Icon(Icons.calendar_today);
  }

  int getIzinCutiId(DataList data) {
    return data.getInt(2)!;
  }

  @override
  void onTap(BuildContext context, DataList data) {
    context.pushNamed(kPageLeaveReviewDetail, queryParameters: {
      'id': getIzinCutiId(data).toString(),
    });
  }
}

class NotificationIzinCutiResponseAccepted extends NotificationType
    with InteractableNotificationType {
  NotificationIzinCutiResponseAccepted()
      : super((context, data) {
          return '{0} telah menyetujui izin cuti anda';
        });

  @override
  Widget? buildIcon(BuildContext context) {
    return const Icon(Icons.verified_outlined);
  }

  int getIzinCutiId(DataList data) {
    return data.getInt(1)!;
  }

  @override
  void onTap(BuildContext context, DataList data) {
    context.pushNamed(kPageLeaveDetail, queryParameters: {
      'id': getIzinCutiId(data).toString(),
    });
  }
}

class NotificationIzinCutiResponseRejected extends NotificationType
    with InteractableNotificationType {
  NotificationIzinCutiResponseRejected()
      : super((context, data) {
          return '{0} telah menolak izin cuti anda dengan alasan: {1}';
        });

  @override
  Widget? buildIcon(BuildContext context) {
    return const Icon(Icons.cancel_outlined);
  }

  String getReason(DataList data) {
    return data.getString(1) ?? '';
  }

  int getIzinCutiId(DataList data) {
    return data.getInt(2)!;
  }

  @override
  void onTap(BuildContext context, DataList data) {
    context.pushNamed(kPageLeaveDetail, queryParameters: {
      'id': getIzinCutiId(data).toString(),
    });
  }
}
