package com.suryaenergi.sdm.backendapi.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProyekData {
    private String project_name;
    private String project_code;
    private String project_type;
    private String project_value;
    private String pimpro;
    private boolean carry_over;
    private String tgl_mulai;
    private String tgl_akhir;
    private String status;
    private String closing_date;
}
