import 'package:flutter/material.dart';
import 'package:seioffice/src/service/localization.dart';

class DefaultSEILocalizations extends SEILocalizations {
  static const List<String> _months = [
    'Januari',
    'Februari',
    'Maret',
    'April',
    'Mei',
    'Juni',
    'Juli',
    'Agustus',
    'September',
    'Oktober',
    'November',
    'Desember'
  ];
  static const List<String> _days = [
    'Senin',
    'Selasa',
    'Rabu',
    'Kamis',
    'Jumat',
    'Sabtu',
    'Minggu',
  ];

  const DefaultSEILocalizations();

  @override
  String get loginDividerOr {
    return 'Atau';
  }

  @override
  String get loginTitle {
    return 'Masuk';
  }

  @override
  String get loginChangeLanguage {
    return 'Bahasa';
  }

  @override
  String get loginChangeTheme {
    return 'Tema';
  }

  @override
  String get loginDialogFailedMessage {
    return 'Surel atau kata sandi salah';
  }

  @override
  String get loginDialogFailedTitle {
    return 'Gagal Masuk';
  }

  @override
  String get loginButtonGoogle {
    return 'Masuk dengan Suryaenergi';
  }

  @override
  String get loginButtonLogin {
    return 'Masuk';
  }

  @override
  String get loginHintPassword {
    return 'Kata Sandi';
  }

  @override
  String get loginHintEmail {
    return 'Surel';
  }

  @override
  String get loginSubtitle {
    return 'Masuk menggunakan email perusahaan';
  }

  @override
  String get buttonNo {
    return 'Tidak';
  }

  @override
  String get buttonYes {
    return 'Ya';
  }

  @override
  String get buttonCancel {
    return 'Batal';
  }

  @override
  String get buttonOk {
    return 'Ok';
  }

  @override
  String get loading {
    return 'Memuat';
  }

  @override
  String get greetingMorning {
    return 'Pagi';
  }

  @override
  String get greetingAfternoon {
    return 'Siang';
  }

  @override
  String get greetingEvening {
    return 'Sore';
  }

  @override
  String get greetingNight {
    return 'Malam';
  }

  @override
  String get errorTitle {
    return 'Error';
  }

  @override
  String get errorMessageGeneral {
    return 'Terjadi kesalahan';
  }

  @override
  String formatTime(Duration duration,
      {bool disableAbbreviation = false,
      TimeUnits? highestUnit,
      TimeUnits? lowestUnit}) {
    List<String> result = [];
    if (duration.inDays > 0 &&
        (highestUnit == null || highestUnit.index >= TimeUnits.day.index) &&
        (lowestUnit == null || lowestUnit.index <= TimeUnits.day.index)) {
      result.add(disableAbbreviation
          ? '${duration.inDays} hari'
          : '${duration.inDays}h');
      duration = duration - Duration(days: duration.inDays);
    }
    if (duration.inHours > 0 &&
        (highestUnit == null || highestUnit.index >= TimeUnits.hour.index) &&
        (lowestUnit == null || lowestUnit.index <= TimeUnits.hour.index)) {
      result.add(disableAbbreviation
          ? '${duration.inHours} jam'
          : '${duration.inHours}j');
      duration = duration - Duration(hours: duration.inHours);
    }
    if (duration.inMinutes > 0 &&
        (highestUnit == null || highestUnit.index >= TimeUnits.minute.index) &&
        (lowestUnit == null || lowestUnit.index <= TimeUnits.minute.index)) {
      result.add(disableAbbreviation
          ? '${duration.inMinutes} menit'
          : '${duration.inMinutes}m');
      duration = duration - Duration(minutes: duration.inMinutes);
    }
    if (duration.inSeconds > 0 &&
        (highestUnit == null || highestUnit.index >= TimeUnits.second.index) &&
        (lowestUnit == null || lowestUnit.index <= TimeUnits.second.index)) {
      result.add(disableAbbreviation
          ? '${duration.inSeconds} detik'
          : '${duration.inSeconds}d');
      duration = duration - Duration(seconds: duration.inSeconds);
    }
    if (result.isEmpty) {
      switch (lowestUnit ?? TimeUnits.second) {
        case TimeUnits.second:
          result.add(disableAbbreviation ? '0 detik' : '0d');
          break;
        case TimeUnits.minute:
          result.add(disableAbbreviation ? '0 menit' : '0m');
          break;
        case TimeUnits.hour:
          result.add(disableAbbreviation ? '0 jam' : '0j');
          break;
        case TimeUnits.day:
          result.add(disableAbbreviation ? '0 hari' : '0h');
          break;
      }
    }
    return result.join(' ');
  }

