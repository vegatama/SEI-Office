package com.suryaenergi.sdm.backendapi.response;

import com.suryaenergi.sdm.backendapi.pojo.DataEmployee;
import com.suryaenergi.sdm.backendapi.pojo.DataPeserta;
import com.suryaenergi.sdm.backendapi.pojo.RuangMeetingData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DaftarHadirResponse {
    private String msg;
    private String daftar_hadir_id;
    private String kegiatan;
    private String subyek;
    private String tanggal;
    private String waktu_mulai;
    private String waktu_selesai;
    private String pimpinan;
    private String tempat;
    private String keterangan;
    private int jumlah_peserta;
    private List<DataPeserta> data_peserta;
    private List<String> undangan;
    private DataEmployee notulis;
    private String risalah;
    private RuangMeetingData ruang_meeting;
    private String pembuat_id;
}
