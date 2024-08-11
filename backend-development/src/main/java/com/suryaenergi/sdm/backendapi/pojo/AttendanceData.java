package com.suryaenergi.sdm.backendapi.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceData {
    private int day;
    private int month;
    private int year;
    private LocalTime jamMasuk; // jika null, gunakan default value (08:00)
    /**
     * @see com.suryaenergi.sdm.backendapi.pojo.DataAbsenSaya#STATUS_LIBUR
     */
    private String status;
    /*
    Check in dihitung dari jam pertama kali swipe/check in di hari itu.
    Check out dihitung dari jam terakhir kali swipe/check out di hari itu.
    Swipe atau data dari mesin absensi yang berada di tengah-tengah waktu check in dan check out
    diabaikan. Sehingga, jika ada 3 swipe, yaitu 07:00, 07:30, 08:00, maka yang dihitung adalah
    07:00 dan 08:00.
     */
    private LocalTime firstCheckIn;
    private LocalTime lastCheckOut;
    private String keterangan;
    private int approved;

    private int approvedCheckOut;
    private String keteranganCheckOut;
    private Double longitude;
    private Double latitude;
    private Double distance;
    private Double distanceCheckOut;
    private Double centerLongitude;
    private Double centerLatitude;
    private Double radius;
    private Double longitudeCheckOut;
    private Double latitudeCheckOut;

    private String rejectReason;
    private String checkOutRejectReason;
}
