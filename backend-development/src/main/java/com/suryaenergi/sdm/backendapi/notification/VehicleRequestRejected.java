package com.suryaenergi.sdm.backendapi.notification;

import com.suryaenergi.sdm.backendapi.pojo.NotificationData;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class VehicleRequestRejected implements NotificationData.Data {
    private String reviewerName;
    private String vehicleRequestId;
    private String projectName;
    private String reason;

    @Override
    public void loadData(NotificationData.DataList data) {
        reviewerName = data.getString(0);
        vehicleRequestId = data.getString(1);
        projectName = data.getString(2);
        reason = data.getString(3);
    }

    @Override
    public String getMessage() {
        return "{0} menolak permintaan kendaraan anda untuk proyek {2} dengan alasan: {3}";
    }

    @Override
    public String getSubject() {
        return "Permintaan Kendaraan Ditolak";
    }

    @Override
    public List<?> getData() {
        return List.of(reviewerName, vehicleRequestId, projectName, reason);
    }
}
