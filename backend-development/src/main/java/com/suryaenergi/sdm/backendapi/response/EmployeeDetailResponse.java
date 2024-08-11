package com.suryaenergi.sdm.backendapi.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDetailResponse {
    private String msg;
    private String email;
    private String name;
    private String nik;
    private String employee_code;
    private String direktorat;
    private String golongan;
    private String job_title;
    private String keterangan;
    private String mobile_phone_no;
    private String person_id_mesin_absen;
    private String phone_no;
    private Double thp;
    private String unit_kerja;
    private String atasan_user_id;
    private String bagian_fungsi;
    private Long grade;
    private Long sisa_cuti;
    private String birthday;
    private String user_id_nav;

    private String first_name;
    private String middle_name;
    private String last_name;
    private String gender;
    private String place_birthdate;
    private String religion;
    private String address;
    private String post_code;
    private String city;
    private String gol_darah;
    private String status_pernikahan;
    private String npwp;

    private String no_telp_darurat;
    private String status_darurat;

    private String pendidikan_terakhir;
    private String jurusan_pendidikan;
    private String spesialis_pendidikan;
    private String institusi_pendidikan;

    private String facebook;
    private String twitter;
    private String instagram;

    private int jumlah_keluarga = 0;
    private String nama_istri_suami = "";
    private String dob_istri_suami = "";
    private String nama_anak_1 = "";
    private String dob_anak_1 = "";
    private String nama_anak_2 = "";
    private String dob_anak_2 = "";
    private String nama_anak_3 = "";
    private String dob_anak_3 = "";
    private String nama_anak_4 = "";
    private String dob_anak_4 = "";
    private String nama_anak_5 = "";
    private String dob_anak_5 = "";

    private String bank_branch;
    private String bank_account;
    private String bank_branch2;
    private String bank_account2;

    private String company_code;
    private String jabatan;
    private String klasifikasi_level_jabatan;
    private String job_stream;
    private String klasifikasi_job;
    private String rumpun_jabatan;
    private String lokasi_kerja;
    private String mulai_kerja;
    private String akhir_kerja;
    private String status_kerja;
    private String avatar;
}
