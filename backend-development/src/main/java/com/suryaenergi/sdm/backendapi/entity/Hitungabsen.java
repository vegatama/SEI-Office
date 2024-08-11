package com.suryaenergi.sdm.backendapi.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "hitung_absen")
public class Hitungabsen implements Serializable {
    private static final long serialVersionUID = 5170916145635486785L;
    private static final String ID = "ID";
    private static final String CREATED_DATETIME = "CREATED_AT";
    private static final String UPDATED_DATETIME = "LAST_UPDATE";
    private static final String EMPLOYEE_CODE = "EMPLOYEE_CODE";
    private static final String TAHUN = "TAHUN";
    private static final String BULAN = "BULAN";
    private static final String THP = "THP";
    private static final String SISA_CUTI = "SISA_CUTI";
    private static final String TOTAL_TERLAMBAT = "TOTAL_TERLAMBAT";
    private static final String TOTAL_KURANG_JAM = "TOTAL_KURANG_JAM";
    private static final String TOTAL_TANPA_KETERANGAN = "TOTAL_TANPA_KETERANGAN";
    private static final String TOTAL_IZIN = "TOTAL_IZIN";
    private static final String TOTAL_LUPA = "TOTAL_LUPA";
    private static final String TOTAL_FORM_LUPA = "TOTAL_FORM_LUPA";
    private static final String PEMOTONG_HARIAN = "PEMOTONG_HARIAN";
    private static final String POTONGAN_LUPA = "POTONGAN_LUPA";
    private static final String POTONGAN_TERLAMBAT = "POTONGAN_TERLAMBAT";
    private static final String POTONGAN_ALPHA = "POTONGAN_ALPHA";
    private static final String JATAH_CUTI_TERPAKAI = "JATAH_CUTI_TERPAKAI";
    private static final String POTONGAN_IZIN = "POTONGAN_IZIN";
    private static final String TOTAL_POTONGAN = "TOTAL_POTONGAN";
    private static final String TOTAL_SAKIT = "TOTAL_SAKIT";
    private static final String TOTAL_IZIN_HARI = "TOTAL_IZIN_HARI";
    private static final String TOTAL_CUTI = "TOTAL_CUTI";
    private static final String TOTAL_KURANG_KERJA = "TOTAL_KURANG_KERJA";
    private static final String VIEW_ID = "VIEW_ID";
    private static final String POTONGAN_KURANG_JAM = "POTONGAN_KURANG_JAM";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ID)
    private Long id;

    @CreationTimestamp
    @Column(name = CREATED_DATETIME)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = UPDATED_DATETIME)
    private LocalDateTime updateAt;

    @Column(name = EMPLOYEE_CODE, length = 15)
    private String employeeCode;

    @Column(name = TAHUN, length = 4)
    private int tahun;

    @Column(name = BULAN, length = 4)
    private int bulan;

    @Column(name = THP)
    private float thp;

    @Column(name = SISA_CUTI, length = 2)
    private int sisaCuti;

    @Column(name = TOTAL_TERLAMBAT, length = 4)
    private int totalTerlambat;

    @Column(name = TOTAL_KURANG_JAM, length = 4)
    private int totalKurangJam;

    @Column(name = TOTAL_TANPA_KETERANGAN, length = 4)
    private int totalTanpaKeterangan;

    @Column(name = TOTAL_IZIN, length = 4)
    private int totalIzin;

    @Column(name = TOTAL_LUPA, length = 4)
    private int totalLupaCheck;

    @Column(name = TOTAL_FORM_LUPA, length = 4)
    private int totalFormLupa;

    @Column(name = PEMOTONG_HARIAN)
    private float pemotongHarian;

    @Column(name = POTONGAN_LUPA)
    private float potonganLupa;

    @Column(name = POTONGAN_TERLAMBAT)
    private float potonganTerlambat;

    @Column(name = POTONGAN_ALPHA)
    private float potonganAlpha;

    @Column(name = JATAH_CUTI_TERPAKAI, length = 2)
    private int jatahCutiTerpakai;

    @Column(name = POTONGAN_IZIN)
    private float potonganIzin;

    @Column(name = POTONGAN_KURANG_JAM)
    private float potonganKurangJam;

    @Column(name = TOTAL_POTONGAN)
    private float totalPotongan;

    @Column(name = TOTAL_SAKIT)
    private int totalSakit=0;

    @Column(name = TOTAL_IZIN_HARI)
    private int totalIzinHari=0;

    @Column(name = TOTAL_CUTI)
    private int totalCuti=0;

    @Column(name = TOTAL_KURANG_KERJA)
    private int totalKurangKerja=0;

    @Column(name = VIEW_ID)
    private String viewId;
}