  @override
  String formatTimes(int times) {
    return '$times kali';
  }

  @override
  String get emptyAttendanceHistory {
    return 'Tidak ada riwayat kehadiran';
  }

  @override
  String get serviceTitle {
    return 'Layanan';
  }

  @override
  String get serviceAttendance {
    return 'Kehadiran';
  }

  @override
  String get serviceEvents {
    return 'Acara';
  }

  @override
  String get serviceCalendar {
    return 'Kalender';
  }

  @override
  String get serviceDocuments {
    return 'Dokumen';
  }

  @override
  String get serviceVehicles {
    return 'Kendaraan';
  }

  @override
  String get serviceProjects {
    return 'Proyek';
  }

  @override
  String get serviceAttendanceDescription {
    return 'Isi dan kelola kehadiran';
  }

  @override
  String get serviceEventsDescription {
    return 'Lihat daftar acara';
  }

  @override
  String get serviceCalendarDescription {
    return 'Lihat kalender';
  }

  @override
  String get serviceDocumentsDescription {
    return 'Lihat dokumen';
  }

  @override
  String get serviceVehiclesDescription {
    return 'Lihat kendaraan';
  }

  @override
  String get serviceProjectsDescription {
    return 'Lihat proyek';
  }

  @override
  String get dashboardLateMinutes {
    return 'Total keterlambatan bulan ini';
  }

  @override
  String get dashboardWorkHourDeficiency {
    return 'Total kurang jam kerja bulan ini';
  }

  @override
  String get dashboardForgotCheckTime {
    return 'Total lupa check in/out bulan ini';
  }

  @override
  String get attendanceHistoryTitle {
    return 'Riwayat Absensi';
  }

  @override
  String formatDate(DateTime date, {bool time = false}) {
    if (time) {
      return '${_days[date.weekday - 1]}, ${date.day} ${_months[date.month - 1]} ${date.year} ${date.hour}:${date.minute.toString().padLeft(2, '0')}';
    }
    // Senin, 20 Februari 2021
    return '${_days[date.weekday - 1]}, ${date.day} ${_months[date.month - 1]} ${date.year}';
  }

  @override
  String get attendanceTitle {
    return 'Absensi';
  }

  @override
  String get buttonResetFilter {
    return 'Reset Filter';
  }

  @override
  String get buttonApplyFilter {
    return 'Terapkan Filter';
  }

  @override
  String get attendanceFilterStatus {
    return 'Status';
  }

  @override
  String get attendanceFilterTo {
    return 'Sampai Tanggal';
  }

  @override
  String get attendanceFilterFrom {
    return 'Dari Tanggal';
  }

  @override
  String get attendanceFilterTitle {
    return 'Filter';
  }

  @override
  String get attendanceAttachedPhoto {
    return 'Foto Terlampir';
  }

  @override
  String get attendanceStatus {
    return 'Status';
  }

  @override
  String get attendanceReason {
    return 'Alasan';
  }

  @override
  String get attendanceLateTime {
    return 'Waktu Terlambat';
  }

  @override
  String get attendanceCheckOutTime {
    return 'Waktu Check Out';
  }

  @override
  String get attendanceCheckInTime {
    return 'Waktu Check In';
  }

  @override
  String get attendanceCheckIn {
    return 'Check In';
  }

  @override
  String get attendanceCheckOut {
    return 'Check Out';
  }

  @override
  String get attendanceFilterStatusHoliday {
    return 'Libur';
  }

  @override
  String get attendanceFilterStatusPresent {
    return 'Hadir';
  }

  @override
  String get attendanceFilterStatusPermitted {
    return 'Izin';
  }

  @override
  String get attendanceFilterStatusAbsent {
    return 'Tidak Hadir';
  }

  @override
  String get seeMore {
    return 'Lihat Lebih Banyak';
  }

  @override
  String get attendanceStatusUnknown {
    return 'BELUM ABSEN';
  }

  @override
  String get attendanceStatusPresent {
    return 'HADIR';
  }

  @override
  String get attendanceStatusAbsent {
    return 'TIDAK HADIR';
  }

  @override
  String get attendanceStatusPermitted {
    return 'IZIN';
  }

  @override
  String get attendanceStatusHoliday {
    return 'LIBUR';
  }

  @override
  String get attendanceStatusLate {
    return 'HADIR TERLAMBAT';
  }

  @override
  String getWeekday(int day) {
    return _days[day - 1];
  }

  @override
  String getMonth(int month) {
    return _months[month - 1];
  }

