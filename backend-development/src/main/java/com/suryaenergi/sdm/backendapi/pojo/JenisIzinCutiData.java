package com.suryaenergi.sdm.backendapi.pojo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JenisIzinCutiData {
    private Long id;
    private String namaJenis;
    private Boolean cutCuti;
    private List<JenisIzinCutiAbsoluteReviewer> reviewers;
    private TipePengajuan pengajuan;

    public enum TipePengajuan {
        HARIAN,
        MENIT;
    }
}
