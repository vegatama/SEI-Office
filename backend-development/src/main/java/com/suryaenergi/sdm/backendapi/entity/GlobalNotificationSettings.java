package com.suryaenergi.sdm.backendapi.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class GlobalNotificationSettings {

    private static final String EMPLOYEE_ID = "EMPLOYEE_ID";
    private static final String RECEIVE_EVENT_NOTIFICATION = "RECEIVE_EVENT_NOTIFICATION";

    @Id
    @Column(name = EMPLOYEE_ID)
    private Long id;

    @JoinColumn(name = EMPLOYEE_ID)
    @ManyToOne
    @MapsId
    private Employee employee;


    @Column(name = RECEIVE_EVENT_NOTIFICATION)
    private boolean receiveEventNotification;

    public boolean isDefault() {
        return !receiveEventNotification;
    }
}
