package com.suryaenergi.sdm.backendapi.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class LokasiAbsen implements Serializable {
    
    private static final long serialVersionUID = 6785984563875365987L;
    private static final String ID = "ID";
    private static final String LOKASI_ABSEN = "LOKASI_ABSEN";
    private static final String LATITUDE = "LATITUDE";
    private static final String LONGITUDE = "LONGITUDE";
    private static final String RADIUS = "RADIUS";
    private static final String IS_DEFAULT = "IS_DEFAULT";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ID)
    private Long id;

    @Column(name = LOKASI_ABSEN)
    private String lokasiAbsen;

    @Column(name = LATITUDE)
    private Double latitude;

    @Column(name = LONGITUDE)
    private Double longitude;

    @Column(name = RADIUS)
    private Double radius;

    @Column(name = IS_DEFAULT)
    private Boolean isDefault;


}
