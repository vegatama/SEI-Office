package com.suryaenergi.sdm.backendapi.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderVehicleRequest {
    private String id;
    private List<String> pengguna;
    private String waktu_berangkat;
    private String tgl_kembali;
    private String jam_kembali;
    private String tujuan;
    private String keperluan;
    private String kode_proyek;
    private String keterangan;
    private String pemesan;
}
