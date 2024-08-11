package com.suryaenergi.sdm.backendapi.controller;

import com.suryaenergi.sdm.backendapi.request.AbsenSayaRequest;
import com.suryaenergi.sdm.backendapi.request.KirimEmailRequest;
import com.suryaenergi.sdm.backendapi.response.*;
import com.suryaenergi.sdm.backendapi.service.AbsenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("absen")
public class AbsenController {
    private static final String SUCCESS_MESSAGE = "SUCCESS";
    @Autowired
    private AbsenService absenService;

    @GetMapping("/saya")
    public ResponseEntity<AbsenSayaResponse> getByParam(@RequestBody AbsenSayaRequest req)
    {
        AbsenSayaResponse response = absenService.getAbsenSaya(req);
        if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/sayathl")
    public ResponseEntity<AbsenSayathlResponse> getByParamThl(@RequestBody AbsenSayaRequest req)
    {
        AbsenSayathlResponse response = absenService.getAbsenSayaThl(req);
        if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/dashboard/{emp}")
    public ResponseEntity<AbsenDashboardResponse> getAbsenDashboard(@PathVariable String emp){
        AbsenDashboardResponse response = absenService.getAbsenDashboard(emp);
        if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/rekap/{year}/{month}")
    public ResponseEntity<RekapAbsenResponse> getRekapAbsen(@PathVariable int year, @PathVariable int month)
    {
        RekapAbsenResponse response = absenService.getRekapAbsen(year,month);
        if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/calculate/{year}/{month}/{emp}")
    public ResponseEntity<MessageResponse> hitungAbsen(@PathVariable int year, @PathVariable int month, @PathVariable String emp)
    {
        MessageResponse response = absenService.hitungAbsen(year,month,emp);
        if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/rekapemp/{year}/{month}/{day}/{emp}")
    public ResponseEntity<MessageResponse> rekapAbsenEmp(@PathVariable int year, @PathVariable int month, @PathVariable int day, @PathVariable String emp)
    {
        MessageResponse response = absenService.rekapAbsenEmp(year,month,day,emp);
        if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping("/kirimemail")
    public ResponseEntity<MessageResponse> tambahAntrianEmail(@RequestBody KirimEmailRequest req)
    {
        MessageResponse response = absenService.kirimEmail(req);
        if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/emp/{year}/{month}/{day}")
    public ResponseEntity<AbsenDayResponse> absenEmp(@PathVariable int year, @PathVariable int month, @PathVariable int day)
    {
        AbsenDayResponse response = absenService.absenEmpDay(year,month,day);
        if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/empdash/{year}/{month}/{day}")
    public ResponseEntity<AbsenDayDashResponse> absenEmpDash(@PathVariable int year, @PathVariable int month, @PathVariable int day)
    {
        AbsenDayDashResponse response = absenService.absenEmpDashDay(year,month,day);
        if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/mentah/{year}/{month}/{date}/{dept}")
    public ResponseEntity<AbsenMentahResponse> absenMentah(@PathVariable int year, @PathVariable int month, @PathVariable int date, @PathVariable String dept)
    {
        AbsenMentahResponse response = absenService.absenMentah(year,month,date,dept);
        if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
