package com.suryaenergi.sdm.backendapi.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DaftarHadirData {
    private String daftar_hadir_id;
    private String kegiatan;
    private String subyek;
    private String tanggal;
    private String pimpinan;
    private String waktu_mulai;
    private String waktu_selesai;
    private String tempat;
    private int jumlah_peserta;
    private String pembuat;
    private List<DataPeserta> data_peserta;
    private String notulis;
    private String risalah;
    private RuangMeetingData ruang_meeting;
    private String keterangan;
}
