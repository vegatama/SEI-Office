package com.suryaenergi.sdm.backendapi.entity;

import com.suryaenergi.sdm.backendapi.response.IzinCutiDetailResponse;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "izin_cuti")
@Data
public class IzinCuti implements Serializable {

    private static final String ID = "ID";
    private static final String EMP_CODE = "EMP_CODE";
    private static final String CREATED_DATETIME = "CREATED_AT";
    private static final String UPDATED_DATETIME = "LAST_UPDATE";
    private static final String JENIS_CUTI = "JENIS_CUTI";
    private static final String START_DATE = "START_DATE";
    private static final String END_DATE = "END_DATE";
    private static final String REASON = "REASON";
    private static final String STATUS = "STATUS";

    @Id
    @Column(name = ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = EMP_CODE)
    private String empcode;

    @CreationTimestamp
    @Column(name = CREATED_DATETIME)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = UPDATED_DATETIME)
    private LocalDateTime updatedAt;

    @Column(name = JENIS_CUTI)
    private long jenisCuti;

    @Column(name = START_DATE)
    private LocalDateTime startDate;

    @Column(name = END_DATE)
    private LocalDateTime endDate;

    @Column(name = REASON)
    private String reason;

    @Column(name = STATUS)
    private IzinCutiDetailResponse.Status status;

}
