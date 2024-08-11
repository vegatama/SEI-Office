package com.suryaenergi.sdm.backendapi.response;

import com.suryaenergi.sdm.backendapi.pojo.ProyekBudgetDetail;
import com.suryaenergi.sdm.backendapi.pojo.ProyekData;
import com.suryaenergi.sdm.backendapi.pojo.ProyekDpbDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProyekDetailResponse {
    private String msg;
    private ProyekData proyek;
    private List<ProyekBudgetDetail> budgets;
    private List<ProyekDpbDetail> dpbs;
}
