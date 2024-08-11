package com.suryaenergi.sdm.backendapi.notification;

import com.suryaenergi.sdm.backendapi.pojo.NotificationData;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class VehicleRequestReady implements NotificationData.Data {
    private String vehicleRequestId;
    private String projectName;
    private String driverName;
    private String vehicleName;

    @Override
    public void loadData(NotificationData.DataList data) {
        vehicleRequestId = data.getString(0);
        projectName = data.getString(1);
        driverName = data.getString(2);
        vehicleName = data.getString(3);
    }

    @Override
    public String getMessage() {
        return "Driver {2} dengan kendaraan {3} siap untuk proyek {1}";
    }

    @Override
    public String getSubject() {
        return "Driver dan Kendaraan Siap";
    }

    @Override
    public List<?> getData() {
        return List.of(vehicleRequestId, projectName, driverName, vehicleName);
    }
}
