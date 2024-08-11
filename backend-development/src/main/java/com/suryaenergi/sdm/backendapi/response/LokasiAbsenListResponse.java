package com.suryaenergi.sdm.backendapi.response;

import java.util.List;

import com.suryaenergi.sdm.backendapi.pojo.LokasiAbsenData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LokasiAbsenListResponse {
    private String msg;
    private int count;
    private List<LokasiAbsenData> lokasiAbsen;
}
