package com.suryaenergi.sdm.backendapi.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataAbsenSaya {
    private String tanggal;
    private String hari;
    private String jam_masuk;
    private String jam_keluar;
    private String status;
    private String keterangan;
    private boolean is_cuti;
    private boolean is_izin;
    private boolean is_sakit;
    private boolean is_lupa;
    private boolean is_sppd;
    private boolean is_tugas;
    private boolean is_whole_day;
    private boolean is_form_chekin;
    private int kurang_jam;
    private int terlambat;

    // KHUSUS MOBILE
    private double longitude;
    private double latitude;
    public static final String STATUS_LIBUR = "LIBUR";
    public static final String STATUS_MENUNGGU_PERSETUJUAN = "MENUNGGU PERSETUJUAN";
    public static final String STATUS_DITOLAK = "DITOLAK";
    // END OF KHUSUS MOBILE

    // WEB
    public static final String STATUS_TERLAMBAT = "TERLAMBAT";
    public static final String STATUS_TERLAMBAT_DAN_LUPA_CHECK_TIME = "TERLAMBAT & LUPA CHECK TIME";
    public static final String STATUS_LUPA_CHECK_TIME = "LUPA CHECK TIME";
    public static final String STATUS_TERLAMBAT_DAN_KURANG_JAM = "TERLAMBAT & KURANG JAM";
    public static final String STATUS_TANPA_KETERANGAN = "TANPA KETERANGAN";
    public static final String STATUS_KURANG_JAM = "KURANG JAM";
    public static final String STATUS_HADIR_SESUAI_KETENTUAN = "HADIR SESUAI KETENTUAN";
    public static final String STATUS_CUTI = "CUTI";
    public static final String STATUS_PERJALANAN_DINAS = "PERJALANAN DINAS";
    public static final String STATUS_SAKIT = "SAKIT";
    public static final String STATUS_IZIN_PRIBADI = "IZIN PRIBADI";
    public static final String STATUS_LUPA_CHECK_IN = "LUPA CHECK IN";
    public static final String STATUS_TUGAS_LUAR_TANPA_SPPD = "TUGAS LUAR TANPA SPPD";
    //

    public static final LocalTime WORK_HOUR_BEGIN = LocalTime.of(8, 0, 0);
    public static final LocalTime WORK_HOUR_END = LocalTime.of(17, 0, 0);
}
