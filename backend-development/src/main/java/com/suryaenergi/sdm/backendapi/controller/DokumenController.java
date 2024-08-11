package com.suryaenergi.sdm.backendapi.controller;

import com.suryaenergi.sdm.backendapi.request.DokumenAddRequest;
import com.suryaenergi.sdm.backendapi.response.DokumenDetailResponse;
import com.suryaenergi.sdm.backendapi.service.DokumenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.suryaenergi.sdm.backendapi.pojo.Message.SUCCESS_MESSAGE;

@RestController
@RequestMapping("dokumen")
public class DokumenController {
    @Autowired
    private DokumenService dokumenService;

    @GetMapping("{dokid}")
    public ResponseEntity<DokumenDetailResponse> getDokumenDetail(@PathVariable String dokid){
        DokumenDetailResponse response = dokumenService.getDokumenDetail(dokid);
        if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    // @PostMapping
    // public ResponseEntity<DokumenDetailResponse> addDokumen(@RequestBody DokumenAddRequest req){
    //     DokumenDetailResponse response = dokumenService.addDokumen(req);
    //     if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
    //         return ResponseEntity.status(HttpStatus.OK).body(response);
    //     }
    //     else{
    //         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    //     }
    //    }
}
