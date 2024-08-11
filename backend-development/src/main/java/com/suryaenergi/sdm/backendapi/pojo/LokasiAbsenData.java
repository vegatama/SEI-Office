package com.suryaenergi.sdm.backendapi.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LokasiAbsenData {

    private Long id;
    
    private String lokasi_absen;

    private Double latitude;

    private Double longitude;

    private Double radius;

    private Boolean isDefault;
}
