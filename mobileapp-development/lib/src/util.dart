import 'dart:math';

import 'package:flutter/material.dart';
import 'package:intl/intl.dart' as intl;
import 'package:latlong2/latlong.dart';
import 'package:seioffice/src/components/button.dart';
import 'package:seioffice/src/components/dialog.dart';
import 'package:seioffice/src/components/shimmer.dart';
import 'package:seioffice/src/service/localization.dart';

const fontSemiBold = FontWeight.w600;
const fontBold = FontWeight.w700;
const fontLight = FontWeight.w300;
const fontRegular = FontWeight.w400;
const fontMedium = FontWeight.w500;
const fontExtraLight = FontWeight.w200;
const fontThin = FontWeight.w100;
const fontBlack = FontWeight.w900;

const kSuccessMessage = 'SUCCESS';

String? filterSuccessMessage(String message) {
  if (message.toUpperCase() == kSuccessMessage) {
    return null;
  }
  return message;
}

Iterable<Widget> joinWidgets(Iterable<Widget> widgets, Widget separator) sync* {
  final iterator = widgets.iterator;
  if (iterator.moveNext()) {
    yield iterator.current;
    while (iterator.moveNext()) {
      yield separator;
      yield iterator.current;
    }
  }
}

mixin MessageDataHolder {
  String? get errorMessage;
}

extension FutureErrorExtension<T extends MessageDataHolder?> on Future<T> {
  Future<T> handleErrorMessage(BuildContext context) {
    return then((value) {
      if (value == null) {
        return value;
      }
      if (value.errorMessage != null) {
        var intl = context.intl;
        String localizedErrorMessage =
            intl.localizeErrorMessage(value.errorMessage!) ??
                intl.errorMessageGeneral;
        showDialog(
          context: context,
          builder: (context) {
            return PopupDialog(
              title: Text(context.intl.errorTitle),
              content: Text(localizedErrorMessage),
              actions: [
                PrimaryButton(
                  onPressed: () {
                    Navigator.of(context).pop();
                  },
                  child: Text(context.intl.buttonOk),
                ),
              ],
            );
          },
        );
        return Future.error('${value.errorMessage}: $localizedErrorMessage');
      }
      return value;
    });
  }
}

extension WidgetListExtension on List<Widget> {
  Iterable<Widget> joinSeparator(Widget separator) {
    return joinWidgets(this, separator);
  }
}

class SurfaceData {
  final Color color;
  final Color foreground;

  const SurfaceData({
    required this.color,
    required this.foreground,
  });
}

extension ListExtension<T> on List<T> {
  operator *(int times) {
    final result = <T>[];
    for (var i = 0; i < times; i++) {
      result.addAll(this);
    }
    return result;
  }
}

class LocalTime {
  final int hour;
  final int minute;
  final int second;
  final int millisecond;
  final int nano;

  LocalTime({
    this.hour = 0,
    this.minute = 0,
    this.second = 0,
    this.millisecond = 0,
    this.nano = 0,
  });

  LocalTime.fromDateTime(DateTime dateTime)
      : hour = dateTime.hour,
        minute = dateTime.minute,
        second = dateTime.second,
        millisecond = dateTime.millisecond,
        nano = dateTime.microsecond * 1000;

  LocalTime.now() : this.fromDateTime(DateTime.now());

  LocalTime.fromTimeOfDay(TimeOfDay timeOfDay)
      : hour = timeOfDay.hour,
        minute = timeOfDay.minute,
        second = 0,
        millisecond = 0,
        nano = 0;

  TimeOfDay toTimeOfDay() {
    return TimeOfDay(hour: hour, minute: minute);
  }

  DateTime toDateTime(LocalDate date) {
    return DateTime(date.year, date.month, date.day, hour, minute, second,
        millisecond, nano);
  }

  DateTime toToday() {
    return DateTime.now().copyWith(
      hour: hour,
      minute: minute,
      second: second,
      millisecond: millisecond,
      microsecond: nano ~/ 1000,
    );
  }

  @override
  String toString() {
    // convert to ISO 8601 time format
    return '${hour < 10 ? '0' : ''}$hour:${minute < 10 ? '0' : ''}$minute:${second < 10 ? '0' : ''}$second.${millisecond < 10 ? '00' : millisecond < 100 ? '0' : ''}$millisecond';
  }

  factory LocalTime.parse(String time) {
    return LocalTime.fromDateTime(DateTime.parse('1970-01-01T$time'));
  }

  Duration difference(LocalTime other) {
    final time1 = DateTime(1970, 1, 1, hour, minute, second, millisecond, nano);
    final time2 = DateTime(1970, 1, 1, other.hour, other.minute, other.second,
        other.millisecond, other.nano);
    return time1.difference(time2);
  }
}

