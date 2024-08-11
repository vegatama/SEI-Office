package com.suryaenergi.sdm.backendapi.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.suryaenergi.sdm.backendapi.pojo.JatahCutiData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JatahCutiListResponse {
    private String msg;
    private List<JatahCutiData> jatahCuti;
}
