import 'package:seioffice/src/service/api.dart';
import 'package:seioffice/src/util.dart';

class AbsenDayDashResponse extends APIResponse {
  final String msg;
  final String tgl;
  final String hari;
  final int sum_check_time;
  final int sum_check_sekper;
  final int sum_check_time_engineer;
  final int sum_check_time_mppp;
  final int sum_check_time_keuangan;
  final int sum_check_time_sdmu;
  final int sum_check_time_logistik;
  final int sum_check_time_pemasaran;
  final int sum_check_time_bisnis;

  AbsenDayDashResponse.fromJson(Map<String, dynamic> json)
      : this.msg = json.getString('msg')!,
        this.tgl = json.getString('tgl')!,
        this.hari = json.getString('hari')!,
        this.sum_check_time = json.getInt('sum_check_time')!,
        this.sum_check_sekper = json.getInt('sum_check_sekper')!,
        this.sum_check_time_engineer = json.getInt('sum_check_time_engineer')!,
        this.sum_check_time_mppp = json.getInt('sum_check_time_mppp')!,
        this.sum_check_time_keuangan = json.getInt('sum_check_time_keuangan')!,
        this.sum_check_time_sdmu = json.getInt('sum_check_time_sdmu')!,
        this.sum_check_time_logistik = json.getInt('sum_check_time_logistik')!,
        this.sum_check_time_pemasaran =
            json.getInt('sum_check_time_pemasaran')!,
        this.sum_check_time_bisnis = json.getInt('sum_check_time_bisnis')!;
}

class AbsenDayResponse extends APIResponse {
  final String msg;
  final String tgl;
  final String hari;
  final int count;
  final List<AbsenDay> data;

  AbsenDayResponse.fromJson(Map<String, dynamic> json)
      : this.msg = json.getString('msg')!,
        this.tgl = json.getString('tgl')!,
        this.hari = json.getString('hari')!,
        this.count = json.getInt('count')!,
        this.data = (json['data'] as List)
            .map((e) => AbsenDay.fromJson(e as Map<String, dynamic>))
            .toList();
}

class AbsenDay {
  final String empcode;
  final String name;
  final String jam_masuk;
  final String jam_keluar;

  AbsenDay.fromJson(Map<String, dynamic> json)
      : this.empcode = json.getString('empcode')!,
        this.name = json.getString('name')!,
        this.jam_masuk = json.getString('jam_masuk')!,
        this.jam_keluar = json.getString('jam_keluar')!;
}

class MessageResponse extends APIResponse {
  final String msg;

  MessageResponse.fromJson(Map<String, dynamic> json)
      : this.msg = json.getString("msg")!;
}

class RekapAbsenResponse extends APIResponse {
  final String msg;
  final int count;
  final List<DataRekapAbsen> data;

  RekapAbsenResponse.fromJson(Map<String, dynamic> json)
      : this.msg = json.getString('msg')!,
        this.count = json.getInt('count')!,
        this.data = (json['data'] as List)
            .map((e) => DataRekapAbsen.fromJson(e as Map<String, dynamic>))
            .toList();
}

class DataRekapAbsen {
  final String employee_code;
  final String name;
  final String nik;
  final String status;
  final String email;
  final int month;
  final int year;
  final int jumlah_cuti_awal;
  final int jumlah_sisa_cuti;
  final int jumlah_lupa;
  final int jumlah_form_lupa;
  final int jumlah_terlambat;
  final int jumlah_izin;
  final int jumlah_cuti;
  final int jumlah_tanpa_keterangan;
  final int jumlah_izin_hari;
  final int jumlah_sakit;
  final int jumlah_cuti_terpakai;
  final int jumlah_kurang_jam;
  final double thp;
  final double pemotong_harian;
  final double potongan_lupa_checkin;
  final double potongan_tanpa_keterangan;
  final double potongan_kurang_jam;
  final double potongan_terlambat;
  final double potongan_izin;
  final double total_potongan;

