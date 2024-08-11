package com.suryaenergi.sdm.backendapi.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "gl_account")
public class Glaccountnav implements Serializable {
    private static final long serialVersionUID = 4142819827999557183L;
    private static final String ID = "ID";
    private static final String CREATED_DATETIME = "CREATED_AT";
    private static final String UPDATED_DATETIME = "LAST_UPDATE";
    private static final String GL_NO = "GL_NO";
    private static final String GL_NAME = "GL_NAME";
    private static final String INCOME_BALANCE = "INCOME_BALANCE";
    private static final String ACCOUNT_CATEGORY = "ACCOUNT_CATEGORY";
    private static final String ACCOUNT_TYPE = "ACCOUNT_TYPE";

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

    @Column(name = GL_NO, length = 15)
    private String glNo;

    @Column(name = GL_NAME)
    private String glName;

    @Column(name = INCOME_BALANCE, length = 50)
    private String incomeBalance;

    @Column(name = ACCOUNT_CATEGORY, length = 50)
    private String accountCategory;

    @Column(name = ACCOUNT_TYPE, length = 50)
    private String accountType;
}
