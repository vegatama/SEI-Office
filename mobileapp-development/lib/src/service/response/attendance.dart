import 'package:seioffice/src/util.dart';

import '../api.dart';

class AttendanceHistoryResponse extends APIResponse {
  final String message;
  final List<AttendanceData> data;

  AttendanceHistoryResponse.fromJson(JsonObject json)
      : message = json['message'],
        data = (json['data'] as List? ?? [])
            .map((e) => AttendanceData.fromJson(e))
            .toList();
}

class AttendanceData {
  final int day;
  final int month;
  final int year;
  final String? status;
  final LocalTime? firstCheckIn;
  final int approved;
  final String? keterangan;
  final LocalTime? jamMasuk;

  AttendanceData.fromJson(JsonObject json)
      : day = json['day'],
        month = json['month'],
        year = json['year'],
        status = json['status'],
        firstCheckIn = json['firstCheckIn'] != null
            ? LocalTime.parse(json['firstCheckIn'])
            : null,
        approved = json.getInt('approved')!,
        keterangan = json['keterangan'],
        jamMasuk =
            json['jamMasuk'] != null ? LocalTime.parse(json['jamMasuk']) : null;
}

class AttendanceRequestsResponse extends APIResponse {
  final String message;
  final List<EmployeeCheckInApproval> requests;

  AttendanceRequestsResponse.fromJson(JsonObject json)
      : message = json['message'],
        requests = (json['requests'] as List? ?? [])
            .map((e) => EmployeeCheckInApproval.fromJson(e))
            .toList();
}

class EmployeeCheckInApproval {
  final int id;
  final String employeeId;
  final String employeeCode;
  final String employeeName;
  final String employeeJobTitle;
  final String? reason;
  final double latitude;
  final double longitude;
  final double centerLatitude;
  final double centerLongitude;
  final double radius;
  final int approved;
  final LocalDate date;
  final LocalTime time;
  final LocalTime targetTime;
  final bool checkOut;
  final String imageUrl;

  EmployeeCheckInApproval.fromJson(JsonObject json)
      : id = json.getInt('id')!,
        employeeId = json.getString('employeeId')!,
        employeeCode = json.getString('employeeCode')!,
        employeeName = json.getString('employeeName')!,
        employeeJobTitle = json.getString('employeeJobTitle')!,
        reason = json['reason'],
        latitude = json.getDouble('latitude')!,
        longitude = json.getDouble('longitude')!,
        centerLatitude = json.getDouble('centerLatitude')!,
        centerLongitude = json.getDouble('centerLongitude')!,
        radius = json.getDouble('radius')!,
        approved = json.getInt('approved')!,
        date = LocalDate.parse(json['date']),
        time = LocalTime.parse(json['time']),
        targetTime = LocalTime.parse(json['targetTime']),
        checkOut = json.getBool('checkOut')!,
        imageUrl = json['imageUrl'];
}

const kApprovalPending = 0;
const kApprovalApproved = 1;
const kApprovalRejected = -1;

class TodayAttendanceResponse extends APIResponse {
  final String? message;
  final LocalTime? jamMasuk;
  final LocalTime? jamKeluar;
  final LocalTime? checkIn;
  final LocalTime? checkOut;
  final int approved;
  final String? keterangan;
  final String? reviewerEmployeeCode;
  final String? reviewerEmployeeName;
  final double centerLatitude;
  final double centerLongitude;
  final double radius;
  final double? latitude;
  final double? longitude;
  final int approvedCheckOut;
  final String? reviewerCheckOutEmployeeCode;
  final String? reviewerCheckOutEmployeeName;
  final String? keteranganCheckOut;
  final double? latitudeCheckOut;
  final double? longitudeCheckOut;
  final String? imageUrl;
  final String? imageUrlCheckOut;

  TodayAttendanceResponse.fromJson(JsonObject json)
      : message = json['message'],
        jamMasuk =
            json['jamMasuk'] != null ? LocalTime.parse(json['jamMasuk']) : null,
        jamKeluar = json['jamKeluar'] != null
            ? LocalTime.parse(json['jamKeluar'])
            : null,
        checkIn =
            json['checkIn'] != null ? LocalTime.parse(json['checkIn']) : null,
        checkOut =
            json['checkOut'] != null ? LocalTime.parse(json['checkOut']) : null,
        approved = json.getInt('approved')!,
        keterangan = json['keterangan'],
        reviewerEmployeeCode = json['reviewerEmployeeCode'],
        reviewerEmployeeName = json['reviewerEmployeeName'],
        centerLatitude = json.getDouble('centerLatitude')!,
        centerLongitude = json.getDouble('centerLongitude')!,
        radius = json.getDouble('radius')!,
        latitude = json.getDouble('latitude'),
        longitude = json.getDouble('longitude'),
        approvedCheckOut = json.getInt('approvedCheckOut')!,
        reviewerCheckOutEmployeeCode = json['reviewerCheckOutEmployeeCode'],
        reviewerCheckOutEmployeeName = json['reviewerCheckOutEmployeeName'],
        keteranganCheckOut = json['keteranganCheckOut'],
        latitudeCheckOut = json.getDouble('latitudeCheckOut'),
        longitudeCheckOut = json.getDouble('longitudeCheckOut'),
        imageUrl = json['imageUrl'],
        imageUrlCheckOut = json['imageUrlCheckOut'];
}
