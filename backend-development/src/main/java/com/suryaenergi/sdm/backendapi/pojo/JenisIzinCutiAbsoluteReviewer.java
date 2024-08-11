package com.suryaenergi.sdm.backendapi.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JenisIzinCutiAbsoluteReviewer {
    private String empCode;
    private String empName;
    private Integer layerIndex;
    private String empJobTitle;
    private String empAvatar;
}
