package com.suryaenergi.sdm.backendapi.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
public class Sppdnav implements Serializable {
    private static final long serialVersionUID = -2097573573535679614L;
    private static final String ID = "ID";
    private static final String CREATED_DATETIME = "CREATED_AT";
    private static final String UPDATED_DATETIME = "LAST_UPDATE";
    private static final String NO_SPPD = "NO_SPPD";
    private static final String NAMA_KEGIATAN = "NAMA_KEGIATAN";
    private static final String KODE_KEGIATAN = "KODE_KEGIATAN";
    private static final String TUJUAN = "TUJUAN";
    private static final String KEPERLUAN = "KEPERLUAN";
    private static final String WAKTU_SPPD = "WAKTU_SPPD";
    private static final String EMPLOYEE_CODE = "EMPLOYEE_CODE";
    private static final String EMPLOYEE_NAME = "EMPLOYEE_NAME";
    private static final String GOLONGAN = "GOLONGAN";

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

    @Column(name = NO_SPPD, length = 50)
    private String noSppd;

    @Column(name = NAMA_KEGIATAN, length = 150)
    private String namaKegiatan;

    @Column(name = KODE_KEGIATAN, length = 50)
    private String kodeKegiatan;

    @Column(name = TUJUAN, length = 100)
    private String tujuan;

    @Column(name = KEPERLUAN, length = 250)
    private String keperluan;

    @Column(name = WAKTU_SPPD)
    private Date waktuSPPD;

    @Column(name = EMPLOYEE_CODE, length = 15)
    private String employeeCode;

    @Column(name = EMPLOYEE_NAME, length = 150)
    private String employeeName;

    @Column(name = GOLONGAN, length = 15)
    private String golongan;
}