  @override
  String get thisMonth {
    return 'Bulan Ini';
  }

  @override
  String get today {
    return 'Hari Ini';
  }

  @override
  String lateBy(int minutes) {
    Duration duration = Duration(minutes: minutes);
    return 'Terlambat ${formatTime(duration, disableAbbreviation: true)}';
  }

  @override
  String get swipeToCheckIn {
    return 'Geser untuk Check In';
  }

  @override
  String get swipeToCheckOut {
    return 'Geser untuk Check Out';
  }

  @override
  String get checkInSuccessful {
    return 'Check In Berhasil';
  }

  @override
  String get checkOutSuccessful {
    return 'Check Out Berhasil';
  }

  @override
  String get checkInFailed {
    return 'Check In Gagal';
  }

  @override
  String get checkOutFailed {
    return 'Check Out Gagal';
  }

  @override
  String get attendanceStatusPending {
    return 'MENUNGGU PERSETUJUAN';
  }

  @override
  String get attendanceStatusRejected {
    return 'DITOLAK';
  }

  @override
  String get attendanceOutOfRangeTitle {
    return 'Anda Berada di Luar Jangkauan';
  }

  @override
  String get attendanceCheckOutOutOfRangeMessage {
    return 'Proses Check In anda akan membutuhkan persetujuan';
  }

  @override
  String formatTimeOfDay(TimeOfDay time) {
    return '${time.hour.toString().padLeft(2, '0')}:${time.minute.toString().padLeft(2, '0')}';
  }

  @override
  String get profileDataAttribution {
    return 'Atribusi Data';
  }

  @override
  String get profilePrivacy {
    return 'Kebijakan Privasi';
  }

  @override
  String get profileOtherInfo {
    return 'Ketentuan Layanan';
  }

  @override
  String get profileJob {
    return 'Pekerja';
  }

  @override
  String get profilePayment {
    return 'Pembayaran';
  }

  @override
  String get profileJobAdministration {
    return 'Administrasi Pekerjaan';
  }

  @override
  String get profileInformation {
    return 'Data Diri';
  }

  @override
  String get profileAccount {
    return 'Akun';
  }

  @override
  String get leavesTitle {
    return 'Izin dan Cuti';
  }

  @override
  String get recapPermit {
    return 'Rekap Izin dan Cuti';
  }

  @override
  String get remainPermission {
    return 'Akumulasi Izin';
  }

  @override
  String get remainMinute {
    return 'Menit';
  }

  @override
  String get remainDaysOff {
    return 'Sisa Cuti';
  }

  @override
  String get remainDay {
    return 'Hari';
  }

  @override
  String get activeLeave {
    return 'Cuti Aktif';
  }

  @override
  String get remainingLeave {
    return 'Tersisa';
  }

  @override
  String get applyPermissionLeave {
    return 'Ajukan Izin/Cuti';
  }

  @override
  String get permitAndLeaveHistory {
    return 'Ajukan Izin/Cuti';
  }

  @override
  String get leaveAndPermission {
    return 'Cuti dan Izin';
  }

  @override
  String get leaveAndPermissionDescription {
    return 'Kelola dan ajukan cuti/izin anda';
  }

  @override
  String get leaveAndPermissionHistory {
    return 'cuti';
  }

  @override
  String get leaveAndPermissionFail {
    return 'Cuti (Ditolak)';
  }

  @override
  String get seeAll {
    return 'Lihat Semua';
  }

  @override
  String get employmentAdministration {
    return 'Administrasi Pekerjaan';
  }

  @override
  String get companyCode {
    return 'Kode Perusahaan';
  }

  @override
  String get jobTitle {
    return 'Pekerjaan';
  }

  @override
  String get grade {
    return 'Nilai';
  }

  @override
  String get group {
    return 'Golongan';
  }

  @override
  String get part {
    return 'Bagian/Fungsi';
  }

  @override
  String get department {
    return 'Departemen';
  }

  @override
  String get directorate {
    return 'Direktorat';
  }

  @override
  String get jobClassificationLevel {
    return 'Klasifikasi Level Jabatan';
  }

  @override
  String get jobStream {
    return 'Jenis Pekerjaan';
  }

  @override
  String get position {
    return 'Posisi';
  }

  @override
  String get workLocation {
    return 'Lokasi Pekerjaan';
  }

  @override
  String get workingPositionFamily {
    return 'Rumpun Jabatan';
  }

  @override
  String get recapAttendanceDashboard {
    return 'Rekap Kehadiran';
  }

  @override
  String get attendance {
    return 'Kehadiran';
  }