  DataRekapAbsen.fromJson(Map<String, dynamic> json)
      : this.employee_code = json.getString('employee_code')!,
        this.name = json.getString('name')!,
        this.nik = json.getString('nik')!,
        this.status = json.getString('status')!,
        this.email = json.getString('email')!,
        this.month = json.getInt('month')!,
        this.year = json.getInt('year')!,
        this.jumlah_cuti_awal = json.getInt('jumlah_cuti_awal')!,
        this.jumlah_sisa_cuti = json.getInt('jumlah_sisa_cuti')!,
        this.jumlah_lupa = json.getInt('jumlah_lupa')!,
        this.jumlah_form_lupa = json.getInt('jumlah_form_lupa')!,
        this.jumlah_terlambat = json.getInt('jumlah_terlambat')!,
        this.jumlah_izin = json.getInt('jumlah_izin')!,
        this.jumlah_cuti = json.getInt('jumlah_cuti')!,
        this.jumlah_tanpa_keterangan = json.getInt('jumlah_tanpa_keterangan')!,
        this.jumlah_izin_hari = json.getInt('jumlah_izin_hari')!,
        this.jumlah_sakit = json.getInt('jumlah_sakit')!,
        this.jumlah_cuti_terpakai = json.getInt('jumlah_cuti_terpakai')!,
        this.jumlah_kurang_jam = json.getInt('jumlah_kurang_jam')!,
        this.thp = json.getDouble('thp')!,
        this.pemotong_harian = json.getDouble('pemotong_harian')!,
        this.potongan_lupa_checkin = json.getDouble('potongan_lupa_checkin')!,
        this.potongan_tanpa_keterangan =
            json.getDouble('potongan_tanpa_keterangan')!,
        this.potongan_kurang_jam = json.getDouble('potongan_kurang_jam')!,
        this.potongan_terlambat = json.getDouble('potongan_terlambat')!,
        this.potongan_izin = json.getDouble('potongan_izin')!,
        this.total_potongan = json.getDouble('total_potongan')!;
}

class AbsenSayaResponse extends APIResponse {
  final String msg;
  final String employee_code;
  final int jumlah_data;
  final int total_terlambat;
  final int total_lupa_check;
  final int total_form_lupa;
  final int total_tanpa_keterangan;
  final int total_kurang_jam;
  final int total_kurang_kerja;
  final int total_izin_hari;
  final int total_izin_menit;
  final List<DataAbsenSaya> data;
  AbsenSayaResponse.fromJson(Map<String, dynamic> json)
      : this.msg = json.getString('msg')!,
        this.employee_code = json.getString('employee_code')!,
        this.jumlah_data = json.getInt('jumlah_data')!,
        this.total_terlambat = json.getInt('total_terlambat')!,
        this.total_lupa_check = json.getInt('total_lupa_check')!,
        this.total_form_lupa = json.getInt('total_form_lupa')!,
        this.total_tanpa_keterangan = json.getInt('total_tanpa_ket')!,
        this.total_kurang_jam = json.getInt('total_kurang_jam')!,
        this.total_kurang_kerja = json.getInt('total_kurang_kerja')!,
        this.total_izin_hari = json.getInt('total_izin_hari')!,
        this.total_izin_menit = json.getInt('total_izin_menit')!,
        this.data = (json['data'] as List?)
                ?.map((e) => DataAbsenSaya.fromJson(e as Map<String, dynamic>))
                .toList() ??
            [];
}

class AccessMap {
  final bool absensi;
  final bool absensaya;
  final bool rekapabsen;
  final bool master;
  final bool karyawan;
  final bool libur;
  final bool employee;
  final bool kontraklist;
  final bool empabsen;
  final bool kontrakeval;
  final bool dashkaryawan;
  final bool absenmentah;
  final bool izincuti;
  final bool ketidakhadiran;
  final bool proyek;
  final bool dashproyek;
  final bool budget;
  final bool dbp;
  final bool purchase;
  final bool nav;
  final bool mobil;
  final bool dashcar;
  final bool carorder;
  final bool carunit;
  final bool carorderrekap;
  final bool carorderna;
  final bool daftarhadir;

