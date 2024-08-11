package com.suryaenergi.sdm.backendapi.controller;

import com.suryaenergi.sdm.backendapi.request.ProyekListRequest;
import com.suryaenergi.sdm.backendapi.response.DpbRealisasiDetailResponse;
import com.suryaenergi.sdm.backendapi.response.PoListResponse;
import com.suryaenergi.sdm.backendapi.response.ProyekDetailResponse;
import com.suryaenergi.sdm.backendapi.response.ProyekListResponse;
import com.suryaenergi.sdm.backendapi.service.ProyekService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("proyek")
@RestController
public class ProyekController {
    private static final String SUCCESS_MESSAGE = "SUCCESS";
    @Autowired
    private ProyekService proyekService;

    @GetMapping("list/{no}/{size}")
    public ResponseEntity<ProyekListResponse> getListProyek(@PathVariable int no, @PathVariable int size){
        ProyekListResponse response = proyekService.getAllProyek(no,size);
        if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("list2/{tipe}/{tahun}")
    public ResponseEntity<ProyekListResponse> getListProyek2(@PathVariable String tipe, @PathVariable int tahun){
        ProyekListResponse response = proyekService.getAllProyekWithTipeTahun(tipe,tahun);
        if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("all")
    public ResponseEntity<ProyekListResponse> getAllProyek(@RequestBody ProyekListRequest req){
        ProyekListResponse response = proyekService.getAllProyekNoPage(req);
        if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/po/all")
    public ResponseEntity<PoListResponse> getAllPo(){
        PoListResponse response = proyekService.getAllPo();
        if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/detail/{kdproyek}")
    public ResponseEntity<ProyekDetailResponse> getDetailProyek(@PathVariable String kdproyek){
        ProyekDetailResponse response = proyekService.getDetailProyek(kdproyek);
        if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/dpb/detail/{nodpb}")
    public ResponseEntity<DpbRealisasiDetailResponse> getDetailDpbRealisasi(@PathVariable String nodpb){
        DpbRealisasiDetailResponse response = proyekService.getDetailDpb(nodpb);
        if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
