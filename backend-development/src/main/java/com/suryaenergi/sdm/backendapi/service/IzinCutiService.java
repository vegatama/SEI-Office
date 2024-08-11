package com.suryaenergi.sdm.backendapi.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import com.suryaenergi.sdm.backendapi.email.template.EmailTemplateBuilder;
import com.suryaenergi.sdm.backendapi.entity.*;
import com.suryaenergi.sdm.backendapi.pojo.*;
import com.suryaenergi.sdm.backendapi.notification.IzinCutiResponseAccepted;
import com.suryaenergi.sdm.backendapi.notification.IzinCutiResponseRejected;
import com.suryaenergi.sdm.backendapi.notification.NotificationIzinCutiRequest;
import com.suryaenergi.sdm.backendapi.repository.*;
import com.suryaenergi.sdm.backendapi.request.IzinCutiRequest;
import com.suryaenergi.sdm.backendapi.response.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suryaenergi.sdm.backendapi.request.JenisIzinCutiRequest;
import org.springframework.web.multipart.MultipartFile;

@Service
public class IzinCutiService {
    private static final LocalDateTime DEFAULT_START_TIME = LocalDateTime.of(1970, 1, 1, 0, 0, 0);
    
    @Autowired
    private JenisIzinCutiRepository jenisIzinCutiRepository;

    @Autowired
    private IzinCutiReviewerRepository izinCutiReviewerRepository;

    @Autowired
    private IzinCutiRepository izinCutiRepository;

    @Autowired
    private IzinCutiApprovalRepository izinCutiApprovalRepository;

    @Autowired
    private IzinCutiFileRepository izinCutiFileRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private IzinCutiFileService izinCutiFileService;

    @Autowired
    private JatahCutiService jatahCutiService;

    @Autowired
    private KetidakhadiranRepository ketidakhadiranRepository;

    @Autowired
    private EmailService emailService;
    @Autowired
    private FrontEndURLService frontEndURLService;

    public MessageResponse addJenisIzinCuti(JenisIzinCutiRequest request){
        MessageResponse res = new MessageResponse();
        try {
            JenisIzinCuti jenisIzinCuti = new JenisIzinCuti();
            jenisIzinCuti.setIzinCuti(request.getIzinCuti());
            jenisIzinCuti.setCutCuti(request.getCutCuti());
//            tipeIzinCuti.setApproval(request.getApproval());
//            jenisIzinCuti.setPengajuan(request.getPengajuan());

            jenisIzinCuti.setPengajuan(JenisIzinCutiData.TipePengajuan.valueOf(request.getPengajuan().toUpperCase()));
            jenisIzinCutiRepository.save(jenisIzinCuti);
            saveJenisIzinCutiReviewers(request, jenisIzinCuti);
            res.setMsg("SUCCESS");
        } catch (Exception ex) {
            res.setMsg("ERROR: "+ex.getMessage());
        }
        return res;
    }


    public IzinCutiResponse getJenisIzinCutiById(Long id){
        IzinCutiResponse res = new IzinCutiResponse();
        try {
            JenisIzinCuti jenisIzinCuti = jenisIzinCutiRepository.findIzinCutiById(id);
            if (jenisIzinCuti == null) {
                throw new Exception("NOT FOUND");
            } else {
                res.setMsg("SUCCESS");
                res.setId(jenisIzinCuti.getId());
                res.setIzinCuti(jenisIzinCuti.getIzinCuti());
                res.setCutCuti(jenisIzinCuti.getCutCuti());
                List<String> reviewerEmpCodes = new ArrayList<>();
                List<IzinCutiReviewer> izinCutiReviewers = izinCutiReviewerRepository.findByTipeIzinCutiId(jenisIzinCuti.getId());
                for (IzinCutiReviewer izinCutiReviewer : izinCutiReviewers) {
                    reviewerEmpCodes.add(izinCutiReviewer.getEmpCode());
                }
                res.setReviewers(reviewerEmpCodes);
//                res.setPengajuan(jenisIzinCuti.getPengajuan());
                res.setPengajuan(jenisIzinCuti.getPengajuan().name());
            }
        } catch (Exception ex) {
            res.setMsg("ERROR: "+ex.getMessage());
            ex.printStackTrace();
        }
        return res;
    }

    public MessageResponse updateJenisIzinCuti(JenisIzinCutiRequest request){
        MessageResponse res = new MessageResponse();
        try {

            JenisIzinCuti jenisIzinCuti = jenisIzinCutiRepository.findIzinCutiById(request.getId());
            if (jenisIzinCuti == null) {
                throw new Exception("NOT FOUND");  
            }
            if (request.getIzinCuti() != null) {
                jenisIzinCuti.setIzinCuti(request.getIzinCuti());
            }
            if (request.getCutCuti() != null) {
                jenisIzinCuti.setCutCuti(request.getCutCuti());
            }
//            tipeIzinCuti.setApproval(request.getApproval());
//            jenisIzinCuti.setPengajuan(request.getPengajuan());
//            List<IzinCutiReviewer> izinCutiReviewers = izinCutiReviewerRepository.findByTipeIzinCutiId(jenisIzinCuti.getId());
//            // delete all reviewers then add the new ones
//            izinCutiReviewerRepository.deleteAll(izinCutiReviewers);
//            saveJenisIzinCutiReviewers(request, jenisIzinCuti);
            if (request.getReviewer() != null) {
                // make sure theres no duplicate

                List<IzinCutiReviewer> izinCutiReviewers = izinCutiReviewerRepository.findByTipeIzinCutiId(jenisIzinCuti.getId());
                // delete all reviewers then add the new ones
                izinCutiReviewerRepository.deleteAll(izinCutiReviewers);
                saveJenisIzinCutiReviewers(request, jenisIzinCuti);
            }
            if (request.getPengajuan() != null) {
                jenisIzinCuti.setPengajuan(JenisIzinCutiData.TipePengajuan.valueOf(request.getPengajuan().toUpperCase()));
            }
            jenisIzinCutiRepository.save(jenisIzinCuti);
            res.setMsg("SUCCESS");
        } catch (Exception ex) {
            res.setMsg("ERROR: "+ex.getMessage());
            ex.printStackTrace();
        }
        return res;
    }

