package com.suryaenergi.sdm.backendapi.notification;

import com.suryaenergi.sdm.backendapi.pojo.NotificationData;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class NotificationIzinCutiRequest implements NotificationData.Data {
    private String employeeName;
    private String jenisName;
    private long izinCutiId;

    @Override
    public List<?> getData() {
        return List.of(employeeName, jenisName, izinCutiId);
    }

    @Override
    public String getMessage() {
        return "{0} meminta izin cuti: {1}";
    }

    @Override
    public void loadData(NotificationData.DataList data) {
        employeeName = data.getString(0);
        jenisName = data.getString(1);
        izinCutiId = data.getLong(2);
    }

    @Override
    public String getSubject() {
        return "Permintaan Izin Cuti";
    }
}
