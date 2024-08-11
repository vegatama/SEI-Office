import 'package:flutter/cupertino.dart';
import 'package:seioffice/src/util.dart';

import '../api.dart';

class JatahCutiResponse extends APIResponse {
  final String message;
  final int? id;
  final String employeeId;
  final String employeeName;
  final int tahun;
  final int jumlahHari;

  JatahCutiResponse.fromJson(Map<String, dynamic> json)
      : message = json.getString('message')!,
        id = json.getInt('id'),
        employeeId = json.getString('employeeId')!,
        employeeName = json['employeeName'],
        tahun = json.getInt('tahun')!,
        jumlahHari = json.getInt('jumlahHari')!;
}

class JatahCutiListResponse extends APIResponse {
  final String msg;
  final List<JatahCutiData> data;

  JatahCutiListResponse.fromJson(Map<String, dynamic> json)
      : msg = json.getString('msg')!,
        data = (json['jatahCuti'] as List)
            .map((e) => JatahCutiData.fromJson(e))
            .toList();
}

class JatahCutiData {
  final int? id;
  final String employeeId;
  final String employeeName;
  final int tahun;
  final int jumlahCuti;
  final String? keterangan;
  final String? referrer;
  final String? referrerName;
  final DateTime? createdDate;
  final DateTime? updatedDate;

  JatahCutiData.fromJson(Map<String, dynamic> json)
      : id = json.getInt('id'),
        employeeId = json.getString('employeeId')!,
        employeeName = json['employeeName'],
        tahun = json.getInt('tahun')!,
        jumlahCuti = json.getInt('jumlahCuti')!,
        keterangan = json['keterangan'],
        referrer = json['referrer'],
        referrerName = json['referrerName'],
        createdDate = json['createdDate'] == null
            ? null
            : DateTime.parse(json.getString('createdDate')!),
        updatedDate = json['updatedDate'] == null
            ? null
            : DateTime.parse(json.getString('updatedDate')!);
}

class IzinCutiListResponse extends APIResponse {
  final String message;
  final List<IzinCutiData> data;

  IzinCutiListResponse.fromJson(Map<String, dynamic> json)
      : message = json.getString('message')!,
        data = (json['data'] as List)
            .map((e) => IzinCutiData.fromJson(e))
            .toList();
}

class IzinCutiRequestListResponse extends APIResponse {
  final String message;
  final List<IzinCutiRequestData> data;

  IzinCutiRequestListResponse.fromJson(Map<String, dynamic> json)
      : message = json.getString('message')!,
        data = (json['data'] as List)
            .map((e) => IzinCutiRequestData.fromJson(e))
            .toList();
}

class IzinCutiRequestData {
  final int id;
  final IzinCutiStatus status;
  final String jenisName;
  final TipePengajuan tipe;
  final DateTime startDate;
  final DateTime endDate;
  final String employeeName;
  final String employeeJobTitle;
  final String employeeCode;
  final DateTime createdAt;
  final DateTime updatedAt;

  IzinCutiRequestData.fromJson(Map<String, dynamic> json)
      : id = json.getInt('id')!,
        status = IzinCutiStatus.fromString(json.getString('status')!),
        jenisName = json.getString('jenisName')!,
        tipe = TipePengajuan.fromString(json.getString('tipe')!),
        startDate = DateTime.parse(json.getString('startDate')!),
        endDate = DateTime.parse(json.getString('endDate')!),
        employeeName = json.getString('employeeName')!,
        employeeJobTitle = json.getString('employeeJobTitle')!,
        employeeCode = json.getString('employeeCode')!,
        createdAt = DateTime.parse(json.getString('createdAt')!),
        updatedAt = DateTime.parse(json.getString('updatedAt')!);
}

enum IzinCutiStatus {
  PENDING, // time off is waiting for approval
  APPROVED, // time off has been approved by the reviewer
  REJECTED, // time off has been rejected by the reviewer
  CANCELLED, // time off has been cancelled by the user
  EXPIRED, // time off has reached the start date but not yet approved
  ON_GOING, // time off is currently happening
  DONE, // time off has reached the end
  WAITING; // waiting for previous reviewer to approve

  static IzinCutiStatus fromString(String value) {
    switch (value) {
      case 'PENDING':
        return IzinCutiStatus.PENDING;
      case 'APPROVED':
        return IzinCutiStatus.APPROVED;
      case 'REJECTED':
        return IzinCutiStatus.REJECTED;
      case 'CANCELLED':
        return IzinCutiStatus.CANCELLED;
      case 'EXPIRED':
        return IzinCutiStatus.EXPIRED;
      case 'ON_GOING':
        return IzinCutiStatus.ON_GOING;
      case 'DONE':
        return IzinCutiStatus.DONE;
      case 'WAITING':
        return IzinCutiStatus.WAITING;
      default:
        throw ArgumentError('Unknown IzinCutiStatus: $value');
    }
  }
}

enum TipePengajuan {
  HARIAN,
  MENIT;

  static TipePengajuan fromString(String value) {
    switch (value) {
      case 'HARIAN':
        return TipePengajuan.HARIAN;
      case 'MENIT':
        return TipePengajuan.MENIT;
      default:
        throw ArgumentError('Unknown TipePengajuan: $value');
    }
  }

  String localize(BuildContext context) {
    switch (this) {
      case TipePengajuan.HARIAN:
        return 'Harian';
      case TipePengajuan.MENIT:
        return 'Menit';
    }
  }
}

class IzinCutiData {
  final int id;
  final IzinCutiStatus status;
  final String jenisName;
  final TipePengajuan tipe;
  final DateTime startDate;
  final DateTime endDate;
  final DateTime createdAt;
  final DateTime updatedAt;

