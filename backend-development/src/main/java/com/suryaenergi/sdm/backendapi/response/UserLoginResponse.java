package com.suryaenergi.sdm.backendapi.response;

import com.suryaenergi.sdm.backendapi.pojo.LokasiAbsenData;

import com.suryaenergi.sdm.backendapi.pojo.RoleData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginResponse {
    private String msg;
    private String email;
    private String name;
    private String nik;
    private String employee_code;
    private String token;
    private String access;
    private boolean create_password;
    private String status;
    private boolean is_active;
    private String employee_id;
    private String jobTitle;
    private String nav_id;
    private LokasiAbsenData lokasi_absen;
    private RoleData roles;
    private String avatar;
    private String maintenance_mode = "0";
}
