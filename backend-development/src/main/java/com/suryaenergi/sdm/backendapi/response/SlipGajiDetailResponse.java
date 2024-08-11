package com.suryaenergi.sdm.backendapi.response;

import com.suryaenergi.sdm.backendapi.pojo.SlipGajiField;
import com.suryaenergi.sdm.backendapi.pojo.SlipGajiItemData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SlipGajiDetailResponse {
    private long id;
    private String name;
    private String message;
    private int tahun;
    private int bulan;
    private List<SlipGajiField> fields;
    private int revision; // used to check the updateCount in the request
}
