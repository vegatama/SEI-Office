package com.suryaenergi.sdm.backendapi.controller;

import com.suryaenergi.sdm.backendapi.pojo.JatahCutiData;
import com.suryaenergi.sdm.backendapi.request.IzinCutiRequest;
import com.suryaenergi.sdm.backendapi.response.*;
import com.suryaenergi.sdm.backendapi.service.IzinCutiService;
import com.suryaenergi.sdm.backendapi.service.JatahCutiService;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("cuti")
public class CutiController {

    @Autowired
    private IzinCutiService izinCutiService;

    @Autowired
    private JatahCutiService jatahCutiService;

    @GetMapping("/jatah/{empcode}/{tahun}")
    public JatahCutiResponse getJatahCuti(@PathVariable String empcode, @PathVariable int tahun) {
        try {
            JatahCutiData jatahCutiByEmployeeAndTahun = jatahCutiService.getJatahCutiByEmployeeAndTahun(empcode, tahun);
            JatahCutiResponse response = new JatahCutiResponse();
            response.setId(jatahCutiByEmployeeAndTahun.getId());
            response.setEmployeeId(jatahCutiByEmployeeAndTahun.getEmployeeId());
            response.setEmployeeName(jatahCutiByEmployeeAndTahun.getEmployeeName());
            response.setTahun(jatahCutiByEmployeeAndTahun.getTahun());
            response.setJumlahHari(jatahCutiByEmployeeAndTahun.getJumlahCuti());
            response.setMessage("SUCCESS");
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            JatahCutiResponse response = new JatahCutiResponse();
            response.setMessage(e.toString());
            return response;
        }
    }

