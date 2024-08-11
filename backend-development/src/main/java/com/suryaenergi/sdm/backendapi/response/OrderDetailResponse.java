package com.suryaenergi.sdm.backendapi.response;

import com.suryaenergi.sdm.backendapi.pojo.VehiclesData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailResponse {
    private String msg;
    private String order_id;

    /**
     * @see #userList
     */
    @Deprecated
    private List<String> users;

    private List<User> userList;

    /**
     * @see #berangkat_date and #berangkat_time
     */
    @Deprecated
    private String waktu_berangkat;

    private String berangkat_date;
    private String berangkat_time;

    private LocalDate tanggal_kembali;
    private LocalTime jam_kembali;
    
    private String tujuan;
    private String keperluan;

    /**
     * @see #kode_proyek
     */
    @Deprecated
    private String kode_proyek;

    private String project_code;

    private String keterangan;
    private String approval;
    private String time_approval;
    private VehiclesData mobil;
    private String driver;
    private String hp_driver;
    private String waktu_kembali;
    private String status;
    private String need_approve;
    private String need_approve_id;

    @Data
    @AllArgsConstructor
    public static class User {
        private String id;
        private String name;
    }
}
