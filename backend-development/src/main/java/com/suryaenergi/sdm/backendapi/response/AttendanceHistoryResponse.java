package com.suryaenergi.sdm.backendapi.response;

import com.suryaenergi.sdm.backendapi.pojo.AttendanceData;
import com.suryaenergi.sdm.backendapi.pojo.DataAbsenSaya;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class AttendanceHistoryResponse {
    private String message;
    private List<AttendanceData> data;
}
