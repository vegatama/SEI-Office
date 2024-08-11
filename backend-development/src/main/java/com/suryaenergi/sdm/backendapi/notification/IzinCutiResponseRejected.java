package com.suryaenergi.sdm.backendapi.notification;

import com.suryaenergi.sdm.backendapi.pojo.NotificationData;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class IzinCutiResponseRejected implements NotificationData.Data {
    private String reviewerName;
    private String reason;
    private long izinCutiId;

    @Override
    public List<?> getData() {
        return List.of(reviewerName, reason, izinCutiId);
    }

    @Override
    public String getMessage() {
        return "{0} menolak izin cuti anda dengan alasan: {1}";
    }

    @Override
    public void loadData(NotificationData.DataList data) {
        reviewerName = data.getString(0);
        reason = data.getString(1);
        izinCutiId = data.getLong(2);
    }

    @Override
    public String getSubject() {
        return "Persetujuan Izin Cuti Ditolak";
    }
}
