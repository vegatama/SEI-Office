package com.suryaenergi.sdm.backendapi.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JatahCutiData {
    private Long id;
    private String employeeId;
    private String empcode;
    private String employeeName;
    private int tahun;
    private int jumlahCuti;
    private String keterangan;
    private String referrer;
    private String referrerName;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
