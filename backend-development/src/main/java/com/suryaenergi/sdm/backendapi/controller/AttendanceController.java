package com.suryaenergi.sdm.backendapi.controller;

import com.suryaenergi.sdm.backendapi.pojo.AttendanceData;
import com.suryaenergi.sdm.backendapi.pojo.EmployeeCheckInApproval;
import com.suryaenergi.sdm.backendapi.request.PendingSwipeRequest;
import com.suryaenergi.sdm.backendapi.request.SwipeRequest;
import com.suryaenergi.sdm.backendapi.response.*;
import com.suryaenergi.sdm.backendapi.service.AbsenService;
import com.suryaenergi.sdm.backendapi.service.AttendanceImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("mobileapp/attendance")
public class AttendanceController {
    @Autowired
    private AbsenService absenService;

    @Autowired
    private AttendanceImageService attendanceImageService;

    @PostMapping("/swipe")
    public ResponseEntity<MessageResponse> swipe(@RequestBody SwipeRequest request) {
        try {
            absenService.swipe(request.getEmpcode(), request.getLatitude(), request.getLongitude());
            return ResponseEntity.ok(new MessageResponse("SUCCESS"));
        } catch (RuntimeException e) {
            // bad request
            return ResponseEntity.ok(new MessageResponse(e.getMessage()));
        } catch (Throwable t) {
            // server error
            t.printStackTrace();
            return ResponseEntity.status(500).body(new MessageResponse(t.getMessage()));
        }
    }

    @GetMapping("/history") // /history?status=a,b,c&employee_code=abc&from=dd-mm-yyyy&to=dd-mm-yyyy
    public ResponseEntity<AttendanceHistoryResponse> getHistory(@RequestParam String employee_code, @RequestParam(required = false) String from, @RequestParam(required = false) String to) {
        LocalDate fromDate = from == null ? null : LocalDate.parse(from);
        LocalDate toDate = to == null ? null : LocalDate.parse(to);
        try {
            List<AttendanceData> history = absenService.getHistory(employee_code, fromDate, toDate);
            return ResponseEntity.ok(new AttendanceHistoryResponse("SUCCESS", history));
        } catch (RuntimeException e) {
            // bad request
            return ResponseEntity.ok(new AttendanceHistoryResponse(e.getMessage(), null));
        } catch (Throwable t) {
            // server error
            t.printStackTrace();
            return ResponseEntity.status(500).body(new AttendanceHistoryResponse(t.getMessage(), null));
        }
    }

    @GetMapping("/historybymonth")
    public ResponseEntity<AttendanceHistoryResponse> getHistoryByMonth(@RequestParam String employee_code, @RequestParam int month, @RequestParam int year) {
        try {
            LocalDate fromDate = LocalDate.of(year, month, 1);
            LocalDate toDate = fromDate.plusMonths(1).minusDays(1);
            // if this month is todays month, limit to current date
            if (fromDate.getMonthValue() == LocalDate.now().getMonthValue()) {
                toDate = LocalDate.now();
            }
            List<AttendanceData> history = absenService.getHistory(employee_code, fromDate, toDate);
            return ResponseEntity.ok(new AttendanceHistoryResponse("SUCCESS", history));
        } catch (RuntimeException e) {
            // bad request
            return ResponseEntity.ok(new AttendanceHistoryResponse(e.getMessage(), null));
        } catch (Throwable t) {
            // server error
            t.printStackTrace();
            return ResponseEntity.status(500).body(new AttendanceHistoryResponse(t.getMessage(), null));
        }
    }


