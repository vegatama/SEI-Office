package com.suryaenergi.sdm.backendapi.response;

import com.suryaenergi.sdm.backendapi.pojo.DataRekapAbsenHeaderOnly;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RekapAbsenListResponse {
    private String msg;
    private int count=0;
    private List<DataRekapAbsenHeaderOnly> data;
}
