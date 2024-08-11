import 'package:seioffice/src/service/api.dart';
import 'package:seioffice/src/service/response/absen.dart';
import 'package:seioffice/src/service/response/hadir.dart';
import 'package:seioffice/src/service/response/view.dart';
import 'package:seioffice/src/util.dart';

class DeleteHadirRequest extends APIRequest<MessageResponse> {
  final String hid;

  DeleteHadirRequest({
    required this.hid,
  });

  @override
  APIMethod get method => apiDelete;

  @override
  APIResponse parseResponse(JsonObject json) {
    return MessageResponse.fromJson(json);
  }

  @override
  String get path => '/hadir/$hid';
}

class HadirHidRequest extends APIRequest<DaftarHadirResponse> {
  final String hid;

  HadirHidRequest({
    required this.hid,
  });

  @override
  APIMethod get method => apiGet;

  @override
  APIResponse parseResponse(JsonObject json) {
    return DaftarHadirResponse.fromJson(json);
  }

  @override
  String get path => '/hadir/$hid';
}

class HadirListRequest extends APIRequest<DaftarHadirListResponse> {
  final String pid;

  HadirListRequest({
    required this.pid,
  });

  @override
  APIMethod get method => apiGet;

  @override
  APIResponse parseResponse(JsonObject json) {
    return DaftarHadirListResponse.fromJSon(json);
  }

  @override
  String get path => '/hadir/list/$pid';
}

class HadirPesertaRequest extends APIRequest<MessageResponse> {
  final String nama;
  final String bagian;
  final String email_phone;
  final String daftar_id;

  HadirPesertaRequest({
    required this.nama,
    required this.bagian,
    required this.email_phone,
    required this.daftar_id,
  });

  @override
  APIMethod get method => apiPost;

  @override
  APIResponse parseResponse(JsonObject json) {
    return MessageResponse.fromJson(json);
  }

  @override
  String get path => '/hadir/peserta';

  @override
  Map<String, dynamic>? get body => {
        'nama': nama,
        'bagian': bagian,
        'email_phone': email_phone,
        'daftar_id': daftar_id,
      };
}

class PostHadirRequest extends APIRequest<DaftarHadirResponse> {
  final String kegiatan;
  final String pimpinan;
  final String subyek;
  final String tanggal;
  final String pembuat;
  final String waktu_mulai;
  final String waktu_selesai;
  final String tempat;

  PostHadirRequest({
    required this.kegiatan,
    required this.pimpinan,
    required this.subyek,
    required this.tanggal,
    required this.pembuat,
    required this.waktu_mulai,
    required this.waktu_selesai,
    required this.tempat,
  });

  @override
  APIMethod get method => apiPost;

  @override
  APIResponse parseResponse(JsonObject json) {
    return DaftarHadirResponse.fromJson(json);
  }

  @override
  String get path => '/hadir/';

  @override
  Map<String, dynamic>? get body => {
        'kegiatan': kegiatan,
        'pimpinan': pimpinan,
        'subyek': subyek,
        'tanggal': tanggal,
        'pembuat': pembuat,
        'waktu_mulai': waktu_mulai,
        'waktu_selesai': waktu_selesai,
        'tempat': tempat,
      };
}

class PutHadirRequest extends APIRequest<DaftarHadirResponse> {
  final String kegiatan;
  final String pimpinan;
  final String subyek;
  final String tanggal;
  final String pembuat;
  final String waktu_mulai;
  final String waktu_selesai;
  final String tempat;

  PutHadirRequest({
    required this.kegiatan,
    required this.pimpinan,
    required this.subyek,
    required this.tanggal,
    required this.pembuat,
    required this.waktu_mulai,
    required this.waktu_selesai,
    required this.tempat,
  });

  @override
  APIMethod get method => apiPut;

  @override
  APIResponse parseResponse(JsonObject json) {
    return DaftarHadirResponse.fromJson(json);
  }

  @override
  String get path => '/hadir/';

  @override
  Map<String, dynamic>? get body => {
        'kegiatan': kegiatan,
        'pimpinan': pimpinan,
        'subyek': subyek,
        'tanggal': tanggal,
        'pembuat': pembuat,
        'waktu_mulai': waktu_mulai,
        'waktu_selesai': waktu_selesai,
        'tempat': tempat,
      };
}
