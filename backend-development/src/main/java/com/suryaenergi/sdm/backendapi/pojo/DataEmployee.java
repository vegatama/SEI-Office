package com.suryaenergi.sdm.backendapi.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataEmployee {
    private String id;
    private String email;
    private String name;
    private String nik;
    private String employee_code;
    private String direktorat;
    private String job_title;
    private Double thp;
    private String unit_kerja;
    private String bagian_fungsi;
    private Long sisa_cuti;
    private String status;
    private String lokasi_absen;
}
