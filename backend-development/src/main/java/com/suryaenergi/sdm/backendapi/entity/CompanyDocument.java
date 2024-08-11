package com.suryaenergi.sdm.backendapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDocument {
    private static final String ID = "id";
    private static final String DOCUMENT_NUMBER = "document_number";
    private static final String UPLOADER_EMPLOYEE_CODE = "uploader_employee_code";
    private static final String CATEGORY = "category";
    private static final String DOCUMENT_NAME = "document_name";
    private static final String APPROVAL_DATE = "approval_date";
    private static final String REVISION = "revision";
    private static final String FILE_URL = "file_url";
    private static final String FILE_ID = "file_id";

    @Id
    @Column(name = ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = DOCUMENT_NUMBER)
    private String documentNumber;

    @Column(name = CATEGORY)
    private Category category;

    @Column(name = DOCUMENT_NAME)
    private String documentName;

    @Column(name = UPLOADER_EMPLOYEE_CODE)
    private String uploaderEmployeeCode;

    @Column(name = APPROVAL_DATE)
    private LocalDate approvalDate;

    @Column(name = REVISION)
    private int revision;

    @Column(name = FILE_URL)
    private String fileUrl;

    @Column(name = FILE_ID)
    private String fileId;

    public enum Category {
        KEBIJAKAN,
        PEDOMAN,
        PROSEDUR,
        INSTRUKSI_KERJA,
        FORMULIR,
        TEMPLATE;
    }
}
