package com.suryaenergi.sdm.backendapi.pojo;

import com.suryaenergi.sdm.backendapi.response.IzinCutiDetailResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class IzinCutiData {
    private long id;
    private IzinCutiDetailResponse.Status status;
    private String jenisName;
    private JenisIzinCutiData.TipePengajuan tipe;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
