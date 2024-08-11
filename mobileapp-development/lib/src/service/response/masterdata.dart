import 'package:seioffice/src/service/api.dart';
import 'package:seioffice/src/service/response/cuti.dart';
import 'package:seioffice/src/util.dart';

class DeptListResponse extends APIResponse {
  final String msg;
  final int count;
  final List<DataDepartemen> data;

  DeptListResponse.fromJSon(Map<String, dynamic> json)
      : this.msg = json.getString('msg')!,
        this.count = json.getInt('count')!,
        this.data = (json['data'] as List)
            .map((e) => DataDepartemen.fromJson(e as Map<String, dynamic>))
            .toList();
}

class DataDepartemen {
  final String id;
  final String name;

  DataDepartemen.fromJson(Map<String, dynamic> json)
      : this.id = json.getString('Id')!,
        this.name = json.getString('name')!;
}

class HariliburListResponse extends APIResponse {
  final String msg;
  final int count;
  final List<DataHariLibur> data;

  HariliburListResponse.fromJson(Map<String, dynamic> json)
      : this.msg = json.getString('Msg')!,
        this.count = json.getInt('Count')!,
        this.data = (json['data'] as List)
            .map((e) => DataHariLibur.fromJson(e as Map<String, dynamic>))
            .toList();
}

class DataHariLibur {
  final String id;
  final DateTime tanggal;
  final String keterangan;

  DataHariLibur({
    required this.id,
    required this.tanggal,
    required this.keterangan,
  });

  factory DataHariLibur.fromJson(Map<String, dynamic> json) {
    return DataHariLibur(
      id: json['id'] as String,
      tanggal: DateTime.parse(json['Tanggal'] as String),
      keterangan: json['keterangan'] as String,
    );
  }
}

class VehicleListResponse extends APIResponse {
  final String msg;
  final int count;
  final List<VehiclesData> data;

  VehicleListResponse.fromJson(Map<String, dynamic> json)
      : this.msg = json.getString('msg')!,
        this.count = json.getInt('count')!,
        this.data = (json['data'] as List)
            .map((e) => VehiclesData.fromJson(e as Map<String, dynamic>))
            .toList();
}

class VehiclesData {
  final String vehicle_id;
  final String plat_number;
  final String type;
  final String year;
  final String ownership;
  final String certificate_expired;
  final String tax_expired;
  final String merk;
  final String bbm;
  final String keterangan;

  VehiclesData.fromJson(Map<String, dynamic> json)
      : this.vehicle_id = json.getString('vehicle_id')!,
        this.plat_number = json.getString('plat_number')!,
        this.type = json.getString('type')!,
        this.year = json.getString('year')!,
        this.ownership = json.getString('ownership')!,
        this.certificate_expired = json.getString('certificate_expired')!,
        this.tax_expired = json.getString('tax_expired')!,
        this.merk = json.getString('merk')!,
        this.bbm = json.getString('bbm')!,
        this.keterangan = json.getString('Keterangan')!;
}

class VehicleResponse extends APIResponse {
  final String msg;
  final String vehicle_id;
  final String plat_number;
  final String type;
  final String year;
  final String ownership;
  final String certificate_expired;
  final String tax_expired;
  final String merk;
  final String bbm;
  final String keterangan;

  VehicleResponse.fromJson(Map<String, dynamic> json)
      : this.msg = json.getString('msg')!,
        this.vehicle_id = json.getString('vehicle_id')!,
        this.plat_number = json.getString('plat_number')!,
        this.type = json.getString('type')!,
        this.year = json.getString('year')!,
        this.ownership = json.getString('ownership')!,
        this.certificate_expired = json.getString('certificate_expired')!,
        this.tax_expired = json.getString('tax_expired')!,
        this.merk = json.getString('merk')!,
        this.bbm = json.getString('bbm')!,
        this.keterangan = json.getString('Keterangan')!;
}

class JenisIzinCutiListResponse extends APIResponse {
  final String msg;
  final int count;
  final List<JenisIzinCutiData> izinCuti;

  JenisIzinCutiListResponse.fromJson(Map<String, dynamic> json)
      : this.msg = json.getString('msg')!,
        this.count = json.getInt('count')!,
        this.izinCuti = (json['izinCuti'] as List)
            .map((e) => JenisIzinCutiData.fromJson(e as Map<String, dynamic>))
            .toList();
}