    private void saveJenisIzinCutiReviewers(JenisIzinCutiRequest request, JenisIzinCuti jenisIzinCuti) {
        // make sure theres no dupe
        Set<String> empCodes = new HashSet<>();
        Set<Integer> layerIndexes = new HashSet<>();
        for (JenisIzinCutiReviewer reviewer : request.getReviewer()) {
            if (reviewer.getLayerIndex() != null && reviewer.getEmpCode() != null) {
                throw new IllegalArgumentException("INVALID_REVIEWER");
            }
            if (reviewer.getLayerIndex() == null && reviewer.getEmpCode() == null) {
                throw new IllegalArgumentException("INVALID_REVIEWER");
            }
            if (reviewer.getLayerIndex() != null) {
                if (layerIndexes.contains(reviewer.getLayerIndex())) {
                    throw new IllegalArgumentException("DUPLICATE_LAYER_INDEX");
                }
                layerIndexes.add(reviewer.getLayerIndex());
            }
            if (reviewer.getEmpCode() != null) {
                if (empCodes.contains(reviewer.getEmpCode())) {
                    throw new IllegalArgumentException("DUPLICATE_EMP_CODE");
                }
                empCodes.add(reviewer.getEmpCode());
            }
            IzinCutiReviewer izinCutiReviewer = new IzinCutiReviewer();
            izinCutiReviewer.setTipeIzinCutiId(jenisIzinCuti.getId());
            izinCutiReviewer.setEmpCode(reviewer.getEmpCode());
            izinCutiReviewer.setLayerIndex(reviewer.getLayerIndex());
            izinCutiReviewer.setTipeIzinCutiId(jenisIzinCuti.getId());
            izinCutiReviewerRepository.save(izinCutiReviewer);
        }
    }

    public MessageResponse deleteJenisIzinCuti(Long id){
        MessageResponse res = new MessageResponse();
        try {
            JenisIzinCuti jenisIzinCuti = jenisIzinCutiRepository.findIzinCutiById(id);
            if (jenisIzinCuti == null) {
                throw new Exception("NOT FOUND");
            }
            jenisIzinCutiRepository.delete(jenisIzinCuti);
            List<IzinCutiReviewer> izinCutiReviewers = izinCutiReviewerRepository.findByTipeIzinCutiId(id);
            izinCutiReviewerRepository.deleteAll(izinCutiReviewers);
            res.setMsg("SUCCESS");
        } catch (Exception ex) {
            res.setMsg("ERROR: "+ex.getMessage());
        }
        return res;
    }

    public JenisIzinCutiListResponse getAllJenisIzinCuti(String empCode){
        JenisIzinCutiListResponse res = new JenisIzinCutiListResponse();
        List<JenisIzinCutiData> izinCutiList = new ArrayList<>();
        Employee currentEmployee = employeeRepository.findByEmployeeCode(empCode);
        if (currentEmployee == null) {
            res.setMsg("EMPLOYEE_NOT_FOUND");
            return res;
        }
        try {
            Map<String, Employee> employeeCacheByCode = new HashMap<>();
            Map<String, Employee> employeeCacheById = new HashMap<>();
            List<JenisIzinCuti> listIzinCuti = (List<JenisIzinCuti>) jenisIzinCutiRepository.findAll();
            if (!listIzinCuti.isEmpty()) {
                for(JenisIzinCuti jenisIzinCuti :listIzinCuti){
                    JenisIzinCutiData jenisIzinCutiData = new JenisIzinCutiData();
                    jenisIzinCutiData.setId(jenisIzinCuti.getId());
                    jenisIzinCutiData.setNamaJenis(jenisIzinCuti.getIzinCuti());
                    jenisIzinCutiData.setCutCuti(jenisIzinCuti.getCutCuti());
                    List<JenisIzinCutiAbsoluteReviewer> reviewerEmpCodes = new ArrayList<>();
                    List<IzinCutiReviewer> izinCutiReviewers = izinCutiReviewerRepository.findByTipeIzinCutiId(jenisIzinCuti.getId());
                    for (IzinCutiReviewer izinCutiReviewer : izinCutiReviewers) {
                        Integer layerIndex = izinCutiReviewer.getLayerIndex();
                        if (layerIndex != null) {
                            JenisIzinCutiAbsoluteReviewer reviewer = new JenisIzinCutiAbsoluteReviewer();
                            Employee atasan = employeeCacheById.computeIfAbsent(currentEmployee.getAtasanUserId(), id -> employeeRepository.findFirstByUserIdNav(id));
                            for (int i = 0; i < layerIndex; i++) {
                                if (atasan == null) {
                                    break;
                                }
                                atasan = employeeCacheById.computeIfAbsent(atasan.getAtasanUserId(), id -> employeeRepository.findFirstByUserIdNav(id));
                            }
                            if (atasan != null) {
                                reviewer.setEmpCode(atasan.getEmployeeCode());
                                reviewer.setEmpName(atasan.getFullName());
                                reviewer.setEmpJobTitle(atasan.getJobTitle());
                                reviewer.setEmpAvatar(atasan.getAvatar());
                                reviewer.setLayerIndex(layerIndex);
                            }
                            reviewerEmpCodes.add(reviewer); // add it regardless of the result
                        } else {
                            Employee employee = employeeCacheByCode.computeIfAbsent(izinCutiReviewer.getEmpCode(), ec -> employeeRepository.findByEmployeeCode(ec));
                            if (employee == null) {
                                continue;
                            }
                            JenisIzinCutiAbsoluteReviewer reviewer = new JenisIzinCutiAbsoluteReviewer();
                            reviewer.setEmpCode(izinCutiReviewer.getEmpCode());
                            reviewer.setEmpName(employee.getFullName());
                            reviewer.setEmpJobTitle(employee.getJobTitle());
                            reviewer.setEmpAvatar(employee.getAvatar());
                            reviewerEmpCodes.add(reviewer);
                        }
                    }
                    jenisIzinCutiData.setReviewers(reviewerEmpCodes);
                    jenisIzinCutiData.setPengajuan(jenisIzinCuti.getPengajuan());
                    izinCutiList.add(jenisIzinCutiData);
                }
            }
            res.setMsg("SUCCESS");
            res.setCount(izinCutiList.size());
            res.setIzinCuti(izinCutiList);
        } catch (Exception ex) {
            res.setMsg("ERROR: "+ex.getMessage());
        }
        return res;
    }

