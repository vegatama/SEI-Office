package com.suryaenergi.sdm.backendapi.notification;

import com.suryaenergi.sdm.backendapi.pojo.NotificationData;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
public class SimpleNotification implements NotificationData.Data {
    private String message;

    @Override
    public void loadData(NotificationData.DataList data) {
        message = data.getString(0);
    }

    @Override
    public List<?> getData() {
        return List.of(message);
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getSubject() {
        return "Notification";
    }
}
