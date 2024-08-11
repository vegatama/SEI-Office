package com.suryaenergi.sdm.backendapi.notification;

import com.suryaenergi.sdm.backendapi.pojo.NotificationData;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class AttendanceSwipeRequest implements NotificationData.Data {
    private String employeeName;
    private long pendingSwipeId;
    private boolean isCheckOut;

    @Override
    public List<?> getData() {
        return List.of(employeeName, pendingSwipeId, isCheckOut);
    }

    @Override
    public String getMessage() {
        if (isCheckOut) {
            return "{0} meminta persetujuan check-out";
        }
        return "{0} meminta persetujuan check-in";
    }

    @Override
    public void loadData(NotificationData.DataList data) {
        employeeName = data.getString(0);
        pendingSwipeId = data.getLong(1);
        isCheckOut = data.getBoolean(2);
    }

    @Override
    public String getSubject() {
        return "Permintaan Persetujuan Swipe Kehadiran";
    }
}
