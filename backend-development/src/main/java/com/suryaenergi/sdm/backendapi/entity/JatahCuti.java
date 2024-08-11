package com.suryaenergi.sdm.backendapi.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "jatah_cuti")
public class JatahCuti implements Serializable {
    private static final long serialVersionUID = 6391720154238780387L;
    private static final String ID = "ID";
    private static final String EMPLOYEE_ID = "EMPLOYEE_ID";
    private static final String TAHUN = "TAHUN";
    private static final String JUMLAH_CUTI = "JUMLAH_CUTI";
    private static final String KETERANGAN = "KETERANGAN";
    private static final String CREATED_AT = "CREATED_AT";
    private static final String UPDATED_AT = "UPDATED_AT";
    private static final String REFERRER = "REFERRER";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ID)
    private Long id;
    @Column(name = EMPLOYEE_ID)
    private String employeeId;
    @Column(name = TAHUN)
    private int tahun;
    @Column(name = JUMLAH_CUTI)
    private int jumlahCuti;
    @Column(name = KETERANGAN)
    private String keterangan;
    @Column(name = CREATED_AT)
    @CreationTimestamp
    private LocalDateTime createdAt;
    @Column(name = UPDATED_AT)
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    @Column(name = REFERRER)
    private String referrer;
}
