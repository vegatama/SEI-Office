import 'dart:convert';

import 'package:seioffice/src/service/api.dart';
import 'package:seioffice/src/service/response/absen.dart';
import 'package:seioffice/src/util.dart';

class EmployeeListResponse extends APIResponse {
  final String msg;
  final int count;
  final List<DataEmployee> data;

  EmployeeListResponse.fromJson(JsonObject json)
      : msg = json.getString('msg')!,
        count = json.getInt('count')!,
        data = (json['data'] as List)
            .map((e) => DataEmployee.fromJson(e as JsonObject))
            .toList();
}

class DataEmployee {
  final String id;
  final String email;
  final String name;
  final String nik;
  final String employee_code;
  final String direktorat;
  final String job_title;
  final double? thp;
  final String unit_kerja;
  final String bagian_fungsi;
  final int? sisa_cuti;
  final String status;

  DataEmployee.fromJson(JsonObject json)
      : id = json.getString('id')!,
        email = json.getString('email')!,
        name = json.getString('name')!,
        nik = json.getString('nik')!,
        employee_code = json.getString('employee_code')!,
        direktorat = json.getString('direktorat')!,
        job_title = json.getString('job_title')!,
        thp = json.getDouble('thp'),
        unit_kerja = json.getString('unit_kerja')!,
        bagian_fungsi = json.getString('bagian_fungsi')!,
        sisa_cuti = json.getInt('sisa_cuti'),
        status = json.getString('status')!;
}

class BirthdayResponse extends APIResponse {
  final String msg;
  final int count;
  final List<EmployeeBirthday> data;

  BirthdayResponse.fromJson(JsonObject json)
      : msg = json.getString('msg')!,
        count = json.getInt('count')!,
        data = (json['data'] as List)
            .map((e) => EmployeeBirthday.fromJson(e as JsonObject))
            .toList();
}

class EmployeeBirthday {
  final String name;
  final String birthday;

  EmployeeBirthday.fromJson(JsonObject json)
      : name = json.getString('name')!,
        birthday = json.getString('birthday')!;
}

class CutiDetilResponse extends APIResponse {
  final String msg;
  final String employee_code;
  final String name;
  final int jumlah_data;
  final List<DetilCuti> data;

  CutiDetilResponse.fromJson(JsonObject json)
      : msg = json.getString('msg')!,
        employee_code = json.getString('employee_code')!,
        name = json.getString('name')!,
        jumlah_data = json.getInt('jumlah_data')!,
        data = (json['data'] as List)
            .map((e) => DetilCuti.fromJson(e as JsonObject))
            .toList();
}

class DetilCuti {
  final String tgl;
  final String keterangan;

  DetilCuti.fromJson(JsonObject json)
      : tgl = json.getString('tgl')!,
        keterangan = json.getString('keterangan')!;
}

class EmployeeCutiListResponse extends APIResponse {
  final String msg;
  final int count;
  final List<DataCutiEmployee> data;

  EmployeeCutiListResponse.fromJson(JsonObject json)
      : msg = json.getString('msg')!,
        count = json.getInt('count')!,
        data = (json['data'] as List)
            .map((e) => DataCutiEmployee.fromJson(e as JsonObject))
            .toList();
}

class DataCutiEmployee {
  final String employee_code;
  final String name;
  final String nik;
  final String bagian_fungsi;
  final String sisa_cuti;
  final String tgl_cuti;
  final int jumlah_cuti;

  DataCutiEmployee.fromJson(JsonObject json)
      : employee_code = json.getString('employee_code')!,
        name = json.getString('name')!,
        nik = json.getString('nik')!,
        bagian_fungsi = json.getString('bagian_fungsi')!,
        sisa_cuti = json.getString('sisa_cuti')!,
        tgl_cuti = json.getString('tgl_cuti')!,
        jumlah_cuti = json.getInt('jumlah_cuti')!;
}

class UserLoginResponse extends APIResponse {
  final String msg;
  final String email;
  final String name;
  final String nik;
  final String employee_code;
  final String token;
  final String access;
  final bool create_password;
  final String status;
  final bool is_active;
  final String employee_id;
  final String nav_id;
  final String? avatar;

  UserLoginResponse.fromJson(JsonObject json)
      : msg = json.getString('msg')!,
        email = json.getString('email')!,
        name = json.getString('name')!,
        nik = json.getString('nik')!,
        employee_code = json.getString('employee_code')!,
        token = json.getString('token')!,
        access = json.getString('access')!,
        create_password = json.getBool('create_password')!,
        status = json.getString('status')!,
        is_active = json.getBool('_active')!,
        employee_id = json.getString('employee_id')!,
        nav_id = json.getString('nav_id')!,
        avatar = json.getString('avatar');

  AccessMap? _cachedAccessMap;

  AccessMap get accessMap {
    return _cachedAccessMap ??= AccessMap.fromJson(jsonDecode(access));
  }
}

