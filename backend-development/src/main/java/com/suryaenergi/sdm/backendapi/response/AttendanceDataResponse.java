package com.suryaenergi.sdm.backendapi.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class AttendanceDataResponse {
    private String message;
    private LocalTime jamMasuk;
    private LocalTime jamKeluar;
    private LocalTime checkIn;
    private LocalTime checkOut;
    private int approved;
    private String reviewerEmployeeCode;
    private String reviewerEmployeeName;
    private String keterangan;
    // TODO: mobile
    private int approvedCheckOut;
    private String reviewerCheckOutEmployeeCode;
    private String reviewerCheckOutEmployeeName;
    private String keteranganCheckOut;
    private Double longitudeCheckOut;
    private Double latitudeCheckOut;
    private String imageUrl;
    private String imageUrlCheckOut;
    //
    private double centerLongitude;
    private double centerLatitude;
    private double radius;
    // TODO: mobile, make nullable
    private Double longitude;
    private Double latitude;

    private String rejectReason;
    private String checkOutRejectReason;
}
