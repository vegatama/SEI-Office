package com.suryaenergi.sdm.backendapi.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IzinCutiRequest {
    private String empcode;
    private long jenisCuti;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String reason;
}
