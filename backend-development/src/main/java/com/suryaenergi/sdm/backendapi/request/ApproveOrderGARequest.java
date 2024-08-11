package com.suryaenergi.sdm.backendapi.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApproveOrderGARequest {
    private String order_id;
    private String approval_id;
    private String vehicleId;
    private String driver;
    private String hpDriver;
}
