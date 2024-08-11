package com.suryaenergi.sdm.backendapi.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "antrian_email")
@Data
public class Antrianemail implements Serializable {
    private static final long serialVersionUID = -7479468328866979857L;
    private static final String ID = "ID";
    private static final String CREATED_DATETIME = "CREATED_AT";
    private static final String UPDATED_DATETIME = "LAST_UPDATE";
    private static final String SUBJECT = "SUBJECT";
    private static final String EMAIL = "EMAIL";
    private static final String CONTENT = "CONTENT";
    private static final String STATUS = "STATUS";
    private static final String TIME_SENT = "TIME_SENT";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ID)
    private Long id;

    @Column(name = EMAIL)
    private String email;

    @Column(name = SUBJECT)
    private String subject;

    @Column(name = CONTENT, columnDefinition = "TEXT")
    private String content;

    @Column(name = STATUS)
    private boolean status;

    @CreationTimestamp
    @Column(name = TIME_SENT)
    private LocalDateTime timeSent;

    @CreationTimestamp
    @Column(name = CREATED_DATETIME)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = UPDATED_DATETIME)
    private LocalDateTime updateAt;
}
