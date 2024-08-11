package com.suryaenergi.sdm.backendapi.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "slip_gaji_entry_data")
public class SlipGajiEntryData {
    private static final String ID = "ID";
    private static final String SLIP_FIELD = "SLIP_FIELD";
    private static final String EMPLOYEE = "EMPLOYEE";
    private static final String VALUE = "VALUE";
    private static final String IS_SENT = "IS_SENT";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ID)
    private Long id;

//    @ManyToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = SLIP_FIELD, referencedColumnName = "ID")
//    private SlipGajiFieldTemplate slipField;
    @Column(name = SLIP_FIELD)
    private Long slipField;

//    @ManyToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = EMPLOYEE, referencedColumnName = "ID")
//    private Employee employee;
    @Column(name = EMPLOYEE)
    private String employee; // employeeCode

    @Column(name = VALUE)
    private Integer value;

    @Column(name = IS_SENT)
    private Boolean isSent;

}
