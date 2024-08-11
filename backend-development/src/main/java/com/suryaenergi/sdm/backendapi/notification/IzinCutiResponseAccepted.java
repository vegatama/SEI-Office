package com.suryaenergi.sdm.backendapi.notification;

import com.suryaenergi.sdm.backendapi.pojo.NotificationData;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class IzinCutiResponseAccepted implements NotificationData.Data {
    private String reviewerName;
    private long izinCutiId;

    @Override
    public List<?> getData() {
        return List.of(reviewerName, izinCutiId);
    }

    @Override
    public String getMessage() {
        return "{0} menyetujui izin cuti anda";
    }

    @Override
    public void loadData(NotificationData.DataList data) {
        reviewerName = data.getString(0);
        izinCutiId = data.getLong(1);
    }

    @Override
    public String getSubject() {
        return "Persetujuan Izin Cuti Diterima";
    }
}
