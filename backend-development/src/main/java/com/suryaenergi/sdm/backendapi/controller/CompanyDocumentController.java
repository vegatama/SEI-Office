package com.suryaenergi.sdm.backendapi.controller;

import com.suryaenergi.sdm.backendapi.entity.CompanyDocument;
import com.suryaenergi.sdm.backendapi.entity.Employee;
import com.suryaenergi.sdm.backendapi.pojo.CompanyDocumentData;
import com.suryaenergi.sdm.backendapi.repository.EmployeeRepository;
import com.suryaenergi.sdm.backendapi.response.CompanyDocumentListResponse;
import com.suryaenergi.sdm.backendapi.response.CompanyDocumentResponse;
import com.suryaenergi.sdm.backendapi.response.CompanyDocumentViewResponse;
import com.suryaenergi.sdm.backendapi.response.MessageResponse;
import com.suryaenergi.sdm.backendapi.service.CompanyDocumentFileService;
import com.suryaenergi.sdm.backendapi.service.CompanyDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/documents")
public class CompanyDocumentController {

    @Autowired
    private CompanyDocumentService documentService;

    @Autowired
    private CompanyDocumentFileService fileService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @PostMapping
    public ResponseEntity<CompanyDocumentResponse> addDocument(
            @RequestParam String documentNumber,
            @RequestParam CompanyDocument.Category category,
            @RequestParam String documentName,
            @RequestParam LocalDate approvalDate,
            @RequestParam int revision,
            @RequestParam MultipartFile file,
            @RequestParam String uploaderEmployeeCode
    ) {
        try {
            CompanyDocument newDocument = documentService.addDocument(
                    documentNumber,
                    category,
                    documentName,
                    approvalDate,
                    revision,
                    file,
                    uploaderEmployeeCode
            );
            CompanyDocumentResponse response = new CompanyDocumentResponse();
            response.setMessage("SUCCESS");
            response.setId(newDocument.getId());
            response.setDocumentNumber(newDocument.getDocumentNumber());
            response.setCategory(newDocument.getCategory());
            response.setDocumentName(newDocument.getDocumentName());
            response.setApprovalDate(newDocument.getApprovalDate());
            response.setRevision(newDocument.getRevision());
            response.setFileUrl(newDocument.getFileUrl());
            Employee uploader = employeeRepository.findByEmployeeCode(uploaderEmployeeCode);
            if (uploader != null) {
                response.setUploaderName(uploader.getFullName());
                response.setUploaderEmployeeCode(uploader.getEmployeeCode());
            }
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            CompanyDocumentResponse response = new CompanyDocumentResponse();
            response.setMessage("ERROR:"+e);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<CompanyDocumentListResponse> getAllDocuments(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size, @RequestParam(required = false) String ownerEmployeeCode) {
        try {
            if (page != null && page < 0) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            if (size == null) {
                size = 10;
            }
            List<CompanyDocument> documents = page == null ? documentService.getAllDocuments(ownerEmployeeCode) : documentService.getAllDocuments(page, size, ownerEmployeeCode);
            CompanyDocumentListResponse response = new CompanyDocumentListResponse();
            response.setMessage("SUCCESS");
            List<CompanyDocumentData> documentData = new ArrayList<>();
            for (CompanyDocument document : documents) {
                CompanyDocumentData data = new CompanyDocumentData();
                data.setId(document.getId());
                data.setDocumentNumber(document.getDocumentNumber());
                data.setCategory(document.getCategory());
                data.setDocumentName(document.getDocumentName());
                data.setApprovalDate(document.getApprovalDate());
                data.setRevision(document.getRevision());
                data.setFileUrl(document.getFileUrl());
                Employee uploader = employeeRepository.findByEmployeeCode(document.getUploaderEmployeeCode());
                if (uploader != null) {
                    data.setUploaderName(uploader.getFullName());
                    data.setUploaderEmployeeCode(uploader.getEmployeeCode());
                }
                documentData.add(data);
            }
            response.setData(documentData);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            CompanyDocumentListResponse response = new CompanyDocumentListResponse();
            response.setMessage("ERROR:"+e);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyDocumentResponse> getDocumentById(@PathVariable Long id) {
        try {
            CompanyDocument document = documentService.getDocumentById(id);
            if (document == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            CompanyDocumentResponse response = new CompanyDocumentResponse();
            response.setMessage("SUCCESS");
            response.setId(document.getId());
            response.setDocumentNumber(document.getDocumentNumber());
            response.setCategory(document.getCategory());
            response.setDocumentName(document.getDocumentName());
            response.setApprovalDate(document.getApprovalDate());
            response.setRevision(document.getRevision());
            response.setFileUrl(document.getFileUrl());
            Employee uploader = employeeRepository.findByEmployeeCode(document.getUploaderEmployeeCode());
            if (uploader != null) {
                response.setUploaderName(uploader.getFullName());
                response.setUploaderEmployeeCode(uploader.getEmployeeCode());
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            CompanyDocumentResponse response = new CompanyDocumentResponse();
            response.setMessage("ERROR:"+e);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/view/{id}")
    public ResponseEntity<CompanyDocumentViewResponse> viewDocument(@PathVariable Long id) {
        try {
            CompanyDocument document = documentService.getDocumentById(id);
            if (document == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            String fileId = document.getFileId();
            if (fileId == null) {
                CompanyDocumentViewResponse response = new CompanyDocumentViewResponse();
                response.setMessage("ERROR:FILE_NOT_FOUND");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            CompanyDocumentViewResponse response = new CompanyDocumentViewResponse();
            response.setMessage("SUCCESS");
            response.setUrl(fileService.generateViewLink(fileId));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            CompanyDocumentViewResponse response = new CompanyDocumentViewResponse();
            response.setMessage("ERROR:"+e);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> updateDocument(
            @PathVariable Long id,
            @RequestParam String documentNumber,
            @RequestParam CompanyDocument.Category category,
            @RequestParam String documentName,
            @RequestParam LocalDate approvalDate,
            @RequestParam String employeeCode,
            @RequestParam int revision
    ) {
        try {
            documentService.updateDocument(
                    id,
                    documentNumber,
                    category,
                    documentName,
                    approvalDate,
                    revision,
                    employeeCode
            );
            MessageResponse response = new MessageResponse();
            response.setMsg("SUCCESS");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            MessageResponse response = new MessageResponse();
            response.setMsg("ERROR:"+e);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteDocument(@PathVariable Long id, @RequestParam String employeeCode) {
        try {
            documentService.deleteDocument(id, employeeCode);
            MessageResponse response = new MessageResponse();
            response.setMsg("SUCCESS");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            MessageResponse response = new MessageResponse();
            response.setMsg("ERROR:"+e);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}