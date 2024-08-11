package com.suryaenergi.sdm.backendapi.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApproveOrderRequest {
    private String order_id;
    private String approval_id;
    private String assignedVehicleId;
    private String assignedDriverName;
    private String assignedDriverPhone;
    private String assignedOtherPlatNumber;
    private String assignedOtherTipe;
    private String assignedOtherMerk;
    private String assignedOtherTahun;
    private String assignedOtherBBM;
    private String assignedOtherPemilik;
    private String assignedOtherPKB;
    private String assignedOtherKeterangan;
}
