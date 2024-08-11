import 'package:seioffice/src/service/api.dart';
import 'package:seioffice/src/service/response/absen.dart';
import 'package:seioffice/src/service/response/view.dart';
import 'package:seioffice/src/util.dart';

class ViewAbsenRequest extends APIRequest<AbsenSayaResponse> {
  final String viewid;

  ViewAbsenRequest({
    required this.viewid,
  });

  @override
  APIMethod get method => apiGet;

  @override
  APIResponse parseResponse(JsonObject json) {
    return AbsenSayaResponse.fromJson(json);
  }

  @override
  String get path => '/view/absen/$viewid';
}

class ViewHadirRequest extends APIRequest<DaftarHadirResponse> {
  final String hid;

  ViewHadirRequest({
    required this.hid,
  });

  @override
  APIMethod get method => apiGet;

  @override
  APIResponse parseResponse(JsonObject json) {
    return DaftarHadirResponse.fromJson(json);
  }

  @override
  String get path => '/view/hadir/$hid';
}

class ViewPesertaRequest extends APIRequest<MessageResponse> {
  final String nama;
  final String bagian;
  final String email_phone;
  final String daftar_id;

  ViewPesertaRequest({
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
  String get path => '/view/peserta';

  @override
  Map<String, dynamic> get body {
    return {
      'nama': nama,
      'bagian': bagian,
      'email_phone': email_phone,
      'daftar_id': daftar_id,
    };
  }
}
