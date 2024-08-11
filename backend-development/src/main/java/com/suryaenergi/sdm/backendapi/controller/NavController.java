package com.suryaenergi.sdm.backendapi.controller;

import com.suryaenergi.sdm.backendapi.request.NavHelloRequest;
import com.suryaenergi.sdm.backendapi.response.MessageResponse;
import com.suryaenergi.sdm.backendapi.service.NotifikasiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.suryaenergi.sdm.backendapi.pojo.Message.SUCCESS_MESSAGE;

@RestController
@RequestMapping("navapi")
public class NavController {
    @Autowired
    private NotifikasiService notifikasiService;

    @GetMapping("/hello/{user}/{dokumen}")
    public ResponseEntity<MessageResponse> helloWorld(@RequestHeader("API-KEY") String header, @PathVariable String user, @PathVariable String dokumen){
        MessageResponse res = new MessageResponse();
        if(header.equalsIgnoreCase("SEI531ONETRILIUN")){
            res.setMsg("OK:"+user+"-"+dokumen);
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }
        else{
            res.setMsg("NOT FOUND");
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }
    }

    @GetMapping("/um/{user}/{dokumen}")
    public ResponseEntity<MessageResponse> notifUM(@RequestHeader("API-KEY") String header, @PathVariable String user, @PathVariable String dokumen){
        MessageResponse res = new MessageResponse();
        if(header.equalsIgnoreCase("SEI531ONETRILIUN")){
            MessageResponse response = notifikasiService.simpanNotifUM(user,dokumen);
            if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
            else{
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
        }
        else{
            res.setMsg("FORBIDDEN");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(res);
        }
    }

    @GetMapping("/sppd/{user}/{dokumen}")
    public ResponseEntity<MessageResponse> notifSPPD(@RequestHeader("API-KEY") String header, @PathVariable String user, @PathVariable String dokumen){
        MessageResponse res = new MessageResponse();
        if(header.equalsIgnoreCase("SEI531ONETRILIUN")){
            MessageResponse response = notifikasiService.simpanNotifSPPD(user,dokumen);
            if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
            else{
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
        }
        else{
            res.setMsg("FORBIDDEN");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(res);
        }
    }

    @GetMapping("/dpbj/{user}/{dokumen}")
    public ResponseEntity<MessageResponse> notifDPB(@RequestHeader("API-KEY") String header, @PathVariable String user, @PathVariable String dokumen){
        MessageResponse res = new MessageResponse();
        if(header.equalsIgnoreCase("SEI531ONETRILIUN")){
            MessageResponse response = notifikasiService.simpanNotifDPB(user,dokumen);
            if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
            else{
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
        }
        else{
            res.setMsg("FORBIDDEN");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(res);
        }
    }
}
