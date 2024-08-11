package com.suryaenergi.sdm.backendapi.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GlobalNotificationResponse {
    private String message;
    private String employeeName;
    private String employeeCode;
    private String jobTitle;
    private boolean receiveEventNotification;
}
