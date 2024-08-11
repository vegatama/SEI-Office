import 'package:seioffice/src/service/api.dart';
import 'package:seioffice/src/service/response/masterdata.dart';
import 'package:seioffice/src/util.dart';

class OrderListResponse extends APIResponse {
  final String msg;
  final int count;
  final List<OrderVehicleData> data;

  OrderListResponse.fromJson(Map<String, dynamic> json)
      : this.msg = json.getString('msg')!,
        this.count = json.getInt('count')!,
        this.data = (json['data'] as List)
            .map((e) => OrderVehicleData.fromJson(e as Map<String, dynamic>))
            .toList();
}

class OrderVehicleData {
  final String order_id;
  final List<String> users;
  final String waktu_berangkat;
  final String tanggal_kembali;
  final String tujuan;
  final String keperluan;
  final String kode_proyek;
  final String keterangan;
  final String approval;
  final String time_approval;
  final List<VehiclesData> data;
  final String driver;
  final String hp_driver;
  final String waktu_kembali;
  final String status;
  final String need_approve;

  OrderVehicleData.fromJson(Map<String, dynamic> json)
      : this.order_id = json.getString('order_id')!,
        this.users = (json['users'] as List<dynamic>)
            .map((user) => user.toString())
            .toList(),
        this.waktu_berangkat = json.getString('waktu_berangkat')!,
        this.waktu_kembali = json.getString('waktu_kembali')!,
        this.tanggal_kembali = json.getString('tanggal_kembali')!,
        this.tujuan = json.getString('tujuan')!,
        this.keperluan = json.getString('keperluan')!,
        this.kode_proyek = json.getString('kode_proyek')!,
        this.keterangan = json.getString('keterangan')!,
        this.approval = json.getString('approval')!,
        this.time_approval = json.getString('time_approval')!,
        this.driver = json.getString('driver')!,
        this.hp_driver = json.getString('hp_driver')!,
        this.status = json.getString('status')!,
        this.need_approve = json.getString('need_approve')!,
        this.data = (json['data'] as List)
            .map((e) => VehiclesData.fromJson(e as Map<String, dynamic>))
            .toList();
}
