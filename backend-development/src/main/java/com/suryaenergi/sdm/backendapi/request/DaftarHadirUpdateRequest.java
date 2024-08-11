package com.suryaenergi.sdm.backendapi.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DaftarHadirUpdateRequest {
    private String daftar_id;
    private String pimpinan;
    private String kegiatan;
    private String tanggal;
    private String waktu_mulai;
    private String waktu_selesai;
    private String subyek;
    private String tempat;
    private String notulis;
    private String keterangan;

    private Long ruang_meeting_id;
}
