package com.suryaenergi.sdm.backendapi.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JenisIzinCutiReviewer {
    private Integer layerIndex;
    private String empCode;
    // either one of them is null, can't be both
}
