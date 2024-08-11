package com.suryaenergi.sdm.backendapi.response;

import java.util.List;

import com.suryaenergi.sdm.backendapi.pojo.JenisIzinCutiData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JenisIzinCutiListResponse {
    private String msg;
    private int count;
    private List<JenisIzinCutiData> izinCuti;
}