    public List<IzinCutiData> getAllIzinCuti(String empCode, Long from, List<IzinCutiDetailResponse.Status> status, LocalDateTime start, LocalDateTime end) {
        if (start == null) {
            start = DEFAULT_START_TIME;
        }
        if (end == null) {
            end = LocalDateTime.now();
        }
        if (status == null || status.isEmpty()) {
            status = new ArrayList<>();
            status.add(IzinCutiDetailResponse.Status.PENDING);
            status.add(IzinCutiDetailResponse.Status.APPROVED);
            status.add(IzinCutiDetailResponse.Status.REJECTED);
            status.add(IzinCutiDetailResponse.Status.CANCELLED);
        }
        List<IzinCuti> list = from == null ? izinCutiRepository.findByEmpCode(empCode, status, start, end) : izinCutiRepository.findByEmpCode(empCode, from, status, start, end);
        List<IzinCutiData> res = new ArrayList<>();
        Map<Long, JenisIzinCuti> cache = new HashMap<>();
        for (IzinCuti izinCuti : list) {
            IzinCutiData data = new IzinCutiData();
            data.setId(izinCuti.getId());
            data.setStatus(izinCuti.getStatus());
            JenisIzinCuti jenisIzinCuti = cache.computeIfAbsent(izinCuti.getJenisCuti(), id -> jenisIzinCutiRepository.findIzinCutiById(id));
            if (jenisIzinCuti != null) {
                data.setJenisName(jenisIzinCuti.getIzinCuti());
                data.setTipe(jenisIzinCuti.getPengajuan());
            }
            data.setStartDate(izinCuti.getStartDate());
            data.setEndDate(izinCuti.getEndDate());
//            if (izinCuti.getStartDate().isBefore(LocalDateTime.now()) && izinCuti.getStatus() == IzinCutiDetailResponse.Status.PENDING) {
//                data.setStatus(IzinCutiDetailResponse.Status.EXPIRED);
//            } else
            if (izinCuti.getStatus() == IzinCutiDetailResponse.Status.APPROVED && izinCuti.getEndDate().isBefore(LocalDateTime.now())) {
                data.setStatus(IzinCutiDetailResponse.Status.DONE);
            } else if (izinCuti.getStatus() == IzinCutiDetailResponse.Status.APPROVED && izinCuti.getStartDate().isBefore(LocalDateTime.now()) && izinCuti.getEndDate().isAfter(LocalDateTime.now())) {
                data.setStatus(IzinCutiDetailResponse.Status.ON_GOING);
            }
            data.setCreatedAt(izinCuti.getCreatedAt());
            data.setUpdatedAt(izinCuti.getUpdatedAt());
            res.add(data);
        }
        return res;
    }

    public IzinCutiDetailResponse getIzinCuti(long id) {
        IzinCuti izinCuti = izinCutiRepository.findIzinCutiById(id);
        if (izinCuti == null) {
            return null;
        }
        Employee employee = employeeRepository.findByEmployeeCode(izinCuti.getEmpcode());
        if (employee == null) {
            return null;
        }
        IzinCutiDetailResponse res = new IzinCutiDetailResponse();
        res.setMessage("SUCCESS");
        res.setEmployeeCode(employee.getEmployeeCode());
        res.setEmployeeName(employee.getFullName());
        res.setEmployeeJobTitle(employee.getJobTitle());
        res.setId(izinCuti.getId());
        res.setStatus(izinCuti.getStatus());
        res.setCreatedAt(izinCuti.getCreatedAt());
        res.setUpdatedAt(izinCuti.getUpdatedAt());
        LocalDateTime now = LocalDateTime.now();
//        if (izinCuti.getStartDate().isBefore(now) && izinCuti.getStatus() == IzinCutiDetailResponse.Status.PENDING) {
//            res.setStatus(IzinCutiDetailResponse.Status.EXPIRED);
//        } else
        if (izinCuti.getStatus() == IzinCutiDetailResponse.Status.APPROVED && izinCuti.getEndDate().isBefore(now)) {
            res.setStatus(IzinCutiDetailResponse.Status.DONE);
        } else if (izinCuti.getStatus() == IzinCutiDetailResponse.Status.APPROVED && izinCuti.getStartDate().isBefore(now) && izinCuti.getEndDate().isAfter(now)) {
            res.setStatus(IzinCutiDetailResponse.Status.ON_GOING);
        }
        JenisIzinCuti jenisIzinCuti = jenisIzinCutiRepository.findIzinCutiById(izinCuti.getJenisCuti());
        if (jenisIzinCuti != null) {
            JenisIzinCutiData jenis = new JenisIzinCutiData();
            jenis.setId(jenisIzinCuti.getId());
            jenis.setNamaJenis(jenisIzinCuti.getIzinCuti());
            jenis.setCutCuti(jenisIzinCuti.getCutCuti());
            jenis.setPengajuan(jenisIzinCuti.getPengajuan());
            // NOTE: jangan isi approval, karena sudah diwakilkan oleh pengajuan
            res.setJenis(jenis);
        }
        res.setStartDate(izinCuti.getStartDate());
        res.setEndDate(izinCuti.getEndDate());
        res.setReason(izinCuti.getReason());
        List<IzinCutiApproval> approvals = izinCutiApprovalRepository.findAllByIzinCutiId(id);
        List<IzinCutiApprovalData> approvalData = new ArrayList<>();
        Map<String, Employee> cache = new HashMap<>();
        boolean waiting = false;
        for (IzinCutiApproval approval : approvals) {
            IzinCutiApprovalData data = new IzinCutiApprovalData();
            Employee reviewerEmployee = cache.computeIfAbsent(approval.getReviewerEmpCode(), empCode -> employeeRepository.findByEmployeeCode(empCode));
            if (reviewerEmployee == null) {
                continue;
            }
            data.setReviewerName(reviewerEmployee.getFullName());
            data.setReviewerEmployeeCode(reviewerEmployee.getEmployeeCode());
            data.setReviewerJobTitle(reviewerEmployee.getJobTitle());
            data.setStatus(approval.getStatus());
            if (approval.getStatus() == IzinCutiDetailResponse.Status.REJECTED) {
                data.setReason(approval.getReason());
            }
            data.setCreatedAt(approval.getCreatedAt());
            data.setUpdatedAt(approval.getUpdatedAt());
            if (waiting) {
                data.setStatus(IzinCutiDetailResponse.Status.WAITING);
            } else if (approval.getStatus() == IzinCutiDetailResponse.Status.PENDING) {
                waiting = true;
            }
            approvalData.add(data);
        }
        List<IzinCutiFileData> fileData = new ArrayList<>();
        List<IzinCutiFile> files = izinCutiFileRepository.findAllByIzinCutiId(id);
        for (IzinCutiFile file : files) {
            IzinCutiFileData data = new IzinCutiFileData();
            data.setId(file.getId());
            data.setFileName(file.getOriginalFileName());
            data.setFileDownloadUri(file.getFileDownloadUri());
            fileData.add(data);
        }
        res.setApprovals(approvalData);
        res.setFiles(fileData);
        return res;
    }

