package com.suryaenergi.sdm.backendapi.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "pending_swipe_request")
public class PendingSwipeRequest implements Serializable {
    private static final String ID = "ID";
    private static final String EMPLOYEE_CODE = "EMPLOYEE_CODE";
    private static final String EMPLOYEE_NAME = "EMPLOYEE_NAME";
    private static final String EMPLOYEE_JOB_TITLE = "EMPLOYEE_JOB_TITLE";
    private static final String EMPLOYEE_MESIN_ID = "EMPLOYEE_MESIN_ID";
    private static final String ASSIGNED_REVIEWER_EMPLOYEE_CODE = "ASSIGNED_REVIEWER_EMPLOYEE_CODE";
    private static final String DATE_TIME = "DATE_TIME";
    private static final String DATE = "DATE";
    private static final String LATITUDE = "LATITUDE";
    private static final String LONGITUDE = "LONGITUDE";
    private static final String RADIUS = "RADIUS";
    private static final String CENTER_LATITUDE = "CENTER_LATITUDE";
    private static final String CENTER_LONGITUDE = "CENTER_LONGITUDE";
    private static final String REASON = "REASON";
    private static final String APPROVED = "APPROVED";
    private static final String CHECKOUT = "CHECKOUT";
    private static final String IMAGE_URL = "IMAGE_URL";
    private static final String REJECT_REASON = "REJECT_REASON";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ID)
    private Long id;

    @Column(name = EMPLOYEE_CODE, length = 15)
    private String employeeCode;

    @Column(name = EMPLOYEE_NAME, length = 250)
    private String employeeName;

    @Column(name = EMPLOYEE_JOB_TITLE, length = 150)
    private String employeeJobTitle;

    @Column(name = ASSIGNED_REVIEWER_EMPLOYEE_CODE, length = 15)
    private String assignedReviewerEmployeeCode;

    @Column(name = EMPLOYEE_MESIN_ID, length = 50)
    private String employeeMesinId;

    @Column(name = DATE_TIME)
    private LocalDateTime dateTime;

    @Column(name = DATE)
    private LocalDate date;

    @Column(name = LATITUDE)
    private double latitude;

    @Column(name = LONGITUDE)
    private double longitude;

    @Column(name = RADIUS)
    private double radius;

    @Column(name = CENTER_LATITUDE)
    private double centerLatitude;

    @Column(name = CENTER_LONGITUDE)
    private double centerLongitude;

    @Column(name = REASON)
    private String reason;

    @Column(name = APPROVED)
    private int approved;

    @Column(name = CHECKOUT)
    private boolean checkout; // is checkout

    @Column(name = IMAGE_URL)
    private String imageUrl;

    @Column(name = REJECT_REASON)
    private String rejectReason;
}
