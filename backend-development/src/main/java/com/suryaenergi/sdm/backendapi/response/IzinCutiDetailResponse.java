package com.suryaenergi.sdm.backendapi.response;

import com.suryaenergi.sdm.backendapi.pojo.IzinCutiApprovalData;
import com.suryaenergi.sdm.backendapi.pojo.IzinCutiFileData;
import com.suryaenergi.sdm.backendapi.pojo.JenisIzinCutiData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IzinCutiDetailResponse {
    private String message;
    private long id;
    private String employeeCode;
    private String employeeName;
    private String employeeJobTitle;
    private Status status;
    private JenisIzinCutiData jenis;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String reason;
    private List<IzinCutiApprovalData> approvals;
    private List<IzinCutiFileData> files;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public enum Status {
        PENDING, // pengajuan masih menunggu persetujuan
        APPROVED, // pengajuan disetujui oleh reviewer
        REJECTED, // pengajuan ditolak oleh reviewer
        CANCELLED, // pengajuan dibatalkan oleh user
        ON_GOING, // pengajuan sudah disetujui, dan sedang berlangsung
        DONE, // pengajuan sudah selesai
        WAITING, // pengajuan menunggu persetujuan dari peninjau sebelumnya
    }
}