class EmployeeDetailResponse extends APIResponse {
  final String msg;
  final String email;
  final String name;
  final String nik;
  final String employee_code;
  final String direktorat;
  final String golongan;
  final String job_title;
  final String keterangan;
  final String mobile_phone_no;
  final String person_id_mesin_absen;
  final String phone_no;
  final double? thp;
  final String unit_kerja;
  final String atasan_user_id;
  final String bagian_fungsi;
  final int? grade;
  final int? sisa_cuti;
  final String birthday;
  final String user_id_nav;
  final String first_name;
  final String middle_name;
  final String gender;
  final String place_birthdate;
  final String religion;
  final String address;
  final String post_code;
  final String city;
  final String gol_darah;
  final String status_pernikahan;
  final String npwp;
  final String no_telp_darurat;
  final String status_darurat;
  final String pendidikan_terakhir;
  final String jurusan_pendidikan;
  final String spesialis_pendidikan;
  final String facebook;
  final String twitter;
  final String instagram;
  final int jumlah_keluarga;
  final String nama_istri_suami;
  final String dob_istri_suami;
  final String nama_anak_1;
  final String dob_anak_1;
  final String nama_anak_2;
  final String dob_anak_2;
  final String nama_anak_3;
  final String dob_anak_3;
  final String nama_anak_4;
  final String dob_anak_4;
  final String nama_anak_5;
  final String dob_anak_5;
  final String bank_branch;
  final String bank_account;
  final String bank_branch2;
  final String bank_account2;
  final String company_code;
  final String jabatan;
  final String klasifikasi_level_jabatan;
  final String job_stream;
  final String rumpun_jabatan;
  final String lokasi_kerja;
  final String mulai_kerja;
  final String akhir_kerja;
  final String status_kerja;
  final String? avatar;

  EmployeeDetailResponse.fromJson(JsonObject json)
      : msg = json.getString('msg')!,
        email = json.getString('email')!,
        name = json.getString('name')!,
        nik = json.getString('nik')!,
        employee_code = json.getString('employee_code')!,
        direktorat = json.getString('direktorat')!,
        golongan = json.getString('golongan')!,
        job_title = json.getString('job_title')!,
        keterangan = json.getString('keterangan')!,
        mobile_phone_no = json.getString('mobile_phone_no')!,
        person_id_mesin_absen = json.getString('person_id_mesin_absen')!,
        phone_no = json.getString('phone_no')!,
        thp = json.getDouble('thp'),
        unit_kerja = json.getString('unit_kerja')!,
        atasan_user_id = json.getString('atasan_user_id')!,
        bagian_fungsi = json.getString('bagian_fungsi')!,
        grade = json.getInt('grade'),
        sisa_cuti = json.getInt('sisa_cuti'),
        birthday = json.getString('birthday')!,
        user_id_nav = json.getString('user_id_nav')!,
        first_name = json.getString('first_name')!,
        middle_name = json.getString('middle_name')!,
        gender = json.getString('gender')!,
        place_birthdate = json.getString('place_birthdate')!,
        religion = json.getString('religion')!,
        address = json.getString('address')!,
        post_code = json.getString('post_code')!,
        city = json.getString('city')!,
        gol_darah = json.getString('gol_darah')!,
        status_pernikahan = json.getString('status_pernikahan')!,
        npwp = json.getString('npwp')!,
        no_telp_darurat = json.getString('no_telp_darurat')!,
        status_darurat = json.getString('status_darurat')!,
        pendidikan_terakhir = json.getString('pendidikan_terakhir')!,
        jurusan_pendidikan = json.getString('jurusan_pendidikan')!,
        spesialis_pendidikan = json.getString('spesialis_pendidikan')!,
        facebook = json.getString('facebook')!,
        twitter = json.getString('twitter')!,
        instagram = json.getString('instagram')!,
        jumlah_keluarga = json.getInt('jumlah_keluarga')!,
        nama_istri_suami = json.getString('nama_istri_suami')!,
        dob_istri_suami = json.getString('dob_istri_suami')!,
        nama_anak_1 = json.getString('nama_anak_1')!,
        dob_anak_1 = json.getString('dob_anak_1')!,
        nama_anak_2 = json.getString('nama_anak_2')!,
        dob_anak_2 = json.getString('dob_anak_2')!,
        nama_anak_3 = json.getString('nama_anak_3')!,
        dob_anak_3 = json.getString('dob_anak_3')!,
        nama_anak_4 = json.getString('nama_anak_4')!,
        dob_anak_4 = json.getString('dob_anak_4')!,
        nama_anak_5 = json.getString('nama_anak_5')!,
        dob_anak_5 = json.getString('dob_anak_5')!,
        bank_branch = json.getString('bank_branch')!,
        bank_account = json.getString('bank_account')!,
        bank_branch2 = json.getString('bank_branch2')!,
        bank_account2 = json.getString('bank_account2')!,
        company_code = json.getString('company_code')!,
        jabatan = json.getString('jabatan')!,
        klasifikasi_level_jabatan =
            json.getString('klasifikasi_level_jabatan')!,
        job_stream = json.getString('job_stream')!,
        rumpun_jabatan = json.getString('rumpun_jabatan')!,
        lokasi_kerja = json.getString('lokasi_kerja')!,
        mulai_kerja = json.getString('mulai_kerja')!,
        akhir_kerja = json.getString('akhir_kerja')!,
        status_kerja = json.getString('status_kerja')!,
        avatar = json.getString('avatar');
}

class EmpDashboardResponse extends APIResponse {
  final String msg;
  final int emp_total;
  final int emp_tetap;
  final int emp_ktw;
  final int emp_thl;

  EmpDashboardResponse.fromJson(JsonObject json)
      : msg = json.getString('msg')!,
        emp_total = json.getInt('emp_total')!,
        emp_tetap = json.getInt('emp_tetap')!,
        emp_ktw = json.getInt('emp_ktw')!,
        emp_thl = json.getInt('emp_thl')!;
}
