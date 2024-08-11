package com.suryaenergi.sdm.backendapi.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DokumenAddRequest {
    private String no_dokumen;
    private String nama_dokumen;
    private String tgl_pengesahan;
    private int revisi;
    private String sistem_manajemen;
    private String file_dokumen;
}
