import 'package:seioffice/src/service/api.dart';
import 'package:seioffice/src/service/response/absen.dart';
import 'package:seioffice/src/util.dart';

class AbsenEmpDashRequest extends APIRequest<AbsenDayDashResponse> {
  final int year;
  final int month;
  final int day;

  AbsenEmpDashRequest({
    required this.year,
    required this.day,
    required this.month,
  });

  @override
  APIMethod get method => apiGet;

  @override
  APIResponse parseResponse(JsonObject json) {
    return AbsenDayDashResponse.fromJson(json);
  }

  @override
  String get path => '/absen/empdash/$year/$month/$day';
}

class AbsenEmpRequest extends APIRequest<AbsenDayResponse> {
  final int year;
  final int month;
  final int day;

  AbsenEmpRequest({
    required this.year,
    required this.day,
    required this.month,
  });

  @override
  APIMethod get method => apiGet;

  @override
  APIResponse parseResponse(JsonObject json) {
    return AbsenDayResponse.fromJson(json);
  }

  @override
  String get path => '/absen/emp/$year/$month/$day';
}

class AbsenKirimEmailRequest extends APIRequest<MessageResponse> {
  final int year;
  final int month;
  final String tanggal_akhir;
  final List<String> employee_code;

  AbsenKirimEmailRequest({
    required this.year,
    required this.month,
    required this.tanggal_akhir,
    required this.employee_code,
  });

  @override
  APIMethod get method => apiGet;

  @override
  APIResponse parseResponse(JsonObject json) {
    return MessageResponse.fromJson(json);
  }

  @override
  String get path => '/absen/kirimemail';

  @override
  Map<String, dynamic> get body {
    return {
      'year': year,
      'month': month,
      'tanggal_akhir': tanggal_akhir,
      'employee_code': employee_code,
    };
  }
}

class AbsenRekapEmpRequest extends APIRequest<MessageResponse> {
  final int year;
  final int month;
  final int day;
  final String emp;

  AbsenRekapEmpRequest({
    required this.year,
    required this.month,
    required this.day,
    required this.emp,
  });

  @override
  APIMethod get method => apiGet;

  @override
  APIResponse parseResponse(JsonObject json) {
    return MessageResponse.fromJson(json);
  }

  @override
  String get path => '/absen/rekapemp/$year/$month/$day/$emp';
}

class AbsenCalculateRequest extends APIRequest<MessageResponse> {
  final int year;
  final int month;
  final String emp;

  AbsenCalculateRequest({
    required this.year,
    required this.month,
    required this.emp,
  });

  @override
  APIMethod get method => apiGet;

  @override
  APIResponse parseResponse(JsonObject json) {
    return MessageResponse.fromJson(json);
  }

  @override
  String get path => '/absen/calculate/$year/$month/$emp';
}

class AbsenRekapRequest extends APIRequest<RekapAbsenResponse> {
  final int year;
  final int month;

  AbsenRekapRequest({
    required this.year,
    required this.month,
  });

  @override
  APIMethod get method => apiGet;

  @override
  APIResponse parseResponse(JsonObject json) {
    return RekapAbsenResponse.fromJson(json);
  }

  @override
  String get path => '/absen/rekap/$year/$month';
}

class AbsenSayaRequest extends APIRequest<AbsenSayaResponse> {
  final String employee_code;
  final int month;
  final int year;

  AbsenSayaRequest({
    required this.employee_code,
    required this.year,
    required this.month,
  });

  @override
  APIMethod get method => apiGet;

  @override
  APIResponse parseResponse(JsonObject json) {
    return AbsenSayaResponse.fromJson(json);
  }

  @override
  String get path => '/absen/saya';

  @override
  Map<String, dynamic> get body {
    return {
      'employee_code': employee_code,
      'month': month,
      'year': year,
    };
  }
}

class AbsenSayaThlRequest extends APIRequest<AbsenSayathlResponse> {
  final String employee_code;
  final int month;
  final int year;

  AbsenSayaThlRequest({
    required this.employee_code,
    required this.month,
    required this.year,
  });

  @override
  APIMethod get method => apiGet;

  @override
  APIResponse parseResponse(JsonObject json) {
    return AbsenSayathlResponse.fromJson(json);
  }

  @override
  String get path => '/absen/sayayhl';

  @override
  Map<String, dynamic> get body {
    return {
      'employee_code': employee_code,
      'month': month,
      'year': year,
    };
  }
}

class AbsenDashboard extends APIRequest<AbsenDashboardResponse> {
  final String emp;

  AbsenDashboard({
    required this.emp,
  });

  @override
  APIMethod get method => apiGet;
  @override
  APIResponse parseResponse(JsonObject json) {
    return AbsenDashboardResponse.fromJson(json);
  }

  @override
  String get path => '/absen/dashboard/$emp';
}

class AbsenMentahRequest extends APIRequest<AbsenMentahResponse> {
  final int year;
  final int month;
  final int date;
  final String dept;

  AbsenMentahRequest({
    required this.year,
    required this.month,
    required this.date,
    required this.dept,
  });

  @override
  APIMethod get method => apiGet;

  @override
  APIResponse parseResponse(JsonObject json) {
    return AbsenDashboardResponse.fromJson(json);
  }

  @override
  String get path => '/absen/mentah/$year/$month/$dept';
}
