package com.suryaenergi.sdm.backendapi.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProyekBudgetDetail {
    private String gl_account;
    private String gl_name;
    private String budget = "0";
    private String penggunaan = "0";
    private String realisasi = "0";
}
