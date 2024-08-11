package com.suryaenergi.sdm.backendapi.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DokumenDetailResponse {
    private String msg;
    private String dokumen_id;
    private String no_dokumen;
    private String nama_dokumen;
    private String tgl_pengesehan;
    private int revisi;
    private String sistem_manajemen;
    private String file_dokumen;
}
