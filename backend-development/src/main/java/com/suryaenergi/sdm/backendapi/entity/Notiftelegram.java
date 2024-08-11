package com.suryaenergi.sdm.backendapi.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
public class Notiftelegram implements Serializable {
    private static final long serialVersionUID = -6055710195430872315L;
    private static final String ID = "ID";
    private static final String CREATED_DATETIME = "CREATED_AT";
    private static final String UPDATED_DATETIME = "LAST_UPDATE";
    private static final String MESSAGE = "MESSAGE";
    private static final String TELEGRAM_ID = "TELEGRAM_ID";
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

    @Column(name = TELEGRAM_ID, length = 50)
    private String telegramId;

    @Column(name = MESSAGE, columnDefinition = "TEXT")
    private String message;

    @Column(name = STATUS, length = 15)
    private String status;

    private int retry = 0;
}
