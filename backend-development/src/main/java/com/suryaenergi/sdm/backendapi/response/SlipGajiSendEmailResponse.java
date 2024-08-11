package com.suryaenergi.sdm.backendapi.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SlipGajiSendEmailResponse {
    private String message;
    private int employeeSent;
    private int employeeAlreadySent;
    private int employeeSentWithIncompleteData;
}
