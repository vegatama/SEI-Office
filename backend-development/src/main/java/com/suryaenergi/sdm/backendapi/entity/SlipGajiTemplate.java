package com.suryaenergi.sdm.backendapi.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "slip_gaji_template")
public class SlipGajiTemplate {
    private static final String ID = "ID";
    private static final String NAME = "NAME";
    private static final String TAHUN = "TAHUN";
    private static final String BULAN = "BULAN";
    private static final String REVISI = "REVISI";
    private static final String WAKTU_REVISI = "WAKTU_REVSI";
    private static final String CREATED_AT = "CREATED_AT";
    @Column(name = ID)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column(name = TAHUN)
    private Integer tahun;

    @Column(name = BULAN)
    private Integer bulan;

    @Column(name = REVISI)
    private Integer revisi;

    @Column(name = WAKTU_REVISI)
    private LocalDateTime waktuRevisi;

    @Column(name = CREATED_AT)
    @CreationTimestamp
    private LocalDateTime createdAt;
}