  IzinCutiData.fromJson(Map<String, dynamic> json)
      : id = json.getInt('id')!,
        status = IzinCutiStatus.fromString(json.getString('status')!),
        jenisName = json.getString('jenisName')!,
        tipe = TipePengajuan.fromString(json.getString('tipe')!),
        startDate = DateTime.parse(json.getString('startDate')!),
        endDate = DateTime.parse(json.getString('endDate')!),
        createdAt = DateTime.parse(json.getString('createdAt')!),
        updatedAt = DateTime.parse(json.getString('updatedAt')!);
}

class IzinCutiDetailResponse extends APIResponse {
  final String message;
  final int id;
  final String employeeCode;
  final String employeeName;
  final String employeeJobTitle;
  final IzinCutiStatus status;
  final JenisIzinCutiData jenis;
  final DateTime startDate;
  final DateTime endDate;
  final String reason;
  final List<IzinCutiApprovalData> approvals;
  final List<IzinCutiFileData> files;
  final DateTime createdAt;
  final DateTime updatedAt;

  IzinCutiDetailResponse.fromJson(Map<String, dynamic> json)
      : message = json.getString('message')!,
        id = json.getInt('id')!,
        status = IzinCutiStatus.fromString(json.getString('status')!),
        jenis = JenisIzinCutiData.fromJson(json['jenis']),
        startDate = DateTime.parse(json.getString('startDate')!),
        endDate = DateTime.parse(json.getString('endDate')!),
        reason = json.getString('reason')!,
        approvals = (json['approvals'] as List)
            .map((e) => IzinCutiApprovalData.fromJson(e))
            .toList(),
        files = (json['files'] as List)
            .map((e) => IzinCutiFileData.fromJson(e))
            .toList(),
        employeeCode = json.getString('employeeCode')!,
        employeeName = json.getString('employeeName')!,
        employeeJobTitle = json.getString('employeeJobTitle')!,
        createdAt = DateTime.parse(json.getString('createdAt')!),
        updatedAt = DateTime.parse(json.getString('updatedAt')!);
}

class JenisIzinCutiData {
  final int id;
  final String namaJenis;
  final bool cutCuti;
  final List<JenisIzinCutiAbsoluteReviewer>? reviewers;
  final TipePengajuan pengajuan;

  JenisIzinCutiData.fromJson(Map<String, dynamic> json)
      : id = json.getInt('id')!,
        namaJenis = json.getString('namaJenis')!,
        cutCuti = json.getBool('cutCuti') ?? false,
        reviewers = (json['reviewers'] as List?)
            ?.map((e) => JenisIzinCutiAbsoluteReviewer.fromJson(e))
            .toList(),
        pengajuan = TipePengajuan.fromString(json.getString('pengajuan')!);
}

class JenisIzinCutiAbsoluteReviewer {
  final String empCode;
  final String empName;
  final String empJobTitle;
  final String? empAvatar;
  final int? layerIndex;

  JenisIzinCutiAbsoluteReviewer.fromJson(Map<String, dynamic> json)
      : empCode = json.getString('empCode')!,
        empName = json.getString('empName')!,
        layerIndex = json.getInt('layerIndex'),
        empJobTitle = json.getString('empJobTitle')!,
        empAvatar = json['empAvatar'];

  @override
  String toString() {
    return 'JenisIzinCutiAbsoluteReviewer{empCode: $empCode, empName: $empName, layerIndex: $layerIndex}';
  }

  @override
  bool operator ==(Object other) {
    if (identical(this, other)) return true;

    return other is JenisIzinCutiAbsoluteReviewer &&
        other.empCode == empCode &&
        other.empName == empName &&
        other.layerIndex == layerIndex;
  }

  @override
  int get hashCode {
    return empCode.hashCode ^ empName.hashCode ^ layerIndex.hashCode;
  }
}

class IzinCutiApprovalData {
  final String reviewerEmployeeCode;
  final String reviewerName;
  final String reviewerJobTitle;
  final IzinCutiStatus status;
  final String? reason;
  final DateTime createdAt;
  final DateTime updatedAt;

  IzinCutiApprovalData.fromJson(Map<String, dynamic> json)
      : reviewerEmployeeCode = json.getString('reviewerEmployeeCode')!,
        reviewerName = json.getString('reviewerName')!,
        reviewerJobTitle = json.getString('reviewerJobTitle')!,
        status = IzinCutiStatus.fromString(json.getString('status')!),
        reason = json['reason'],
        createdAt = DateTime.parse(json.getString('createdAt')!),
        updatedAt = DateTime.parse(json.getString('updatedAt')!);
}

class IzinCutiFileData {
  final int id;
  final String fileName;
  final String fileDownloadUri;

  IzinCutiFileData.fromJson(Map<String, dynamic> json)
      : id = json.getInt('id')!,
        fileName = json.getString('fileName')!,
        fileDownloadUri = json.getString('fileDownloadUri')!;
}

class IzinCutiDashboardResponse extends APIResponse {
  final String message;
  final int akumulasiIzin;
  final int sisaCuti;
  final IzinCutiData? cutiAktif;

  IzinCutiDashboardResponse.fromJson(Map<String, dynamic> json)
      : message = json.getString('message')!,
        akumulasiIzin = json.getInt('akumulasiIzin')!,
        sisaCuti = json.getInt('sisaCuti')!,
        cutiAktif = json['cutiAktif'] == null
            ? null
            : IzinCutiData.fromJson(json['cutiAktif']);
}