    public void rejectIzinCuti(long izinCutiId, String reviewerEmpCode, String reason) {
        if (reason == null) {
            reason = "";
        }
        Employee reviewer = employeeRepository.findByEmployeeCode(reviewerEmpCode);
        if (reviewer == null) {
            throw new IllegalArgumentException("REVIEWER_NOT_FOUND");
        }
        IzinCutiApproval approval = izinCutiApprovalRepository.findFirstByIzinCutiIdAndReviewerEmpCodeAndStatusIn(izinCutiId, reviewerEmpCode, Arrays.asList(
                IzinCutiDetailResponse.Status.PENDING,
                IzinCutiDetailResponse.Status.APPROVED
        ));
        if (approval == null) {
            return;
        }
        IzinCuti izinCuti = izinCutiRepository.findIzinCutiById(izinCutiId);
        if (izinCuti == null) {
            return;
        }
//        if (izinCuti.getStatus() == IzinCutiDetailResponse.Status.APPROVED) {
//            // tidak bisa reject jika sudah disetujui
//            throw new IllegalArgumentException("CANNOT_REJECT_APPROVED");
//        }
        if (izinCuti.getStatus() == IzinCutiDetailResponse.Status.CANCELLED) {
            // tidak bisa reject jika sudah ditolak atau dibatalkan
            throw new IllegalArgumentException("CANNOT_REJECT_REJECTED");
        }
        if (izinCuti.getStatus() == IzinCutiDetailResponse.Status.REJECTED) {
            // tidak bisa reject jika sudah ditolak
            throw new IllegalArgumentException("CANNOT_REJECT_REJECTED");
        }
        if (izinCuti.getStatus() == IzinCutiDetailResponse.Status.APPROVED) {
            // tidak bisa reject jika sudah disetujui
            throw new IllegalArgumentException("CANNOT_REJECT_APPROVED");
        }
//        if (izinCuti.getStartDate().isBefore(LocalDateTime.now())) {
//            // tidak bisa reject jika sudah lewat tanggal mulai
//            throw new IllegalArgumentException("CANNOT_REJECT_EXPIRED");
//        }
        Employee employee = employeeRepository.findByEmployeeCode(izinCuti.getEmpcode());
        if (employee == null) {
            throw new IllegalArgumentException("EMPLOYEE_NOT_FOUND");
        }
        approval.setReason(reason);
        approval.setStatus(IzinCutiDetailResponse.Status.REJECTED);
        izinCutiApprovalRepository.save(approval);
        // if the request is already rejected, no need to change
        if (izinCuti.getStatus() == IzinCutiDetailResponse.Status.REJECTED || izinCuti.getStatus() == IzinCutiDetailResponse.Status.CANCELLED) {
            return;
        }
        // send notification to employee
        notificationService.pushNotification(new IzinCutiResponseRejected(reviewer.getFullName(), reason, izinCutiId).build(), employee);

        // send email to employee
        emailService.sendEmail(employee, "Persetujuan Izin/Cuti Ditolak",
                EmailTemplateBuilder.create("Persetujuan Izin/Cuti Ditolak")
                        .append("Halo, ")
                        .append("Berikut ini adalah detail dari izin/cuti yang anda ajukan yang telah ditolak:")
                        .appendEntry("Nama", employee.getFullName())
                        .appendEntry("Peninjau", reviewer.getFullName())
                        .appendEntry("Alasan", reason)
                        .append("Anda dapat mengajukan kembali izin/cuti anda melalui aplikasi.")
                        .appendBtnPrimary("Lihat Detail di Aplikasi", frontEndURLService.getIzinCutiDetailURL(izinCutiId)).generate());

        izinCuti.setStatus(IzinCutiDetailResponse.Status.REJECTED);
        izinCutiRepository.save(izinCuti);
        // restore jatah cuti
        JenisIzinCuti jenisIzinCuti = jenisIzinCutiRepository.findIzinCutiById(izinCuti.getJenisCuti());
        if (jenisIzinCuti != null && jenisIzinCuti.getCutCuti()) {
            int year = LocalDate.now().getYear();
            if (jenisIzinCuti.getPengajuan() == JenisIzinCutiData.TipePengajuan.HARIAN) {
                long days = ChronoUnit.DAYS.between(izinCuti.getStartDate().toLocalDate(), izinCuti.getEndDate().toLocalDate());
                jatahCutiService.addJatahCuti(izinCuti.getEmpcode(), year, (int) days, JatahCutiService.KETERANGAN_PEMAKAIAN_CUTI_TIDAK_DISETUJUI, String.valueOf(izinCutiId));
            } // menit di akumulasi setiap bulan, dan di handle di cron jobs
        }
        // delete ketidakhadiran
        ketidakhadiranRepository.deleteAllByIdIzincuti(izinCutiId);
    }