class LocalDate {
  final int year;
  final int month;
  final int day;

  LocalDate({
    required this.year,
    required this.month,
    required this.day,
  });

  LocalDate.fromDateTime(DateTime dateTime)
      : year = dateTime.year,
        month = dateTime.month,
        day = dateTime.day;

  LocalDate.now() : this.fromDateTime(DateTime.now());

  @override
  String toString() {
    return '$year-${month < 10 ? '0' : ''}$month-${day < 10 ? '0' : ''}$day';
  }

  factory LocalDate.parse(String date) {
    return LocalDate.fromDateTime(DateTime.parse(date));
  }

  Duration difference(LocalDate other) {
    final date1 = DateTime(year, month, day);
    final date2 = DateTime(other.year, other.month, other.day);
    return date1.difference(date2);
  }
}

extension PlaceholderExtension on Widget {
  Widget placeholder(AsyncSnapshot snapshot, [Widget Function()? builder]) {
    return ContentPlaceholder(
      showContent: snapshot.hasData,
      child: builder == null
          ? this
          : snapshot.hasData
              ? builder()
              : this,
    );
  }
}

typedef JsonObject = Map<String, dynamic>;
typedef JsonList = List<dynamic>;

extension JsonExtension on Map<String, dynamic> {
  int? getInt(String key) {
    dynamic value = this[key];
    if (value == null) {
      return 0;
    }
    if (value is num) {
      return value.toInt();
    }
    if (value is String) {
      return int.tryParse(value);
    }
    throw ArgumentError.value(value, key, 'Value is not an int');
  }

  double? getDouble(String key) {
    dynamic value = this[key];
    if (value == null) {
      return 0;
    }
    if (value is num) {
      return value.toDouble();
    }
    if (value is String) {
      return double.tryParse(value);
    }
    throw ArgumentError.value(value, key, 'Value is not a double');
  }

  String? getString(String key) {
    dynamic value = this[key];
    if (value == null) {
      return '';
    }
    if (value is String) {
      return value;
    }
    throw ArgumentError.value(value, key, 'Value is not a string');
  }

  bool? getBool(String key) {
    dynamic value = this[key];
    if (value == null) {
      return false;
    }
    if (value is bool) {
      return value;
    }
    if (value is String) {
      return value.toLowerCase() == 'true';
    }
    throw ArgumentError.value(value, key, 'Value is not a bool');
  }

  T? getEnum<T extends Enum>(String key, List<T> values) {
    dynamic value = this[key];
    if (value == null) {
      return null;
    }
    T? enumValue = values.where((element) => element.name == value).firstOrNull;
    if (enumValue != null) {
      return enumValue;
    }
    throw ArgumentError.value(value, key, 'Value is not a valid enum value');
  }

  T? getEnumIndex<T extends Enum>(String key, List<T> values) {
    dynamic value = this[key];
    if (value == null) {
      return null;
    }
    if (value is int) {
      return values[value];
    }
    throw ArgumentError.value(value, key, 'Value is not a valid enum index');
  }
}

extension DateTimeExtension on DateTime {
  String format(String pattern) {
    return intl.DateFormat(pattern).format(this);
  }
}

enum AttendanceFilterStatus {
  present([
    AttendanceStatus.present,
    AttendanceStatus.late,
    AttendanceStatus.lateAndForgotCheckTime,
    AttendanceStatus.lateAndDeficientHours,
    AttendanceStatus.forgotCheckTime,
    AttendanceStatus.deficientHours,
    AttendanceStatus.pendingApproval,
  ]),
  permitted([
    AttendanceStatus.businessTrip,
    AttendanceStatus.sick,
    AttendanceStatus.permit,
    AttendanceStatus.fieldWork,
    AttendanceStatus.leave,
  ]),
  absent([
    AttendanceStatus.absent,
    AttendanceStatus.forgotCheckIn,
  ]),
  holiday([
    AttendanceStatus.holiday,
  ]);

  final List<AttendanceStatus> statuses;

  const AttendanceFilterStatus(this.statuses);

  static AttendanceFilterStatus? match(AttendanceStatus status) {
    for (final value in values) {
      if (value.statuses.contains(status)) {
        return value;
      }
    }
    return null;
  }
}

const String kIzinCutiEmployeeNotFound = 'EMPLOYEE_NOT_FOUND';
const String kIzinCutiJenisNotFound = 'JENIS_NOT_FOUND';
const String kIzinCutiReviewerNotFound = 'REVIEWER_NOT_FOUND';
const String kIzinCutiOverlap = 'CUTI_OVERLAP';
const String kIzinCutiJatahHabis = 'JATAH_CUTI_HABIS';

