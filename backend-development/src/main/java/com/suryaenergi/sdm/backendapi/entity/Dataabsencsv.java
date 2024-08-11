package com.suryaenergi.sdm.backendapi.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@Table(name = "data_mentah_absen")
public class Dataabsencsv implements Serializable {
    private static final long serialVersionUID = 8163732220467774474L;
    private static final String ID = "ID";
    private static final String PERSON_ID = "PERSON_ID";
    private static final String NAME = "NAME";
    private static final String DATE = "DATE";
    private static final String TIME = "TIME";

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
}
