package com.suryaenergi.sdm.backendapi.controller;

import com.suryaenergi.sdm.backendapi.response.EmployeeListResponse;
import com.suryaenergi.sdm.backendapi.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("public")
public class PublicController {
    private static final String SUCCESS_MESSAGE = "SUCCESS";
    @Autowired
    private EmployeeService employeeService;
    @GetMapping("/listemp")
    public ResponseEntity<EmployeeListResponse> getListEmployee(@RequestHeader("API-KEY") String header)
    {
        if(header.equalsIgnoreCase("LENSEI442531")) {
            EmployeeListResponse response = employeeService.getAllEmployee();
            if (response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
