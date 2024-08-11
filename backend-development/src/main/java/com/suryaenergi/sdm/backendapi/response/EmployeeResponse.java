package com.suryaenergi.sdm.backendapi.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmployeeResponse {
    private String msg;
    private String employee_code;
    private String nik;
    private String full_name;
    private String email;
}
