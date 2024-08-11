package com.suryaenergi.sdm.backendapi.notification;

import com.suryaenergi.sdm.backendapi.pojo.NotificationData;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class VehiclePendingRequest implements NotificationData.Data {
    private String requestorName;
    private String vehicleRequestId;
    private String projectName;
    private String destination;
    private LocalDateTime departureTime;
    @Override
    public void loadData(NotificationData.DataList data) {
        requestorName = data.getString(0);
        vehicleRequestId = data.getString(1);
        projectName = data.getString(2);
        destination = data.getString(3);
        departureTime = data.getLocalDateTime(4);
    }

    @Override
    public List<?> getData() {
        return List.of(requestorName, vehicleRequestId, projectName, destination, departureTime.toString());
    }

    @Override
    public String getMessage() {
        return
                "{0} meminta persetujuan pengajuan kendaraan untuk proyek {2} ke {3} pada {4}";
    }

    @Override
    public String getSubject() {
        return "Permintaan Persetujuan Pengajuan Kendaraan";
    }
}
