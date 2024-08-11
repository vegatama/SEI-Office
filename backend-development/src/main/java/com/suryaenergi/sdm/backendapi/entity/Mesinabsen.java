package com.suryaenergi.sdm.backendapi.entity;

import lombok.Data;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@Data
@Entity
@Table(name = "absensi")
public class Mesinabsen implements Serializable {

    private static final long serialVersionUID = 5460399226923894254L;
    private static final String ID = "ID_ABSENSI";
    private static final String PERSON_ID = "ID_MESIN";
    private static final String NAME = "PERSON_NAME";
    private static final String DATE = "DATE";
    private static final String TIME = "TIME";
    private static final String DATETIME = "DATETIME";
    private static final String DIRECTION = "DIRECTION";
    private static final String DEVICE_NAME = "DEVICE_NAME";
    private static final String DEVICE_SN = "DEVICE_SN";
    private static final String CARD_NO = "CARD_NO";
    private static final String IS_PROSES = "IS_PROSES";
    private static final String LATITUDE = "LATITUDE";
    private static final String LONGITUDE = "LONGITUDE";
    private static final String CENTER_LATITUDE = "CENTER_LATITUDE";
    private static final String CENTER_LONGITUDE = "CENTER_LONGITUDE";
    private static final String RADIUS = "RADIUS";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ID)
    private Long id;

    @Column(name = PERSON_ID, length = 100)
    private String personId;

    @Column(name = NAME, length = 150)
    private String name;

    @Column(name = DATE)
    private LocalDate date;

    @Column(name = TIME)
    private LocalTime time;

    @Column(name = DATETIME)
    private LocalDateTime dateTime;

    @Column(name = DIRECTION, length = 50)
    private String direction;

    @Column(name = DEVICE_NAME)
    private String deviceName;

    @Column(name = DEVICE_SN)
    private String deviceSn;

    @Column(name = CARD_NO)
    private String cardNo;

    @Column(name = IS_PROSES)
    private boolean isProses=false;

    @Column(name = LATITUDE)
    private Double latitude;

    @Column(name = LONGITUDE)
    private Double longitude;

    @Column(name = CENTER_LATITUDE)
    private Double centerLatitude;

    @Column(name = CENTER_LONGITUDE)
    private Double centerLongitude;

    @Column(name = RADIUS)
    private Double radius;
}
