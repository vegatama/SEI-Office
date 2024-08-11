package com.suryaenergi.sdm.backendapi.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.suryaenergi.sdm.backendapi.pojo.CompanyDocumentFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.UUID;

@Service
public class CompanyDocumentFileService {

    public static final String BUCKET_NAME = "seioffice";

    @Value("${objectstorage.path}")
    public static final String STORAGE_PATH = "unset/";

    @Autowired
    private AmazonS3 s3;

    public String generateId() {
        return UUID.randomUUID().toString();
    }
    public CompanyDocumentFile saveFile(MultipartFile image) {
        // save image to database
        String id = generateId();
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(image.getSize());
            metadata.setContentType(image.getContentType());
            metadata.setContentDisposition("attachment; filename=" + image.getOriginalFilename());
            PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET_NAME, STORAGE_PATH + "documents/" + id + ".bin", image.getInputStream(), metadata);
            putObjectRequest = putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead);
            s3.putObject(putObjectRequest);
            URL url = s3.getUrl(BUCKET_NAME, STORAGE_PATH + "documents/" + id + ".bin");
//            return url.toString();
            return new CompanyDocumentFile(id, url.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String generateViewLink(String id) {
        Date expiration = new Date();
        // + 6 hour
        expiration.setTime(expiration.getTime() + 1000 * 60 * 60 * 6);
        ResponseHeaderOverrides responseHeaders = new ResponseHeaderOverrides();
        // disable download/content-disposition
        responseHeaders.setContentDisposition("inline");
        return s3.generatePresignedUrl(new GeneratePresignedUrlRequest(BUCKET_NAME, STORAGE_PATH + "documents/" + id + ".bin").withExpiration(expiration)
                .withResponseHeaders(responseHeaders)).toString();

    }
}
