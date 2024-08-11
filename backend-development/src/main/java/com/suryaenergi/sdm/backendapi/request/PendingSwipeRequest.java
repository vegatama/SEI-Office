package com.suryaenergi.sdm.backendapi.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PendingSwipeRequest {
    private String empcode;
    private double latitude;
    private double longitude;
    private String reason;
    private MultipartFile image;
}
