import 'package:seioffice/src/service/api.dart';
import 'package:seioffice/src/service/response/Proyek.dart';
import 'package:seioffice/src/util.dart';

class ProyekAllRequest extends APIRequest<ProyekListResponse> {
  final String status;

  ProyekAllRequest({
    required this.status,
  });

  @override
  APIMethod get method => apiGet;

  @override
  APIResponse parseResponse(JsonObject json) {
    return ProyekListResponse.fromJson(json);
  }

  @override
  String get path => '/proyek/all';

  @override
  Map<String, dynamic> get body {
    return {
      'status': status,
    };
  }
}

class ProyekDetailRequest extends APIRequest<ProyekDetailResponse> {
  final String kdproyek;

  ProyekDetailRequest({
    required this.kdproyek,
  });

  @override
  APIMethod get method => apiGet;

  @override
  APIResponse parseResponse(JsonObject json) {
    return ProyekDetailResponse.fromJson(json);
  }

  @override
  String get path => '/proyek/detail/$kdproyek/';
}

class ProyekDpbDetailRequest extends APIRequest<DpbRealisasiDetailResponse> {
  final String nodpb;

  ProyekDpbDetailRequest({required this.nodpb});

  @override
  APIMethod get method => apiGet;

  @override
  APIResponse parseResponse(JsonObject json) {
    return DpbRealisasiDetailResponse.fromJson(json);
  }

  @override
  String get path => '/proyek/dpb/detail/$nodpb';
}

class ProyekListRequest extends APIRequest<ProyekListResponse> {
  final int no;
  final int size;

  ProyekListRequest({
    required this.no,
    required this.size,
  });

  @override
  APIMethod get method => apiGet;

  @override
  APIResponse parseResponse(JsonObject json) {
    return ProyekListResponse.fromJson(json);
  }

  @override
  String get path => '/proyek/list/$no/$size';
}

class ProyekList2Request extends APIRequest<PoListResponse> {
  final String tipe;
  final int tahun;

  ProyekList2Request({
    required this.tahun,
    required this.tipe,
  });

  @override
  APIMethod get method => apiGet;

  @override
  APIResponse parseResponse(JsonObject json) {
    return PoListResponse.fromJson(json);
  }

  @override
  String get path => '/proyek/list2/$tipe/$tahun';
}

class ProyekPoAllRequest extends APIRequest<PoListResponse> {
  @override
  APIMethod get method => apiGet;

  @override
  APIResponse parseResponse(JsonObject json) {
    return PoListResponse.fromJson(json);
  }

  @override
  String get path => '/proyek/po/all';
}
