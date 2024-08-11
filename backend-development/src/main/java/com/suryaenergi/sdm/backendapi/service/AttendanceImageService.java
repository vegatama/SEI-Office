package com.suryaenergi.sdm.backendapi.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class AttendanceImageService {

    public static final String BUCKET_NAME = "seioffice";
    @Value("${objectstorage.path}")
    public static final String STORAGE_PATH = "unset/";

    @Autowired
    private AmazonS3 s3;

    public String generateId() {
        return UUID.randomUUID().toString();
    }
    public String saveImage(String employeeCode, MultipartFile image) {
        // save image to database
        String id = generateId();
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(image.getSize());
            metadata.setContentType(image.getContentType());
            metadata.setContentDisposition("attachment; filename=" + image.getOriginalFilename());
            PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET_NAME, STORAGE_PATH + employeeCode + "/attendance_images/" + id + ".bin", image.getInputStream(), metadata);
            putObjectRequest = putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead);
            s3.putObject(putObjectRequest);
            return s3.getUrl(BUCKET_NAME, STORAGE_PATH + employeeCode + "/attendance_images/" + id + ".bin").toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    // get the image bytes
    public String getImage(String employeeCode, String id) {
        validateId(id);
        return s3.getUrl(BUCKET_NAME, STORAGE_PATH + employeeCode + "/attendance_images/" + id + ".bin").toString();
    }

}
