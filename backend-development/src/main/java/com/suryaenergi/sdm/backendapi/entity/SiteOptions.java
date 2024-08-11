package com.suryaenergi.sdm.backendapi.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "options")
@Data
public class SiteOptions implements Serializable {
    @Serial
    private static final long serialVersionUID = 3993689031696904690L;
    private static final String ID = "ID";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ID)
    private Long id;

    private String name;
    private String value;
}
