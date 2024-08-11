package com.suryaenergi.sdm.backendapi.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SlipGajiDataEntry {
    private String employeeCode;
    private String employeeName;
    private List<EntryData> dataList;
    private boolean complete;
    private boolean sent;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EntryData {
        private Long idField;
        private Integer value;
    }
}
