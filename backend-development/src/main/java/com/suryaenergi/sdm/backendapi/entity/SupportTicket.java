package com.suryaenergi.sdm.backendapi.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "support_ticket")
public class SupportTicket implements Serializable {

    private static final long serialVersionUID = 3633000137464559760L;
    private static final String ID = "ID";
    private static final String CREATED_DATETIME = "CREATED_AT";
    private static final String UPDATED_DATETIME = "LAST_UPDATE";
    private static final String REQUESTER_NAME = "REQUESTER_NAME";
    private static final String REQUESTER_EMAIL = "REQUESTER_EMAIL";
    private static final String SUBJECT = "SUBJECT";
    private static final String DEPARTMENT = "DEPARTMENT";
    private static final String DETAIL = "DETAIL";
    private static final String IMAGE = "IMAGE";
    private static final String STATUS = "STATUS";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ID)
    private Long id;

    @CreationTimestamp
    @Column(name = CREATED_DATETIME)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = UPDATED_DATETIME)
    private LocalDateTime updateAt;

    @Column(name = REQUESTER_NAME)
    private String requesterName;

    @Column(name = REQUESTER_EMAIL)
    private String requesterEmail;

    @Column(name = SUBJECT)
    private String subject;

    @Column(name = DEPARTMENT)
    private String department;

    @Column(name = DETAIL, columnDefinition = "TEXT")
    private String detail;

    @Column(name = IMAGE)
    private String image;

    @Column(name = STATUS)
    private String status;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    private Employee assignee;

    private String solution;
}
