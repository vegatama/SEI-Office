package com.suryaenergi.sdm.backendapi.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProyekDpbDetail {
    private String project_code;
    private String project_name;
    private String document_no;
    private String document_date;
    private String amount;
    private String amount_inc_vat;
    private String tgl_dibutuhkan;
    private String status;
}