    public void approveIzinCuti(long izinCutiId, String reviewerEmpCode) {
        Employee reviewer = employeeRepository.findByEmployeeCode(reviewerEmpCode);
        if (reviewer == null) {
            throw new IllegalArgumentException("REVIEWER_NOT_FOUND");
        }
        IzinCuti izinCuti = izinCutiRepository.findIzinCutiById(izinCutiId);
        if (izinCuti == null) {
            throw new IllegalArgumentException("IZIN_CUTI_NOT_FOUND");
        }
        JenisIzinCuti jenisIzinCuti = jenisIzinCutiRepository.findIzinCutiById(izinCuti.getJenisCuti());
        if (jenisIzinCuti == null) {
            throw new IllegalArgumentException("JENIS_IZIN_CUTI_NOT_FOUND");
        }
        List<IzinCutiApproval> approvals = izinCutiApprovalRepository.findAllByIzinCutiId(izinCutiId);
        IzinCutiApproval approval = null;
        boolean previousApproved = false;
//        for (IzinCutiApproval app : approvals) {
//            if (app.getReviewerEmpCode().equals(reviewerEmpCode)) {
//                approval = app;
//                break;
//            }
//        }
        for (int i = 0; i < approvals.size(); i++) {
            IzinCutiApproval app = approvals.get(i);
            if (app.getReviewerEmpCode().equals(reviewerEmpCode) && app.getStatus() == IzinCutiDetailResponse.Status.PENDING) {
                approval = app;
                if (i == 0 || approvals.get(i - 1).getStatus() == IzinCutiDetailResponse.Status.APPROVED) {
                    previousApproved = true;
                }
                break;
            }
        }
        if (approval == null) {
            throw new IllegalArgumentException("REVIEWER_NOT_FOUND");
        }

        if (!previousApproved) {
            throw new IllegalArgumentException("CANNOT_APPROVE_NOT_PREVIOUSLY_APPROVED");
        }
        if (izinCuti.getStatus() == IzinCutiDetailResponse.Status.APPROVED) {
            // tidak bisa approve jika sudah disetujui
            throw new IllegalArgumentException("CANNOT_APPROVE_APPROVED");
        }
        if (izinCuti.getStatus() == IzinCutiDetailResponse.Status.CANCELLED) {
            // tidak bisa approve jika sudah ditolak atau dibatalkan
            throw new IllegalArgumentException("CANNOT_APPROVE_REJECTED");
        }
        Employee employee = employeeRepository.findByEmployeeCode(izinCuti.getEmpcode());
        if (employee == null) {
            throw new IllegalArgumentException("EMPLOYEE_NOT_FOUND");
        }
        approval.setStatus(IzinCutiDetailResponse.Status.APPROVED);
        izinCutiApprovalRepository.save(approval);

        // jika pengajuan sudah ditolak, maka tidak perlu diubah
        if (izinCuti.getStatus() == IzinCutiDetailResponse.Status.REJECTED || izinCuti.getStatus() == IzinCutiDetailResponse.Status.CANCELLED || izinCuti.getStatus() == IzinCutiDetailResponse.Status.APPROVED) {
            return;
        }

        // find next approval
        IzinCutiApproval nextApproval = null;
        for (IzinCutiApproval app : approvals) {
            if (app.getId() > approval.getId() && app.getStatus() == IzinCutiDetailResponse.Status.PENDING) {
                nextApproval = app;
                break;
            }
        }

        // jika ada next approval, maka send notification ke reviewer selanjutnya
        if (nextApproval != null) {
            Employee nextReviewer = employeeRepository.findByEmployeeCode(nextApproval.getReviewerEmpCode());
            if (nextReviewer != null) {
                notificationService.pushNotification(new NotificationIzinCutiRequest(nextReviewer.getFullName(), jenisIzinCuti.getIzinCuti(), izinCutiId).build(), nextReviewer);

                // send email to next reviewer
                emailService.sendEmail(nextReviewer, "Persetujuan Izin/Cuti",
                        EmailTemplateBuilder.create("Persetujuan Izin/Cuti")
                                .append("Halo, ")
                                .append("Anda memiliki permintaan izin/cuti yang perlu anda tinjau:")
                                .appendEntry("Nama", employee.getFullName())
                                .appendEntry("Jenis", jenisIzinCuti.getIzinCuti())
                                .appendEntry("Alasan", izinCuti.getReason())
                                .append("Silahkan tinjau permintaan izin/cuti tersebut di aplikasi.")
                                .appendBtnPrimary("Lihat Detail di Aplikasi", frontEndURLService.getIzinCutiDetailURL(izinCutiId)).generate());
            }
            return;
        }

        boolean allApproved = true;
        for (IzinCutiApproval app : approvals) {
            if (app.getStatus() != IzinCutiDetailResponse.Status.APPROVED) {
                allApproved = false;
                break;
            }
        }
        if (allApproved) {
            // send notification to employee
            notificationService.pushNotification(new IzinCutiResponseAccepted(reviewer.getFullName(), izinCutiId).build(), employee);
            approveIzinCuti(izinCuti, jenisIzinCuti, employee);
        }
    }

