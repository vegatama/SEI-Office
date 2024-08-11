package com.suryaenergi.sdm.backendapi.response;

import com.suryaenergi.sdm.backendapi.entity.SlipGajiEntryData;
import com.suryaenergi.sdm.backendapi.pojo.SlipGajiDataEntry;
import com.suryaenergi.sdm.backendapi.pojo.SlipGajiField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SlipGajiDataResponse {
    private long id;
    private String name;
    private String message;
    private int tahun;
    private int bulan;
    private List<SlipGajiDataEntry> data;
    private List<SlipGajiField> fields;
}
