package com.suryaenergi.sdm.backendapi.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataRekapAbsenHeaderOnly {
    private String employee_code;
    private String name;
    private String nik;
    private String status;
    private int month;
    private int year;
    private float potongan_lupa_chekin;
    private float potongan_tanpa_keterangan;
    private float potongan_kurang_jam;
    private float total_potongan;
}
