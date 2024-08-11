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
@Table(name = "dpb_line")
public class Dpblinenav implements Serializable {
    private static final long serialVersionUID = 7181017472599924547L;
    private static final String ID = "ID";
    private static final String CREATED_DATETIME = "CREATED_AT";
    private static final String UPDATED_DATETIME = "LAST_UPDATE";
    private static final String DOCUMENT_NO = "DOCUMENT_NO";
    private static final String DOCUMENT_DATE = "DOCUMENT_DATE";
    private static final String PROJECT_CODE = "PROJECT_CODE";
    private static final String TANGGAL_DIBUTUHKAN = "TANGGAL_DIBUTUHKAN";
    private static final String ITEM_NO = "ITEM_NO";
    private static final String ITEM_DESCRIPTION = "ITEM_DESCRIPTION";
    private static final String QTY = "QTY";
    private static final String UNIT_OF_MEASURE = "UNIT_OF_MEASURE";
    private static final String DIRECT_UNIT_COST = "DIRECT_UNIT_COST";
    private static final String UNIT_COST_LCY = "UNIT_COST_LCY";
    private static final String VAT_PERCENT = "VAT_PERCENT";
    private static final String LINE_DISCOUNT_AMOUNT = "LINE_DISCOUNT_AMOUNT";
    private static final String AMOUNT = "AMOUNT";
    private static final String AMOUNT_INC_VAT = "AMOUNT_INC_VAT";
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

    @Column(name = DOCUMENT_NO, length = 50)
    private String documentNo;

    @Column(name = DOCUMENT_DATE)
    private LocalDate documentDate;

    @Column(name = PROJECT_CODE, length = 50)
    private String projectCode;

    @Column(name = TANGGAL_DIBUTUHKAN)
    private LocalDate tglDibutuhkan;

    @Column(name = ITEM_NO, length = 50)
    private String itemNo;

    @Column(name = ITEM_DESCRIPTION)
    private String itemDesc;

    @Column(name = QTY)
    private Double qty;

    @Column(name = UNIT_OF_MEASURE, length = 50)
    private String unitMeasure;

    @Column(name = DIRECT_UNIT_COST)
    private Double directUnitCost;

    @Column(name = UNIT_COST_LCY)
    private Double unitCostLcy;

    @Column(name = VAT_PERCENT)
    private Double vatPercent;

    @Column(name = LINE_DISCOUNT_AMOUNT)
    private Double lineDiscountAmount;

    @Column(name = AMOUNT)
    private Double amount;

    @Column(name = AMOUNT_INC_VAT)
    private Double amountIncVat;

    @Column(name = STATUS)
    private String status;
}
