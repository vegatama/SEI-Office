package com.suryaenergi.sdm.backendapi.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AbsenDashboardResponse {
    private String msg;
    private String absen_masuk_today;
    private String absen_keluar_today;
    private String absen_masuk_yesterday;
    private String absen_keluar_yesterday;
    private String total_lupa_check_time;
    private String total_terlambat;
    private String total_kurang_jam;
}
