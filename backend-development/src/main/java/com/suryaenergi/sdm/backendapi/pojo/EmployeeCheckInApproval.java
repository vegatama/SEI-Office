package com.suryaenergi.sdm.backendapi.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeCheckInApproval {
    private long id;
    private String employeeId;
    private String employeeCode;
    private String employeeName;
    private String employeeJobTitle;
    private String reason;
    private double longitude;
    private double latitude;
    private double centerLongitude;
    private double centerLatitude;
    private double radius;
    private int approved;
    private LocalDate date;
    private LocalTime time;
    private LocalTime targetTime;
    private boolean checkOut;
    private String imageUrl;
    private String lokasiAbsen;
    private double distance;
    private String rejectReason;
}
