package com.suryaenergi.sdm.backendapi.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataPeserta {
    private String nama;
    private String bagian;
    private String email_phone;
    private String tanda_tangan;
}
