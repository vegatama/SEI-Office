package com.suryaenergi.sdm.backendapi.pojo;

import com.suryaenergi.sdm.backendapi.pojo.SlipGajiDataEntry;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SlipGajiDataDetail {
    private int tahun;
    private int bulan;
    private List<SlipGajiDataEntry> data;
}
