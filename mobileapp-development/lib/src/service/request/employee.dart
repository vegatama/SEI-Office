import 'package:seioffice/src/service/api.dart';
import 'package:seioffice/src/service/response/absen.dart';
import 'package:seioffice/src/service/response/employee.dart';
import 'package:seioffice/src/util.dart';

class EmployeeListRequest extends APIRequest<EmployeeListResponse> {
  final String status;

  EmployeeListRequest({
    required this.status,
  });

  @override
  APIMethod get method => apiGet;

  @override
  APIResponse parseResponse(JsonObject json) {
    return EmployeeListResponse.fromJson(json);
  }

  @override
  String get path => '/employee/listemp';

  @override
  Map<String, dynamic>? get body => {
        'status': status,
      };
}

class EmployeeBirthdayRequest extends APIRequest<BirthdayResponse> {
  @override
  String get path {
    return '/employee/birthday';
  }

  @override
  APIMethod get method {
    return apiGet;
  }

  @override
  APIResponse parseResponse(JsonObject json) {
    return BirthdayResponse.fromJson(json);
  }
}

class EmployeeCutiDetil extends APIRequest<CutiDetilResponse> {
  final String employee_code;
  final int month;
  final int year;

  EmployeeCutiDetil({
    required this.employee_code,
    required this.month,
    required this.year,
  });

  @override
  APIMethod get method => apiGet;

  @override
  APIResponse parseResponse(JsonObject json) {
    return CutiDetilResponse.fromJson(json);
  }

  @override
  String get path => '/employee/cutidetil';

  @override
  Map<String, dynamic> get body {
    return {
      'employee_code': employee_code,
      'month': month,
      'year': year,
    };
  }
}

class EmployeeListCutiRequest extends APIRequest<EmployeeCutiListResponse> {
  final int year;
  final int month;

  EmployeeListCutiRequest({
    required this.year,
    required this.month,
  });

  @override
  String get path {
    return '/employee/list/cuti/$year/$month';
  }

  @override
  APIMethod get method {
    return apiGet;
  }

  @override
  APIResponse parseResponse(JsonObject json) {
    return EmployeeCutiListResponse.fromJson(json);
  }
}

class EmployeeListThlRequest extends APIRequest<EmployeeCutiListResponse> {
  @override
  String get path {
    return '/employee/list/thl';
  }

  @override
  APIMethod get method {
    return apiGet;
  }

  @override
  APIResponse parseResponse(JsonObject json) {
    return EmployeeCutiListResponse.fromJson(json);
  }
}

class EmployeePasswordChangeRequest extends APIRequest<MessageResponse> {
  final String empcode;
  final String old_password;
  final String new_password;

  EmployeePasswordChangeRequest({
    required this.empcode,
    required this.old_password,
    required this.new_password,
  });

  @override
  String get path {
    return '/employee/password/change';
  }

  @override
  APIMethod get method {
    return apiGet;
  }

  @override
  Map<String, dynamic> get body {
    return {
      'empcode': empcode,
      'old_password': old_password,
      'new_password': new_password,
    };
  }

  @override
  APIResponse parseResponse(JsonObject json) {
    return MessageResponse.fromJson(json);
  }
}

class EmployeeCreatePasswordRequest extends APIRequest<MessageResponse> {
  final String password;
  final String empcode;

  EmployeeCreatePasswordRequest({
    required this.password,
    required this.empcode,
  });

  @override
  String get path {
    return '/employee/password/create';
  }

  @override
  APIMethod get method {
    return apiGet;
  }

  @override
  Map<String, dynamic> get body {
    return {
      'password': password,
      'empcode': empcode,
    };
  }

  @override
  APIResponse parseResponse(JsonObject json) {
    return MessageResponse.fromJson(json);
  }
}

class EmployeeListPaginatedRequest extends APIRequest<EmployeeListResponse> {
  final int no;
  final int size;

  EmployeeListPaginatedRequest({
    required this.no,
    required this.size,
  });

  @override
  String get path {
    return '/employee/list/$no/$size';
  }

  @override
  APIMethod get method {
    return apiGet;
  }

  @override
  APIResponse parseResponse(JsonObject json) {
    return EmployeeListResponse.fromJson(json);
  }
}

class LoginRequest extends APIRequest<UserLoginResponse> {
  final String email;
  final String password;

  LoginRequest({
    required this.email,
    required this.password,
  });

  @override
  String get path {
    return '/employee/login';
  }

  @override
  APIMethod get method {
    return apiPost;
  }

  @override
  APIResponse parseResponse(JsonObject json) {
    return UserLoginResponse.fromJson(json);
  }

  @override
  Map<String, dynamic> get body {
    return {
      'email': email,
      'password': password,
    };
  }
}

class EmployeeRequest extends APIRequest<EmployeeDetailResponse> {
  final String id;

  EmployeeRequest({
    required this.id,
  });

  @override
  String get path {
    return '/employee/$id';
  }

  @override
  APIMethod get method {
    return apiGet;
  }

  @override
  APIResponse parseResponse(JsonObject json) {
    return EmployeeDetailResponse.fromJson(json);
  }
}

class EmployeeGetByNavIdRequest extends APIRequest<EmployeeDetailResponse> {
  final String id;

  EmployeeGetByNavIdRequest({
    required this.id,
  });

  @override
  String get path {
    return '/employee/getbynavid/$id';
  }

  @override
  APIMethod get method {
    return apiGet;
  }

  @override
  APIResponse parseResponse(JsonObject json) {
    return EmployeeDetailResponse.fromJson(json);
  }
}

class EmployeeGetByCodeRequest extends APIRequest<EmployeeDetailResponse> {
  final String id;

  EmployeeGetByCodeRequest({
    required this.id,
  });

  @override
  String get path {
    return '/employee/getbycode/$id';
  }

  @override
  APIMethod get method {
    return apiGet;
  }

  @override
  APIResponse parseResponse(JsonObject json) {
    return EmployeeDetailResponse.fromJson(json);
  }
}

class EmployeeDashboardRequest extends APIRequest<EmpDashboardResponse> {
  @override
  String get path {
    return '/employee/dashboard';
  }

  @override
  APIMethod get method {
    return apiGet;
  }

  @override
  APIResponse parseResponse(JsonObject json) {
    return EmpDashboardResponse.fromJson(json);
  }
}

class EmployeeLoginGoogle extends APIRequest<UserLoginResponse> {
  final String token;

  EmployeeLoginGoogle({
    required this.token,
  });

  @override
  String get path {
    return '/employee/login/google';
  }

  @override
  APIMethod get method {
    return apiPost;
  }

  @override
  APIResponse parseResponse(JsonObject json) {
    return UserLoginResponse.fromJson(json);
  }

  @override
  Map<String, dynamic> get body {
    return {
      'token': token,
    };
  }
}

class EmployeeProfileRequest extends APIRequest<EmployeeDetailResponse> {
  final String empcode;

  EmployeeProfileRequest({
    required this.empcode,
  });

  @override
  String get path {
    return '/employee/profile/$empcode';
  }

  @override
  APIMethod get method {
    return apiGet;
  }

  @override
  APIResponse parseResponse(JsonObject json) {
    return EmployeeDetailResponse.fromJson(json);
  }
}
