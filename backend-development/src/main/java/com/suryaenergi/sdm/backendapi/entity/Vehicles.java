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
public class Vehicles implements Serializable {
    private static final long serialVersionUID = -7334086838908434513L;
    private static final String ID = "ID";
    private static final String CREATED_DATETIME = "CREATED_AT";
    private static final String UPDATED_DATETIME = "LAST_UPDATE";
    private static final String VAHICLE_ID = "VEHICLE_ID";
    private static final String PLAT_NUMBER = "PLAT_NUMBER";
    private static final String MERK = "MERK";
    private static final String TYPE = "TYPE";
    private static final String YEAR = "YEAR";
    private static final String OWNERSHIP = "OWNERSHIP";
    private static final String CERTIFICATE_EXPIRED_DATE = "CERTIFICATE_EXPIRED_DATE";
    private static final String TAX_EXPIRED_DATE = "TAX_EXPIRED_DATE";
    private static final String BBM = "BBM";
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

    @Column(name = VAHICLE_ID, length = 150)
    private String vehicleId;

    @Column(name = PLAT_NUMBER)
    private String platNumber;

    @Column(name = MERK)
    private String merk;

    @Column(name = TYPE)
    private String type;

    @Column(name = YEAR)
    private String year;
    
    @Column(name = BBM)
    private String bbm;

    @Column(name = OWNERSHIP)
    private String ownership;
    
    @Column(name = CERTIFICATE_EXPIRED_DATE)
    private LocalDate certificateExpiredDate;
    
    @Column(name = TAX_EXPIRED_DATE)
    private LocalDate taxExpiredDate;

    @Column(name = KETERANGAN)
    private String keterangan;
}
