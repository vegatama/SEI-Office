package com.suryaenergi.sdm.backendapi.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeAddRequest {
    private String email;
    private String password;
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
    private Float thp;
    private String unit_kerja;
    private String atasan_user_id;
    private String bagian_fungsi;
    private int grade;
    private int sisa_cuti;
}
