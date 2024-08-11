package com.suryaenergi.sdm.backendapi.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "ruang_meeting")
public class RuangMeeting {
    private static final String ID = "ID";
    private static final String CREATED_AT = "CREATED_AT";
    private static final String UPDATED_AT = "UPDATED_AT";
    private static final String NAME = "NAME";
    private static final String CAPACITY = "CAPACITY";
    private static final String DESCRIPTION = "DESCRIPTION";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ID)
    private Long id;

    @Column(name = CREATED_AT)
    private LocalDateTime createdAt;

    @Column(name = UPDATED_AT)
    private LocalDateTime updatedAt;

    @Column(name = NAME)
    private String name;

    @Column(name = CAPACITY)
    private Integer capacity;

    @Column(name = DESCRIPTION)
    private String description;
}
