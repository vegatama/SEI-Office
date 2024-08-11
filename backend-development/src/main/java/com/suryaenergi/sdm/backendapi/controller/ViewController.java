package com.suryaenergi.sdm.backendapi.controller;

import com.suryaenergi.sdm.backendapi.email.Alignment;
import com.suryaenergi.sdm.backendapi.email.BorderSide;
import com.suryaenergi.sdm.backendapi.email.EdgeInsets;
import com.suryaenergi.sdm.backendapi.email.SizeUnit;
import com.suryaenergi.sdm.backendapi.email.template.EmailTemplateBuilder;
import com.suryaenergi.sdm.backendapi.entity.Employee;
import com.suryaenergi.sdm.backendapi.pojo.RuangMeetingData;
import com.suryaenergi.sdm.backendapi.pojo.ViewEventData;
import com.suryaenergi.sdm.backendapi.repository.EmployeeRepository;
import com.suryaenergi.sdm.backendapi.request.PesertaRequest;
import com.suryaenergi.sdm.backendapi.response.AbsenSayaResponse;
import com.suryaenergi.sdm.backendapi.response.DaftarHadirResponse;
import com.suryaenergi.sdm.backendapi.response.MessageResponse;
import com.suryaenergi.sdm.backendapi.response.ViewRuangMeetingResponse;
import com.suryaenergi.sdm.backendapi.service.AbsenService;
import com.suryaenergi.sdm.backendapi.service.DaftarhadirService;
import com.suryaenergi.sdm.backendapi.service.RuangMeetingService;
import com.suryaenergi.sdm.backendapi.service.SlipGajiService;
import io.woo.htmltopdf.HtmlToPdf;
import io.woo.htmltopdf.HtmlToPdfObject;
import io.woo.htmltopdf.PdfPageSize;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;

import static com.suryaenergi.sdm.backendapi.pojo.Message.SUCCESS_MESSAGE;

@RestController
@RequestMapping("/view")
public class ViewController {
    @Autowired
    private AbsenService absenService;
    @Autowired
    private DaftarhadirService daftarhadirService;

    @Autowired
    private RuangMeetingService ruangMeetingService;

    @GetMapping("/absen/{viewid}")
    public ResponseEntity<AbsenSayaResponse> getAbsen(@PathVariable String viewid)
    {
        AbsenSayaResponse response = absenService.getAbsenView(viewid);
        if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/hadir/{hid}")
    public ResponseEntity<DaftarHadirResponse> getHadirDetail(@PathVariable String hid){
        DaftarHadirResponse response = daftarhadirService.getDaftarHadir(hid);
        if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping("peserta")
    public ResponseEntity<MessageResponse> addPeserta(@RequestBody PesertaRequest req){
        MessageResponse response = daftarhadirService.addPeserta(req);
        if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("ruangmeeting")
    public ResponseEntity<ViewRuangMeetingResponse> getRuangMeeting(@RequestParam long id){
        RuangMeetingData ruangMeeting = ruangMeetingService.getRuangMeeting(id);
        ViewRuangMeetingResponse response = new ViewRuangMeetingResponse();
        response.setMessage(SUCCESS_MESSAGE);
        response.setId(ruangMeeting.getId());
        response.setName(ruangMeeting.getName());
        response.setCapacity(ruangMeeting.getCapacity());
        response.setDescription(ruangMeeting.getDescription());
        List<ViewEventData> events = daftarhadirService.getActiveEvents(id);
        response.setEvents(events);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
