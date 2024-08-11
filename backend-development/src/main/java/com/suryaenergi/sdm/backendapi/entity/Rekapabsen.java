package com.suryaenergi.sdm.backendapi.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Data
@Table(name = "rekap_absen")
public class Rekapabsen implements Serializable {
    private static final long serialVersionUID = 4870457459037607434L;
    private static final String ID = "ID";
    private static final String CREATED_DATETIME = "CREATED_AT";
    private static final String UPDATED_DATETIME = "LAST_UPDATE";
    private static final String EMPLOYEE_CODE = "EMPLOYEE_CODE";
    private static final String TANGGAL = "TANGGAL";
    private static final String JAM_MASUK = "JAM_MASUK";
    private static final String JAM_KELUAR = "JAM_KELUAR";
    private static final String KETERANGAN = "KETERANGAN";
    private static final String STATUS = "STATUS";
    private static final String TERLAMBAT = "TERLAMBAT";
    private static final String KURANG_JAM = "KURANG_JAM";
    private static final String IS_CUTI = "IS_CUTI";
    private static final String IS_SPPD = "IS_SPPD";
    private static final String IS_SAKIT = "IS_SAKIT";
    private static final String IS_IZIN_PRIBADI = "IS_IZIN_PRIBADI";
    private static final String IS_LUPA_CHECKIN = "IS_LUPA_CHECKIN";
    private static final String IS_TUGAS_LUAR_TANPA_SPPD = "IS_TUGAS_LUAR_TANPA_SPPD";
    private static final String IS_FORM_CHECKIN = "IS_FORM_CHECKIN";
    private static final String IS_WHOLE_DAY = "IS_WHOLE_DAY";

    // KHUSUS MOBILE
    private static final String REASON = "REASON";
    private static final String ATTENDANCE_STATE = "ATTENDANCE_STATE";
    private static final String REQUEST_CHECKIN = "REQUEST_CHECKIN";
    private static final String LONGITUDE = "LONGITUDE";
    private static final String LATITUDE = "LATITUDE";
    private static final String DISTANCE = "DISTANCE"; // IN METERS
    // END OF KHUSUS MOBILE

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

    @Column(name = TANGGAL)
    private LocalDate tanggal;

    @Column(name = JAM_MASUK)
    private LocalTime jamMasuk;

    @Column(name = JAM_KELUAR)
    private LocalTime jamKeluar;

    @Column(name = TERLAMBAT)
    private int terlambat;

    @Column(name = KURANG_JAM)
    private int kurangJam;

    @Column(name = IS_CUTI)
    private boolean isCuti = false;

    @Column(name = IS_SPPD)
    private boolean isSppd = false;

    @Column(name = IS_SAKIT)
    private boolean isSakit = false;

    @Column(name = IS_IZIN_PRIBADI)
    private boolean isIzinPribadi = false;

    @Column(name = IS_LUPA_CHECKIN)
    private boolean isLupaCheckIn = false;

    @Column(name = IS_TUGAS_LUAR_TANPA_SPPD)
    private boolean isTugasLuarTanpaSppd = false;

    @Column(name = IS_FORM_CHECKIN)
    private boolean isFormCheckin = false;

    @Column(name = IS_WHOLE_DAY)
    private boolean isWholeDay = false;

    @Column(name = STATUS)
    private String status;

    @Column(name = KETERANGAN)
    private String keterangan;

    // KHUSUS MOBILE
    @Column(name = LONGITUDE)
    private Double longitude;

    @Column(name = LATITUDE)
    private Double latitude;
    // END OF KHUSUS MOBILE
}
