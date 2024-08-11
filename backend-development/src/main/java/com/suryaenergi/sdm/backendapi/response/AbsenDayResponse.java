package com.suryaenergi.sdm.backendapi.response;

import com.suryaenergi.sdm.backendapi.pojo.AbsenDay;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AbsenDayResponse {
    private String msg;
    private String tgl;
    private String hari;
    private int count;
    private List<AbsenDay> data;
}
