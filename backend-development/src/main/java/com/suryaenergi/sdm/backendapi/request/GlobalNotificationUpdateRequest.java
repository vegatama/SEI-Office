package com.suryaenergi.sdm.backendapi.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GlobalNotificationUpdateRequest {
    private String employeeCode;
    private Boolean receiveEventNotification;
}
