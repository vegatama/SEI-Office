package com.suryaenergi.sdm.backendapi.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "izin_cuti_file")
@Data
public class IzinCutiFile {
    private static final String ID = "ID";
    private static final String IZIN_CUTI_ID = "IZIN_CUTI_ID";
    private static final String FILE_NAME = "FILE_NAME";
    private static final String ORIGINAL_FILE_NAME = "ORIGINAL_FILE_NAME";
    private static final String FILE_DOWNLOAD_URI = "FILE_DOWNLOAD_URI";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ID)
    private Long id;

    @Column(name = IZIN_CUTI_ID)
    @JoinColumn(name = "id", table = "izin_cuti")
    private Long izinCutiId;

    @Column(name = FILE_NAME)
    private String fileName;

    @Column(name = FILE_DOWNLOAD_URI)
    private String fileDownloadUri;

    @Column(name = ORIGINAL_FILE_NAME)
    private String originalFileName;
}
