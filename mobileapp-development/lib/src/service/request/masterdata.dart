import 'package:seioffice/src/service/api.dart';
import 'package:seioffice/src/service/response/absen.dart';
import 'package:seioffice/src/service/response/masterdata.dart';
import 'package:seioffice/src/util.dart';

class DeleteMasterVehicleRequest extends APIRequest<MessageResponse> {
  final String vehicle_id;

  DeleteMasterVehicleRequest({
    required this.vehicle_id,
  });

  @override
  APIMethod get method => apiDelete;

  @override
  APIResponse parseResponse(JsonObject json) {
    return MessageResponse.fromJson(json);
  }

  @override
  String get path => '/master/vehicle/$vehicle_id';
}

class MasterDeptListRequest extends APIRequest<DeptListResponse> {
  final int no;
  final int size;

  MasterDeptListRequest({
    required this.no,
    required this.size,
  });

  @override
  APIMethod get method => apiGet;

  @override
  APIResponse parseResponse(JsonObject json) {
    return DeptListResponse.fromJSon(json);
  }

  @override
  String get path => '/master/dept/list/$no/$size';
}

class MasterHariliburListRequest extends APIRequest<HariliburListResponse> {
  final int no;
  final int size;

  MasterHariliburListRequest({
    required this.no,
    required this.size,
  });

  @override
  APIMethod get method => apiGet;

  @override
  APIResponse parseResponse(JsonObject json) {
    return HariliburListResponse.fromJson(json);
  }

  @override
  String get path => '/master/harilibur/list/$no/$size/';
}

class MasterVehicle extends APIRequest<VehicleResponse> {
  final String vehicle_id;

  MasterVehicle({required this.vehicle_id});

  @override
  APIMethod get method => apiGet;

  @override
  APIResponse parseResponse(JsonObject json) {
    return VehicleResponse.fromJson(json);
  }

  @override
  String get path => '/master/vehicle/$vehicle_id';
}

class MasterDeptRequest extends APIRequest<MessageResponse> {
  final String name;

  MasterDeptRequest({required this.name});

  @override
  APIMethod get method => apiPost;

  @override
  APIResponse parseResponse(JsonObject json) {
    return MessageResponse.fromJson(json);
  }

  @override
  String get path => '/master/dept';

  @override
  Map<String, dynamic> get body {
    return {
      'name': name,
    };
  }
}

//POST /master/harilibur
class PostMasterHariliburRequest extends APIRequest<MessageResponse> {
  final String tgl;
  final String keterangan;

  PostMasterHariliburRequest({
    required this.keterangan,
    required this.tgl,
  });

  @override
  APIMethod get method => apiPost;

  @override
  APIResponse parseResponse(JsonObject json) {
    return MessageResponse.fromJson(json);
  }

  @override
  String get path => '/master/harilibur';

  @override
  Map<String, dynamic> get body {
    return {
      'keterangan': keterangan,
      'tgl': tgl,
    };
  }
}

//POST /master/jamkerja
class PostMasterJamkerjaRequest extends APIRequest<MessageResponse> {
  final String tanggal;
  final String keterangan;
  final String jam_masuk;
  final String jam_keluar;

  PostMasterJamkerjaRequest({
    required this.tanggal,
    required this.jam_masuk,
    required this.jam_keluar,
    required this.keterangan,
  });

  @override
  APIMethod get method => apiPost;

  @override
  APIResponse parseResponse(JsonObject json) {
    return MessageResponse.fromJson(json);
  }

  @override
  String get path => '/master/jamkerja/';

  @override
  Map<String, dynamic> get body {
    return {
      'tanggal': tanggal,
      'jam_masuk': jam_masuk,
      'jam_keluar': jam_keluar,
      'keterangan': keterangan,
    };
  }
}

class PostMasterVehicle extends APIRequest<MessageResponse> {
  final String plat_number;
  final String year;
  final String type;
  final String merk;
  final String ownership;
  final String certificate_expired;
  final String tax_expired;
  final String bbm;
  final String keterangan;

  PostMasterVehicle({
    required this.plat_number,
    required this.year,
    required this.type,
    required this.merk,
    required this.ownership,
    required this.certificate_expired,
    required this.tax_expired,
    required this.bbm,
    required this.keterangan,
  });

  @override
  APIMethod get method => apiPost;

  @override
  APIResponse parseResponse(JsonObject json) {
    return MessageResponse.fromJson(json);
  }

  @override
  String get path => '/master/vehicle';

  @override
  Map<String, dynamic> get body {
    return {
      'plat_number': plat_number,
      'year': year,
      'type': type,
      'merk': merk,
      'ownership': ownership,
      'certificate_expired': certificate_expired,
      'tax_expired': tax_expired,
      'bbm': bbm,
      'keterangan': keterangan,
    };
  }
}

class PutMasterVehicle extends APIRequest<MessageResponse> {
  final String vehicle_id;
  final String plat_number;
  final String year;
  final String type;
  final String merk;
  final String ownership;
  final String certificate_expired;
  final String tax_expired;
  final String bbm;
  final String keterangan;

  PutMasterVehicle({
    required this.vehicle_id,
    required this.plat_number,
    required this.year,
    required this.type,
    required this.merk,
    required this.ownership,
    required this.certificate_expired,
    required this.tax_expired,
    required this.bbm,
    required this.keterangan,
  });

  @override
  APIMethod get method => apiPut;

  @override
  APIResponse parseResponse(JsonObject json) {
    return MessageResponse.fromJson(json);
  }

  @override
  String get path => '/master/vehicle';

  @override
  Map<String, dynamic> get body {
    return {
      'vehicle_id': vehicle_id,
      'plat_number': plat_number,
      'year': year,
      'type': type,
      'merk': merk,
      'ownership': ownership,
      'certificate_expired': certificate_expired,
      'tax_expired': tax_expired,
      'bbm': bbm,
      'keterangan': keterangan,
    };
  }
}

class GetJenisIzinCutiListRequest
    extends APIRequest<JenisIzinCutiListResponse> {
  final String empCode;

  GetJenisIzinCutiListRequest({
    required this.empCode,
  });

  @override
  Map<String, dynamic> get queryParameters {
    return {
      'empCode': empCode,
    };
  }

  @override
  APIMethod get method => apiGet;

  @override
  APIResponse parseResponse(JsonObject json) {
    return JenisIzinCutiListResponse.fromJson(json);
  }

  @override
  String get path => '/master/izincuti';
}