  const AccessMap({
    this.absensi = false,
    this.absensaya = false,
    this.rekapabsen = false,
    this.master = false,
    this.karyawan = false,
    this.libur = false,
    this.employee = false,
    this.kontraklist = false,
    this.empabsen = false,
    this.kontrakeval = false,
    this.dashkaryawan = false,
    this.absenmentah = false,
    this.izincuti = false,
    this.ketidakhadiran = false,
    this.proyek = false,
    this.dashproyek = false,
    this.budget = false,
    this.dbp = false,
    this.purchase = false,
    this.nav = false,
    this.mobil = false,
    this.dashcar = false,
    this.carorder = false,
    this.carunit = false,
    this.carorderrekap = false,
    this.carorderna = false,
    this.daftarhadir = false,
  });

  bool hasAccess(AccessMap current) {
    if (absensi && !current.absensi) return false;
    if (absensaya && !current.absensaya) return false;
    if (rekapabsen && !current.rekapabsen) return false;
    if (master && !current.master) return false;
    if (karyawan && !current.karyawan) return false;
    if (libur && !current.libur) return false;
    if (employee && !current.employee) return false;
    if (kontraklist && !current.kontraklist) return false;
    if (empabsen && !current.empabsen) return false;
    if (kontrakeval && !current.kontrakeval) return false;
    if (dashkaryawan && !current.dashkaryawan) return false;
    if (absenmentah && !current.absenmentah) return false;
    if (izincuti && !current.izincuti) return false;
    if (ketidakhadiran && !current.ketidakhadiran) return false;
    if (proyek && !current.proyek) return false;
    if (dashproyek && !current.dashproyek) return false;
    if (budget && !current.budget) return false;
    if (dbp && !current.dbp) return false;
    if (purchase && !current.purchase) return false;
    if (nav && !current.nav) return false;
    if (mobil && !current.mobil) return false;
    if (dashcar && !current.dashcar) return false;
    if (carorder && !current.carorder) return false;
    if (carunit && !current.carunit) return false;
    if (carorderrekap && !current.carorderrekap) return false;
    if (carorderna && !current.carorderna) return false;
    if (daftarhadir && !current.daftarhadir) return false;
    return true;
  }

  AccessMap.fromJson(JsonObject map)
      : absensi = map.getBool('absensi')!,
        absensaya = map.getBool('absensaya')!,
        rekapabsen = map.getBool('rekapabsen')!,
        master = map.getBool('master')!,
        karyawan = map.getBool('karyawan')!,
        libur = map.getBool('libur')!,
        employee = map.getBool('employee')!,
        kontraklist = map.getBool('kontraklist')!,
        empabsen = map.getBool('empabsen')!,
        kontrakeval = map.getBool('kontrakeval')!,
        dashkaryawan = map.getBool('dashkaryawan')!,
        absenmentah = map.getBool('absenmentah')!,
        izincuti = map.getBool('izincuti')!,
        ketidakhadiran = map.getBool('ketidakhadiran')!,
        proyek = map.getBool('proyek')!,
        dashproyek = map.getBool('dashproyek')!,
        budget = map.getBool('budget')!,
        dbp = map.getBool('dbp')!,
        purchase = map.getBool('purchase')!,
        nav = map.getBool('nav')!,
        mobil = map.getBool('mobil')!,
        dashcar = map.getBool('dashcar')!,
        carorder = map.getBool('carorder')!,
        carunit = map.getBool('carunit')!,
        carorderrekap = map.getBool('carorderrekap')!,
        carorderna = map.getBool('carorderna')!,
        daftarhadir = map.getBool('daftarhadir')!;
}

