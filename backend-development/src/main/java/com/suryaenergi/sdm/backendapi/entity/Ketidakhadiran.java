package com.suryaenergi.sdm.backendapi.entity;

import lombok.Data;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
public class Ketidakhadiran implements Serializable {
    private static final long serialVersionUID = -7000649392010743478L;
    private static final String ID = "ID";
    private static final String EMPLOYEE_CODE = "EMPLOYEE_CODE";
    private static final String EMPLOYEE_NAME = "EMPLOYEE_NAME";
    private static final String CAUSE_OF_ABSENCE = "CAUSE_OF_ABSENCE";
    private static final String TANGGAL = "TANGGAL";
    private static final String FROM_TIME = "FROM_TIME";
    private static final String TO_TIME = "TO_TIME";
    private static final String IS_WHOLE_DAY = "IS_WHOLE_DAY";
    private static final String IS_MEMOTONG_CUTI = "IS_MEMOTONG_CUTI";
    private static final String IS_SURAT_KETERANGAN_SAKIT = "IS_SURAT_KETERANGAN_SAKIT";
    private static final String IS_SURAT_LUPA_CHECKIN = "IS_SURAT_LUPA_CHECKIN";
    private static final String NO_SPPD = "NO_SPPD";
    private static final String ID_MESIN_ABSEN = "ID_MESIN_ABSEN";
    private static final String DESCRIPTION = "DESCRIPTION";
    private static final String ID_IZINCUTI = "ID_IZINCUTI";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ID)
    private Long id;

    @Column(name = EMPLOYEE_CODE, length = 15)
    private String employeeCode;

    @Column(name = EMPLOYEE_NAME, length = 150)
    private String employeeName;

    @Column(name = CAUSE_OF_ABSENCE, length = 50)
    private String causeOfAbsence;

    @Column(name = TANGGAL)
    private LocalDate tanggal;

    @Column(name = FROM_TIME)
    private LocalTime fromTime;

    @Column(name = TO_TIME)
    private LocalTime toTime;

    @Column(name = IS_WHOLE_DAY)
    private boolean wholeDay;

    @Column(name = IS_MEMOTONG_CUTI)
    private boolean memotongCuti;

    @Column(name = IS_SURAT_KETERANGAN_SAKIT)
    private boolean suratKeteranganSakit;

    @Column(name = IS_SURAT_LUPA_CHECKIN)
    private boolean suratLupaCheckin;

    @Column(name = NO_SPPD, length = 50)
    private String noSppd;

    @Column(name = ID_MESIN_ABSEN, length = 25)
    private String idMesinAbsen;

    @Column(name = DESCRIPTION)
    private String description;

    @Column(name = ID_IZINCUTI)
    private Long idIzinCuti;
}
