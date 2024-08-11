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
@Table(name = "jam_kerja")
public class Jamkerja implements Serializable {
    private static final String ID = "ID";
    private static final String CREATED_DATETIME = "CREATED_AT";
    private static final String UPDATED_DATETIME = "LAST_UPDATE";
    private static final String TANGGAL = "TANGGAL";
    private static final String JAM_MASUK = "JAM_MASUK";
    private static final String JAM_KELUAR = "JAM_KELUAR";
    private static final String KETERANGAN = "KETERANGAN";

    private static final long serialVersionUID = -3517500373304832112L;
    private static final String JAM_KERJA_ID = "JAM_KERJA_ID";

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

    @Column(name = TANGGAL)
    private LocalDate tanggal;
    
    @Column(name = JAM_MASUK)
    private LocalTime jamMasuk;
    
    @Column(name = JAM_KELUAR)
    private LocalTime jamKeluar;

    @Column(name = KETERANGAN)
    private String keterangan;

    @Column(name = JAM_KERJA_ID, length = 150)
    private String jamKerjaId;
}
