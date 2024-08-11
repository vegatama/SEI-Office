package com.suryaenergi.sdm.backendapi.response;

import com.suryaenergi.sdm.backendapi.pojo.IzinCutiData;
import com.suryaenergi.sdm.backendapi.pojo.IzinCutiRequestData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IzinCutiRequestListResponse {
    private String message;
    private List<IzinCutiRequestData> data;
}
