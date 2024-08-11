package com.suryaenergi.sdm.backendapi.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DataCutiEmployee {
    private String employee_code;
    private String name;
    private String nik;
    private String bagian_fungsi;
    private String sisa_cuti;
    private String tgl_cuti;
    private int jumlah_cuti;
}
