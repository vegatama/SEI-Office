package com.suryaenergi.sdm.backendapi.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KirimEmailRequest {
    private int year;
    private int month;
    private String tanggal_akhir;
    private List<String> employee_code;
}
