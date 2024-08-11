package com.suryaenergi.sdm.backendapi.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehiclesData {
    private String vehicle_id;
    private String plat_number;
    private String type;
    private String year;
    private String ownership;
    private LocalDate certifcate_expired;
    private LocalDate tax_expired;
    private String merk;
    private String bbm;
    private String keterangan;
}
