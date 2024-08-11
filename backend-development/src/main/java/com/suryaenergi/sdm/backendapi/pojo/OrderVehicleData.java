package com.suryaenergi.sdm.backendapi.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderVehicleData {
    private String order_id;
    private List<String> users;
    private String waktu_berangkat;
    private LocalDate tanggal_kembali;
    private String tujuan;
    private String keperluan;
    private String kode_proyek;
    private String keterangan;
    private String approval;
    private VehiclesData mobil;
    private String driver;
    private String hp_driver;
    private String waktu_kembali;
    private String status;
    private String need_approve;
    private String need_approve_id;
}
