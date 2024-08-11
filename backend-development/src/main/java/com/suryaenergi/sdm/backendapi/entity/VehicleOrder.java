package com.suryaenergi.sdm.backendapi.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Data
@Table(name = "vehicle_order")
public class VehicleOrder implements Serializable {
    private static final long serialVersionUID = -2805094338013632936L;
    private static final String ID = "ID";
    private static final String CREATED_DATETIME = "CREATED_AT";
    private static final String UPDATED_DATETIME = "LAST_UPDATE";
    private static final String VAHICLE_ORDER_ID = "VEHICLE_ORDER_ID";
    private static final String USERS = "USERS";
    private static final String WAKTU_BERANGKAT = "WAKTU_BERANGKAT";
    private static final String TANGGAL_KEMBALI = "TANGGAL_KEMBALI";
    private static final String TUJUAN = "TUJUAN";
    private static final String KEPERLUAN = "KEPERLUAN";
    private static final String KODE_PROYEK = "KODE_PROYEK";
    private static final String KETERANGAN = "KETERANGAN";
    private static final String APPROVAL_PIMPRO_ATASAN = "APPROVAL_PIMPRO_ATASAN";
    private static final String TIME_APPROVAL_MENGETAHUI = "TIME_APPROVAL_MENGETAHUI";
    private static final String BBM = "BBM";
    private static final String DRIVER = "DRIVER";
    private static final String NO_HP_DRIVER = "NO_HP_DRIVER";
    private static final String WAKTU_KEMBALI = "WAKTU_KEMBALI";
    private static final String STATUS = "STATUS";
    private static final String OTHER_PLAT_NUMBER = "OTHER_PLAT_NUMBER";
    private static final String OTHER_TYPE = "OTHER_TYPE";
    private static final String OTHER_YEAR = "OTHER_YEAR";
    private static final String OTHER_OWNERSHIP = "OTHER_OWNERSHIP";
    private static final String OTHER_CERTIFICATE_EXPIRED = "OTHER_CERTIFICATE_EXPIRED";
    private static final String OTHER_TAX_EXPIRED = "OTHER_TAX_EXPIRED";
    private static final String OTHER_MERK = "OTHER_MERK";
    private static final String OTHER_BBM = "OTHER_BBM";
    private static final String OTHER_KETERANGAN = "OTHER_KETERANGAN";
    private static final String JAM_KEMBALI = "JAM_KEMBALI";

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

    @Column(name = VAHICLE_ORDER_ID, length = 150)
    private String vehicleOrderId;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "penumpang_order",
            joinColumns = { @JoinColumn(name = "order_id") },
            inverseJoinColumns = { @JoinColumn(name = "employee_id") })
    private List<Employee> users;

    @Column(name = WAKTU_BERANGKAT)
    private LocalDateTime waktuBerangkat;

    @Column(name = TANGGAL_KEMBALI)
    private LocalDate tanggalKembali;

    @Column(name = JAM_KEMBALI)
    private LocalTime jamKembali;

    @Column(name = TUJUAN)
    private String tujuan;

    @Column(name = KEPERLUAN)
    private String keperluan;

    @ManyToOne
    @JoinColumn(name="proyek_id", referencedColumnName = "id")
    private Proyeknav proyek;

    @Column(name = KETERANGAN)
    private String keterangan;

    @ManyToOne
    @JoinColumn(name="approval_pimpro_atasan_id", referencedColumnName = "id")
    private Employee approvalPimproAtasan;

    @ManyToOne
    @JoinColumn(name="vehicle_id", referencedColumnName = "id")
    private Vehicles mobil;

    @Column(name = OTHER_PLAT_NUMBER)
    private String otherPlatNumber;

    @Column(name = OTHER_TYPE)
    private String otherType;

    @Column(name = OTHER_YEAR)
    private String otherYear;

    @Column(name = OTHER_OWNERSHIP)
    private String otherOwnership;

    @Column(name = OTHER_CERTIFICATE_EXPIRED)
    private LocalDate otherCertificateExpired;

    @Column(name = OTHER_TAX_EXPIRED)
    private LocalDate otherTaxExpired;

    @Column(name = OTHER_MERK)
    private String otherMerk;

    @Column(name = OTHER_BBM)
    private String otherBbm;

    @Column(name = OTHER_KETERANGAN)
    private String otherKeterangan;

    @ManyToOne
    @JoinColumn(name="pemesan_id", referencedColumnName = "id")
    private Employee pemesan;

    @Column(name = DRIVER)
    private String driver;

    @Column(name = NO_HP_DRIVER)
    private String noHpDriver;

    @Column(name = WAKTU_KEMBALI)
    private LocalDateTime waktuKembali;
    
    @Column(name = STATUS)
    private String status;

    @ManyToOne
    @JoinColumn(name="need_approval_id", referencedColumnName = "id")
    private Employee needApproval;
}
