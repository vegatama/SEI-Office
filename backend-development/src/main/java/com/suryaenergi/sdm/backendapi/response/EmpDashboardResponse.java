package com.suryaenergi.sdm.backendapi.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmpDashboardResponse {
    private String msg;
    private int emp_total;
    private int emp_tetap;
    private int emp_kwt;
    private int emp_thl;
}
