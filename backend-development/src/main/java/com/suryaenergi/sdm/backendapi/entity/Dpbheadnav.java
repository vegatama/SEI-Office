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
@Table(name = "dpb_head")
public class Dpbheadnav implements Serializable {
    private static final long serialVersionUID = 902301227170718708L;
    private static final String ID = "ID";
    private static final String CREATED_DATETIME = "CREATED_AT";
    private static final String UPDATED_DATETIME = "LAST_UPDATE";
    private static final String DOCUMENT_NO = "DOCUMENT_NO";
    private static final String DOCUMENT_DATE = "DOCUMENT_DATE";
    private static final String PROJECT_CODE = "PROJECT_CODE";
    private static final String TANGGAL_DIBUTUHKAN = "TANGGAL_DIBUTUHKAN";
    private static final String AMOUNT = "AMOUNT";
    private static final String AMOUNT_INC_VAT = "AMOUNT_INC_VAT";
    private static final String STATUS = "STATUS";
    private static final String PROJECT_NAME = "PROJECT_NAME";
    private static final String PEMOHON_PIMPRO = "PEMOHON_PIMPRO";
    private static final String LINE_AMOUNT_LCY = "LINE_AMOUNT_LCY";

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

    @Column(name = DOCUMENT_NO, length = 50)
    private String documentNo;

    @Column(name = PROJECT_CODE, length = 50)
    private String projectCode;

    @Column(name = AMOUNT)
    private Double amount;

    @Column(name = AMOUNT_INC_VAT)
    private Double amountIncVat;

    @Column(name = DOCUMENT_DATE)
    private LocalDate documentDate;

    @Column(name = TANGGAL_DIBUTUHKAN)
    private LocalDate tglDibutuhkan;

    @Column(name = PROJECT_NAME)
    private String projectName;
    
    @Column(name = PEMOHON_PIMPRO, length = 50)
    private String pemohonPimpro;

    @Column(name = LINE_AMOUNT_LCY)
    private Double lineAmountLcy;

    @Column(name = STATUS, length = 50)
    private String status;
}
