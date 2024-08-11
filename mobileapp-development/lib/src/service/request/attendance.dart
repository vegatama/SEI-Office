import 'package:dio/dio.dart';
import 'package:seioffice/src/service/api.dart';
import 'package:seioffice/src/service/response/absen.dart';
import 'package:seioffice/src/service/response/attendance.dart';

import '../../util.dart';

class AttendanceSwipeRequest extends APIRequest<MessageResponse> {
  final String empcode;
  final double latitude;
  final double longitude;

  AttendanceSwipeRequest({
    required this.empcode,
    required this.latitude,
    required this.longitude,
  });

  @override
  String get path {
    return '/mobileapp/attendance/swipe';
  }

  @override
  APIMethod get method {
    return apiPost;
  }

  @override
  Map<String, dynamic>? get body => {
        'empcode': empcode,
        'latitude': latitude,
        'longitude': longitude,
      };

  @override
  APIResponse parseResponse(JsonObject json) {
    return MessageResponse.fromJson(json);
  }
}

class AttendanceHistoryRequest extends APIRequest<AttendanceHistoryResponse> {
  final String employee_code;
  final LocalDate? from;
  final LocalDate? to;

  AttendanceHistoryRequest({
    required this.employee_code,
    this.from,
    this.to,
  });

  @override
  APIMethod get method => apiGet;

  @override
  APIResponse parseResponse(JsonObject json) {
    return AttendanceHistoryResponse.fromJson(json);
  }

  @override
  String get path => '/mobileapp/attendance/history';

  @override
  Map<String, dynamic> get queryParameters {
    return {
      'employee_code': employee_code,
      'from': from?.toString(),
      'to': to?.toString(),
    };
  }
}

class AttendanceRequestSwipe extends APIRequest<MessageResponse> {
  final String empcode;
  final double latitude;
  final double longitude;
  final String? reason;
  final MultipartFile photo;

  AttendanceRequestSwipe({
    required this.empcode,
    required this.latitude,
    required this.longitude,
    required this.photo,
    this.reason,
  });

  @override
  String get path => '/mobileapp/attendance/requestswipe';

  @override
  APIMethod get method => apiPost;

  @override
  final bool isMultipart = true;

  @override
  Map<String, dynamic> get body {
    return {
      'empcode': empcode,
      'latitude': latitude,
      'longitude': longitude,
      'reason': reason,
      'image': photo,
    };
  }

  @override
  APIResponse parseResponse(JsonObject json) {
    return MessageResponse.fromJson(json);
  }
}

class AttendanceConfirmRequest extends APIRequest<MessageResponse> {
  final int id;
  final bool approve;
  final String empCode;

  AttendanceConfirmRequest({
    required this.id,
    required this.approve,
    required this.empCode,
  });

  @override
  String get path => '/mobileapp/attendance/confirm';

  @override
  APIMethod get method => apiGet;

  @override
  APIResponse parseResponse(JsonObject json) {
    return MessageResponse.fromJson(json);
  }

  @override
  Map<String, dynamic> get queryParameters {
    return {
      'id': id,
      'approve': approve,
      'empCode': empCode,
    };
  }
}

class AttendanceTodayRequest extends APIRequest<TodayAttendanceResponse> {
  final String employeeCode;

  AttendanceTodayRequest({
    required this.employeeCode,
  });

  @override
  String get path => '/mobileapp/attendance/today';

  @override
  APIMethod get method => apiGet;

  @override
  APIResponse parseResponse(JsonObject json) {
    return TodayAttendanceResponse.fromJson(json);
  }

  @override
  Map<String, dynamic> get queryParameters {
    return {
      'employee_code': employeeCode,
    };
  }
}

class AttendanceAtRequest extends APIRequest<TodayAttendanceResponse> {
  final String employeeCode;
  final LocalDate date;

  AttendanceAtRequest({
    required this.employeeCode,
    required this.date,
  });

  @override
  String get path => '/mobileapp/attendance/at';

  @override
  APIMethod get method => apiGet;

  @override
  APIResponse parseResponse(JsonObject json) {
    return TodayAttendanceResponse.fromJson(json);
  }

  @override
  Map<String, dynamic> get queryParameters {
    return {
      'employee_code': employeeCode,
      'year': date.year,
      'month': date.month,
      'day': date.day,
    };
  }
}

class AttendanceApprovalRequestsRequest
    extends APIRequest<AttendanceRequestsResponse> {
  final String employee_code;
  final int? from; // last id for pagination purpose
  final bool showAll; // show all requests or only pending requests
  final bool descending;

  AttendanceApprovalRequestsRequest({
    required this.employee_code,
    this.from,
    this.showAll = true,
    this.descending = true,
  });

  @override
  String get path => '/mobileapp/attendance/requests';

  @override
  APIMethod get method => apiGet;

  @override
  APIResponse parseResponse(JsonObject json) {
    return AttendanceRequestsResponse.fromJson(json);
  }

  @override
  Map<String, dynamic> get queryParameters {
    return {
      'employee_code': employee_code,
      'from': from,
      'showAll': showAll,
      'descending': descending,
    };
  }
}
