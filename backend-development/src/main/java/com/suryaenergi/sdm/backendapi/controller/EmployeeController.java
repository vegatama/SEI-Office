package com.suryaenergi.sdm.backendapi.controller;

import com.suryaenergi.sdm.backendapi.entity.Employee;
import com.suryaenergi.sdm.backendapi.request.*;
import com.suryaenergi.sdm.backendapi.response.*;
import com.suryaenergi.sdm.backendapi.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("employee")
public class EmployeeController {
    private static final String SUCCESS_MESSAGE = "SUCCESS";
    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> login(@RequestBody UserLoginRequest req)
    {
        UserLoginResponse response = employeeService.verifyUser(req);
        if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
            return ResponseEntity.ok().body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @PostMapping("login/google")
    public ResponseEntity<UserLoginResponse> loginGoogle(@RequestBody UserLoginGoogleRequest req)
    {
        UserLoginResponse response = employeeService.verifyUserGoogle(req);
        if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
            return ResponseEntity.ok().body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    /*PostMapping
    public ResponseEntity<EmployeeResponse> addEmployee(@RequestBody EmployeeAddRequest req)
    {
        EmployeeResponse response = employeeService.addEmployee(req);

        if(response.getMsg().equalsIgnoreCase("SUCCESS")){
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }*/

    @GetMapping("/seed")
    public ResponseEntity<MessageResponse> deploySeed()
    {
        MessageResponse response = employeeService.deploySeed();
        if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)){
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDetailResponse> getByParam(@PathVariable String id)
    {
        EmployeeDetailResponse response = employeeService.getEmployeeDetail(id);
        if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/getbynavid/{id}")
    public ResponseEntity<EmployeeDetailResponse> getByNavId(@PathVariable String id)
    {
        EmployeeDetailResponse response = employeeService.getEmployeeDetailByNavId(id);
        if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/getbycode/{id}")
    public ResponseEntity<EmployeeDetailResponse> getByEmpCode(@PathVariable String id)
    {
        EmployeeDetailResponse response = employeeService.getEmployeeDetailByCode(id);
        if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/list/{no}/{size}")
    public ResponseEntity<EmployeeListResponse> getListEmployee(@PathVariable int no, @PathVariable int size)
    {
        EmployeeListResponse response = employeeService.getEmployeeList(no,size);
        if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }


    @PostMapping("password/create")
    public ResponseEntity<MessageResponse> createPassword(@RequestBody CreatePasswordRequest req)
    {
        MessageResponse response = employeeService.createPassword(req);
        if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
            return ResponseEntity.ok().body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @PostMapping("password/change")
    public ResponseEntity<MessageResponse> changePassword(@RequestBody ChangePasswordRequest req)
    {
        MessageResponse response = employeeService.changePassword(req);
        if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
            return ResponseEntity.ok().body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @GetMapping("list/thl")
    public ResponseEntity<EmployeeListResponse> thlList()
    {
        EmployeeListResponse response = employeeService.getThlList();
        if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
            return ResponseEntity.ok().body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @GetMapping("list/cuti/{year}/{month}")
    public ResponseEntity<EmployeeCutiListResponse> cutiList(@PathVariable int year, @PathVariable int month)
    {
        EmployeeCutiListResponse response = employeeService.getCutiList(year,month);
        if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
            return ResponseEntity.ok().body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @GetMapping("/cutidetil")
    public ResponseEntity<CutiDetilResponse> getByParamCuti(@RequestBody AbsenSayaRequest req)
    {
        CutiDetilResponse response = employeeService.getCutiDetil(req);
        if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/birthday")
    public ResponseEntity<BirthdayResponse> getBirthdayList()
    {
        BirthdayResponse response = employeeService.getBirthdayList();
        if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/dashboard")
    public ResponseEntity<EmpDashboardResponse> getEmployeeDashboard()
    {
        EmpDashboardResponse response = employeeService.getDashboardData();
        if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/listemp")
    public ResponseEntity<EmployeeListResponse> getEmployeeList(@RequestBody EmployeeListRequest req)
    {
        EmployeeListResponse response = employeeService.getEmployeeListWithStatus(req);
        if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/profile/{empcode}")
    public ResponseEntity<EmployeeDetailResponse> getProfileEmployee(@PathVariable String empcode)
    {
        EmployeeDetailResponse response = employeeService.getEmployeeDetailByCode(empcode);
        if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PutMapping("/update/lokasi")
    public ResponseEntity<UpdateLokasiAbsenResponse> updateLokasiAbsen(@RequestBody UpdateLokasiAbsenRequest req){
        UpdateLokasiAbsenResponse response = employeeService.updateLokasiAbsen(req);
        if(response.getMsg().equalsIgnoreCase(SUCCESS_MESSAGE)) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
