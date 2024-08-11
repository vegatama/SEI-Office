package com.suryaenergi.sdm.backendapi.request;

import com.suryaenergi.sdm.backendapi.pojo.SlipGajiField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SlipGajiTemplateUpdate {
    private Long id;
    private String name;
    private int tahun;
    private int bulan;
    private List<SlipGajiField> fields;
}
