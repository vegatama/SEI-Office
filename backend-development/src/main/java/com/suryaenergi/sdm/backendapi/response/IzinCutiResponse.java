package com.suryaenergi.sdm.backendapi.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IzinCutiResponse {
    private String msg;
    private Long id;
    private String izinCuti;
    private Boolean cutCuti;
    private List<String> reviewers;
    private String pengajuan;
}
