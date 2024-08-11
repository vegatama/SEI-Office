package com.suryaenergi.sdm.backendapi.controller;

import com.suryaenergi.sdm.backendapi.entity.Employee;
import com.suryaenergi.sdm.backendapi.repository.EmployeeRepository;
import com.suryaenergi.sdm.backendapi.request.SlipGajiTemplateUpdate;
import com.suryaenergi.sdm.backendapi.response.*;
import com.suryaenergi.sdm.backendapi.service.SlipGajiService;
import io.woo.htmltopdf.HtmlToPdf;
import io.woo.htmltopdf.HtmlToPdfObject;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static com.suryaenergi.sdm.backendapi.pojo.Message.ERROR_MESSAGE;
import static com.suryaenergi.sdm.backendapi.pojo.Message.SUCCESS_MESSAGE;

@RestController
@RequestMapping("slipgaji")
public class SlipGajiController {

    @Autowired
    private SlipGajiService slipGajiService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping(value ="/pdf/{id}/{empCode}")
    public ResponseEntity<byte[]> viewPDF(@PathVariable String empCode, @PathVariable long id) {
        try {
            Employee employee = employeeRepository.findByEmployeeCode(empCode);
            if (employee == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            String content = slipGajiService.getPDFForEmployee(employee, id);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            HtmlToPdf converter = HtmlToPdf.create().object(HtmlToPdfObject.forHtml(content))
                    .compression(false)
                    .documentTitle("Slip Gaji");
            try (InputStream in = converter.convert()) {
                byte[] buffer = new byte[1024];
                int len;
                while ((len = in.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, len);
                }
            }
            String password = slipGajiService.getPDFPassword(employee);
            PDDocument pdd = PDDocument.load(outputStream.toByteArray());
            AccessPermission ap = new AccessPermission();
            StandardProtectionPolicy spp = new StandardProtectionPolicy(password, password, ap);
            spp.setEncryptionKeyLength(128);
            spp.setPermissions(ap);
            pdd.protect(spp);
            outputStream.reset();
            pdd.save(outputStream);
            pdd.close();
            return ResponseEntity.ok(outputStream.toByteArray());
        } catch (Throwable e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage().getBytes(StandardCharsets.UTF_8));
        }
    }

    @GetMapping("send/{id}")
    public ResponseEntity<SlipGajiSendEmailResponse> sendSlipGaji(@PathVariable long id) {
        try {
            SlipGajiSendEmailResponse response = slipGajiService.sendToEmails(id);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (IllegalArgumentException e) {
            SlipGajiSendEmailResponse errorResponse = new SlipGajiSendEmailResponse();
            errorResponse.setMessage(ERROR_MESSAGE + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @GetMapping(value = "download/{id}")
    // download the template file
    public ResponseEntity<byte[]> getSlipGajiTemplate(@PathVariable long id) {
        try {
            byte[] file = slipGajiService.generateTemplateFile(id);
            return ResponseEntity.status(HttpStatus.OK).body(file);
        } catch (Throwable t) {
            t.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(t.toString().getBytes(StandardCharsets.UTF_8));
        }
    }

    @GetMapping("data/{id}")
    public ResponseEntity<SlipGajiDataResponse> getSlipGajiData(@PathVariable long id) {
        try {
            SlipGajiDataResponse response = slipGajiService.getData(id);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (IllegalArgumentException e) {
            SlipGajiDataResponse errorResponse = new SlipGajiDataResponse();
            errorResponse.setMessage(ERROR_MESSAGE + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PostMapping("update")
    public ResponseEntity<MessageResponse> updateSlipGaji(@RequestBody SlipGajiTemplateUpdate body) {
        try {
            slipGajiService.createOrUpdateTemplate(body);
            return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(SUCCESS_MESSAGE));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(ERROR_MESSAGE + e.getMessage()));
        } catch (Throwable t) {
            t.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse(t.toString()));
        }
    }


    @PostMapping("upload/{id}")
    public ResponseEntity<MessageResponse> uploadSlipGaji(@RequestParam("file") MultipartFile file, @PathVariable long id) {
        try {
            slipGajiService.loadData(file.getBytes(), id);
            return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(SUCCESS_MESSAGE));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(ERROR_MESSAGE + e.getMessage()));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Failed to upload file"));
        }
    }

    @GetMapping("detail/{id}")
    public ResponseEntity<SlipGajiDetailResponse> getSlipGaji(@PathVariable long id) {
        try {
            SlipGajiDetailResponse detail = slipGajiService.getDetail(id);
            return ResponseEntity.status(HttpStatus.OK).body(detail);
        } catch (IllegalArgumentException e) {
            SlipGajiDetailResponse errorResponse = new SlipGajiDetailResponse();
            errorResponse.setMessage(ERROR_MESSAGE + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @GetMapping("")
    public ResponseEntity<SlipGajiTemplateListResponse> getSlipGaji() {
        try {
            SlipGajiTemplateListResponse detail = slipGajiService.getTemplateList();
            return ResponseEntity.status(HttpStatus.OK).body(detail);
        } catch (IllegalArgumentException e) {
            SlipGajiTemplateListResponse errorResponse = new SlipGajiTemplateListResponse();
            errorResponse.setMessage(ERROR_MESSAGE + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}
