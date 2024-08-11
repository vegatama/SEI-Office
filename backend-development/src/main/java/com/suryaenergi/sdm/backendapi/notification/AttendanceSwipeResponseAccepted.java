package com.suryaenergi.sdm.backendapi.notification;

import com.suryaenergi.sdm.backendapi.pojo.NotificationData;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class AttendanceSwipeResponseAccepted implements NotificationData.Data {
    private String reviewerName;
    private LocalDate time;
    private boolean isCheckOut;

    @Override
    public List<?> getData() {
        return List.of(reviewerName, time.toString(), isCheckOut);
    }

    @Override
    public String getMessage() {
        return "{0} menyetujui check-in anda";
    }

    @Override
    public void loadData(NotificationData.DataList data) {
        reviewerName = data.getString(0);
        time = LocalDate.parse(data.getString(1));
        isCheckOut = data.getBoolean(2);
    }

    @Override
    public String getSubject() {
        return "Persetujuan Swipe Kehadiran Diterima";
    }
}