extension ColorExtension on Color {
  Color withDarkness(double factor) {
    return Color.fromARGB(
      alpha,
      (red * factor).toInt(),
      (green * factor).toInt(),
      (blue * factor).toInt(),
    );
  }

  Color withLightness(double factor) {
    return Color.fromARGB(
      alpha,
      (red + (255 - red) * factor).toInt(),
      (green + (255 - green) * factor).toInt(),
      (blue + (255 - blue) * factor).toInt(),
    );
  }

  double get darkness {
    return (red * 0.299 + green * 0.587 + blue * 0.114) / 255;
  }

  Color withContrast(double factor) {
    return darkness > 0.5 ? withDarkness(1 - factor) : withLightness(factor);
  }
}

enum KeteranganJatahCuti {
  inputData('INPUT_DATA'),
  timeOffUsed('PEMAKAIAN_CUTI'),
  timeOffCancellation('PEMBATALAN_CUTI'),
  timeOffRejected('PEMAKAIAN_CUTI_TIDAK_DI_SETUJUI'),
  timeOffAccumulated('AKUMULASI_IZIN');

  final String enumName;
  const KeteranganJatahCuti(this.enumName);

  static KeteranganJatahCuti? parse(String value) {
    for (final item in values) {
      if (item.enumName == value) {
        return item;
      }
    }
    return null;
  }

  String localize(BuildContext context) {
    switch (this) {
      case KeteranganJatahCuti.inputData:
        return 'Input data';
      case KeteranganJatahCuti.timeOffUsed:
        return 'Cuti digunakan';
      case KeteranganJatahCuti.timeOffCancellation:
        return 'Cuti dibatalkan';
      case KeteranganJatahCuti.timeOffRejected:
        return 'Cuti tidak disetujui';
      case KeteranganJatahCuti.timeOffAccumulated:
        return 'Akumulasi izin';
    }
  }
}

double calculateDistance(LatLng a, LatLng b) {
  const double earthRadius = 6371.0;
  final double lat1 = a.latitude;
  final double lon1 = a.longitude;
  final double lat2 = b.latitude;
  final double lon2 = b.longitude;
  final double dLat = (lat2 - lat1) * (pi / 180);
  final double dLon = (lon2 - lon1) * (pi / 180);
  final double x = sin(dLat / 2) * sin(dLat / 2) +
      cos(lat1 * (pi / 180)) *
          cos(lat2 * (pi / 180)) *
          sin(dLon / 2) *
          sin(dLon / 2);
  final double y = 2 * atan2(sqrt(x), sqrt(1 - x));
  final double z = earthRadius * y;
  return z;
}

enum AttendanceStatus {
  holiday('LIBUR'),
  late('TERLAMBAT'),
  lateAndForgotCheckTime('TERLAMBAT & LUPA CHECK TIME'),
  forgotCheckTime('LUPA CHECK TIME'),
  lateAndDeficientHours('TERLAMBAT & KURANG JAM'),
  deficientHours('KURANG JAM'),
  absent('TANPA KETERANGAN'),
  present('HADIR SESUAI KETENTUAN'),
  leave('CUTI'),
  businessTrip('PERJALANAN DINAS'),
  sick('SAKIT'),
  permit('IZIN PRIBADI'),
  forgotCheckIn('LUPA CHECK IN'),
  fieldWork('TUGAS LUAR TANPA SPPD'),
  pendingApproval('MENUNGGU PERSETUJUAN'),
  rejected('DITOLAK');

  final String name;

  const AttendanceStatus(this.name);

  static AttendanceStatus? parse(String status) {
    for (final value in values) {
      if (value.name == status) {
        return value;
      }
    }
    return null;
  }
}

extension FutureNotifierExtension<T> on Future<T> {
  Future<T> notifyError(BuildContext context,
      {Widget Function(
              BuildContext context, Object? error, StackTrace? stackTrace)?
          builder,
      String? message}) {
    return catchError((error, stackTrace) {
      if (!context.mounted) {
        return;
      }
      print(error);
      print(stackTrace);
      showDialog(
        context: context,
        builder: (context) {
          Widget widget;
          if (builder == null) {
            widget = PopupDialog(
              title: Text(context.intl.errorTitle),
              content: Text(message ?? context.intl.errorMessageGeneral),
              actions: [
                PrimaryButton(
                  onPressed: () {
                    Navigator.of(context).pop();
                  },
                  child: Text(context.intl.buttonOk),
                ),
              ],
            );
          } else {
            widget = builder(context, error, stackTrace);
          }
          return widget;
        },
      );
    });
  }
}
