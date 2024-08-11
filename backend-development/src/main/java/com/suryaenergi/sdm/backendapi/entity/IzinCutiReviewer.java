package com.suryaenergi.sdm.backendapi.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "izin_cuti_reviewer")
@Data
public class IzinCutiReviewer {
    private static final String ID = "ID";
    private static final String TIPE_IZIN_CUTI_ID = "TIPE_IZIN_CUTI_ID";
    private static final String EMP_CODE = "EMP_CODE";
    private static final String LAYER_INDEX = "LAYER_INDEX";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ID)
    private Long id;

    @Column(name = TIPE_IZIN_CUTI_ID)
    private Long tipeIzinCutiId;

    @Column(name = EMP_CODE)
    private String empCode;

    @Column(name = LAYER_INDEX)
    private Integer layerIndex;

    // either one of them (empCode or layerIndex) is null, can't be both

}
