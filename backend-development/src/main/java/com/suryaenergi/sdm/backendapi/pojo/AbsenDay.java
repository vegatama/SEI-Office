package com.suryaenergi.sdm.backendapi.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AbsenDay {
    private String empcode;
    private String name;
    private String jam_masuk;
    private String jam_keluar;
}
