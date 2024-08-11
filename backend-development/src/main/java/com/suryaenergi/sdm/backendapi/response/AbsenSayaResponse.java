package com.suryaenergi.sdm.backendapi.response;

import com.suryaenergi.sdm.backendapi.pojo.DataAbsenSaya;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AbsenSayaResponse {
    private String msg;
    private String employee_code;
    private String nama;
    private int tahun;
    private int bulan;
    private int jumlah_data;
    private int total_terlambat;
    private int total_lupa_check;
    private int total_form_lupa;
    private int total_tanpa_keterangan;
    private int total_kurang_jam;
    private int total_kurang_kerja;
    private int total_izin_hari;
    private int total_izin_menit;
    private List<DataAbsenSaya> data;
}
