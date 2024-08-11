package com.suryaenergi.sdm.backendapi.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HariLiburRequest {
    private String id; // nullable for insert
    private String tgl;
    private String keterangan;
}
