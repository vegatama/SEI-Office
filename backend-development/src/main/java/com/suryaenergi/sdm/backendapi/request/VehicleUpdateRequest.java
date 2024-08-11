package com.suryaenergi.sdm.backendapi.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleUpdateRequest {
    private String vehicle_id;
    private String plat_number;
    private String year;
    private String type;
    private String merk;
    private String ownership;
    private String certificate_expired;
    private String tax_expired;
    private String bbm;
    private String keterangan;
}
