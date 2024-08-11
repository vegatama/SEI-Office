package com.suryaenergi.sdm.backendapi.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DaftarHadirRequest {
    private String kegiatan;
    private String pimpinan;
    private String subyek;
    private String tanggal;
    private String pembuat;
    private String waktu_mulai;
    private String waktu_selesai;
    private String tempat;
    private String notulis;
    private List<String> undangan;
    private String isi_undangan;
    private String keterangan;

    // ruang_meeting diisi jika acara menggunakan
    // ruang yang sudah ada di database, jika
    // tidak ada (misalnya: zoom meeting) maka
    // diisi di field tempat, dan ruang_meeting di-isi
    // null
    private Long ruang_meeting;
}
