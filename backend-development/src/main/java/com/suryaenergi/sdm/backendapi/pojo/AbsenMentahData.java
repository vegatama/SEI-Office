package com.suryaenergi.sdm.backendapi.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AbsenMentahData {
    private String empcode;
    private String name;
    private String tgl;
    private String hari;
    private String jam_masuk;
    private String jam_keluar;
    private String status;
}
