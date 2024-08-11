package com.suryaenergi.sdm.backendapi.pojo;

import com.suryaenergi.sdm.backendapi.entity.CompanyDocument;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDocumentData {
    private Long id;
    private String documentNumber;
    private CompanyDocument.Category category;
    private String documentName;
    private LocalDate approvalDate;
    private int revision;
    private String fileUrl;
    private String uploaderName;
    private String uploaderEmployeeCode;
}
