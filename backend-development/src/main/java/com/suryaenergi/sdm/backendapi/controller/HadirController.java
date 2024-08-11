package com.suryaenergi.sdm.backendapi.controller;

import com.suryaenergi.sdm.backendapi.request.*;
import com.suryaenergi.sdm.backendapi.response.DaftarHadirResponse;
import com.suryaenergi.sdm.backendapi.response.DaftarhadirListResponse;
import com.suryaenergi.sdm.backendapi.response.MessageResponse;
import com.suryaenergi.sdm.backendapi.service.DaftarhadirService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.suryaenergi.sdm.backendapi.pojo.Message.SUCCESS_MESSAGE;

@RestController
@RequestMapping("hadir")
public class HadirController {
    @Autowired
    private DaftarhadirService daftarhadirService;

    @GetMapping("/list/{pid}")
    public ResponseEntity<DaftarhadirListResponse> getListDaftarHadirByPid(@PathVariable String pid){
        DaftarhadirListResponse response = daftarhadirService.getDaftarHadirByPid(pid);
        if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<DaftarhadirListResponse> getListDaftarHadir(){
        DaftarhadirListResponse response = daftarhadirService.getListDaftarHadir();
        if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("{hid}")
    public ResponseEntity<DaftarHadirResponse> getDetailDaftarHadir(@PathVariable String hid){
        DaftarHadirResponse response = daftarhadirService.getDaftarHadir(hid);
        if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping
    public ResponseEntity<DaftarHadirResponse> addDaftarHadir(@RequestBody DaftarHadirRequest req){
        DaftarHadirResponse response = daftarhadirService.addDaftarHadir(req);
        if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PutMapping
    public ResponseEntity<DaftarHadirResponse> updateDaftarHadir(@RequestBody DaftarHadirUpdateRequest req){
        DaftarHadirResponse response = daftarhadirService.editDaftarHadir(req);
        if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PutMapping("risalah")
    public ResponseEntity<DaftarHadirResponse> updateRisalah(@RequestBody RisalahUpdateRequest req){
        DaftarHadirResponse response = daftarhadirService.updateRisalah(req);
        if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PutMapping("finish")
    public ResponseEntity<MessageResponse> finishKegiatan(@RequestBody DaftarHadirFinish req){
        MessageResponse response = daftarhadirService.finishKegiatan(req);
        if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @DeleteMapping("{hid}")
    public ResponseEntity<MessageResponse> deleteDaftarHadir(@PathVariable String hid){
        MessageResponse response = daftarhadirService.deleteDaftarHadir(hid);
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
}