    @PostMapping("/jatah")
    public MessageResponse addJatahCutiBalance(@RequestParam String empcode, @RequestParam int tahun, @RequestParam int jumlahHari) {
        try {
            jatahCutiService.addJatahCuti(empcode, tahun, jumlahHari, JatahCutiService.KETERANGAN_INPUT_DATA_JATAH_CUTI, null);
            return new MessageResponse("SUCCESS");
        } catch (RuntimeException e) {
            return new MessageResponse(e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
            return new MessageResponse(t.toString());
        }
    }

    @GetMapping("/jatah")
    public JatahCutiListResponse getJatahCutiList(@RequestParam String empcode, @RequestParam int year, @RequestParam(required = false) Long after) {
        try {
            return new JatahCutiListResponse("SUCCESS", jatahCutiService.getJatahCutiList(empcode, year, after));
        } catch (Exception e) {
            e.printStackTrace();
            return new JatahCutiListResponse(e.toString(), Collections.emptyList());
        }
    }

    @GetMapping("/jatah/group")
    public JatahCutiListResponse getJatahCutiList() {
        try {
            return new JatahCutiListResponse("SUCCESS", jatahCutiService.groupJatahCutiByEmployeeIdAndTahun());
        } catch (Exception e) {
            e.printStackTrace();
            return new JatahCutiListResponse(e.toString(), Collections.emptyList());
        }
    }

    @GetMapping("/jatah/group/all/{tahun}")
    public JatahCutiListResponse getJatahCutiList(@PathVariable int tahun) {
        try {
            return new JatahCutiListResponse("SUCCESS", jatahCutiService.groupJatahCutiByEmployeeIdAndTahun(tahun));
        } catch (Exception e) {
            e.printStackTrace();
            return new JatahCutiListResponse(e.toString(), Collections.emptyList());
        }
    }

    @GetMapping("/jatah/group/{empcode}/{tahun}")
    public JatahCutiResponse getJatahCutiList(@PathVariable String empcode, @PathVariable int tahun) {
        try {
            JatahCutiData jatahCutiByEmployeeAndTahun = jatahCutiService.getJatahCutiByEmployeeAndTahun(empcode, tahun);
            JatahCutiResponse response = new JatahCutiResponse();
            response.setId(jatahCutiByEmployeeAndTahun.getId());
            response.setEmployeeId(jatahCutiByEmployeeAndTahun.getEmployeeId());
            response.setEmployeeName(jatahCutiByEmployeeAndTahun.getEmployeeName());
            response.setTahun(jatahCutiByEmployeeAndTahun.getTahun());
            response.setJumlahHari(jatahCutiByEmployeeAndTahun.getJumlahCuti());
            response.setMessage("SUCCESS");
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            JatahCutiResponse response = new JatahCutiResponse();
            response.setMessage(e.toString());
            return response;
        }
    }

    @GetMapping("/jatah/group/{empcode}")
    public JatahCutiListResponse getJatahCutiList(@PathVariable String empcode) {
        try {
            return new JatahCutiListResponse("SUCCESS", jatahCutiService.getJatahCutiByEmployee(empcode));
        } catch (Exception e) {
            e.printStackTrace();
            return new JatahCutiListResponse(e.toString(), Collections.emptyList());
        }
    }

    @PutMapping("/jatah")
    public MessageResponse setJatahCutiBalance(@RequestParam String empcode, @RequestParam int tahun, @RequestParam int jumlahHari) {
        try {
            jatahCutiService.setJatahCuti(empcode, tahun, jumlahHari);
            return new MessageResponse("SUCCESS");
        } catch (RuntimeException e) {
            return new MessageResponse(e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
            return new MessageResponse(t.toString());
        }
    }

    @GetMapping("/list")
    public IzinCutiListResponse getIzinCutiList(@RequestParam String empcode, @RequestParam(required = false) Long after, @RequestParam(required = false) List<IzinCutiDetailResponse.Status> status, @RequestParam(required = false) LocalDateTime start, @RequestParam(required = false) LocalDateTime end) {
        try {
            return new IzinCutiListResponse("SUCCESS", izinCutiService.getAllIzinCuti(empcode, after, status, start, end));
        } catch (Exception e) {
            e.printStackTrace();
            return new IzinCutiListResponse(e.toString(), Collections.emptyList());
        }
    }

    @GetMapping("/requests")
    public IzinCutiRequestListResponse getReviewedIzinCutiList(@RequestParam String empcode, @RequestParam(required = false) Long after, @RequestParam(required = false) List<IzinCutiDetailResponse.Status> status, @RequestParam(required = false) LocalDateTime start, @RequestParam(required = false) LocalDateTime end) {
        try {
            return new IzinCutiRequestListResponse("SUCCESS", izinCutiService.getReviewedIzinCuti(empcode, after, status, start, end));
        } catch (Exception e) {
            e.printStackTrace();
            return new IzinCutiRequestListResponse(e.toString(), Collections.emptyList());
        }
    }

    @GetMapping("/detail")
    public IzinCutiDetailResponse getIzinCutiDetail(@RequestParam long izinCutiId) {
        try {
            return izinCutiService.getIzinCuti(izinCutiId);
        } catch (Exception e) {
            IzinCutiDetailResponse response = new IzinCutiDetailResponse();
            response.setMessage(e.toString());
            e.printStackTrace();
            return response;
        }
    }

    @GetMapping("/approve")
    public MessageResponse approveCuti(@RequestParam long izinCutiId, @RequestParam String reviewerEmpCode) {
        try {
            izinCutiService.approveIzinCuti(izinCutiId, reviewerEmpCode);
            return new MessageResponse("SUCCESS");
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new MessageResponse(e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
            return new MessageResponse(t.toString());
        }
    }

    @GetMapping("/reject")
    public MessageResponse rejectCuti(@RequestParam long izinCutiId, @RequestParam String reviewerEmpCode, @RequestParam(required = false) String reason) {
        try {
            izinCutiService.rejectIzinCuti(izinCutiId, reviewerEmpCode, reason);
            return new MessageResponse("SUCCESS");
        } catch (RuntimeException e) {
            return new MessageResponse(e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
            return new MessageResponse(t.toString());
        }
    }

    @GetMapping("/cancel")
    public MessageResponse cancelCuti(@RequestParam long izinCutiId) {
        try {
            izinCutiService.cancelIzinCuti(izinCutiId);
            return new MessageResponse("SUCCESS");
        } catch (RuntimeException e) {
            return new MessageResponse(e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
            return new MessageResponse(t.toString());
        }
    }

    @PostMapping(value = "/request", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public MessageResponse requestCuti(@RequestParam String empcode,
                                       @RequestParam long jenis,
                                       @RequestParam String start,
                                       @RequestParam String end,
                                       @RequestParam String reason,
                                       @RequestParam(required = false) MultipartFile[] documents) {
        if (documents == null) {
            documents = new MultipartFile[0];
        }
        try {
            LocalDateTime startDate = LocalDateTime.parse(start);
            LocalDateTime endDate = LocalDateTime.parse(end);
            IzinCutiRequest request = new IzinCutiRequest(empcode, jenis, startDate, endDate, reason);
            izinCutiService.requestIzinCuti(request, documents);
            return new MessageResponse("SUCCESS");
        } catch (RuntimeException e) {
            return new MessageResponse(e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
            return new MessageResponse(t.toString());
        }
    }

    @GetMapping("/dashboard")
    public IzinCutiDashboardResponse getIzinCutiDashboard(@RequestParam String empcode) {
        try {
            return izinCutiService.getIzinCutiDashboard(empcode);
        } catch (RuntimeException e) {
            return new IzinCutiDashboardResponse(e.getMessage(), 0, 0, null);
        } catch (Exception e) {
            IzinCutiDashboardResponse response = new IzinCutiDashboardResponse();
            response.setMessage(e.toString());
            e.printStackTrace();
            return response;
        }
    }
}
