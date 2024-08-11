import 'package:seioffice/src/service/api.dart';
import 'package:seioffice/src/util.dart';

class ProyekListResponse extends APIResponse {
  final String msg;
  final int count;
  final List<ProyekData> data;

  ProyekListResponse.fromJson(Map<String, dynamic> json)
      : this.msg = json.getString('msg')!,
        this.count = json.getInt('count')!,
        this.data = (json['data'] as List)
            .map((e) => ProyekData.fromJson(e as Map<String, dynamic>))
            .toList();
}

class ProyekData {
  final String project_name;
  final String project_code;
  final String project_type;
  final String project_value;
  final String pimpro;
  final bool carry_over;
  final String tgl_mulai;
  final String tgl_akhir;
  final String status;
  final String closing_date;

  ProyekData.fromJson(Map<String, dynamic> json)
      : this.project_name = json.getString('project_name')!,
        this.project_code = json.getString('project_code')!,
        this.project_type = json.getString('project_type')!,
        this.project_value = json.getString('project_value')!,
        this.pimpro = json.getString('pimpro')!,
        this.carry_over = json.getBool('carry_over')!,
        this.tgl_mulai = json.getString('tgl_mulai')!,
        this.tgl_akhir = json.getString('tgl_akhir')!,
        this.status = json.getString('status')!,
        this.closing_date = json.getString('closing_date')!;
}

class ProyekDetailResponse extends APIResponse {
  final String msg;
  final ProyekData proyek;
  final List<ProyekBudgetDetail> budgets;
  final List<ProyekDpbDetail> dpbs;

  ProyekDetailResponse.fromJson(Map<String, dynamic> json)
      : this.msg = json.getString('msg')!,
        this.proyek =
            ProyekData.fromJson(json['proyek'] as Map<String, dynamic>),
        this.budgets = (json['budgets'] as List)
            .map((e) => ProyekBudgetDetail.fromJson(e as Map<String, dynamic>))
            .toList(),
        this.dpbs = (json['dpbs'] as List)
            .map((e) => ProyekDpbDetail.fromJson(e as Map<String, dynamic>))
            .toList();
}

class ProyekBudgetDetail {
  final String gl_account;
  final String gl_name;
  final String budget;
  final String penggunaan;
  final String realisasi;

  ProyekBudgetDetail.fromJson(Map<String, dynamic> json)
      : this.gl_account = json.getString('gl_account')!,
        this.gl_name = json.getString('gl_name')!,
        this.budget = json.getString('budget')!,
        this.penggunaan = json.getString('penggunaan')!,
        this.realisasi = json.getString('realisasi')!;
}

class ProyekDpbDetail {
  final String project_code;
  final String project_name;
  final String document_no;
  final String document_date;
  final String amount;
  final String amount_inc_vat;
  final String tgl_dibutuhkan;
  final String status;

  ProyekDpbDetail.fromJson(Map<String, dynamic> json)
      : this.project_code = json.getString('project_code')!,
        this.project_name = json.getString('project_name')!,
        this.document_no = json.getString('document_no')!,
        this.document_date = json.getString('document_date')!,
        this.amount = json.getString('amount')!,
        this.amount_inc_vat = json.getString('amount_inc_vat')!,
        this.tgl_dibutuhkan = json.getString('tgl_dibutuhkan')!,
        this.status = json.getString('status')!;
}

class DpbRealisasiDetailResponse extends APIResponse {
  final String msg;
  final ProyekDpbDetail dpb_detail;
  final List<DpbLineRealisasi> dpb_lines;

  DpbRealisasiDetailResponse.fromJson(Map<String, dynamic> json)
      : this.msg = json.getString('msg')!,
        this.dpb_detail = ProyekDpbDetail.fromJson(
            json['dpb_detail'] as Map<String, dynamic>),
        this.dpb_lines = (json['dpb_lines'] as List)
            .map((e) => DpbLineRealisasi.fromJson(e as Map<String, dynamic>))
            .toList();
}

class DpbLineRealisasi {
  final String item_no;
  final String item_description;
  final String qty;
  final String unit_of_measure;
  final String direct_unit_cost;
  final String amount;
  final String amount_inc_vat;
  final String vat_percent;
  final String unit_cost_lcy;
  final List<P0Detail> data;

  DpbLineRealisasi.fromJson(Map<String, dynamic> json)
      : this.item_no = json.getString('item_no')!,
        this.item_description = json.getString('item_description')!,
        this.qty = json.getString('qty')!,
        this.unit_of_measure = json.getString('unit_of_measure')!,
        this.direct_unit_cost = json.getString('direct_unit_cost')!,
        this.amount = json.getString('amount')!,
        this.amount_inc_vat = json.getString('amount_inc_vat')!,
        this.vat_percent = json.getString('vat_percent')!,
        this.unit_cost_lcy = json.getString('unit_cost_lcy')!,
        this.data = (json['data'] as List)
            .map((e) => P0Detail.fromJson(e as Map<String, dynamic>))
            .toList();
}

class P0Detail {
  final String po_number;
  final String po_project_code;
  final String po_date;
  final String po_qty;
  final String po_status;
  final String po_unit_of_measure;
  final String po_description;
  final String po_direct_unit;
  final String po_vat_percent;
  final String po_amount;
  final String po_amount_include_vat;
  final String po_unit_cost_lcy;
  final String po_buy_from;
  final String po_amount_lcy;

  P0Detail.fromJson(Map<String, dynamic> json)
      : this.po_number = json.getString('po_number')!,
        this.po_project_code = json.getString('po_project_code')!,
        this.po_date = json.getString('po_date')!,
        this.po_qty = json.getString('po_qty')!,
        this.po_status = json.getString('po_status')!,
        this.po_unit_of_measure = json.getString('po_unit_of_measure')!,
        this.po_description = json.getString('po_description')!,
        this.po_direct_unit = json.getString('po_direct_unit')!,
        this.po_vat_percent = json.getString('po_vat_percent')!,
        this.po_amount = json.getString('po_amount')!,
        this.po_amount_include_vat = json.getString('po_amount_include_vat')!,
        this.po_unit_cost_lcy = json.getString('po_unit_cost_lcy')!,
        this.po_buy_from = json.getString('po_buy_from')!,
        this.po_amount_lcy = json.getString('po_amount_lcy')!;
}

class PoListResponse extends APIResponse {
  final String msg;
  final int count;
  final List<P0Detail> data;

  PoListResponse.fromJson(Map<String, dynamic> json)
      : this.msg = json.getString('msg')!,
        this.count = json.getInt('count')!,
        this.data = (json['data'] as List)
            .map((e) => P0Detail.fromJson(e as Map<String, dynamic>))
            .toList();
}