  @override
  String get minuteAttendance {
    return 'Menit';
  }

  @override
  String get totallyLate {
    return 'Total Keterlambatan';
  }

  @override
  String get attendanceHistory {
    return 'Riwayat Kehadiran';
  }

  @override
  String get totalForgotCheckOut {
    return 'Total Lupa Check Out';
  }

  @override
  String get totalShortWorkHour {
    return 'Total Kurang Jam Kerja';
  }

  @override
  String get times {
    return 'Kali';
  }

  @override
  String get entryTime {
    return 'Jam Masuk';
  }

  @override
  String get exitTime {
    return 'Jam Keluar ';
  }

  @override
  String get approved {
    return 'Disetujui';
  }

  @override
  String get rejected {
    return 'Belum Disetujui';
  }

  @override
  String get pending {
    return 'Menunggu Persetujuan';
  }

  @override
  String get leaveHistoryAndPermits {
    return 'Riwayat Cuti dan Izin';
  }

  @override
  String get detailKind {
    return 'Jenis Cuti';
  }

  @override
  String get detailLeaveHistoryType {
    return 'Tipe Cuti';
  }

  @override
  String get detailStartDate {
    return 'Tanggal Mulai';
  }

  @override
  String get detailEndDate {
    return 'Tanggal Selesai';
  }

  @override
  String get detailTimelineText1 {
    return 'Permintaan Diajukan';
  }

  @override
  String get leaveTimelineBegins {
    return 'Cuti Dimulai';
  }

  @override
  String get leaveTimelineEnds {
    return 'Cuti Selesai';
  }

  @override
  String approveBy(String name) {
    return 'Disetujui oleh $name';
  }

  @override
  String pendingBy(String name) {
    return 'Menunggu persetujuan $name';
  }

  @override
  String rejectBy(String name) {
    return 'Ditolak oleh $name';
  }

  @override
  String get detailTabs {
    return 'Detail';
  }

  @override
  String get timeLineTabs {
    return 'Timeline';
  }

  @override
  String get documentTabs {
    return 'Dokumen';
  }

  @override
  String get cancelButton {
    return 'Batalkan Permintaan';
  }

  @override
  String get applicationLeaveNew {
    return 'Pengajuan Izin Dan Cuti';
  }

  @override
  String get applicationListNew {
    return 'Cuti';
  }

  @override
  String get applicationListReject {
    return 'Cuti (Ditolak)';
  }

  @override
  String get requestLeaveAndPermits {
    return 'Permintaan Izin dan Cuti';
  }

  @override
  String get approvedButton {
    return 'Setujui Permintaan';
  }

  @override
  String get rejectedButton {
    return 'Tolak Request';
  }

  @override
  String get name {
    return 'Nama';
  }

  @override
  String get selectLeaveType {
    return 'Pilih Jenis Cuti/Izin';
  }

  @override
  String step(int step, int total) {
    return 'Langkah $step dari $total';
  }

  @override
  String get titleChooseDate {
    return 'Select Leave/Permit Date';
  }

  @override
  String get chooseStartDate {
    return 'Dari Tanggal';
  }

  @override
  String get chooseEndDate {
    return 'Sampai Tanggal';
  }

  @override
  String get chooseDate {
    return 'Pilih Tanggal';
  }

  @override
  String get leaveApplicationReviewer {
    return 'Peninjau Permohonan Cuti/Izin';
  }

  @override
  String get leaveApplicationReviewerDesc {
    return 'Wajib';
  }

  @override
  String get leaveApplicationReviewerDesc2 {
    return 'Opsional';
  }

  @override
  String get remarksAndSupportingFiles {
    return 'Keterangan dan Berkas Pendukung';
  }

  @override
  String get leaveApplicationRemarks {
    return 'Keterangan';
  }

  @override
  String get leaveApplicationSupportingFiles {
    return 'Berkas Pendukung';
  }

  @override
  String get leaveApplicationSupportingFilesDesc {
    return 'Tidak ada berkas yang diunggah';
  }

  @override
  String get leaveApplicationSummary {
    return 'Ringkasan Pengajuan Cuti/Izin';
  }

  @override
  String joinWithAnd(Iterable<String> strings) {
    if (strings.isEmpty) return '';
    if (strings.length == 1) return strings.first;
    if (strings.length == 2) return '${strings.first} dan ${strings.last}';
    return '${strings.take(strings.length - 1).join(', ')}, dan ${strings.last}';
  }

  @override
  String? localizeErrorMessage(String errorMessage) {
    return null;
  }
}
