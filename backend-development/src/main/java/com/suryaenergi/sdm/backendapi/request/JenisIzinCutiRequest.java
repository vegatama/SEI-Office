package com.suryaenergi.sdm.backendapi.request;

import com.suryaenergi.sdm.backendapi.pojo.JenisIzinCutiReviewer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JenisIzinCutiRequest {
    private Long id;
    private String izinCuti;
    private Boolean cutCuti;
    private List<JenisIzinCutiReviewer> reviewer;
    private String pengajuan;
}
