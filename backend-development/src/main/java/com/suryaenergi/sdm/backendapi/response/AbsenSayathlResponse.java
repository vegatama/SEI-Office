package com.suryaenergi.sdm.backendapi.response;

import com.suryaenergi.sdm.backendapi.pojo.DataAbsenSaya;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AbsenSayathlResponse {
    private String msg;
    private String employee_code;
    private String nama;
    private int jumlah_data;
    private List<DataAbsenSaya> data;
}
