import 'package:dio/dio.dart';
import 'package:flutter/foundation.dart';
import 'package:http_parser/http_parser.dart';
import 'package:seioffice/src/service/api.dart';
import 'package:seioffice/src/service/response/absen.dart';
import 'package:seioffice/src/service/response/cuti.dart';

import '../../util.dart';

class GetJatahCutiRequest extends APIRequest<JatahCutiResponse> {
  final String empcode;
  final int tahun;

  GetJatahCutiRequest({
    required this.empcode,
    required this.tahun,
  });

  @override
  String get path {
    return '/cuti/jatah/$empcode/$tahun';
  }

  @override
  APIResponse parseResponse(JsonObject json) {
    return JatahCutiResponse.fromJson(json);
  }

  @override
  APIMethod get method => apiGet;
}

class GetJatahCutiListRequest extends APIRequest<JatahCutiListResponse> {
  final String empcode;
  final int year;
  final int? after;

  GetJatahCutiListRequest({
    required this.empcode,
    required this.year,
    this.after,
  });

  @override
  String get path {
    return '/cuti/jatah';
  }

  @override
  Map<String, dynamic> get queryParameters {
    return {
      'empcode': empcode,
      'year': year,
      if (after != null) 'after': after,
    };
  }

  @override
  APIMethod get method {
    return apiGet;
  }

  @override
  APIResponse parseResponse(JsonObject json) {
    return JatahCutiListResponse.fromJson(json);
  }
}

class GetJatahCutiGroupRequest extends APIRequest<JatahCutiListResponse> {
  final String empcode;

  GetJatahCutiGroupRequest({
    required this.empcode,
  });

  @override
  String get path {
    return '/cuti/jatah/group/$empcode';
  }

  @override
  APIMethod get method {
    return apiGet;
  }

  @override
  APIResponse parseResponse(JsonObject json) {
    return JatahCutiListResponse.fromJson(json);
  }
}

class GetReviewedIzinCutiListRequest
    extends APIRequest<IzinCutiRequestListResponse> {
  final String empcode;
  final int? after;
  final List<IzinCutiStatus>? status;
  final DateTime? start;
  final DateTime? end;

  GetReviewedIzinCutiListRequest({
    required this.empcode,
    this.after,
    this.status,
    this.start,
    this.end,
  });

  @override
  String get path {
    return '/cuti/requests';
  }

  @override
  APIMethod get method {
    return apiGet;
  }

  @override
  Map<String, dynamic> get queryParameters {
    return {
      'empcode': empcode,
      if (after != null) 'after': after,
      if (status != null) 'status': status!.map((e) => e.name).join(','),
      if (start != null) 'start': start!.toString(),
      if (end != null) 'end': end!.toString(),
    };
  }

  @override
  APIResponse parseResponse(JsonObject json) {
    return IzinCutiRequestListResponse.fromJson(json);
  }
}

class GetIzinCutiListRequest extends APIRequest<IzinCutiListResponse> {
  final String empcode;
  final int? after;
  final List<IzinCutiStatus>? status;
  final DateTime? start;
  final DateTime? end;

  GetIzinCutiListRequest({
    required this.empcode,
    this.after,
    this.status,
    this.start,
    this.end,
  });

  @override
  String get path {
    return '/cuti/list';
  }

  @override
  APIMethod get method {
    return apiGet;
  }

  @override
  Map<String, dynamic> get queryParameters {
    return {
      'empcode': empcode,
      if (after != null) 'after': after,
      if (status != null) 'status': status!.map((e) => e.name).join(','),
      if (start != null) 'start': start!.toString(),
      if (end != null) 'end': end!.toString(),
    };
  }

  @override
  APIResponse parseResponse(JsonObject json) {
    return IzinCutiListResponse.fromJson(json);
  }
}

class GetIzinCutiDetail extends APIRequest<IzinCutiDetailResponse> {
  final int izinCutiId;

  GetIzinCutiDetail({
    required this.izinCutiId,
  });

  @override
  String get path {
    return '/cuti/detail';
  }

  @override
  Map<String, dynamic> get queryParameters {
    return {
      'izinCutiId': izinCutiId,
    };
  }

