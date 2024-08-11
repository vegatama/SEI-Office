package com.suryaenergi.sdm.backendapi.entity;

import com.suryaenergi.sdm.backendapi.response.IzinCutiDetailResponse;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "izin_cuti_approval")
@Data
public class IzinCutiApproval implements Serializable {

    private static final String ID = "ID";
    private static final String IZIN_CUTI_ID = "IZIN_CUTI_ID";
    private static final String REVIEWER_EMP_CODE = "REVIEWER_EMP_CODE";
    private static final String STATUS = "STATUS";
    private static final String CREATED_DATETIME = "CREATED_AT";
    private static final String UPDATED_DATETIME = "LAST_UPDATE";
    private static final String REASON = "REASON";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ID)
    private Long id;

    @Column(name = IZIN_CUTI_ID)
    @JoinColumn(name = "id", table = "izin_cuti")
    private Long izinCutiId;

    @Column(name = REVIEWER_EMP_CODE)
    private String reviewerEmpCode;

    @Column(name = STATUS)
    private IzinCutiDetailResponse.Status status;

    @CreationTimestamp
    @Column(name = CREATED_DATETIME)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = UPDATED_DATETIME)
    private LocalDateTime updatedAt;

    @Column(name = REASON)
    private String reason;
}
