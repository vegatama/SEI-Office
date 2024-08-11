package com.suryaenergi.sdm.backendapi.entity;

import com.suryaenergi.sdm.backendapi.pojo.JenisIzinCutiData;
import jakarta.persistence.*;
import lombok.Data;

@Table(name = "jenis_izin_cuti")
@Data
@Entity
public class JenisIzinCuti {
    private static final long serialVersionUID = 6395720174938790387L;
    private static final String ID = "ID";
    private static final String NAMA_IZIN_CUTI = "NAMA_IZIN_CUTI";
    private static final String CUT_CUTI = "CUT_CUTI";
//    private static final String APPROVAL = "APPROVAL";
    private static final String PENGAJUAN = "PENGAJUAN";
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ID)
    private Long id;

    @Column(name = NAMA_IZIN_CUTI)
    private String izinCuti;
    
    @Column(name = CUT_CUTI)
    private Boolean cutCuti;

//    @Column(name = APPROVAL)
//    private List<String> approval;

    @Column(name = PENGAJUAN)
    private JenisIzinCutiData.TipePengajuan pengajuan;
}
