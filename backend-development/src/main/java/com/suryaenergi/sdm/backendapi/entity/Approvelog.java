package com.suryaenergi.sdm.backendapi.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
public class Approvelog implements Serializable {

    @Serial
    private static final long serialVersionUID = -8504327020908904479L;

    private static final String ID = "ID";
    private static final String CREATED_DATETIME = "CREATED_AT";
    private static final String UPDATED_DATETIME = "LAST_UPDATE";

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

    @ManyToOne
    @JoinColumn(name="from_id", referencedColumnName = "id")
    private Employee fromUser;

    @ManyToOne
    @JoinColumn(name="to_id", referencedColumnName = "id")
    private Employee toUser;

    private String status;

    private String alasanReject;
}
