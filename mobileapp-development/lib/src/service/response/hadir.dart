import 'package:seioffice/src/service/api.dart';
import 'package:seioffice/src/util.dart';

class DaftarHadirListResponse extends APIResponse {
  final String msg;
  final int count;
  final List<DaftarHadirData> data;

  DaftarHadirListResponse.fromJSon(Map<String, dynamic> json)
      : this.msg = json.getString('msg')!,
        this.count = json.getInt('count')!,
        this.data = (json['data'] as List)
            .map((e) => DaftarHadirData.fromJson(e as Map<String, dynamic>))
            .toList();
}

class DaftarHadirData {
  final String daftar_hadir_id;
  final String kegiatan;
  final String subyek;
  final String tanggal;
  final String pimpinan;
  final String waktu_mulai;
  final String waktu_selesai;
  final String tempat;
  final String jumlah_peserta;
  final String pembuat;
  final List<DataPeserta> data;

  DaftarHadirData.fromJson(Map<String, dynamic> json)
      : this.daftar_hadir_id = json.getString('daftar_hadir_id')!,
        this.kegiatan = json.getString('kegiatan')!,
        this.subyek = json.getString('subyek')!,
        this.tanggal = json.getString('tanggal')!,
        this.pimpinan = json.getString('pimpinan')!,
        this.waktu_mulai = json.getString('waktu_mulai')!,
        this.waktu_selesai = json.getString('waktu_selesai')!,
        this.tempat = json.getString('tempat')!,
        this.jumlah_peserta = json.getString('jmlh_peserta')!,
        this.pembuat = json.getString('pembuat')!,
        this.data = (json['data'] as List)
            .map((e) => DataPeserta.fromJson(e as Map<String, dynamic>))
            .toList();
}

class DataPeserta {
  final String nama;
  final String bagian;
  final String email_phone;
  final String tanda_tangan;

  DataPeserta.fromJson(Map<String, dynamic> json)
      : this.nama = json.getString('nama')!,
        this.bagian = json.getString('bagian')!,
        this.email_phone = json.getString('email_phone')!,
        this.tanda_tangan = json.getString('tanda_Tangan')!;
}
