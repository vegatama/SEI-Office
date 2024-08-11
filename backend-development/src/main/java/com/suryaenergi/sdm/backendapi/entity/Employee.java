package com.suryaenergi.sdm.backendapi.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Data
public class Employee implements Serializable, UserDetails {
    private static final long serialVersionUID = 2860768049963281885L;
    private static final String EMPLOYEE_CODE = "EMPLOYEE_CODE";
    private static final String FIRST_NAME = "FIRST_NAME";
    private static final String MIDDLE_NAME = "MIDDLE_NAME";
    private static final String LAST_NAME = "LAST_NAME";
    private static final String FULL_NAME = "FULL_NAME";
    private static final String STATUS = "STATUS";
    private static final String KETERANGAN = "KETERANGAN";
    private static final String SISA_CUTI = "SISA_CUTI";
    private static final String THP = "THP";
    private static final String NIK = "NIK";
    private static final String JOB_TITLE = "JOB_TITLE";
    private static final String BAGIAN_FUNGSI = "BAGIAN_FUNGSI";
    private static final String UNIT_KERJA = "UNIT_KERJA";
    private static final String DIREKTORAT = "DIREKTORAT";
    private static final String USER_ID = "USER_ID_NAV";
    private static final String ATASAN_USER_ID = "ATASAN_USER_ID";
    private static final String GRADE = "GRADE";
    private static final String GOLONGAN = "GOLONGAN";
    private static final String PHONE_NO = "PHONE_NO";
    private static final String MOBILE_PHONE_NO = "MOBILE_PHONE_NO";
    private static final String PERSON_ID_MESIN_ABSEN = "PERSON_ID_MESIN_ABSEN";
    private static final String CREATED_DATETIME = "CREATED_AT";
    private static final String UPDATED_DATETIME = "LAST_UPDATE";
    private static final String EMPLOYEE_ID = "EMPLOYEE_ID";
    private static final String TANGGAL_LAHIR = "TANGGAL_LAHIR";
    private static final String EMAIL = "EMAIL";
    private static final String GENDER = "GENDER";
    private static final String RELIGION = "RELIGION";
    private static final String BIRTH_PLACE = "BIRTH_PLACE";
    private static final String ADDRESS = "ADDRESS";
    private static final String CITY = "CITY";
    private static final String COUNTRY = "COUNTRY";
    private static final String POST_CODE = "POST_CODE";
    private static final String GOLONGAN_DARAH = "GOLONGAN_DARAH";
    private static final String STATUS_PERNIKAHAN = "STATUS_PERNIKAHAN";
    private static final String NPWP = "NPWP";
    private static final String TELP_DARURAT = "TELP_DARURAT";
    private static final String STATUS_TELP_DARURAT = "STATUS_TELP_DARURAT";
    private static final String PENDIDIKAN_TERAKHIR = "PENDIDIKAN_TERAKHIR";
    private static final String JURUSAN_PENDIDIKAN = "JURUSAN_PENDIDIKAN";
    private static final String INSTITUSI_PENDIDIKAN = "INSTITUSI_PENDIDIKAN";
    private static final String FACEBOOK = "FACEBOOK";
    private static final String TWITTER = "TWITTER";
    private static final String INSTAGRAM = "INSTAGRAM";
    private static final String BANK_BRANCH = "BANK_BRANCH";
    private static final String BANK_ACCOUNT = "BANK_ACCOUNT";
    private static final String BANK_BRANCH2 = "BANK_BRANCH2";
    private static final String BANK_ACCOUNT2 = "BANK_ACCOUNT2";
    private static final String MULAI_KERJA = "MULAI_KERJA";
    private static final String AKHIR_KERJA = "AKHIR_KERJA";
    private static final String ANGGOTA_KELUARGA = "JUMLAH_ANGGOTA_KELUARGA";
    private static final String NAMA_ISTRI_SUAMI = "NAMA_ISTRI_SUAMI";
    private static final String TGL_LAHIR_ISTRI_SUAMI = "TGL_LAHIR_SUAMI_ISTRI";
    private static final String NAMA_ANAK1 = "NAMA_ANAK1";
    private static final String TGL_LAHIR_ANAK1 = "TGL_LAHIR_ANAK1";
    private static final String NAMA_ANAK2 = "NAMA_ANAK2";
    private static final String TGL_LAHIR_ANAK2 = "TGL_LAHIR_ANAK2";
    private static final String NAMA_ANAK3 = "NAMA_ANAK3";
    private static final String TGL_LAHIR_ANAK3 = "TGL_LAHIR_ANAK3";
    private static final String NAMA_ANAK4 = "NAMA_ANAK4";
    private static final String TGL_LAHIR_ANAK4 = "TGL_LAHIR_ANAK4";
    private static final String NAMA_ANAK5 = "NAMA_ANAK5";
    private static final String TGL_LAHIR_ANAK5 = "TGL_LAHIR_ANAK5";
    private static final String PASSWORD = "PASSWORD";
    private static final String ACCESS = "ACCESS";
    private static final String IS_ACTIVE = "IS_ACTIVE";
    private static final String ID = "ID";
    private static final String SPESIALIS_PENDIDIKAN = "SPESIALIS_PENDIDIKAN";
    private static final String COMPANY_CODE = "COMPANY_CODE";
    private static final String JABATAN = "JABATAN";
    private static final String KLASIFIKASI_LEVEL_JABATAN = "KLASIFIKASI_LEVEL_JABATAN";
    private static final String JOBSTREAM = "JOBSTREAM";
    private static final String KLASIFIKASI_JOB = "KLASIFIKASI_JOB";
    private static final String RUMPUN_JABATAN = "RUMPUN_JABATAN";
    private static final String LOKASI_KERJA = "LOKASI_KERJA";
    private static final String TELEGRAM_ID = "TELEGRAM_ID";
    private static final String LAST_LOGIN = "LAST_LOGIN";
    private static final String TALENTA_USER_ID = "TALENTA_USER_ID";
    private static final String AVATAR = "AVATAR";
    private static final String LOKASI_ABSEN_ID = "LOKASI_ABSEN_ID";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ID)
    private Long id;

    @Column(name = EMPLOYEE_ID, length = 150)
    private String employeeId;

    @Column(name = EMPLOYEE_CODE, length = 15)
    private String employeeCode;

    @Column(name = FIRST_NAME, length = 50)
    private String firstName;

    @Column(name = EMAIL, length = 100)
    private String email;

    @Column(name = MIDDLE_NAME, length = 50)
    private String middleName;

    @Column(name = LAST_NAME, length = 50)
    private String lastName;

    @Column(name = FULL_NAME, length = 250)
    private String fullName;

    @Column(name = STATUS, length = 50)
    private String status;

    @Column(name = KETERANGAN)
    private String keterangan;

    @Column(name = SISA_CUTI)
    private Long sisaCuti;

    @Column(name = THP)
    private Double thp;

    @Column(name = NIK, length = 15)
    private String nik;

    @Column(name = JOB_TITLE, length = 50)
    private String jobTitle;

    @Column(name = BAGIAN_FUNGSI, length = 50)
    private String bagianFungsi;

    @Column(name = UNIT_KERJA, length = 150)
    private String unitKerja;

    @Column(name = DIREKTORAT, length = 50)
    private String direktorat;

    @Column(name = USER_ID, length = 15)
    private String userIdNav;

    @Column(name = ATASAN_USER_ID, length = 15)
    private String atasanUserId;

    @Column(name = GRADE)
    private Long grade;

    @Column(name = GOLONGAN, length = 50)
    private String golongan;

    @Column(name = PHONE_NO, length = 25)
    private String phoneNo;

    @Column(name = MOBILE_PHONE_NO, length = 50)
    private String mobilePhoneNo;

    @Column(name = PERSON_ID_MESIN_ABSEN, length = 15)
    private String personIdMesinAbsen;

    @Column(name = TANGGAL_LAHIR)
    private LocalDate tanggalLahir;

    @CreationTimestamp
    @Column(name = CREATED_DATETIME)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = UPDATED_DATETIME)
    private LocalDateTime updateAt;

    @Column(name = GENDER, length = 25)
    private String gender;

    @Column(name = RELIGION, length = 100)
    private String religion;

    @Column(name = BIRTH_PLACE, length = 150)
    private String birthPlace;

    @Column(name = ADDRESS, columnDefinition = "TEXT")
    private String address;

    @Column(name = CITY, length = 100)
    private String city;

    @Column(name = COUNTRY, length = 100)
    private String country;

    @Column(name = POST_CODE, length = 25)
    private String postCode;

    @Column(name = GOLONGAN_DARAH, length = 10)
    private String golonganDarah;

    @Column(name = STATUS_PERNIKAHAN, length = 50)
    private String statusPernikahan;

    @Column(name = NPWP, length = 25)
    private String npwp;

    @Column(name = TELP_DARURAT, length = 50)
    private String telpDarurat;

    @Column(name = STATUS_TELP_DARURAT, length = 50)
    private String statusTelpDarurat;

    @Column(name = PENDIDIKAN_TERAKHIR, length = 25)
    private String pendidikanTerakhir;

    @Column(name = JURUSAN_PENDIDIKAN, length = 100)
    private String jurusanPendidikan;

    @Column(name = INSTITUSI_PENDIDIKAN, length = 100)
    private String institusiPendidikan;

    @Column(name = SPESIALIS_PENDIDIKAN, length = 25)
    private String spesialisPendidikan;

    @Column(name = FACEBOOK, length = 100)
    private String facebook;

    @Column(name = TWITTER, length = 100)
    private String twitter;

    @Column(name = INSTAGRAM, length = 100)
    private String instagram;

    @Column(name = BANK_BRANCH, length = 100)
    private String bankBranch;

    @Column(name = BANK_ACCOUNT, length = 100)
    private String bankAccount;

    @Column(name = BANK_BRANCH2, length = 100)
    private String bankBranch2;

    @Column(name = BANK_ACCOUNT2, length = 100)
    private String bankAccount2;

    @Column(name = MULAI_KERJA)
    private LocalDate mulaiKerja;

    @Column(name = AKHIR_KERJA)
    private LocalDate akhirKerja;

    @Column(name = ANGGOTA_KELUARGA)
    private int anggotaKeluarga = 0;

    @Column(name = NAMA_ISTRI_SUAMI, length = 250)
    private String namaIstriSuami;

    @Column(name = TGL_LAHIR_ISTRI_SUAMI)
    private LocalDate tglLahirIstriSuami;

    @Column(name = NAMA_ANAK1, length = 250)
    private String namaAnak1;

    @Column(name = TGL_LAHIR_ANAK1)
    private LocalDate tglLahirAnak1;

    @Column(name = NAMA_ANAK2, length = 250)
    private String namaAnak2;

    @Column(name = TGL_LAHIR_ANAK2)
    private LocalDate tglLahirAnak2;

    @Column(name = NAMA_ANAK3, length = 250)
    private String namaAnak3;

    @Column(name = TGL_LAHIR_ANAK3)
    private LocalDate tglLahirAnak3;

    @Column(name = NAMA_ANAK4, length = 250)
    private String namaAnak4;

    @Column(name = TGL_LAHIR_ANAK4)
    private LocalDate tglLahirAnak4;

    @Column(name = NAMA_ANAK5, length = 250)
    private String namaAnak5;

    @Column(name = TGL_LAHIR_ANAK5)
    private LocalDate tglLahirAnak5;

    @Column(name = PASSWORD, length = 255)
    private String password;

    //to be deleted
    @Column(name = ACCESS, columnDefinition = "TEXT")
    private String access;
    //end to be deleted

    @ManyToOne
    @JoinColumn(name = "roles_id", referencedColumnName = "id")
    private Roles roles;

    @Column(name = IS_ACTIVE)
    private boolean isActive;
    
    @Column(name = COMPANY_CODE, length = 25)
    private String companyCode;

    @Column(name = JABATAN, length = 100)
    private String jabatan;

    @Column(name = KLASIFIKASI_LEVEL_JABATAN, length = 100)
    private String klasifikasiLevelJabatan;

    @Column(name = JOBSTREAM, length = 100)
    private String jobstream;

    @Column(name = KLASIFIKASI_JOB, length = 100)
    private String klasifikasiJob;

    @Column(name = RUMPUN_JABATAN, length = 100)
    private String rumpunJabatan;

    @Column(name = LOKASI_KERJA, length = 100)
    private String lokasiKerja;

    @Column(name = TELEGRAM_ID)
    private String telegramId;
    
    @Column(name = LAST_LOGIN)
    private LocalDateTime lastLogin;

    @Column(name = TALENTA_USER_ID)
    private String talentaUserId;

    @Column(name = AVATAR)
    private String avatar;

    private boolean notifEmail = false;

    @ManyToOne
    @JoinColumn(name = "lokasi_absen_id", referencedColumnName = "id")
    private LokasiAbsen lokasiAbsenId;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
