package com.suryaenergi.sdm.backendapi.pojo;

import com.suryaenergi.sdm.backendapi.entity.SlipGajiFieldTemplate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SlipGajiField {
    private Long id; // nullable, if null then insert, if not null then update
    private String name;
    private Integer order; // nullable, if null then don't update, if not null then update
    private SlipGajiFieldTemplate.Kategori category; // nullable, if null then don't update, if not null then update
}
