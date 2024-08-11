package com.suryaenergi.sdm.backendapi.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JatahCutiRequest {
    private Long id;
    private String name;
    private int tahun;
    private int jumlahHari;
    private int jumlahHariTerpakai;
}