    @PostMapping(value =  "/requestswipe", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MessageResponse> requestSwipe(@RequestParam String empcode, @RequestParam double latitude, @RequestParam double longitude, @RequestParam String reason, @RequestParam MultipartFile image) {
        try {
            PendingSwipeRequest request = new PendingSwipeRequest(empcode, latitude, longitude, reason, image);
            String s = attendanceImageService.saveImage(request.getEmpcode(), request.getImage());
            absenService.requestSwipe(request.getEmpcode(), request.getLatitude(), request.getLongitude(), request.getReason(), s);
            return ResponseEntity.ok(new MessageResponse("SUCCESS"));
        } catch (RuntimeException e) {
            // bad request
            return ResponseEntity.ok(new MessageResponse(e.getMessage()));
        } catch (Throwable t) {
            // server error
            t.printStackTrace();
            return ResponseEntity.status(500).body(new MessageResponse(t.getMessage()));
        }
    }

    @GetMapping("/confirm")
    public ResponseEntity<MessageResponse> confirm(@RequestParam long id, @RequestParam boolean approve, @RequestParam String empCode, @RequestParam(required = false) String reason) {
        if (!approve && (reason == null || reason.isEmpty())) {
//            return ResponseEntity.ok(new MessageResponse("REASON_REQUIRED"));
            // TEMPORARY: FALLBACK TO DEFAULT REASON
            reason = "-";
            // THIS WAS DUE TO UNFINISHED IMPLEMENTATION ON THE FRONTEND
        }
        absenService.confirmRequest(id, approve, empCode, reason);
        return ResponseEntity.ok(new MessageResponse("SUCCESS"));
    }

    @GetMapping("/today")
    public ResponseEntity<AttendanceDataResponse> getToday(@RequestParam String employee_code) {
        try {
            return ResponseEntity.ok(absenService.getAttendanceData(employee_code, LocalDate.now()));
        } catch (RuntimeException e) {
            // bad request
            AttendanceDataResponse response = new AttendanceDataResponse();
            response.setMessage(e.getMessage());
            return ResponseEntity.ok(response);
        } catch (Throwable t) {
            // server error
            t.printStackTrace();
            AttendanceDataResponse response = new AttendanceDataResponse();
            response.setMessage(t.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/at")
    public ResponseEntity<AttendanceDataResponse> getMonth(@RequestParam String employee_code, @RequestParam int month, @RequestParam int year, @RequestParam int day) {
        try {
            return ResponseEntity.ok(absenService.getAttendanceData(employee_code, LocalDate.of(year, month, day)));
        } catch (RuntimeException e) {
            // bad request
            AttendanceDataResponse response = new AttendanceDataResponse();
            response.setMessage(e.getMessage());
            return ResponseEntity.ok(response);
        } catch (Throwable t) {
            // server error
            t.printStackTrace();
            AttendanceDataResponse response = new AttendanceDataResponse();
            response.setMessage(t.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/requestdetail")
    public ResponseEntity<EmployeeCheckInApprovalResponse> getRequest(@RequestParam long id) {
        try {
            EmployeeCheckInApproval approval = absenService.getRequest(id);
            if (approval == null) {
                return ResponseEntity.ok(new EmployeeCheckInApprovalResponse("NOT FOUND", null));
            }
            return ResponseEntity.ok(new EmployeeCheckInApprovalResponse("SUCCESS", approval));
        } catch (RuntimeException e) {
            // bad request
            return ResponseEntity.ok(new EmployeeCheckInApprovalResponse(e.getMessage(), null));
        } catch (Throwable t) {
            // server error
            t.printStackTrace();
            return ResponseEntity.status(500).body(new EmployeeCheckInApprovalResponse(t.getMessage(), null));
        }
    }

    @GetMapping("/requests")
    public ResponseEntity<AttendanceRequestsResponse> getRequests(
            @RequestParam String employee_code,
            @RequestParam(required = false) Long from,
            @RequestParam(required = false, defaultValue = "false") boolean showAll) {
        // employee_code as the atasan, finds all employees under him/her
        // and check if they have any attendance requests
        try {
            List<EmployeeCheckInApproval> requests = absenService.getRequests(employee_code, from, showAll);
            return ResponseEntity.ok(new AttendanceRequestsResponse("SUCCESS", requests));
        } catch (RuntimeException e) {
            // bad request
            return ResponseEntity.ok(new AttendanceRequestsResponse(e.getMessage(), null));
        } catch (Throwable t) {
            // server error
            t.printStackTrace();
            return ResponseEntity.status(500).body(new AttendanceRequestsResponse(t.getMessage(), null));
        }
    }

    @GetMapping("/needapproval")
    public ResponseEntity<AttendanceRequestsResponse> getRequests(
            @RequestParam String employee_code,
            @RequestParam(required = false) Integer day,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year) {
        try {
            List<EmployeeCheckInApproval> requests = absenService.getRequests(employee_code, day, month, year);
            return ResponseEntity.ok(new AttendanceRequestsResponse("SUCCESS", requests));
        } catch (RuntimeException e) {
            // bad request
            return ResponseEntity.ok(new AttendanceRequestsResponse(e.getMessage(), null));
        } catch (Throwable t) {
            // server error
            t.printStackTrace();
            return ResponseEntity.status(500).body(new AttendanceRequestsResponse(t.getMessage(), null));
        }
    }

    @GetMapping("/myrequests")
    public ResponseEntity<AttendanceRequestsResponse> getRequests(@RequestParam String employee_code, @RequestParam(required = false) Long from) {
        // employee_code as the atasan, finds all employees under him/her
        // and check if they have any attendance requests
        try {
            List<EmployeeCheckInApproval> requests = absenService.getMyRequests(employee_code, from);
            return ResponseEntity.ok(new AttendanceRequestsResponse("SUCCESS", requests));
        } catch (RuntimeException e) {
            // bad request
            return ResponseEntity.ok(new AttendanceRequestsResponse(e.getMessage(), null));
        } catch (Throwable t) {
            // server error
            t.printStackTrace();
            return ResponseEntity.status(500).body(new AttendanceRequestsResponse(t.getMessage(), null));
        }
    }
}