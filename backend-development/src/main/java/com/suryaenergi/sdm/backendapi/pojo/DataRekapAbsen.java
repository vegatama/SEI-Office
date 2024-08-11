package com.suryaenergi.sdm.backendapi.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataRekapAbsen {
    private String employee_code;
    private String name;
    private String nik;
    private String status;
    private String email;
    private int month;
    private int year;
    private int jumlah_cuti_awal;
    private int jumlah_sisa_cuti;
    private int jumlah_lupa;
    private int jumlah_form_lupa;
    private int jumlah_terlambat;
    private int jumlah_izin;
    private int jumlah_cuti;
    private int jumlah_tanpa_keterangan;
    private int jumlah_izin_hari;
    private int jumlah_sakit;
    private int jumlah_cuti_terpakai;
    private int jumlah_kurang_jam;
    private float thp;
    private float pemotong_harian;
    private float potongan_lupa_chekin;
    private float potongan_tanpa_keterangan;
    private float potongan_kurang_jam;
    private float potongan_terlambat;
    private float potongan_izin;
    private float total_potongan;
}
