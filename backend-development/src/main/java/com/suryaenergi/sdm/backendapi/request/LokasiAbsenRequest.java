package com.suryaenergi.sdm.backendapi.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LokasiAbsenRequest {
    private Long id;
    private String lokasi_absen;
    private Double latitude;
    private Double longitude;
    private Double radius;
    private Boolean isDefault;
}
