package com.suryaenergi.sdm.backendapi.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JamKerjaRequest {
    private String tanggal;
    private String keterangan;
    private String jam_masuk;
    private String jam_keluar;
}