class DataAbsenSaya {
  final String tanggal;
  final String hari;
  final String jam_masuk;
  final String jam_keluar;
  final String status;
  final String keterangan;
  final bool is_cuti;
  final bool is_izin;
  final bool is_sakit;
  final bool is_lupa;
  final bool is_sppd;
  final bool is_tugas;
  final bool is_whole_day;
  final bool is_form_checkin;
  final int kurang_jam;
  final int terlambat;
  // DataAbsenSaya({
  //   required this.tanggal,
  //   required this.hari,
  //   required this.jam_masuk,
  //   required this.jam_keluar,
  //   required this.status,
  //   required this.keterangan,
  //   this.is_cuti,
  //   this.is_izin,
  //   this.is_sakit,
  //   this.is_lupa,
  //   this.is_sppd,
  //   this.is_tugas,
  //   this.is_whole_day,
  //   this.is_form_checkin,
  //   required this.kurang_jam,
  //   required this.terlambat,
  // });
  // is_<jenis_absen> is optional (default to false), everything else is required
  // example: this.is_cuti = false
  DataAbsenSaya({
    required this.tanggal,
    required this.hari,
    required this.jam_masuk,
    required this.jam_keluar,
    required this.status,
    required this.keterangan,
    this.is_cuti = false,
    this.is_izin = false,
    this.is_sakit = false,
    this.is_lupa = false,
    this.is_sppd = false,
    this.is_tugas = false,
    this.is_whole_day = false,
    this.is_form_checkin = false,
    required this.kurang_jam,
    required this.terlambat,
  });
  DataAbsenSaya.fromJson(Map<String, dynamic> json)
      : this.tanggal = json.getString('tanggal')!,
        this.hari = json.getString('hari')!,
        this.jam_masuk = json.getString('jam_masuk')!,
        this.jam_keluar = json.getString('jam_keluar')!,
        this.status = json.getString('status')!,
        this.keterangan = json.getString('keterangan')!,
        this.is_cuti = json.getBool('is_cuti')!,
        this.is_izin = json.getBool('is_izin')!,
        this.is_sakit = json.getBool('is_sakit')!,
        this.is_lupa = json.getBool('is_lupa')!,
        this.is_sppd = json.getBool('is_sppd')!,
        this.is_tugas = json.getBool('is_tugas')!,
        this.is_whole_day = json.getBool('is_whole_day')!,
        this.is_form_checkin = json.getBool('is_form_ checkin')!,
        this.kurang_jam = json.getInt("Kurang Jam")!,
        this.terlambat = json.getInt("Terlambat")!;
}

class AbsenSayathlResponse extends APIResponse {
  final String msg;
  final String employee_code;
  final int jumlah_data;
  final List<DataAbsenSaya> data;

  AbsenSayathlResponse.fromJson(Map<String, dynamic> json)
      : this.msg = json.getString('msg')!,
        this.employee_code = json.getString('employee_code')!,
        this.jumlah_data = json.getInt('jumlah_data')!,
        this.data = (json['data'] as List)
            .map((e) => DataAbsenSaya.fromJson(e as Map<String, dynamic>))
            .toList();
}

class AbsenDashboardResponse extends APIResponse {
  final String msg;
  final String absen_masuk_today;
  final String absen_keluar_today;
  final String absen_masuk_yesterday;
  final String absen_keluar_yesterday;
  final String total_lupa_check_time;
  final String total_terlambat;
  final String total_kurang_jam;

  AbsenDashboardResponse.fromJson(Map<String, dynamic> json)
      : this.msg = json.getString('msg')!,
        this.absen_masuk_today = json.getString('absen_masuk_today')!,
        this.absen_keluar_today = json.getString('absen_keluar_today')!,
        this.absen_masuk_yesterday = json.getString('absen_masuk_yesterday')!,
        this.total_lupa_check_time = json.getString('total_lupa_check_time')!,
        this.total_terlambat = json.getString('total_terlambat')!,
        this.absen_keluar_yesterday = json.getString('absen_keluar_yesterday')!,
        this.total_kurang_jam = json.getString('total_kurang_jam')!;
}

class AbsenMentahResponse extends APIResponse {
  final String msg;
  final int count;
  final List<AbsenMentahData> data;

  AbsenMentahResponse.fromJson(Map<String, dynamic> json)
      : this.msg = json.getString('msg')!,
        this.count = json.getInt('count')!,
        this.data = (json['data'] as List)
            .map((e) => AbsenMentahData.fromJson(e as Map<String, dynamic>))
            .toList();
}

class AbsenMentahData {
  final String empcode;
  final String name;
  final String tgl;
  final String hari;
  final String jam_masuk;
  final String jam_keluar;
  final String status;

  AbsenMentahData.fromJson(Map<String, dynamic> json)
      : this.empcode = json.getString('empcode')!,
        this.name = json.getString('name')!,
        this.tgl = json.getString('tgl')!,
        this.hari = json.getString('hari')!,
        this.jam_masuk = json.getString('jam_masuk')!,
        this.jam_keluar = json.getString('jam_keluar')!,
        this.status = json.getString('status')!;
}
