package com.suryaenergi.sdm.backendapi.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "proyek")
public class Proyeknav implements Serializable {
    private static final long serialVersionUID = -6951576785159564825L;
    private static final String ID = "ID";
    private static final String CREATED_DATETIME = "CREATED_AT";
    private static final String UPDATED_DATETIME = "LAST_UPDATE";
    private static final String DIMENSION_CODE = "DIMENSION_CODE";
    private static final String PROJECT_CODE = "PROJECT_CODE";
    private static final String PROJECT_TYPE = "PROJECT_TYPE";
    private static final String PROJECT_NAME = "PROJECT_NAME";
    private static final String PIMPRO = "PIMPRO";
    private static final String NILAI_PROJECT = "NILAI_PROJECT";
    private static final String TANGGAL_MULAI = "TANGGAL_MULAI";
    private static final String TANGGAL_AKHIR = "TANGGAL_AKHIR";
    private static final String CARRY_OVER = "CARRY_OVER";
    private static final String CLOSING_DATE = "CLOSING_DATE";
    private static final String STATUS = "STATUS";

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

    @Column(name = DIMENSION_CODE, length = 100)
    private String dimensionCode;

    @Column(name = PROJECT_CODE, length = 100)
    private String projectCode;

    @Column(name = PROJECT_TYPE, length = 50)
    private String projectType;

    @Column(name = PROJECT_NAME)
    private String projectName;

    @Column(name = PIMPRO)
    private String pimpro;

    @Column(name = NILAI_PROJECT)
    private Double nilaiProject;

    @Column(name = TANGGAL_MULAI)
    private LocalDate tglMulai;

    @Column(name = TANGGAL_AKHIR)
    private LocalDate tglAkhir;

    @Column(name = CARRY_OVER)
    private boolean carryOver;

    @Column(name = CLOSING_DATE)
    private LocalDate closingDate;
    
    @Column(name = STATUS, length = 100)
    private String status;
}
