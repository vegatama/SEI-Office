package com.suryaenergi.sdm.backendapi.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "budget_proyek")
public class Budgetnav implements Serializable {
    private static final long serialVersionUID = 8868296837176343738L;
    private static final String ID = "ID";
    private static final String CREATED_DATETIME = "CREATED_AT";
    private static final String UPDATED_DATETIME = "LAST_UPDATE";
    private static final String BUDGET_TYPE = "BUDGET_TYPE";
    private static final String PROJECT_CODE = "PROJECT_CODE";
    private static final String GL_ACCOUNT = "GL_ACCOUNT";
    private static final String ENTRY_DATE = "ENTRY_DATE";
    private static final String AMOUNT = "AMOUNT";

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

    @Column(name = BUDGET_TYPE, length = 50)
    private String type;

    @Column(name = PROJECT_CODE, length = 100)
    private String projectCode;

    @Column(name = GL_ACCOUNT, length = 50)
    private String glAccount;

    @Column(name = ENTRY_DATE)
    private LocalDate entryDate;

    @Column(name = AMOUNT)
    private Double amount;
}
