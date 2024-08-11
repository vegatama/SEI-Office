package com.suryaenergi.sdm.backendapi.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class HariLiburResponse {
    private String message;
    private String id;
    private String tanggal;
    private String keterangan;
}
