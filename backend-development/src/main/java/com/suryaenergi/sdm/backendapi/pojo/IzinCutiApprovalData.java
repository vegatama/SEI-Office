package com.suryaenergi.sdm.backendapi.pojo;

import com.suryaenergi.sdm.backendapi.response.IzinCutiDetailResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class IzinCutiApprovalData {
    private String reviewerEmployeeCode;
    private String reviewerName;
    private String reviewerJobTitle;
    private IzinCutiDetailResponse.Status status;
    private String reason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
