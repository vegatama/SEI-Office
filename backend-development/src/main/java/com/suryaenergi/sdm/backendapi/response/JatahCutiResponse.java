package com.suryaenergi.sdm.backendapi.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JatahCutiResponse {
    private String message;
    private Long id;
    private String employeeId;
    private String empcode;
    private String employeeName;
    private int tahun;
    private int jumlahHari;
}