  @override
  APIMethod get method {
    return apiGet;
  }

  @override
  APIResponse parseResponse(JsonObject json) {
    return IzinCutiDetailResponse.fromJson(json);
  }
}

class ApproveCutiRequest extends APIRequest<MessageResponse> {
  final int izinCutiId;
  final String reviewerEmpCode;

  ApproveCutiRequest({
    required this.izinCutiId,
    required this.reviewerEmpCode,
  });

  @override
  String get path {
    return '/cuti/approve';
  }

  @override
  APIMethod get method {
    return apiGet; // should be post but whatever ig
  }

  @override
  Map<String, dynamic> get queryParameters {
    return {
      'izinCutiId': izinCutiId,
      'reviewerEmpCode': reviewerEmpCode,
    };
  }

  @override
  APIResponse parseResponse(JsonObject json) {
    return MessageResponse.fromJson(json);
  }
}

class RejectCutiRequest extends APIRequest<MessageResponse> {
  final int izinCutiId;
  final String reviewerEmpCode;
  final String reason;

  RejectCutiRequest({
    required this.izinCutiId,
    required this.reviewerEmpCode,
    required this.reason,
  });

  @override
  String get path {
    return '/cuti/reject';
  }

  @override
  APIMethod get method {
    return apiGet; // should be post but whatever ig
  }

  @override
  Map<String, dynamic> get queryParameters {
    return {
      'izinCutiId': izinCutiId,
      'reviewerEmpCode': reviewerEmpCode,
      'reason': reason,
    };
  }

  @override
  APIResponse parseResponse(JsonObject json) {
    return MessageResponse.fromJson(json);
  }
}

class CancelCutiRequest extends APIRequest<MessageResponse> {
  final int izinCutiId;
  final String empCode;

  CancelCutiRequest({
    required this.izinCutiId,
    required this.empCode,
  });

  @override
  String get path {
    return '/cuti/cancel';
  }

  @override
  APIMethod get method {
    return apiGet; // should be post but whatever ig
  }

  @override
  Map<String, dynamic> get queryParameters {
    return {
      'izinCutiId': izinCutiId,
      'empCode': empCode,
    };
  }

  @override
  APIResponse parseResponse(JsonObject json) {
    return MessageResponse.fromJson(json);
  }
}

class IzinCutiRequestFile {
  final String name;
  final Uint8List bytes;
  final MediaType? contentType;

  IzinCutiRequestFile({
    required this.name,
    required this.bytes,
    this.contentType,
  });
}

class IzinCutiRequest extends APIRequest<MessageResponse> {
  final String empcode;
  final int jenis;
  final DateTime start;
  final DateTime end;
  final String reason;
  final List<IzinCutiRequestFile> documents;

  IzinCutiRequest({
    required this.empcode,
    required this.jenis,
    required this.start,
    required this.end,
    required this.reason,
    required this.documents,
  });

  @override
  String get path {
    return '/cuti/request';
  }

  @override
  final bool isMultipart = true;

  @override
  Map<String, dynamic> get body {
    return {
      'empcode': empcode,
      'jenis': jenis,
      'start': start.toIso8601String(),
      'end': end.toIso8601String(),
      'reason': reason,
      'documents': documents.map((e) {
        return MultipartFile.fromBytes(e.bytes,
            filename: e.name, contentType: e.contentType);
      }).toList(),
    };
  }

  @override
  APIMethod get method {
    return apiPost;
  }

  @override
  APIResponse parseResponse(JsonObject json) {
    return MessageResponse.fromJson(json);
  }
}

class IzinCutiDashboardRequest extends APIRequest<IzinCutiDashboardResponse> {
  final String empcode;

  IzinCutiDashboardRequest({
    required this.empcode,
  });

  @override
  String get path {
    return '/cuti/dashboard';
  }

  @override
  Map<String, dynamic> get queryParameters {
    return {
      'empcode': empcode,
    };
  }

  @override
  APIMethod get method {
    return apiGet;
  }

  @override
  APIResponse parseResponse(JsonObject json) {
    return IzinCutiDashboardResponse.fromJson(json);
  }
}