    public void approveIzinCuti(IzinCuti izinCuti, JenisIzinCuti jenisIzinCuti, Employee employee) {
        izinCuti.setStatus(IzinCutiDetailResponse.Status.APPROVED);
        izinCutiRepository.save(izinCuti);


        // send email to employee
        emailService.sendEmail(employee, "Persetujuan Izin/Cuti Diterima",
                EmailTemplateBuilder.create("Persetujuan Izin/Cuti Diterima")
                        .append("Halo, ")
                        .append("Izin/cuti yang anda ajukan telah disetujui:")
                        .appendEntry("Nama", employee.getFullName())
                        .appendEntry("Jenis", jenisIzinCuti.getIzinCuti())
                        .appendEntry("Alasan", izinCuti.getReason())
                        .append("Anda dapat melihat detail izin/cuti anda di aplikasi.")
                        .appendBtnPrimary("Lihat Detail di Aplikasi", frontEndURLService.getIzinCutiDetailURL(izinCuti.getId())).generate());

        if (jenisIzinCuti.getPengajuan() == JenisIzinCutiData.TipePengajuan.HARIAN) {
            LocalDate startDate = izinCuti.getStartDate().toLocalDate();
            LocalDate endDate = izinCuti.getEndDate().toLocalDate();

            for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
                Ketidakhadiran ketidakhadiran = new Ketidakhadiran();
                ketidakhadiran.setEmployeeCode(izinCuti.getEmpcode());
                ketidakhadiran.setEmployeeName(employee.getFullName());
                ketidakhadiran.setDescription(izinCuti.getReason());
                ketidakhadiran.setMemotongCuti(jenisIzinCuti.getCutCuti());
                ketidakhadiran.setIdMesinAbsen(employee.getPersonIdMesinAbsen());
                ketidakhadiran.setTanggal(date);
                ketidakhadiran.setCauseOfAbsence("CUTI");
                ketidakhadiran.setFromTime(LocalTime.MIN);
                ketidakhadiran.setToTime(LocalTime.MAX);
                ketidakhadiran.setWholeDay(true);
                ketidakhadiran.setIdIzinCuti(izinCuti.getId());
                ketidakhadiranRepository.save(ketidakhadiran);
            }
        } else {
            Ketidakhadiran ketidakhadiran = convertKetidakhadiran(izinCuti, employee, jenisIzinCuti);
            ketidakhadiranRepository.save(ketidakhadiran);
        }
    }

    @NotNull
    private static Ketidakhadiran convertKetidakhadiran(IzinCuti izinCuti, Employee employee, JenisIzinCuti jenisIzinCuti) {
        Ketidakhadiran ketidakhadiran = new Ketidakhadiran();
        ketidakhadiran.setEmployeeCode(izinCuti.getEmpcode());
        ketidakhadiran.setEmployeeName(employee.getFullName());
        ketidakhadiran.setDescription(izinCuti.getReason());
        ketidakhadiran.setMemotongCuti(jenisIzinCuti.getCutCuti());
        ketidakhadiran.setIdMesinAbsen(employee.getPersonIdMesinAbsen());
        ketidakhadiran.setTanggal(izinCuti.getStartDate().toLocalDate());
        ketidakhadiran.setCauseOfAbsence("izin pribadi: " + izinCuti.getReason());
        ketidakhadiran.setFromTime(izinCuti.getStartDate().toLocalTime());
        ketidakhadiran.setToTime(izinCuti.getEndDate().toLocalTime());
        ketidakhadiran.setWholeDay(false);
        ketidakhadiran.setIdIzinCuti(izinCuti.getId());
        return ketidakhadiran;
    }

    public void cancelIzinCuti(long izinCutiId) {
        IzinCuti izinCuti = izinCutiRepository.findIzinCutiById(izinCutiId);
        if (izinCuti == null) {
            throw new IllegalArgumentException("IZIN_CUTI_NOT_FOUND");
        }
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate = izinCuti.getStartDate();
        if (izinCuti.getStatus() == IzinCutiDetailResponse.Status.APPROVED && startDate.isBefore(now)) {
            // tidak bisa cancel jika sudah disetujui dan sudah lewat tanggal mulai
            throw new IllegalArgumentException("CANNOT_CANCEL_APPROVED");
        }
        izinCuti.setStatus(IzinCutiDetailResponse.Status.CANCELLED);
        izinCutiRepository.save(izinCuti);
        // restore jatah cuti
        JenisIzinCuti jenisIzinCuti = jenisIzinCutiRepository.findIzinCutiById(izinCuti.getJenisCuti());
        if (jenisIzinCuti != null && jenisIzinCuti.getCutCuti()) {
            int year = LocalDate.now().getYear();
            if (jenisIzinCuti.getPengajuan() == JenisIzinCutiData.TipePengajuan.HARIAN) {
                long days = ChronoUnit.DAYS.between(izinCuti.getStartDate().toLocalDate(), izinCuti.getEndDate().toLocalDate());
                jatahCutiService.addJatahCuti(izinCuti.getEmpcode(), year, (int) days, JatahCutiService.KETERANGAN_PEMBATALAN_CUTI, String.valueOf(izinCutiId));
            } // menit di akumulasi setiap bulan, dan di handle di cron jobs
        }
        // tarik kembali data dari ketidakhadiran

        ketidakhadiranRepository.deleteAllByIdIzincuti(izinCutiId);
    }

    public void requestIzinCuti(IzinCutiRequest request, MultipartFile[] documents) {
        Employee employee = employeeRepository.findByEmployeeCode(request.getEmpcode());
        if (employee == null) {
            throw new IllegalArgumentException("EMPLOYEE_NOT_FOUND");
        }
        int year = LocalDate.now().getYear();
        JenisIzinCuti jenisIzinCuti = jenisIzinCutiRepository.findIzinCutiById(request.getJenisCuti());
        if (jenisIzinCuti == null) {
            throw new IllegalArgumentException("JENIS_CUTI_NOT_FOUND");
        }

        LocalDateTime startDate = request.getStartDate();
        LocalDateTime endDate = request.getEndDate();

        IzinCuti izinCuti = new IzinCuti();
        izinCuti.setEmpcode(request.getEmpcode());
        izinCuti.setJenisCuti(request.getJenisCuti());

        if (jenisIzinCuti.getPengajuan() == JenisIzinCutiData.TipePengajuan.HARIAN) {
            // lakukan pembulatan
            startDate = startDate.withHour(0).withMinute(0).withSecond(0).withNano(0);
            endDate = endDate.plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
            izinCuti.setStartDate(startDate);
            izinCuti.setEndDate(endDate);
        } else {
            izinCuti.setStartDate(request.getStartDate());
            izinCuti.setEndDate(request.getEndDate());
        }

        // PASTIKAN TIDAK ADA CUTI YANG BERIRISAN DENGAN YANG SEDANG DIAJUKAN (CUTI HARUS BERSTATUS APPROVED ATAU PENDING)
//        int overlaps = izinCutiRepository.countOverlappingCuti(request.getEmpcode(), startDate, endDate, List.of(IzinCutiDetailResponse.Status.APPROVED, IzinCutiDetailResponse.Status.PENDING));
//        if (overlaps > 0) {
//            throw new IllegalArgumentException("CUTI_OVERLAP");
//        }
        List<IzinCuti> izinCutiList = izinCutiRepository.findAllByEmpcode(request.getEmpcode());
        TimeRange range = new TimeRange(startDate, endDate);
        for (IzinCuti izin : izinCutiList) {
            TimeRange otherRange = new TimeRange(izin.getStartDate(), izin.getEndDate());
            if (range.overlaps(otherRange)) {
                if (izin.getStatus() == IzinCutiDetailResponse.Status.APPROVED || izin.getStatus() == IzinCutiDetailResponse.Status.PENDING) {
                    throw new IllegalArgumentException("CUTI_OVERLAP");
                }
            }
        }

        int days = (int) (endDate.toLocalDate().toEpochDay() - startDate.toLocalDate().toEpochDay());
        if (jenisIzinCuti.getCutCuti() && jenisIzinCuti.getPengajuan() == JenisIzinCutiData.TipePengajuan.HARIAN) {
            int jatahCutiHari = jatahCutiService.sumJatahCutiBalance(request.getEmpcode(), year);
            if (jatahCutiHari < days) {
                throw new IllegalArgumentException("JATAH_CUTI_HABIS");
            }
            jatahCutiService.addJatahCuti(request.getEmpcode(), year, -days, JatahCutiService.KETERANGAN_PEMAKAIAN_CUTI, String.valueOf(izinCuti.getId()));
        }

        izinCuti.setReason(request.getReason());
        izinCuti.setStatus(IzinCutiDetailResponse.Status.PENDING);
        izinCutiRepository.save(izinCuti);
        List<IzinCutiReviewer> reviewers = izinCutiReviewerRepository.findByTipeIzinCutiId(request.getJenisCuti());
        Map<IzinCutiReviewer, Employee> reviewerCache = new HashMap<>();
        Set<String> empCodes = new HashSet<>();
        for (IzinCutiReviewer reviewer : reviewers) {
            String empCode = reviewer.getEmpCode();
            Employee reviewerEmployee;
            if (empCode == null) {
                Integer layerIndex = reviewer.getLayerIndex();
                if (layerIndex == null) {
                    continue;
                }
                String atasanUserId = employee.getAtasanUserId();
                if (atasanUserId == null || atasanUserId.isEmpty()) {
                    continue;
                }
                Employee atasan = employeeRepository.findFirstByUserIdNav(atasanUserId);
                if (atasan != null) {
                    for (int i = 0; i < layerIndex; i++) {
                        if (atasan == null) {
                            break;
                        }
                        String atasanUserId1 = atasan.getAtasanUserId();
                        if (atasanUserId1 == null || atasanUserId1.isEmpty()) {
                            break;
                        }
                        atasan = employeeRepository.findFirstByUserIdNav(atasanUserId1);
                    }
                }
                if (atasan == null) {
                    continue;
                }
                reviewerEmployee = atasan;
            } else {
                reviewerEmployee = employeeRepository.findByEmployeeCode(empCode);
            }
            if (reviewerEmployee == null) {
                continue;
            }
            // prevent self-review
            if (reviewerEmployee.getEmployeeCode().equals(employee.getEmployeeCode())) {
                continue;
            }
            // prevent duplicate reviewer
            if (empCodes.contains(reviewerEmployee.getEmployeeCode())) {
                continue;
            }
            empCodes.add(reviewerEmployee.getEmployeeCode());
            reviewerCache.put(reviewer, reviewerEmployee);
            IzinCutiApproval approval = new IzinCutiApproval();
            approval.setIzinCutiId(izinCuti.getId());
            approval.setReviewerEmpCode(reviewerEmployee.getEmployeeCode());
            approval.setStatus(IzinCutiDetailResponse.Status.PENDING);
            izinCutiApprovalRepository.save(approval);
//            notificationService.pushNotification(new NotificationIzinCutiRequest(reviewerEmployee.getFullName(), jenisIzinCuti.getIzinCuti(), izinCuti.getId()).build(), reviewerEmployee.getEmployeeCode());
        }

        // send notifikasi ke peninjau pertama
        if (!reviewers.isEmpty()) {
            IzinCutiReviewer firstReviewer = reviewers.get(0);
            Employee firstReviewerEmployee = reviewerCache.get(firstReviewer);
            if (firstReviewerEmployee != null) {
                notificationService.pushNotification(new NotificationIzinCutiRequest(firstReviewerEmployee.getFullName(), jenisIzinCuti.getIzinCuti(), izinCuti.getId()).build(), firstReviewerEmployee);

                // send email to first reviewer
                emailService.sendEmail(firstReviewerEmployee, "Persetujuan Izin/Cuti",
                        EmailTemplateBuilder.create("Persetujuan Izin/Cuti")
                                .append("Halo, ")
                                .append("Anda memiliki permintaan izin/cuti yang perlu anda tinjau:")
                                .appendEntry("Nama", employee.getFullName())
                                .appendEntry("Jenis", jenisIzinCuti.getIzinCuti())
                                .appendEntry("Alasan", request.getReason())
                                .append("Silahkan tinjau permintaan izin/cuti tersebut di aplikasi.")
                                .appendBtnPrimary("Lihat Detail di Aplikasi", frontEndURLService.getIzinCutiDetailURL(izinCuti.getId())).generate());
            }
        } else {
            // jika tidak ada reviewer, maka langsung approve
            approveIzinCuti(izinCuti, jenisIzinCuti, employee);
        }

        for (MultipartFile file : documents) {
            String fileName = izinCutiFileService.uploadFile(file, request.getEmpcode());
            IzinCutiFile izinCutiFile = new IzinCutiFile();
            String name = file.getOriginalFilename();
            if (name == null || name.isEmpty()) {
                name = file.getName();
            }
            izinCutiFile.setOriginalFileName(name);
            izinCutiFile.setIzinCutiId(izinCuti.getId());
            izinCutiFile.setFileName(fileName);
            String downloadUri = izinCutiFileService.getFileUrl(fileName, request.getEmpcode());
            izinCutiFile.setFileDownloadUri(downloadUri);
            izinCutiFileRepository.save(izinCutiFile);
        }
    }

    public IzinCutiDashboardResponse getIzinCutiDashboard(String empcode) {
        Employee employee = employeeRepository.findByEmployeeCode(empcode);
        if (employee == null) {
            throw new IllegalArgumentException("EMPLOYEE_NOT_FOUND");
        }
        IzinCutiDashboardResponse res = new IzinCutiDashboardResponse();
//        List<IzinCuti> izinCutiList = izinCutiRepository.findAllByStatusAndEmpcode(IzinCutiDetailResponse.Status.APPROVED, empcode);
//        List<IzinCutiData> izinCutiDataList = new ArrayList<>();
        IzinCuti approvedCuti = izinCutiRepository.findFirstByStatusAndEmpcode(IzinCutiDetailResponse.Status.APPROVED, empcode, LocalDateTime.now());
        if (approvedCuti != null) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime startDate = approvedCuti.getStartDate();
            LocalDateTime endDate = approvedCuti.getEndDate();
            // if startDate <= now <= endDate
            if (!now.isBefore(startDate) && !now.isAfter(endDate)) {
                IzinCutiData izinCutiData = new IzinCutiData();
                izinCutiData.setId(approvedCuti.getId());
                izinCutiData.setStatus(IzinCutiDetailResponse.Status.ON_GOING);
                JenisIzinCuti jenisIzinCuti = jenisIzinCutiRepository.findIzinCutiById(approvedCuti.getJenisCuti());
                if (jenisIzinCuti != null) {
                    izinCutiData.setJenisName(jenisIzinCuti.getIzinCuti());
                    izinCutiData.setTipe(jenisIzinCuti.getPengajuan());
                    izinCutiData.setStartDate(approvedCuti.getStartDate());
                    izinCutiData.setEndDate(approvedCuti.getEndDate());
                    izinCutiData.setCreatedAt(approvedCuti.getCreatedAt());
                    izinCutiData.setUpdatedAt(approvedCuti.getUpdatedAt());
                    res.setCutiAktif(izinCutiData);
                }
            }
        }
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        int jatahCuti = jatahCutiService.sumJatahCutiBalance(empcode, year);
        Long izinCutiAccumulation = izinCutiRepository.accumulateCutiOnlyIzin(empcode, year, month, JenisIzinCutiData.TipePengajuan.MENIT, List.of(
                IzinCutiDetailResponse.Status.APPROVED,
                IzinCutiDetailResponse.Status.PENDING
        ), LocalDateTime.now());
        res.setSisaCuti(jatahCuti);
        if (izinCutiAccumulation != null) {
            res.setAkumulasiIzin(izinCutiAccumulation);
        }
        res.setMessage("SUCCESS");
        return res;
    }

    public List<IzinCutiRequestData> getReviewedIzinCuti(String empcode, Long after, List<IzinCutiDetailResponse.Status> status, LocalDateTime start, LocalDateTime end) {
        if (start == null) {
            start = DEFAULT_START_TIME;
        }
        if (end == null) {
            end = LocalDateTime.now();
        }
        if (status == null) {
            status = new ArrayList<>();
        }
        if (status.isEmpty()) {
            status.addAll(Arrays.asList(IzinCutiDetailResponse.Status.values()));
        }
        List<IzinCuti> list = after == null ? izinCutiRepository.findAllReviewedByEmpCode(empcode, status, start, end) : izinCutiRepository.findAllReviewedByEmpCode(empcode, after, status, start, end);
        List<IzinCutiRequestData> res = new ArrayList<>();
        Map<Long, JenisIzinCuti> cache = new HashMap<>();
        Map<String, Employee> employeeCache = new HashMap<>();
        for (IzinCuti izinCuti : list) {
            IzinCutiRequestData data = new IzinCutiRequestData();
            data.setId(izinCuti.getId());
            data.setStatus(izinCuti.getStatus());
            JenisIzinCuti jenisIzinCuti = cache.computeIfAbsent(izinCuti.getJenisCuti(), id -> jenisIzinCutiRepository.findIzinCutiById(id));
            if (jenisIzinCuti != null) {
                data.setJenisName(jenisIzinCuti.getIzinCuti());
                data.setTipe(jenisIzinCuti.getPengajuan());
            }
            // status
//            if (izinCuti.getStartDate().isBefore(LocalDateTime.now()) && izinCuti.getStatus() == IzinCutiDetailResponse.Status.PENDING) {
//                data.setStatus(IzinCutiDetailResponse.Status.EXPIRED);
//            } else
            if (izinCuti.getStatus() == IzinCutiDetailResponse.Status.APPROVED && izinCuti.getEndDate().isBefore(LocalDateTime.now())) {
                data.setStatus(IzinCutiDetailResponse.Status.DONE);
            } else if (izinCuti.getStatus() == IzinCutiDetailResponse.Status.APPROVED && izinCuti.getStartDate().isBefore(LocalDateTime.now()) && izinCuti.getEndDate().isAfter(LocalDateTime.now())) {
                data.setStatus(IzinCutiDetailResponse.Status.ON_GOING);
            }
            data.setStartDate(izinCuti.getStartDate());
            data.setEndDate(izinCuti.getEndDate());
            Employee employee = employeeCache.computeIfAbsent(izinCuti.getEmpcode(), ec -> employeeRepository.findByEmployeeCode(ec));
            if (employee == null) {
                continue;
            }
            data.setEmployeeName(employee.getFullName());
            data.setEmployeeCode(employee.getEmployeeCode());
            data.setEmployeeJobTitle(employee.getJobTitle());
            data.setCreatedAt(izinCuti.getCreatedAt());
            data.setUpdatedAt(izinCuti.getUpdatedAt());
            res.add(data);
        }
        return res;
    }
}
