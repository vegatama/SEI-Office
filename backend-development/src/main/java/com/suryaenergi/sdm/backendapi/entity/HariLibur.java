package com.suryaenergi.sdm.backendapi.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "hari_libur")
public class HariLibur implements Serializable {

    private static final long serialVersionUID = 6713561585327445059L;
    private static final String ID = "ID";
    private static final String CREATED_DATETIME = "CREATED_AT";
    private static final String UPDATED_DATETIME = "LAST_UPDATE";
    private static final String HARI_LIBUR_ID = "HARI_LIBUR_ID";
    private static final String TANGGAL = "TANGGAL";
    private static final String KETERANGAN = "KETERANGAN";

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
    
    @Column(name = HARI_LIBUR_ID, length = 150)
    private String hariLiburId;

    @Column(name = TANGGAL)
    private LocalDate tanggal;

    @Column(name = KETERANGAN)
    private String keterangan;
}
