import 'package:seioffice/src/service/api.dart';
import 'package:seioffice/src/service/response/absen.dart';
import 'package:seioffice/src/service/response/order.dart';
import 'package:seioffice/src/util.dart';

class OrderAppRequest extends APIRequest<OrderListResponse> {
  final String pid;

  OrderAppRequest({
    required this.pid,
  });

  @override
  APIMethod get method => apiGet;

  @override
  APIResponse parseResponse(JsonObject json) {
    return OrderListResponse.fromJson(json);
  }

  @override
  String get path => '/order/app/$pid';
}

class OrderDayRequest extends APIRequest<OrderListResponse> {
  final int thn;
  final int bln;
  final int tgl;

  OrderDayRequest({
    required this.thn,
    required this.bln,
    required this.tgl,
  });

  @override
  APIMethod get method => apiGet;

  @override
  APIResponse parseResponse(JsonObject json) {
    return OrderListResponse.fromJson(json);
  }

  @override
  String get path => '/order/day/$thn/$bln/$tgl';
}

class OrderMonthRequest extends APIRequest<OrderListResponse> {
  final int thn;
  final int bln;

  OrderMonthRequest({
    required this.bln,
    required this.thn,
  });

  @override
  APIMethod get method => apiGet;

  @override
  APIResponse parseResponse(JsonObject json) {
    return OrderListResponse.fromJson(json);
  }

  @override
  String get path => '/order/month/$thn/$bln';
}

class OrderNaRequest extends APIRequest<OrderListResponse> {
  final String pid;

  OrderNaRequest({
    required this.pid,
  });

  @override
  APIMethod get method => apiGet;

  @override
  APIResponse parseResponse(JsonObject json) {
    return OrderListResponse.fromJson(json);
  }

  @override
  String get path => '/order/na/$pid';
}

class OrderRequest extends APIRequest<OrderListResponse> {
  final String pid;

  OrderRequest({
    required this.pid,
  });

  @override
  APIMethod get method => apiGet;

  @override
  APIResponse parseResponse(JsonObject json) {
    return OrderListResponse.fromJson(json);
  }

  @override
  String get path => '/order/$pid';
}

class OrderPemesanRequest extends APIRequest<OrderListResponse> {
  final String pid;

  OrderPemesanRequest({
    required this.pid,
  });

  @override
  APIMethod get method => apiGet;
  @override
  APIResponse parseResponse(JsonObject json) {
    return OrderListResponse.fromJson(json);
  }

  @override
  String get path => '/order/pemesan/$pid';
}

class PostOrderRequest extends APIRequest<MessageResponse> {
  final List<String> pengguna;
  final String waktu_berangkat;
  final String tgl_kembali;
  final String tujuan;
  final String keperluan;
  final String kode_proyek;
  final String keterangan;
  final String pemesan;

  PostOrderRequest({
    required this.pengguna,
    required this.waktu_berangkat,
    required this.tgl_kembali,
    required this.tujuan,
    required this.keperluan,
    required this.kode_proyek,
    required this.keterangan,
    required this.pemesan,
  });

  @override
  // TODO: implement method
  APIMethod get method => apiPost;

  @override
  APIResponse parseResponse(JsonObject json) {
    // TODO: implement parseResponse
    return MessageResponse.fromJson(json);
  }

  @override
  // TODO: implement path
  String get path => '/order';

  @override
  Map<String, dynamic> get body {
    return {
      'pengguna': pengguna,
      'waktu_berangkat': waktu_berangkat,
      'tgl_kembali': tgl_kembali,
      'tujuan': tujuan,
      'keperluan': keperluan,
      'kode_proyek': kode_proyek,
      'keterangan': keterangan,
      'pemesan': pemesan,
    };
  }
}
