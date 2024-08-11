package com.suryaenergi.sdm.backendapi.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public class IzinCutiFileService {

    @Autowired
    private AmazonS3 s3;

    public static final String BUCKET_NAME = "seioffice";
    public static final String STORAGE_PATH = "development/";

    private String generateId() {
        return UUID.randomUUID().toString();
    }

    private void validateId(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("ID cannot be empty");
        }
        // ids are UUIDs
        try {
            UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid ID");
        }
    }

    public String uploadFile(MultipartFile file, String employeeCode) {
        // save file to database
        String id = generateId();
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());
            metadata.setContentDisposition("attachment; filename=" + file.getOriginalFilename());
//            s3.putObject(BUCKET_NAME, STORAGE_PATH + employeeCode + "/izin_cuti_files/" + id + ".bin", file.getInputStream(), metadata);
            PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET_NAME, STORAGE_PATH + employeeCode + "/izin_cuti_files/" + id + ".bin", file.getInputStream(), metadata);
            putObjectRequest = putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead);
            s3.putObject(putObjectRequest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return id;
    }

    public String getFileUrl(String id, String employeeCode) {
        validateId(id);
        return s3.getUrl(BUCKET_NAME, STORAGE_PATH + employeeCode + "/izin_cuti_files/" + id + ".bin").toString();
    }
}
