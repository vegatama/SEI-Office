package com.suryaenergi.sdm.backendapi.entity;

import com.suryaenergi.sdm.backendapi.pojo.NotificationData;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "notification")
@Data
public class Notification implements Serializable {
    private static final String ID = "ID";
    private static final String EMP_CODE = "EMP_CODE";
    private static final String CREATED_DATETIME = "CREATED_AT";
    private static final String TYPE = "TYPE";
    private static final String DATA = "DATA";
    private static final String IS_READ = "IS_READ";

    @Id
    @Column(name = ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = EMP_CODE)
    private String empCode;

    @Column(name = CREATED_DATETIME)
    private LocalDateTime createdAt;

    @Column(name = TYPE)
    private String type;

    @Column(name = DATA)
    private String data; // json list string

    @Column(name = IS_READ)
    private boolean isRead;

    public static Notification fromData(NotificationData data, String empCode) {
        Notification notification = new Notification();
        notification.setEmpCode(empCode);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setType(data.getData().getTypeName());
        notification.setData(data.getDataAsString());
        notification.setRead(data.isRead());
        return notification;
    }
}
