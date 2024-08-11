package com.suryaenergi.sdm.backendapi.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataAbsenDayDash {
    private int sum_check_time;
    private int sum_check_time_sekper;
    private int sum_check_time_enginer;
    private int sum_check_time_mppp;
    private int sum_check_time_keuangan;
    private int sum_check_time_sdmu;
    private int sum_check_time_bisnis;
    private int sum_check_time_pemasaran;
    private int sum_check_time_logistik;
}
