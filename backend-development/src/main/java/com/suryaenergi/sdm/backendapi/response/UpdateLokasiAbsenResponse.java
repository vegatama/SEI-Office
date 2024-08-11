package com.suryaenergi.sdm.backendapi.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateLokasiAbsenResponse {
    
    private String msg;

    private String name;

    private String bagian;

    private Long lokasi_absen_id;

    private String lokasi_absen;

}
