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
@Table(name = "purchase_orders")
public class PONav implements Serializable {
    private static final long serialVersionUID = -2621505614944956796L;
    private static final String ID = "ID";
    private static final String CREATED_DATETIME = "CREATED_AT";
    private static final String UPDATED_DATETIME = "LAST_UPDATE";
    private static final String DOCUMENT_DATE = "DOCUMENT_DATE";
    private static final String DOCUMENT_NO = "DOCUMENT_NO";
    private static final String LINE_NO = "LINE_NO";
    private static final String PROJECT_CODE = "PROJECT_CODE";
    private static final String TANGGAL_DIBUTUHKAN = "TANGGAL_DIBUTUHKAN";
    private static final String PURCHASE_TYPE = "PURCHASE_TYPE";
    private static final String PURCHASE_NO = "PURCHASE_NO";
    private static final String DESCRIPTION = "DESCRIPTION";
    private static final String POSTING_GROUP = "POSTING_GROUP";
    private static final String QUANTITY = "QUANTITY";
    private static final String UNIT_OF_MEASURE = "UNIT_OF_MEASURE";
    private static final String PR_NO = "PR_NO";
    private static final String PR_LINE_NO = "PR_LINE_NO";
    private static final String DIRECT_UNIT_COST = "DIRECT_UNIT_COST";
    private static final String UNIT_COST_LCY = "UNIT_COST_LCY";
    private static final String VAT_PERCENT = "VAT_PERCENT";
    private static final String LINE_DISCOUNT_AMOUNT = "LINE_DISCOUNT_AMOUNT";
    private static final String AMOUNT = "AMOUNT";
    private static final String AMOUNT_INCLUDE_VAT = "AMOUNT_INCLUDE_VAT";
    private static final String STATUS = "STATUS";
    private static final String BUY_FROM = "BUY_FROM";
    private static final String AMOUNT_LCY = "AMOUNT_LCY";

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

    @Column(name = LINE_NO)
    private Long lineNo;

    @Column(name = PROJECT_CODE, length = 100)
    private String projectCode;

    @Column(name = TANGGAL_DIBUTUHKAN)
    private LocalDate tanggalDibutuhkan;

    @Column(name = PURCHASE_TYPE, length = 50)
    private String purchaseType;

    @Column(name = PURCHASE_NO, length = 50)
    private String purchaseNo;

    @Column(name = DESCRIPTION)
    private String description;

    @Column(name = POSTING_GROUP, length = 15)
    private String postingGroup;

    @Column(name = QUANTITY)
    private Double qty;

    @Column(name = UNIT_OF_MEASURE, length = 100)
    private String unitOfMeasure;

    @Column(name = PR_NO, length = 150)
    private String prNo;

    @Column(name = PR_LINE_NO)
    private Long prLineNo;

    @Column(name = DIRECT_UNIT_COST)
    private double directUnitCost;

    @Column(name = UNIT_COST_LCY)
    private double unitCostLcy;

    @Column(name = VAT_PERCENT)
    private double vatPercent;

    @Column(name = LINE_DISCOUNT_AMOUNT)
    private double lineDiscountAmount;

    @Column(name = AMOUNT)
    private double amount;

    @Column(name = AMOUNT_INCLUDE_VAT)
    private double amountIncVat;

    @Column(name = STATUS, length = 100)
    private String status;

    @Column(name = DOCUMENT_DATE)
    private LocalDate documentDate;

    @Column(name = BUY_FROM)
    private String buyFrom;

    @Column(name = AMOUNT_LCY)
    private Double amountLcy;
}
