package com.suryaenergi.sdm.backendapi.service;

import com.suryaenergi.sdm.backendapi.entity.CompanyDocument;
import com.suryaenergi.sdm.backendapi.pojo.CompanyDocumentFile;
import com.suryaenergi.sdm.backendapi.repository.CompanyDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
public class CompanyDocumentService {

    @Autowired
    private CompanyDocumentRepository documentRepository;

    @Autowired
    private CompanyDocumentFileService fileService;

    public CompanyDocument addDocument(
            String documentNumber,
            CompanyDocument.Category category,
            String documentName,
            LocalDate approvalDate,
            int revision,
            MultipartFile file,
            String uploaderEmployeeCode) {
        CompanyDocument document = new CompanyDocument();
        document.setDocumentNumber(documentNumber);
        document.setCategory(category);
        document.setDocumentName(documentName);
        document.setApprovalDate(approvalDate);
        document.setUploaderEmployeeCode(uploaderEmployeeCode);
        document.setRevision(revision);
        CompanyDocumentFile fileResult = fileService.saveFile(file);
        document.setFileUrl(fileResult.getFileUrl());
        document.setFileId(fileResult.getFileId());
        return documentRepository.save(document);
    }

    public List<CompanyDocument> getAllDocuments(int page, int size, String employeeCode) {
        if (employeeCode != null) {
            return documentRepository.findAllByUploaderEmployeeCode(employeeCode, Pageable.ofSize(size).withPage(page)).toList();
        }
        return documentRepository.findAllBy(Pageable.ofSize(size).withPage(page)).toList();
    }

    public List<CompanyDocument> getAllDocuments(String employeeCode) {
        if (employeeCode != null) {
            return documentRepository.findAllByUploaderEmployeeCode(employeeCode);
        }
        return documentRepository.findAll();
    }

    public CompanyDocument getDocumentById(Long id) {
        Optional<CompanyDocument> document = documentRepository.findById(id);
        return document.orElse(null);
    }

    public void updateDocument(
            Long id,
            String documentNumber,
            CompanyDocument.Category category,
            String documentName,
            LocalDate approvalDate,
            int revision,
            String employeeCode
    ) {
        Optional<CompanyDocument> existingDocument = documentRepository.findById(id);
        if (existingDocument.isPresent()) {
            CompanyDocument updatedDocument = existingDocument.get();
            if (!employeeCode.equals(updatedDocument.getUploaderEmployeeCode())) {
                throw new RuntimeException("UNAUTHORIZED");
            }
            updatedDocument.setDocumentNumber(documentNumber);
            updatedDocument.setCategory(category);
            updatedDocument.setDocumentName(documentName);
            updatedDocument.setApprovalDate(approvalDate);
            updatedDocument.setRevision(revision);
            documentRepository.save(updatedDocument);
        }
    }

    public void deleteDocument(Long id, String employeeCode) {
        Optional<CompanyDocument> existingDocument = documentRepository.findById(id);
        if (existingDocument.isPresent()) {
            CompanyDocument document = existingDocument.get();
            if (!employeeCode.equals(document.getUploaderEmployeeCode())) {
                throw new RuntimeException("UNAUTHORIZED");
            }
            documentRepository.deleteById(id);
        }
        documentRepository.deleteById(id);
    }
}