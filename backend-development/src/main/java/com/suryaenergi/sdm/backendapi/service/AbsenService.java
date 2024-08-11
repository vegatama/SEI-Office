package com.suryaenergi.sdm.backendapi.service;

import com.suryaenergi.sdm.backendapi.email.template.EmailTemplateBuilder;
import com.suryaenergi.sdm.backendapi.entity.*;
import com.suryaenergi.sdm.backendapi.pojo.*;
import com.suryaenergi.sdm.backendapi.notification.AttendanceSwipeRequest;
import com.suryaenergi.sdm.backendapi.notification.AttendanceSwipeResponseAccepted;
import com.suryaenergi.sdm.backendapi.notification.AttendanceSwipeResponseRejected;
import com.suryaenergi.sdm.backendapi.repository.*;
import com.suryaenergi.sdm.backendapi.request.AbsenSayaRequest;
import com.suryaenergi.sdm.backendapi.request.KirimEmailRequest;
import com.suryaenergi.sdm.backendapi.response.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AbsenService {
    @Autowired
    private RekapabsenRepository rekapabsenRepository;
    @Autowired
    private MesinabsenRepository mesinabsenRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private DataabsencsvRepository dataabsencsvRepository;
    @Autowired
    private KetidakhadiranRepository ketidakhadiranRepository;
    @Autowired
    private HitungabsenRepository hitungabsenRepository;
    @Autowired
    private HariliburRepository hariliburRepository;
    @Autowired
    private AntrianemailRepository antrianemailRepository;
    @Autowired
    private SppdnavRepository sppdnavRepository;
    @Autowired
    private DepartemenRepository departemenRepository;
    @Autowired
    private LokasiAbsenRepository lokasiAbsenRepository;
    @Autowired
    private PendingSwipeRequestRepository pendingSwipeRequestRepository;
    @Autowired
    private JamkerjaRepository jamkerjaRepository;
    @Autowired
    private AttendanceImageService attendanceImageService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private FrontEndURLService frontEndURLService;

    public static final double EARTH_RADIUS = 6378137;
    public static double distanceBetween(double startLatitude, double startLongitude, double endLatitude, double endLongitude) {
        double dLat = Math.toRadians(endLatitude - startLatitude);
        double dLon = Math.toRadians(endLongitude - startLongitude);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(startLatitude)) * Math.cos(Math.toRadians(endLatitude)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c;
    }

    // KHUSUS MOBILE
    public void requestSwipe(String employeeCode, double latitude, double longitude, String reason, String image) {
        Employee employee = employeeRepository.findByEmployeeCode(employeeCode);
        if (employee == null) throw new RuntimeException("EMPLOYEE_NOT_FOUND");
        Employee atasan = employeeRepository.findFirstByUserIdNav(employee.getAtasanUserId());
        if (atasan == null) throw new RuntimeException("ATASAN_NOT_FOUND");
        LokasiAbsen defaultLocation = employee.getLokasiAbsenId();
        if (defaultLocation == null) {
            defaultLocation = lokasiAbsenRepository.findFirstByIsDefault(true);
        }
        if (defaultLocation == null) {
            throw new RuntimeException("DEFAULT_NOT_SET");
        }
        boolean isCheckOut = mesinabsenRepository.existsMesinabsenByPersonIdAndDate(employee.getPersonIdMesinAbsen(), LocalDate.now());
        PendingSwipeRequest pendingSwipeRequest = pendingSwipeRequestRepository.findFirstByEmployeeCodeAndDateAndNotApproved(employeeCode, LocalDate.now());
        if (pendingSwipeRequest == null) {
            pendingSwipeRequest = new PendingSwipeRequest();
        }
        pendingSwipeRequest.setAssignedReviewerEmployeeCode(atasan.getEmployeeCode());
        pendingSwipeRequest.setEmployeeCode(employee.getEmployeeCode());
        pendingSwipeRequest.setEmployeeName(employee.getFullName());
        pendingSwipeRequest.setEmployeeJobTitle(employee.getJobTitle());
        pendingSwipeRequest.setDateTime(LocalDateTime.now());
        pendingSwipeRequest.setDate(LocalDate.now());
        pendingSwipeRequest.setLatitude(latitude);
        pendingSwipeRequest.setLongitude(longitude);
        pendingSwipeRequest.setRadius(defaultLocation.getRadius() == null ? 0 : defaultLocation.getRadius());
        pendingSwipeRequest.setCenterLatitude(defaultLocation.getLatitude());
        pendingSwipeRequest.setCenterLongitude(defaultLocation.getLongitude());
        pendingSwipeRequest.setReason(reason);
        pendingSwipeRequest.setEmployeeMesinId(employee.getPersonIdMesinAbsen());
        pendingSwipeRequest.setImageUrl(image);
        pendingSwipeRequest.setCheckout(isCheckOut);

        double distance = distanceBetween(latitude, longitude, defaultLocation.getLatitude(), defaultLocation.getLongitude());

        pendingSwipeRequestRepository.save(pendingSwipeRequest);
        NotificationData notif = new AttendanceSwipeRequest(employee.getFullName(), pendingSwipeRequest.getId(), isCheckOut).build();
        notificationService.pushNotification(notif, atasan);

        emailService.sendEmail(atasan,
                isCheckOut ? "Permintaan Persetujuan Check-Out" : "Permintaan Persetujuan Check-In",
                EmailTemplateBuilder.create(isCheckOut ? "Permintaan Persetujuan Check-Out" : "Permintaan Persetujuan Check-In")
                        .append("Halo, ")
                        .append(isCheckOut ? "Berikut adalah permintaan persetujuan check-out dari " : "Berikut adalah permintaan persetujuan check-in dari ")
                        .appendEntry("Nama", employee.getFullName())
                        .appendEntry("Tanggal", LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")))
                        .appendEntry("Jam", LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")))
                        .appendEntry("Alasan", reason)
                        .appendEntry("Radius", pendingSwipeRequest.getRadius())
                        .appendEntry("Lokasi", defaultLocation.getLokasiAbsen())
                        .appendEntry("Jarak", distance)
                        .appendNetworkImage(image, "Gambar Bukti")
                        .append("Anda dapat menyetujui atau menolak permintaan ini melalui aplikasi.")
                        .appendBtnPrimary("Lihat Detail di Aplikasi", frontEndURLService.getAbsenRequestDetail(pendingSwipeRequest.getId()))
                        .generate());
    }
    public void swipe(String employeeCode, double latitude, double longitude) {
        Employee employee = employeeRepository.findByEmployeeCode(employeeCode);
        if (employee == null) throw new RuntimeException("EMPLOYEE_NOT_FOUND");
        LokasiAbsen defaultLocation = employee.getLokasiAbsenId();
        if (defaultLocation == null) {
            defaultLocation = lokasiAbsenRepository.findFirstByIsDefault(true);
        }
        if (defaultLocation == null) {
            throw new RuntimeException("DEFAULT_NOT_SET");
        }
        // CEK JARAK
        double distance = distanceBetween(latitude, longitude, defaultLocation.getLatitude(), defaultLocation.getLongitude());
        if (distance > (defaultLocation.getRadius() == null ? 0 : defaultLocation.getRadius())) {
            throw new RuntimeException("OUT_OF_RANGE");
        }
        Mesinabsen mesinabsen = new Mesinabsen();
        LocalDateTime now = LocalDateTime.now();
        mesinabsen.setName(employee.getFullName());
        mesinabsen.setPersonId(employee.getPersonIdMesinAbsen());
        mesinabsen.setDateTime(now);
        mesinabsen.setDate(now.toLocalDate());
        mesinabsen.setTime(now.toLocalTime());
        mesinabsen.setLatitude(latitude);
        mesinabsen.setLongitude(longitude);
        mesinabsen.setRadius(defaultLocation.getRadius() == null ? 0 : defaultLocation.getRadius());
        mesinabsen.setCenterLatitude(defaultLocation.getLatitude());
        mesinabsen.setCenterLongitude(defaultLocation.getLongitude());
        mesinabsen.setDeviceName("MobileApp");
        mesinabsen.setDeviceSn("MobileApp");
        mesinabsen.setDirection("");
        StringBuilder mesinId = new StringBuilder(employee.getPersonIdMesinAbsen());
        while (mesinId.length() < 10) {
            mesinId.insert(0, "0");
        }
        mesinabsen.setCardNo(mesinId.toString());
        mesinabsenRepository.save(mesinabsen);
    }
    public void confirmRequest(long id, boolean approve, String reviewerEmpCode, String reason) {
        Employee reviewer = employeeRepository.findByEmployeeCode(reviewerEmpCode);
        if (reviewer == null) {
            throw new RuntimeException("REVIEWER_NOT_FOUND");
        }
        PendingSwipeRequest pendingSwipeRequest = pendingSwipeRequestRepository.findById(id).orElse(null);
        if (pendingSwipeRequest != null && pendingSwipeRequest.getApproved() == 0) {
            Employee employee = employeeRepository.findByEmployeeCode(pendingSwipeRequest.getEmployeeCode());
            if (employee == null) {
                throw new RuntimeException("EMPLOYEE_NOT_FOUND");
            }
            pendingSwipeRequest.setApproved(approve ? 1 : -1);
            if (!approve) {
                pendingSwipeRequest.setRejectReason(reason);
            }
            pendingSwipeRequestRepository.save(pendingSwipeRequest);
            boolean isCheckOut = mesinabsenRepository.existsMesinabsenByPersonIdAndDate(pendingSwipeRequest.getEmployeeMesinId(), pendingSwipeRequest.getDate());
            if (approve) {
                Mesinabsen mesinabsen = new Mesinabsen();
                mesinabsen.setName(pendingSwipeRequest.getEmployeeName());
                mesinabsen.setPersonId(pendingSwipeRequest.getEmployeeMesinId());
                mesinabsen.setDateTime(pendingSwipeRequest.getDateTime());
                mesinabsen.setDate(pendingSwipeRequest.getDate());
                mesinabsen.setTime(pendingSwipeRequest.getDateTime().toLocalTime());
                mesinabsen.setLatitude(pendingSwipeRequest.getLatitude());
                mesinabsen.setLongitude(pendingSwipeRequest.getLongitude());
                mesinabsen.setRadius(pendingSwipeRequest.getRadius());
                mesinabsen.setCenterLatitude(pendingSwipeRequest.getCenterLatitude());
                mesinabsen.setCenterLongitude(pendingSwipeRequest.getCenterLongitude());
                mesinabsen.setDeviceName("MobileApp");
                mesinabsen.setDeviceSn("MobileApp");
                mesinabsen.setDirection("");
                StringBuilder mesinId = new StringBuilder(pendingSwipeRequest.getEmployeeMesinId());
                while (mesinId.length() < 10) {
                    mesinId.insert(0, "0");
                }
                mesinabsen.setCardNo(mesinId.toString());
                mesinabsenRepository.save(mesinabsen);
                NotificationData notif = new AttendanceSwipeResponseAccepted(reviewer.getFullName(), pendingSwipeRequest.getDate(), isCheckOut).build();
                notificationService.pushNotification(notif, employee);

                emailService.sendEmail(employee,
                        isCheckOut ? "Persetujuan Check-Out Diterima" : "Persetujuan Check-In Diterima",
                        EmailTemplateBuilder.create(isCheckOut ? "Persetujuan Check-Out Diterima" : "Persetujuan Check-In Diterima")
                                .append("Halo, ")
                                .append(isCheckOut ? "Permintaan persetujuan check-out anda telah disetujui oleh " : "Permintaan persetujuan check-in anda telah disetujui oleh ")
                                .appendEntry("Nama", reviewer.getFullName())
                                .appendEntry("Tanggal", pendingSwipeRequest.getDate().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")))
                                .appendEntry("Jam", pendingSwipeRequest.getDateTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")))
                                .append("Kehadiran anda telah dicatat di sistem kami.")
                                .appendBtnPrimary("Lihat Detail di Aplikasi", frontEndURLService.getAbsenRequestDetail(pendingSwipeRequest.getId()))
                                .generate());
            } else {
                NotificationData notif = new AttendanceSwipeResponseRejected(reviewer.getFullName(), pendingSwipeRequest.getDate(), isCheckOut).build();
                notificationService.pushNotification(notif, employee);

                emailService.sendEmail(employee,
                        isCheckOut ? "Persetujuan Check-Out Ditolak" : "Persetujuan Check-In Ditolak",
                        EmailTemplateBuilder.create(isCheckOut ? "Persetujuan Check-Out Ditolak" : "Persetujuan Check-In Ditolak")
                                .append("Halo, ")
                                .append(isCheckOut ? "Permintaan persetujuan check-out anda telah ditolak oleh " : "Permintaan persetujuan check-in anda telah ditolak oleh ")
                                .appendEntry("Nama", reviewer.getFullName())
                                .appendEntry("Tanggal", pendingSwipeRequest.getDate().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")))
                                .appendEntry("Jam", pendingSwipeRequest.getDateTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")))
                                .append("Anda dapat mengajukan kembali permintaan ini melalui aplikasi.")
                                .appendBtnPrimary("Lihat Detail di Aplikasi", frontEndURLService.getAbsenRequestDetail(pendingSwipeRequest.getId()))
                                .generate());
            }
        }
    }

    public List<EmployeeCheckInApproval> getRequests(String atasanEmployeeCode, Long from, boolean showAll) {
        Employee atasan = employeeRepository.findByEmployeeCode(atasanEmployeeCode);
        if (atasan != null) {
            LokasiAbsen defaultLocation = atasan.getLokasiAbsenId();
            if (defaultLocation == null) {
                defaultLocation = lokasiAbsenRepository.findFirstByIsDefault(true);
            }
            List<PendingSwipeRequest> pendingSwipeRequestList;
            if (from == null) {
                // first page
                pendingSwipeRequestList = pendingSwipeRequestRepository.findAllOnlyUnapproved(atasanEmployeeCode, showAll ? List.of(-2, -1, 0, 1) : List.of(0));
            } else {
                pendingSwipeRequestList = pendingSwipeRequestRepository.findAllOnlyUnapproved(atasanEmployeeCode, from, showAll ? List.of(-2, -1, 0, 1) : List.of(0));
            }
            Map<LocalDate, Jamkerja> cachedJamKerja = new HashMap<>();
            List<EmployeeCheckInApproval> result = new ArrayList<>(pendingSwipeRequestList.size());
            for (PendingSwipeRequest pendingSwipeRequest : pendingSwipeRequestList) {
                Jamkerja jamkerja = cachedJamKerja.computeIfAbsent(pendingSwipeRequest.getDate(), tgl -> jamkerjaRepository.findFirstByTanggal(tgl));
                EmployeeCheckInApproval approval = convertPendingCheckIn(pendingSwipeRequest, jamkerja, defaultLocation);
                result.add(approval);
            }
            return result;
        }
        return null;
    }

    public List<EmployeeCheckInApproval> getMyRequests(String employeeCode, Long from) {
        Employee atasan = employeeRepository.findByEmployeeCode(employeeCode);
        if (atasan != null) {
            LokasiAbsen defaultLocation = atasan.getLokasiAbsenId();
            if (defaultLocation == null) {
                defaultLocation = lokasiAbsenRepository.findFirstByIsDefault(true);
            }
            List<PendingSwipeRequest> pendingSwipeRequestList;
            if (from == null) {
                // first page
                pendingSwipeRequestList = pendingSwipeRequestRepository.findAllOnlyUnapprovedSelf(employeeCode);
            } else {
                pendingSwipeRequestList = pendingSwipeRequestRepository.findAllLimitedSelf(employeeCode, from);
            }
            Map<LocalDate, Jamkerja> cachedJamKerja = new HashMap<>();
            List<EmployeeCheckInApproval> result = new ArrayList<>(pendingSwipeRequestList.size());
            for (PendingSwipeRequest pendingSwipeRequest : pendingSwipeRequestList) {
                Jamkerja jamkerja = cachedJamKerja.computeIfAbsent(pendingSwipeRequest.getDate(), tgl -> jamkerjaRepository.findFirstByTanggal(tgl));
                EmployeeCheckInApproval approval = convertPendingCheckIn(pendingSwipeRequest, jamkerja, defaultLocation);
                result.add(approval);
            }
            return result;
        }
        return null;
    }

    private EmployeeCheckInApproval convertPendingCheckIn(PendingSwipeRequest pendingSwipeRequest, Jamkerja jamkerja, LokasiAbsen lokasiAbsen) {
        EmployeeCheckInApproval approval = new EmployeeCheckInApproval();
        approval.setId(pendingSwipeRequest.getId());
        approval.setEmployeeCode(pendingSwipeRequest.getEmployeeCode());
        approval.setEmployeeName(pendingSwipeRequest.getEmployeeName());
        approval.setEmployeeJobTitle(pendingSwipeRequest.getEmployeeJobTitle());
        approval.setLatitude(pendingSwipeRequest.getLatitude());
        approval.setLongitude(pendingSwipeRequest.getLongitude());
        approval.setRadius(pendingSwipeRequest.getRadius());
        approval.setCenterLatitude(pendingSwipeRequest.getCenterLatitude());
        approval.setCenterLongitude(pendingSwipeRequest.getCenterLongitude());
        approval.setReason(pendingSwipeRequest.getReason());
        approval.setApproved(pendingSwipeRequest.getApproved());
        approval.setDate(pendingSwipeRequest.getDate());
        approval.setTime(pendingSwipeRequest.getDateTime().toLocalTime());
        approval.setCheckOut(pendingSwipeRequest.isCheckout());
        if (pendingSwipeRequest.getImageUrl() != null) {
            approval.setImageUrl(pendingSwipeRequest.getImageUrl());
        }
        if (jamkerja != null) {
            approval.setTargetTime(jamkerja.getJamMasuk());
        } else {
            approval.setTargetTime(DataAbsenSaya.WORK_HOUR_BEGIN);
        }
        approval.setRejectReason(pendingSwipeRequest.getRejectReason());
        approval.setLokasiAbsen(lokasiAbsen.getLokasiAbsen());
        // distance
        double distance = distanceBetween(pendingSwipeRequest.getLatitude(), pendingSwipeRequest.getLongitude(), lokasiAbsen.getLatitude(), lokasiAbsen.getLongitude());
        approval.setDistance(distance);
        return approval;
    }

    /*
    History untuk bulan ini belum direkap sehingga perlu diambil dari mesin absen secara langsung
     */
    public List<AttendanceData> getThisMonthsHistory(String employeeCode, Integer dayFrom, Integer dayTo) {
        LocalDate now = LocalDate.now();
        if (dayTo == null) {
            dayTo = now.getDayOfMonth();
        }
        if (dayFrom == null) {
            dayFrom = dayTo - 6;
        }
        // max per fetch is 1 week
//        if (dayTo > dayFrom + 6) {
//            throw new RuntimeException("MAX_DATE_RANGE_7_DAYS");
//        }
        Employee employee = employeeRepository.findByEmployeeCode(employeeCode);
        if (employee == null) {
            throw new RuntimeException("EMPLOYEE_NOT_FOUND");
        }
        LokasiAbsen defaultLocation = employee.getLokasiAbsenId();
        if (defaultLocation == null) {
            defaultLocation = lokasiAbsenRepository.findFirstByIsDefault(true);
        }
        if (defaultLocation == null) {
            throw new RuntimeException("DEFAULT_NOT_SET");
        }
        boolean telahCheckIn = false;
        boolean telahCheckOut = false;
        boolean pendingCheckIn = false;
        boolean pendingCheckOut = false;
        LocalDate from = LocalDate.of(now.getYear(), now.getMonth(), dayFrom);
        LocalDate to = LocalDate.of(now.getYear(), now.getMonth(), dayTo);
        List<Mesinabsen> mesinabsenList = mesinabsenRepository.findAllByPersonIdAndDateBetween(employee.getPersonIdMesinAbsen(), from, to);
        List<AttendanceData> result = new ArrayList<>(dayTo - dayFrom + 1);
        Map<LocalDate, Jamkerja> cachedJamKerja = new HashMap<>();
        Map<LocalDate, HariLibur> cachedHariLibur = new HashMap<>();
        for (int day = dayTo; day >= dayFrom; day--) {
            LocalDate date = LocalDate.of(now.getYear(), now.getMonth(), day);
            Mesinabsen swipePertama = null;
            Mesinabsen swipeTerakhir = null;
            int size = 0;
            for (Mesinabsen mesinabsen : mesinabsenList) {
                if (!mesinabsen.getDate().equals(date)) {
                    continue;
                }
                if (swipePertama == null || mesinabsen.getTime().isBefore(swipePertama.getTime())) {
                    swipePertama = mesinabsen;
                }
                if (swipeTerakhir == null || mesinabsen.getTime().isAfter(swipeTerakhir.getTime())) {
                    swipeTerakhir = mesinabsen;
                }
                size++;
            }
            if (swipePertama != null && swipePertama.equals(swipeTerakhir)) {
                swipeTerakhir = null;
            }
            telahCheckIn = swipePertama != null;
            telahCheckOut = swipeTerakhir != null;
            if (size == 0) {
                // cek apakah hari libur
                HariLibur hariLibur = cachedHariLibur.computeIfAbsent(date, tgl -> hariliburRepository.findFirstByTanggal(tgl));
                AttendanceData dataAbsenSaya;
                if (hariLibur != null || date.getDayOfWeek() == DayOfWeek.SUNDAY || date.getDayOfWeek() == DayOfWeek.SATURDAY) {
                    dataAbsenSaya = getDataKosong(date, DataAbsenSaya.STATUS_LIBUR, "Hari Libur", employeeCode);
                    result.add(dataAbsenSaya);
                    continue;
                }
            }
            AttendanceData dataAbsenSaya = new AttendanceData();
            dataAbsenSaya.setDay(date.getDayOfMonth());
            dataAbsenSaya.setMonth(date.getMonthValue());
            dataAbsenSaya.setYear(date.getYear());
            if (swipePertama == null) {
                dataAbsenSaya.setFirstCheckIn(null);
            } else {
                dataAbsenSaya.setFirstCheckIn(swipePertama.getTime());
                dataAbsenSaya.setLongitude(swipePertama.getLongitude() == null ? defaultLocation.getLongitude() : swipePertama.getLongitude());
                dataAbsenSaya.setLatitude(swipePertama.getLatitude() == null ? defaultLocation.getLatitude() : swipePertama.getLatitude());
                dataAbsenSaya.setCenterLatitude(defaultLocation.getLatitude());
                dataAbsenSaya.setCenterLongitude(defaultLocation.getLongitude());
                dataAbsenSaya.setRadius(defaultLocation.getRadius() == null ? 0 : defaultLocation.getRadius());
                dataAbsenSaya.setDistance(distanceBetween(swipePertama.getLatitude(), swipePertama.getLongitude(), defaultLocation.getLatitude(), defaultLocation.getLongitude()));
            }
            if (swipeTerakhir == null) {
                dataAbsenSaya.setLastCheckOut(null);
            } else {
                dataAbsenSaya.setLastCheckOut(swipeTerakhir.getTime());
                dataAbsenSaya.setLongitudeCheckOut(swipeTerakhir.getLongitude() == null ? defaultLocation.getLongitude() : swipeTerakhir.getLongitude());
                dataAbsenSaya.setLatitudeCheckOut(swipeTerakhir.getLatitude() == null ? defaultLocation.getLatitude() : swipeTerakhir.getLatitude());
                dataAbsenSaya.setCenterLatitude(defaultLocation.getLatitude());
                dataAbsenSaya.setCenterLongitude(defaultLocation.getLongitude());
                dataAbsenSaya.setRadius(defaultLocation.getRadius() == null ? 0 : defaultLocation.getRadius());
                dataAbsenSaya.setDistanceCheckOut(distanceBetween(swipeTerakhir.getLatitude(), swipeTerakhir.getLongitude(), defaultLocation.getLatitude(), defaultLocation.getLongitude()));
            }
            Jamkerja jamKerja = cachedJamKerja.computeIfAbsent(date, tgl -> jamkerjaRepository.findFirstByTanggal(tgl));
            if (jamKerja != null) {
                dataAbsenSaya.setJamMasuk(jamKerja.getJamMasuk());
            } else {
                dataAbsenSaya.setJamMasuk(DataAbsenSaya.WORK_HOUR_BEGIN);
            }
            HariLibur hariLibur = cachedHariLibur.computeIfAbsent(date, tgl -> hariliburRepository.findFirstByTanggal(tgl));
            if (hariLibur != null) {
                dataAbsenSaya.setStatus(DataAbsenSaya.STATUS_LIBUR);
                dataAbsenSaya.setKeterangan(hariLibur.getKeterangan());
            }
            PendingSwipeRequest pendingSwipeRequest = pendingSwipeRequestRepository.findFirstByEmployeeCodeAndDateAndCheckout(employeeCode, date, false);
            if (pendingSwipeRequest != null) {
                telahCheckIn = true;
                dataAbsenSaya.setApproved(pendingSwipeRequest.getApproved());
                dataAbsenSaya.setKeterangan(pendingSwipeRequest.getReason());
                if (pendingSwipeRequest.getApproved() != 1) {
                    dataAbsenSaya.setFirstCheckIn(pendingSwipeRequest.getDateTime().toLocalTime());
                    dataAbsenSaya.setLongitude(pendingSwipeRequest.getLongitude());
                    dataAbsenSaya.setLatitude(pendingSwipeRequest.getLatitude());
                    dataAbsenSaya.setCenterLongitude(pendingSwipeRequest.getCenterLongitude());
                    dataAbsenSaya.setCenterLatitude(pendingSwipeRequest.getCenterLatitude());
                    dataAbsenSaya.setRadius(pendingSwipeRequest.getRadius());
                    dataAbsenSaya.setDistance(distanceBetween(pendingSwipeRequest.getLatitude(), pendingSwipeRequest.getLongitude(), pendingSwipeRequest.getCenterLatitude(), pendingSwipeRequest.getCenterLongitude()));
                    dataAbsenSaya.setRejectReason(pendingSwipeRequest.getRejectReason());
                }
                if (pendingSwipeRequest.getApproved() == 0) {
                    pendingCheckIn = true;
                }
            } else {
                dataAbsenSaya.setApproved(1);
            }
            if (telahCheckIn) {
                pendingSwipeRequest = pendingSwipeRequestRepository.findFirstByEmployeeCodeAndDateAndCheckoutOrderByDateTimeDesc(employeeCode, date, true);
                if (pendingSwipeRequest != null) {
                    telahCheckOut = true;
                    dataAbsenSaya.setApprovedCheckOut(pendingSwipeRequest.getApproved());
                    dataAbsenSaya.setKeteranganCheckOut(pendingSwipeRequest.getReason());
                    dataAbsenSaya.setLastCheckOut(pendingSwipeRequest.getDateTime().toLocalTime());
                    if (pendingSwipeRequest.getApproved() == 0) {
                        pendingCheckOut = true;
                    }
                    if (pendingSwipeRequest.getApproved() != 1) {
                        dataAbsenSaya.setLastCheckOut(pendingSwipeRequest.getDateTime().toLocalTime());
                        dataAbsenSaya.setLongitudeCheckOut(pendingSwipeRequest.getLongitude());
                        dataAbsenSaya.setLatitudeCheckOut(pendingSwipeRequest.getLatitude());
                        dataAbsenSaya.setCenterLongitude(pendingSwipeRequest.getCenterLongitude());
                        dataAbsenSaya.setCenterLatitude(pendingSwipeRequest.getCenterLatitude());
                        dataAbsenSaya.setRadius(pendingSwipeRequest.getRadius());
                        dataAbsenSaya.setDistanceCheckOut(distanceBetween(pendingSwipeRequest.getLatitude(), pendingSwipeRequest.getLongitude(), pendingSwipeRequest.getCenterLatitude(), pendingSwipeRequest.getCenterLongitude()));
                    }
                    dataAbsenSaya.setCheckOutRejectReason(pendingSwipeRequest.getRejectReason());
                } else {
                    dataAbsenSaya.setApprovedCheckOut(1);
                }
            }
            if (hariLibur == null) {
                if (!telahCheckIn) {
                    dataAbsenSaya.setStatus(DataAbsenSaya.STATUS_TANPA_KETERANGAN);
                } else {
                    if (pendingCheckIn) {
                        dataAbsenSaya.setStatus(DataAbsenSaya.STATUS_MENUNGGU_PERSETUJUAN);
                    } else {
                        if (telahCheckOut && pendingCheckOut) {
                            dataAbsenSaya.setStatus(DataAbsenSaya.STATUS_MENUNGGU_PERSETUJUAN);
                        } else if (dataAbsenSaya.getStatus() == null) {
                            dataAbsenSaya.setStatus(DataAbsenSaya.STATUS_HADIR_SESUAI_KETENTUAN);
                        }
                    }
                }
            }
            result.add(dataAbsenSaya);
        }
        return result;
    }

    public AttendanceDataResponse getAttendanceData(String employeeCode, LocalDate now) {
        Employee employee = employeeRepository.findByEmployeeCode(employeeCode);
        if (employee == null) {
            throw new RuntimeException("EMPLOYEE_NOT_FOUND");
        }
        LokasiAbsen defaultLocation = employee.getLokasiAbsenId();
        if (defaultLocation == null) {
            defaultLocation = lokasiAbsenRepository.findFirstByIsDefault(true);
        }
        if (defaultLocation == null) {
            throw new RuntimeException("DEFAULT_NOT_SET");
        }
        List<Mesinabsen> mesinabsenList = mesinabsenRepository.findAllByPersonIdAndDate(employee.getPersonIdMesinAbsen(), now);
        Mesinabsen swipePertama = null;
        Mesinabsen swipeTerakhir = null;
        for (Mesinabsen mesinabsen : mesinabsenList) {
            if (swipePertama == null || mesinabsen.getTime().isBefore(swipePertama.getTime())) {
                swipePertama = mesinabsen;
            }
            if (swipeTerakhir == null || mesinabsen.getTime().isAfter(swipeTerakhir.getTime())) {
                swipeTerakhir = mesinabsen;
            }
        }
        if (swipePertama != null && swipePertama.equals(swipeTerakhir)) {
            swipeTerakhir = null;
        }
        if (mesinabsenList.isEmpty()) {
            // cek apakah hari libur
            HariLibur hariLibur = hariliburRepository.findFirstByTanggal(now);
            if (hariLibur != null || now.getDayOfWeek() == DayOfWeek.SUNDAY || now.getDayOfWeek() == DayOfWeek.SATURDAY) {
                AttendanceDataResponse response = new AttendanceDataResponse();
                response.setMessage("LIBUR");
                response.setCenterLatitude(defaultLocation.getLatitude() == null ? 0 : defaultLocation.getLatitude());
                response.setCenterLongitude(defaultLocation.getLongitude() == null ? 0 : defaultLocation.getLongitude());
                response.setRadius(defaultLocation.getRadius() == null ? 0 : defaultLocation.getRadius());
                if (hariLibur != null) {
                    response.setKeterangan(hariLibur.getKeterangan());
                }
                return response;
            }
        } else if (mesinabsenList.size() == 1) {
            swipeTerakhir = null;
        }
        AttendanceDataResponse response = new AttendanceDataResponse();
        response.setCheckIn(swipePertama == null ? null : swipePertama.getTime());
        response.setCheckOut(swipeTerakhir == null ? null : swipeTerakhir.getTime());
        if (swipePertama == null) {
            response.setLatitude(0D);
            response.setLongitude(0D);
        } else {
            Double latitude = swipePertama.getLatitude();
            Double longitude = swipePertama.getLongitude();
            if (latitude == null || longitude == null) {
                response.setLatitude(defaultLocation.getLatitude());
                response.setLongitude(defaultLocation.getLongitude());
            } else {
                response.setLatitude(latitude);
                response.setLongitude(longitude);
            }
        }
        PendingSwipeRequest pendingSwipeRequest = pendingSwipeRequestRepository.findFirstByEmployeeCodeAndDateAndCheckout(employeeCode, now, false);
        PendingSwipeRequest pendingCheckOut = pendingSwipeRequestRepository.findFirstByEmployeeCodeAndDateAndCheckoutOrderByDateTimeDesc(employeeCode, now, true);
        if (pendingSwipeRequest != null) {
            if (pendingSwipeRequest.getApproved() == 0 || swipePertama == null) {
                response.setCheckIn(pendingSwipeRequest.getDateTime().toLocalTime());
                response.setLatitude(pendingSwipeRequest.getLatitude());
                response.setLongitude(pendingSwipeRequest.getLongitude());
            }
            response.setImageUrl(pendingSwipeRequest.getImageUrl());
            response.setApproved(pendingSwipeRequest.getApproved());
            response.setKeterangan(pendingSwipeRequest.getReason());
            Employee reviewer = employeeRepository.findByEmployeeCode(pendingSwipeRequest.getAssignedReviewerEmployeeCode());
            if (reviewer != null) {
                response.setReviewerEmployeeCode(reviewer.getEmployeeCode());
                response.setReviewerEmployeeName(reviewer.getFullName());
                response.setRejectReason(pendingSwipeRequest.getRejectReason());
            }
        } else {
            response.setApproved(swipePertama == null ? 0 : 1);
        }
        if (pendingCheckOut != null) {
            if (pendingCheckOut.getApproved() == 0 || swipeTerakhir == null) {
                response.setCheckOut(pendingCheckOut.getDateTime().toLocalTime());
                response.setLatitudeCheckOut(pendingCheckOut.getLatitude());
                response.setLongitudeCheckOut(pendingCheckOut.getLongitude());
            }
            response.setImageUrlCheckOut(pendingCheckOut.getImageUrl());
            response.setApprovedCheckOut(pendingCheckOut.getApproved());
            response.setKeteranganCheckOut(pendingCheckOut.getReason());
            Employee reviewer = employeeRepository.findByEmployeeCode(pendingCheckOut.getAssignedReviewerEmployeeCode());
            if (reviewer != null) {
                response.setReviewerCheckOutEmployeeCode(reviewer.getEmployeeCode());
                response.setReviewerCheckOutEmployeeName(reviewer.getFullName());
                response.setCheckOutRejectReason(pendingCheckOut.getRejectReason());
            }
        } else {
            response.setApprovedCheckOut(swipeTerakhir == null ? 0 : 1);
        }
        Jamkerja jamKerja = jamkerjaRepository.findFirstByTanggal(now);
        if (jamKerja != null) {
            response.setJamMasuk(jamKerja.getJamMasuk());
            response.setJamKeluar(jamKerja.getJamKeluar());
        } else {
            response.setJamMasuk(DataAbsenSaya.WORK_HOUR_BEGIN);
            response.setJamKeluar(DataAbsenSaya.WORK_HOUR_END);
        }
        response.setCenterLatitude(defaultLocation.getLatitude() == null ? 0 : defaultLocation.getLatitude());
        response.setCenterLongitude(defaultLocation.getLongitude() == null ? 0 : defaultLocation.getLongitude());
        response.setRadius(defaultLocation.getRadius() == null ? 0 : defaultLocation.getRadius());
        response.setMessage("SUCCESS");
        return response;
    }

    // where from <= x <= to
    public List<AttendanceData> getHistory(String employeeCode, LocalDate from, LocalDate to) {
        if (to == null) {
            // until now
            to = LocalDate.now();
        }
        if (from == null) {
            from = to.minusDays(6);
        }
        // max per fetch is 1 week
//        if (to.isAfter(from.plusDays(6))) {
//            throw new RuntimeException("MAX_DATE_RANGE_7_DAYS");
//        }
        // check if date range is in first month
        LocalDate now = LocalDate.now();
        // splits into 2 ranges if date range is in 2 months (only if from or to is not in the same month as now)
        if (from.getMonthValue() != to.getMonthValue() && (from.getMonthValue() == now.getMonthValue() || to.getMonthValue() == now.getMonthValue())) {
            LocalDate to1 = LocalDate.of(from.getYear(), from.getMonth(), from.lengthOfMonth());
            LocalDate from2 = LocalDate.of(to.getYear(), to.getMonth(), 1);
            List<AttendanceData> result = new ArrayList<>();
            result.addAll(getHistory(employeeCode, from, to1));
            result.addAll(getHistory(employeeCode, from2, to));
            return result;
        } else if (from.getMonthValue() == now.getMonthValue()) {
            return getThisMonthsHistory(employeeCode, from.getDayOfMonth(), to.getDayOfMonth());
        }
        Map<LocalDate, HariLibur> cachedHariLibur = new HashMap<>();
        Map<LocalDate, Jamkerja> cachedJamKerja = new HashMap<>();
        List<Rekapabsen> rekapabsenList = rekapabsenRepository.findByEmployeeCodeAndTanggalBetween(employeeCode, from, to);
        Employee employee = employeeRepository.findByEmployeeCode(employeeCode);
        if (employee == null) {
            return null;
        }
        LokasiAbsen defaultLocation = employee.getLokasiAbsenId();
        if (defaultLocation == null) {
            defaultLocation = lokasiAbsenRepository.findFirstByIsDefault(true);
        }
        if (defaultLocation == null) {
            throw new RuntimeException("DEFAULT_NOT_SET");
        }
        Map<LocalDate, AttendanceData> dataAbsenSayaMap = new HashMap<>();
        for (Rekapabsen rekapabsen : rekapabsenList) {
            LocalDate tanggal = rekapabsen.getTanggal();
            boolean isSundayOrSaturday = tanggal.getDayOfWeek() == DayOfWeek.SATURDAY || tanggal.getDayOfWeek() == DayOfWeek.SUNDAY;
            if (isSundayOrSaturday) {
                // jika hari sabtu atau minggu, maka tidak perlu diisi
                AttendanceData dataAbsenSaya = getDataKosong(tanggal, DataAbsenSaya.STATUS_LIBUR, "Hari Libur", employeeCode);
                dataAbsenSayaMap.put(tanggal, dataAbsenSaya);
                continue;
            }
//            boolean telahCheckIn = rekapabsen.getJamMasuk() != null;
//            boolean telahCheckOut = rekapabsen.getJamKeluar() != null;
//            boolean pendingCheckIn = false;
//            boolean pendingCheckOut = false;
            HariLibur hariLibur = cachedHariLibur.computeIfAbsent(tanggal, tgl -> hariliburRepository.findFirstByTanggal(tgl));
            Jamkerja jamKerja = cachedJamKerja.computeIfAbsent(tanggal, tgl -> jamkerjaRepository.findFirstByTanggal(tgl));
            AttendanceData dataAbsenSaya = new AttendanceData();
            dataAbsenSaya.setDay(tanggal.getDayOfMonth());
            dataAbsenSaya.setMonth(tanggal.getMonthValue());
            dataAbsenSaya.setYear(tanggal.getYear());
            dataAbsenSaya.setFirstCheckIn(rekapabsen.getJamMasuk());
            dataAbsenSaya.setLastCheckOut(rekapabsen.getJamKeluar());
            dataAbsenSaya.setLongitude(rekapabsen.getLongitude() == null ? defaultLocation.getLongitude() : rekapabsen.getLongitude());
            dataAbsenSaya.setLatitude(rekapabsen.getLatitude() == null ? defaultLocation.getLatitude() : rekapabsen.getLatitude());
            dataAbsenSaya.setCenterLongitude(defaultLocation.getLongitude());
            dataAbsenSaya.setCenterLatitude(defaultLocation.getLatitude());
            dataAbsenSaya.setRadius(defaultLocation.getRadius() == null ? 0 : defaultLocation.getRadius());
            dataAbsenSaya.setDistance(distanceBetween(rekapabsen.getLatitude(), rekapabsen.getLongitude(), defaultLocation.getLatitude(), defaultLocation.getLongitude()));
            if (hariLibur != null) {
                dataAbsenSaya.setStatus(DataAbsenSaya.STATUS_LIBUR);
                dataAbsenSaya.setKeterangan(hariLibur.getKeterangan());
            } else {
                dataAbsenSaya.setKeterangan(rekapabsen.getKeterangan());
                dataAbsenSaya.setStatus(rekapabsen.getStatus());
            }
            if (jamKerja != null) {
                dataAbsenSaya.setJamMasuk(jamKerja.getJamMasuk());
            } else {
                dataAbsenSaya.setJamMasuk(DataAbsenSaya.WORK_HOUR_BEGIN);
            }
            PendingSwipeRequest pendingSwipeRequest = pendingSwipeRequestRepository.findFirstByEmployeeCodeAndDateAndCheckout(employeeCode, tanggal, false);
            if (pendingSwipeRequest != null) {
                dataAbsenSaya.setApproved(pendingSwipeRequest.getApproved());
                dataAbsenSaya.setKeterangan(pendingSwipeRequest.getReason());
                dataAbsenSaya.setRejectReason(pendingSwipeRequest.getRejectReason());
            } else {
                dataAbsenSaya.setApproved(1);
            }
            pendingSwipeRequest = pendingSwipeRequestRepository.findFirstByEmployeeCodeAndDateAndCheckout(employeeCode, tanggal, true);
            if (pendingSwipeRequest != null) {
                dataAbsenSaya.setApprovedCheckOut(pendingSwipeRequest.getApproved());
                dataAbsenSaya.setKeteranganCheckOut(pendingSwipeRequest.getReason());
                dataAbsenSaya.setCheckOutRejectReason(pendingSwipeRequest.getRejectReason());
            } else {
                dataAbsenSaya.setApprovedCheckOut(1);
            }
            if (dataAbsenSaya.getApproved() != 1)
            dataAbsenSayaMap.put(tanggal, dataAbsenSaya);
        }
        // isi data absen yang hilang dengan hari libur jika ada
        for (LocalDate date = to; date.isAfter(from) || date.isEqual(from); date = date.minusDays(1)) {
            // cek apakah tanggal ini sudah ada di data absen
            if (!dataAbsenSayaMap.containsKey(date)) {
                boolean isSundayOrSaturday = date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY;
                if (isSundayOrSaturday) {
                    AttendanceData dataAbsenSaya = getDataKosong(date, DataAbsenSaya.STATUS_LIBUR, "Hari Libur", employeeCode);
                    dataAbsenSayaMap.put(date, dataAbsenSaya);
                    continue;
                }
                // cek apakah hari libur
                HariLibur hariLibur = cachedHariLibur.computeIfAbsent(date, tgl -> hariliburRepository.findFirstByTanggal(tgl));
                // jika hari libur, maka tambahkan yang hilang, jika tidak, biarkan
                if (hariLibur != null) {
                    AttendanceData dataAbsenSaya = getDataKosong(date, DataAbsenSaya.STATUS_LIBUR, hariLibur.getKeterangan(), employeeCode);
                    dataAbsenSayaMap.put(date, dataAbsenSaya);
                } else {
                    // tidak hadir
                    AttendanceData dataAbsenSaya = getDataKosong(date, DataAbsenSaya.STATUS_TANPA_KETERANGAN, "Tidak Hadir", employeeCode);
                    dataAbsenSayaMap.put(date, dataAbsenSaya);
                }
            }
        }
        return new ArrayList<>(dataAbsenSayaMap.values());
    }

    @NotNull
    private AttendanceData getDataKosong(LocalDate date, String status, String keterangan, String employeeCode) {
        AttendanceData dataAbsenSaya = new AttendanceData();
        dataAbsenSaya.setDay(date.getDayOfMonth());
        dataAbsenSaya.setMonth(date.getMonthValue());
        dataAbsenSaya.setYear(date.getYear());
        dataAbsenSaya.setStatus(status);
        dataAbsenSaya.setKeterangan(keterangan);
        dataAbsenSaya.setJamMasuk(DataAbsenSaya.WORK_HOUR_BEGIN);
        if (!Objects.equals(status, DataAbsenSaya.STATUS_LIBUR)) {
            PendingSwipeRequest pendingSwipeRequest = pendingSwipeRequestRepository.findFirstByEmployeeCodeAndDateAndCheckout(employeeCode, date, false);
            if (pendingSwipeRequest != null) {
                dataAbsenSaya.setApproved(pendingSwipeRequest.getApproved());
                dataAbsenSaya.setKeterangan(pendingSwipeRequest.getReason());
                dataAbsenSaya.setFirstCheckIn(pendingSwipeRequest.getDateTime().toLocalTime());
            } else {
                dataAbsenSaya.setApproved(1);
            }
            pendingSwipeRequest = pendingSwipeRequestRepository.findFirstByEmployeeCodeAndDateAndCheckout(employeeCode, date, true);
            if (pendingSwipeRequest != null) {
                dataAbsenSaya.setApprovedCheckOut(pendingSwipeRequest.getApproved());
                dataAbsenSaya.setKeteranganCheckOut(pendingSwipeRequest.getReason());
            } else {
                dataAbsenSaya.setApprovedCheckOut(1);
            }
        }
        return dataAbsenSaya;
    }


    // END OF KHUSUS MOBILE

    public void rekapAbsen() {
        try {
            System.out.println("Rekap Absen Cakara");
            System.out.println("=========================");
            List<Employee> employeeCakaraList = employeeRepository.findAllByStatus("Cakara (Calon Karyawan)");
            rekapDataAbsen(employeeCakaraList);
            System.out.println("Rekap Absen Pegawai Tetap");
            System.out.println("=========================");
            List<Employee> employeeTetapList = employeeRepository.findAllByStatus("Karyawan Tetap");
            rekapDataAbsen(employeeTetapList);
            System.out.println("Rekap Absen Pegawai KWT");
            System.out.println("=========================");
            List<Employee> employeeKwtList = employeeRepository.findAllByStatus("KWT (Kerja Waktu Tertentu)");
            rekapDataAbsen(employeeKwtList);
        }
        catch (Exception ex){
            System.out.println("Error: "+ex.getMessage());
        }
    }

    private void rekapDataAbsen(List<Employee> employeeList) throws ParseException {
        Date tanggal = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(tanggal);
        int month = c.get(Calendar.MONTH)+1;
        int year = c.get(Calendar.YEAR);
        int day = c.get(Calendar.DATE);
        YearMonth yearMonthObject = YearMonth.of(year, month);
        int daysInMonth = yearMonthObject.lengthOfMonth();
        c.add(Calendar.MONTH, -1);
        int monthsebelum = c.get(Calendar.MONTH)+1;
        int yearsebelum = c.get(Calendar.YEAR);
        YearMonth yearMonthObjectS = YearMonth.of(yearsebelum, monthsebelum);
        int daysInMonthSebelum = yearMonthObjectS.lengthOfMonth();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-d");
        for(Employee employee: employeeList){
            //employee = employeeRepository.findByEmployeeCode("EMP006");
            String personId = employee.getPersonIdMesinAbsen();
            String employeCode = employee.getEmployeeCode();

            //dapetin data di mesin absen dari person id
            List<Mesinabsen> mesinabsenList = mesinabsenRepository.findAllByPersonIdAndIsProses(personId,false);
            if(mesinabsenList != null) {
                //Rekap data bulan ini
                if(day > 0) {
                    //for (int i = 1; i <= day; i++) {
                        int i = 8;
                        String tgl = "";
                        if (i < 10)
                            tgl = year + "-" + month + "-0" + i;
                        else
                            tgl = year + "-" + month + "-" + i;

                        LocalDate tanggalCek = LocalDate.parse(tgl);
                        String status = "TANPA KETERANGAN";

                        Date tmpdate = new SimpleDateFormat("yyyy-MM-dd").parse(tgl);
                        Format f = new SimpleDateFormat("EEEE");
                        String nmhari = f.format(tmpdate);

                        HariLibur hariLibur = hariliburRepository.findByTanggal(tanggalCek);

                        if (!nmhari.equals("Saturday") && !nmhari.equals("Sunday") && hariLibur == null) {
                            //cari data di list
                            List<Mesinabsen> result = mesinabsenList.stream()
                                    .filter(a -> Objects.equals(a.getDate(), tanggalCek))
                                    .collect(Collectors.toList());
                            //cek jam masuk dan jam keluar
                            LocalTime jamMasuk = LocalTime.of(23, 59, 59);
                            LocalTime jamKeluar = LocalTime.of(0, 0, 1);

                            if (result.size() == 0) {
                                jamMasuk = LocalTime.of(0, 0, 0);
                                jamKeluar = LocalTime.of(0, 0, 0);
                            }

                            for (Mesinabsen mesinabsen : result) {
                                int com1 = jamMasuk.compareTo(mesinabsen.getTime());
                                int com2 = jamKeluar.compareTo(mesinabsen.getTime());
                                if (com1 > 0)
                                    jamMasuk = mesinabsen.getTime();
                                if (com2 < 0)
                                    jamKeluar = mesinabsen.getTime();
                            }
                            //cek menit keterlambatan
                            LocalTime batasTerlambat = LocalTime.of(9, 0, 0);
                            int com3 = jamMasuk.compareTo(batasTerlambat);
                            int mntTerlambat = 0;
                            if (com3 > 0) {
                                Duration duration = Duration.between(batasTerlambat, jamMasuk);
                                mntTerlambat = (int) duration.toMinutes();
                                if(mntTerlambat > 0) {
                                    status = "TERLAMBAT";
                                }
                            }
                            //cek menit kurangjam
                            Duration waktuKerja = null;
                            LocalTime mulaiKerja = LocalTime.of(8, 0, 0);
                            LocalTime maksKerja = LocalTime.of(18, 1, 0);
                            LocalTime mulaiPulang = LocalTime.of(17, 1, 0);
                            int cmpMulai = mulaiKerja.compareTo(jamMasuk);
                            LocalTime defaultTime = LocalTime.of(0, 0, 0);
                            int cmpMasukDefault = jamMasuk.compareTo(defaultTime);
                            if (cmpMulai > 0 && cmpMasukDefault != 0) {
                                waktuKerja = Duration.between(mulaiKerja, jamKeluar);
                            } else {
                                waktuKerja = Duration.between(jamMasuk, jamKeluar);
                            }

                            int mntKurangJam = 0;
                            if (waktuKerja.toMinutes() < 540) {
                                if (status.equals("TERLAMBAT")) {
                                    if (waktuKerja.toMinutes() <= 5) {
                                        int cmpPulang = mulaiPulang.compareTo(jamMasuk);
                                        if (cmpPulang > 0) {
                                            status = "TERLAMBAT & LUPA CHECK TIME";
                                        }
                                        else
                                            status = "LUPA CHECK TIME";
                                    } else {
                                        int cmpPulang = maksKerja.compareTo(jamKeluar);
                                        if (cmpPulang > 0) {
                                            status = "TERLAMBAT & KURANG JAM";
                                            mntKurangJam = (int) Duration.between(jamKeluar, maksKerja).toMinutes();
                                        }
                                    }
                                } else {
                                    if (waktuKerja.toSeconds() == 0) {
                                        status = "TANPA KETERANGAN";
                                    } else if (waktuKerja.toMinutes() <= 5) {
                                        status = "LUPA CHECK TIME";
                                    } else {
                                        status = "KURANG JAM";
                                        mntKurangJam = (int) (540 - waktuKerja.toMinutes());
                                    }
                                }
                            }

                            if (mntKurangJam >= 540) {
                                if (waktuKerja.toSeconds() > 5) {
                                    status = "LUPA CHECK TIME";
                                } else {
                                    //LocalTime jam = LocalTime.of(0, 0, 0);
                                    //int cmp1 = jamMasuk.compareTo(jam);
                                    //int cmp2 = jamKeluar.compareTo(jam);
                                    //if(cmp1 != 0 && cmp2 != 0)
                                    status = "TANPA KETERANGAN";
                                }
                            }

                            if (waktuKerja.toMinutes() >= 540 && com3 <= 0) {
                                status = "HADIR SESUAI KETENTUAN";
                            }

                            LocalTime jam = LocalTime.of(0, 0, 0);
                            int cmp1 = jamMasuk.compareTo(jamKeluar);

                            if (cmp1 == 0) {
                                int cmp2 = jamMasuk.compareTo(jam);
                                int cmp3 = jamKeluar.compareTo(jam);

                                if (status.contains("TERLAMBAT")) {
                                    int cmpPulang = mulaiPulang.compareTo(jamMasuk);
                                    if (cmpPulang > 0) {
                                        status = "TERLAMBAT & LUPA CHECK TIME";
                                    }
                                    else
                                        status = "LUPA CHECK TIME";
                                } else if (cmp2 != 0 && cmp3 != 0)
                                    status = "LUPA CHECK TIME";
                            }

                            //cek jika jam masuk lebih dari jam 5
                            int cekJammasuk = mulaiPulang.compareTo(jamMasuk);
                            if(cekJammasuk < 0){
                                status = "LUPA CHECK TIME";
                            }

                            //cek jika hari tersebut ada data di rekap ketidakhadiran
                            Ketidakhadiran ketidakhadiran = ketidakhadiranRepository.findByEmployeeCodeAndTanggal(employeCode, tanggalCek);

                            //cek jika hari tersebut ada data sppd
                            Date tanggalCekDate = Date.from(tanggalCek.atStartOfDay(ZoneId.systemDefault()).toInstant());
                            Sppdnav sppdnav = sppdnavRepository.findByEmployeeCodeAndWaktuSPPD(employeCode,tanggalCekDate);

                            //input data ke tabel rekap
                            Rekapabsen rekapabsen = rekapabsenRepository.findByEmployeeCodeAndTanggal(employeCode, tanggalCek);
                            if (rekapabsen == null)
                                rekapabsen = new Rekapabsen();

                            rekapabsen.setEmployeeCode(employeCode);
                            rekapabsen.setJamMasuk(jamMasuk);
                            rekapabsen.setJamKeluar(jamKeluar);
                            if (ketidakhadiran != null) {
                                String statusSebelum = status;
                                status = ketidakhadiran.getCauseOfAbsence();
                                if (status.equals("CUTI")) {
                                    rekapabsen.setCuti(true);
                                } else if (status.equals("PERJALANAN DINAS")) {
                                    rekapabsen.setSppd(true);
                                } else if (status.equals("SAKIT")) {
                                    rekapabsen.setSakit(true);
                                } else if (status.equals("IZIN PRIBADI")) {
                                    rekapabsen.setIzinPribadi(true);
                                } else if (status.equals("LUPA CHECK IN")) {
                                    mntKurangJam = 0;
                                    if (statusSebelum.equals("TERLAMBAT"))
                                        status = "TERLAMBAT & LUPA CHECK TIME";
                                    else
                                        status = "LUPA CHECK TIME";
                                    rekapabsen.setLupaCheckIn(true);
                                    rekapabsen.setFormCheckin(ketidakhadiran.isSuratLupaCheckin());
                                } else if (status.equals("TUGAS LUAR TANPA SPPD")) {
                                    rekapabsen.setTugasLuarTanpaSppd(true);
                                }
                                rekapabsen.setKeterangan(ketidakhadiran.getDescription());
                                rekapabsen.setFormCheckin(ketidakhadiran.isSuratLupaCheckin());
                                rekapabsen.setWholeDay(ketidakhadiran.isWholeDay());
                                //rekapabsen.setSppd(true);
                                //rekapabsen.setKeterangan("SPPD No: "+sppdnav.getNoSppd());
                                //status = "PERJALANAN DINAS";
                            }

                            if(sppdnav != null){
                                status = "PERJALANAN DINAS";
                                rekapabsen.setKeterangan("No: "+sppdnav.getNoSppd()+" - "+sppdnav.getKeperluan());
                                rekapabsen.setSppd(true);
                            }

                            //rekapabsen.setWholeDay(ketidakhadiran.isWholeDay());
                            rekapabsen.setKurangJam(mntKurangJam);
                            rekapabsen.setTerlambat(mntTerlambat);
                            rekapabsen.setTanggal(tanggalCek);
                            rekapabsen.setStatus(status);
                            rekapabsenRepository.save(rekapabsen);
                            //System.out.println("Successfully insert rekap absen: " + employeCode + " - " + tanggalCek + " ");

                            //update data mesin absen
                        /*for (Mesinabsen mesinabsen : mesinabsenList) {
                            Mesinabsen mesinabsenUpdate = mesinabsenRepository.findAllByPersonIdAndDate(mesinabsen.getPersonId(), mesinabsen.getDate());
                            if (mesinabsenUpdate != null) {
                                mesinabsenUpdate.setProses(true);
                                mesinabsenRepository.save(mesinabsenUpdate);
                            }
                        }*/
                        }
                    //}
                }
            }

        }
    }

    public void pindahData() {
        try{
            List<Dataabsencsv> dataabsencsvList = (List<Dataabsencsv>) dataabsencsvRepository.findAll();
            for(Dataabsencsv dataabsencsv: dataabsencsvList){
                Mesinabsen mesinabsen = new Mesinabsen();
                mesinabsen.setName(dataabsencsv.getName());
                mesinabsen.setDate(dataabsencsv.getDate());
                mesinabsen.setTime(dataabsencsv.getTime());
                mesinabsen.setPersonId(dataabsencsv.getPersonId().substring(1));
                mesinabsen.setDeviceName("Data CSV");
                mesinabsen.setDeviceSn("Spring Boot");
                mesinabsen.setDirection("");
                mesinabsen.setCardNo("");
                mesinabsen.setDateTime(LocalDateTime.of(mesinabsen.getDate(),mesinabsen.getTime()));
                mesinabsenRepository.save(mesinabsen);
                System.out.println("Successfully move data: "+dataabsencsv.getName()+" - "+dataabsencsv.getDate().toString());
                dataabsencsvRepository.delete(dataabsencsv);

            }
        }
        catch (Exception ex){
            System.out.println("Error: "+ex.getMessage());
        }
    }

    public Rekapabsen getTodayRekapAbsen(AbsenSayaRequest req) {
        // karena untuk hari ini belum ada rekapan, maka ambil data dari mesin absen
        // untuk hari ini
        Rekapabsen rekapabsen = new Rekapabsen();
        try {
            Employee employee = employeeRepository.findByEmployeeCode(req.getEmployee_code());
            if (employee == null) {
                return null;
            }
            LokasiAbsen defaultLocation = employee.getLokasiAbsenId();
            if (defaultLocation == null) {
                defaultLocation = lokasiAbsenRepository.findFirstByIsDefault(true);
            }
            if (defaultLocation == null) {
                throw new RuntimeException("DEFAULT_NOT_SET");
            }
            LocalDate now = LocalDate.now();
            List<Mesinabsen> mesinabsenList = mesinabsenRepository.findAllByPersonIdAndDate(employee.getPersonIdMesinAbsen(), now);
            Mesinabsen swipePertama = null;
            Mesinabsen swipeTerakhir = null;
            for (Mesinabsen mesinabsen : mesinabsenList) {
                if (swipePertama == null || mesinabsen.getTime().isBefore(swipePertama.getTime())) {
                    swipePertama = mesinabsen;
                }
                if (swipeTerakhir == null || mesinabsen.getTime().isAfter(swipeTerakhir.getTime())) {
                    swipeTerakhir = mesinabsen;
                }
            }
            if (mesinabsenList.isEmpty()) {
                // cek apakah hari libur
                HariLibur hariLibur = hariliburRepository.findFirstByTanggal(now);
                if (hariLibur != null || now.getDayOfWeek() == DayOfWeek.SUNDAY || now.getDayOfWeek() == DayOfWeek.SATURDAY) {
                    rekapabsen.setStatus("LIBUR");
                    rekapabsen.setKeterangan(hariLibur == null ? null : hariLibur.getKeterangan());
                    rekapabsen.setWholeDay(true);
                    return rekapabsen;
                }
            } else if (mesinabsenList.size() == 1) {
                swipeTerakhir = null;
            }
            rekapabsen.setEmployeeCode(employee.getEmployeeCode());
            rekapabsen.setTanggal(now);
            rekapabsen.setJamMasuk(swipePertama == null ? null : swipePertama.getTime());
            rekapabsen.setJamKeluar(swipeTerakhir == null ? null : swipeTerakhir.getTime());
            rekapabsen.setWholeDay(false);
            rekapabsen.setFormCheckin(false);
            rekapabsen.setLongitude(swipePertama == null ? defaultLocation.getLongitude() : swipePertama.getLongitude());
            rekapabsen.setLatitude(swipePertama == null ? defaultLocation.getLatitude() : swipePertama.getLatitude());
            rekapabsen.setStatus("HADIR");
            rekapabsenRepository.save(rekapabsen);
        } catch (Exception ex) {
            rekapabsen.setStatus("ERROR: " + ex.getMessage());
        }
        return rekapabsen;
    }

    public AbsenSayaResponse getAbsenSaya(AbsenSayaRequest req) {
        AbsenSayaResponse res = new AbsenSayaResponse();
        try{
            Employee employee = employeeRepository.findByEmployeeCode(req.getEmployee_code());
            if (employee == null) {
                return null;
            }
            LokasiAbsen defaultLocation = employee.getLokasiAbsenId();
            if (defaultLocation == null) {
                defaultLocation = lokasiAbsenRepository.findFirstByIsDefault(true);
            }
            if (defaultLocation == null) {
                throw new RuntimeException("DEFAULT_NOT_SET");
            }
            List<Rekapabsen> rekapabsenList = rekapabsenRepository.findByYearAndMonthAndEmployeeCode(req.getYear(), req.getMonth(), req.getEmployee_code());
            res.setMsg("SUCCESS");
            res.setEmployee_code(employee.getEmployeeCode());
            res.setNama(employee.getFullName());
            res.setJumlah_data(rekapabsenList.size());
            res.setTahun(req.getYear());
            res.setBulan(req.getMonth());
            List<DataAbsenSaya> dataAbsenSayaList = new ArrayList<>();
//            LocalDate now = LocalDate.now();
//            boolean hasToday = rekapabsenList.stream().anyMatch(a -> a.getTanggal().isEqual(now));
//            if (!hasToday) {
//                Rekapabsen todayRekap = getTodayRekapAbsen(req);
//                if (todayRekap != null) {
//                    rekapabsenList.add(todayRekap);
//                }
//            }
            for(Rekapabsen rekapabsen:rekapabsenList){
                DataAbsenSaya dataAbsenSaya = new DataAbsenSaya();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                dataAbsenSaya.setTanggal(rekapabsen.getTanggal().format(formatter));
                dataAbsenSaya.setHari(getNamaHari(rekapabsen.getTanggal()));
                dataAbsenSaya.setJam_masuk(rekapabsen.getJamMasuk().toString());
                dataAbsenSaya.setJam_keluar(rekapabsen.getJamKeluar().toString());
                dataAbsenSaya.setStatus(rekapabsen.getStatus());
                dataAbsenSaya.setKeterangan(rekapabsen.getKeterangan());
                dataAbsenSaya.set_cuti(rekapabsen.isCuti());
                dataAbsenSaya.set_izin(rekapabsen.isIzinPribadi());
                dataAbsenSaya.set_sakit(rekapabsen.isSakit());
                dataAbsenSaya.set_sppd(rekapabsen.isSppd());
                dataAbsenSaya.set_tugas(rekapabsen.isTugasLuarTanpaSppd());
                dataAbsenSaya.setKurang_jam(rekapabsen.getKurangJam());
                dataAbsenSaya.setTerlambat(rekapabsen.getTerlambat());
                dataAbsenSaya.set_whole_day(rekapabsen.isWholeDay());
                dataAbsenSaya.set_form_chekin(rekapabsen.isFormCheckin());

                // KHUSUS MOBILE
//                Double longitude = rekapabsen.getLongitude();
//                Double latitude = rekapabsen.getLatitude();
//                if (longitude == null || latitude == null) {
//                    // calculate distance from default location
//                    longitude = defaultLocation.getLongitude();
//                    latitude = defaultLocation.getLatitude();
//                    if (longitude == null || latitude == null) {
//                        throw new RuntimeException("DEFAULT_NOT_SET");
//                    }
//                    dataAbsenSaya.setLongitude(longitude);
//                    dataAbsenSaya.setLatitude(latitude);
//                }
                // END OF KHUSUS MOBILE

                dataAbsenSayaList.add(dataAbsenSaya);
            }
            res.setData(dataAbsenSayaList);

            Hitungabsen hitungabsen = hitungabsenRepository.findByTahunAndBulanAndEmployeeCode(req.getYear(),req.getMonth(),req.getEmployee_code());
            if(hitungabsen != null) {
                res.setTotal_terlambat(hitungabsen.getTotalTerlambat());
                res.setTotal_izin_menit(hitungabsen.getTotalIzin());
                res.setTotal_kurang_jam(hitungabsen.getTotalKurangJam());
                res.setTotal_lupa_check(hitungabsen.getTotalLupaCheck());
                res.setTotal_form_lupa(hitungabsen.getTotalFormLupa());
                res.setTotal_tanpa_keterangan(hitungabsen.getTotalTanpaKeterangan());
                res.setTotal_kurang_kerja(hitungabsen.getTotalKurangKerja());
                res.setTotal_izin_hari(hitungabsen.getTotalIzinHari());
                res.setEmployee_code(hitungabsen.getEmployeeCode());
            }
        }
        catch (Exception ex){
            res.setMsg("ERROR: "+ex.getMessage());
        }

        return res;
    }

    public RekapAbsenResponse getRekapAbsen(int year, int month) {
        RekapAbsenResponse res = new RekapAbsenResponse();
        List<DataRekapAbsen> dataRekapAbsenList = new ArrayList<>();
        try{
            List<Hitungabsen> hitungabsenList = hitungabsenRepository.findByTahunAndBulan(year,month);
            for(Hitungabsen hitungabsen:hitungabsenList){
                Employee employee = employeeRepository.findByEmployeeCode(hitungabsen.getEmployeeCode());
                if(employee != null) {
                    DataRekapAbsen dataRekapAbsen = new DataRekapAbsen();
                    dataRekapAbsen.setEmployee_code(employee.getEmployeeCode());
                    dataRekapAbsen.setNik(employee.getNik());
                    dataRekapAbsen.setName(employee.getFullName());
                    dataRekapAbsen.setStatus(employee.getStatus());
                    dataRekapAbsen.setEmail(employee.getEmail());
                    dataRekapAbsen.setJumlah_cuti_awal(employee.getSisaCuti().intValue());
                    dataRekapAbsen.setYear(hitungabsen.getTahun());
                    dataRekapAbsen.setMonth(hitungabsen.getBulan());
                    dataRekapAbsen.setJumlah_sisa_cuti(hitungabsen.getSisaCuti());
                    dataRekapAbsen.setJumlah_lupa(hitungabsen.getTotalLupaCheck());
                    dataRekapAbsen.setJumlah_form_lupa(hitungabsen.getTotalFormLupa());
                    dataRekapAbsen.setJumlah_terlambat(hitungabsen.getTotalTerlambat());
                    dataRekapAbsen.setJumlah_izin(hitungabsen.getTotalIzin());
                    dataRekapAbsen.setJumlah_cuti(hitungabsen.getTotalCuti());
                    dataRekapAbsen.setJumlah_tanpa_keterangan(hitungabsen.getTotalTanpaKeterangan());
                    dataRekapAbsen.setJumlah_izin_hari(hitungabsen.getTotalIzinHari());
                    dataRekapAbsen.setJumlah_sakit(hitungabsen.getTotalSakit());
                    dataRekapAbsen.setJumlah_cuti_terpakai(hitungabsen.getJatahCutiTerpakai());
                    dataRekapAbsen.setThp(hitungabsen.getThp());
                    dataRekapAbsen.setPemotong_harian(hitungabsen.getPemotongHarian());
                    dataRekapAbsen.setPotongan_lupa_chekin(hitungabsen.getPotonganLupa());
                    dataRekapAbsen.setPotongan_tanpa_keterangan(hitungabsen.getPotonganAlpha());
                    dataRekapAbsen.setPotongan_kurang_jam(hitungabsen.getPotonganKurangJam());
                    dataRekapAbsen.setPotongan_terlambat(hitungabsen.getPotonganTerlambat());
                    dataRekapAbsen.setPotongan_izin(hitungabsen.getPotonganIzin());
                    dataRekapAbsen.setTotal_potongan(hitungabsen.getTotalPotongan());
                    dataRekapAbsen.setJumlah_kurang_jam(hitungabsen.getTotalKurangJam());
                    dataRekapAbsenList.add(dataRekapAbsen);
                }
            }
            res.setMsg("SUCCESS");
            res.setCount(hitungabsenList.size());
            res.setData(dataRekapAbsenList);
        }
        catch (Exception ex){
            res.setMsg("ERROR: "+ex.getMessage());
        }

        return res;
    }

    public MessageResponse hitungAbsen(int year, int month, String emp) {
        MessageResponse res = new MessageResponse();

        try{
            Employee employee = employeeRepository.findByEmployeeCode(emp);
            String employeeCode = employee.getEmployeeCode();
            float thp = employee.getThp().floatValue();
            int sisaCuti = employee.getSisaCuti().intValue(), totalCuti = 0;
            float potonganHarian = thp/22;
            int totalTerlambat = 0, totalIzin = 0, totalKurangJam = 0, totalAlpha = 0, totalLupaChek = 0, totalFormCheck = 0, cutiTerpakai = 0;
            float potonganTerlambat = 0, potonganIzin = 0, potonganCheck = 0, potonganAlpha = 0, totalPotongan = 0;
            int totalIzinHari = 0, totalSakit = 0, totalKurangKerja = 0;
            //employeeCode = "EMP005";
            List<Rekapabsen> rekapabsenList = rekapabsenRepository.findByYearAndMonthAndEmployeeCode(year,month,employeeCode);

            Hitungabsen hitungabsen = hitungabsenRepository.findByEmployeeCodeAndTahunAndBulan(employeeCode,year,month);
            if(hitungabsen == null){
                hitungabsen = new Hitungabsen();
                hitungabsen.setEmployeeCode(employeeCode);
                hitungabsen.setTahun(year);
                hitungabsen.setBulan(month);
                hitungabsen.setThp(thp);
                //hitungabsen.setSisaCuti(sisaCuti);
                hitungabsen.setPemotongHarian(potonganHarian);
            }
            else{
                //sisaCuti = hitungabsen.getSisaCuti();
                //potonganHarian = hitungabsen.getPemotongHarian();

                //sementara
                hitungabsen.setThp(thp);
                //hitungabsen.setSisaCuti(sisaCuti);
                hitungabsen.setPemotongHarian(potonganHarian);
            }

            //dapetin data cuti dari perhitungan bulan sebelumnya
            if(month != 1){
                int monthSebelum = month -1;
                Hitungabsen hitungabsenBulanSebelum = hitungabsenRepository.findByEmployeeCodeAndTahunAndBulan(employeeCode,year,monthSebelum);
                if(hitungabsenBulanSebelum != null){
                    sisaCuti = hitungabsenBulanSebelum.getSisaCuti();
                }
            }

            for(Rekapabsen rekapabsen: rekapabsenList){
                String status = rekapabsen.getStatus();
                if(!status.equals("HADIR SESUAI KETENTUAN") && !status.equals("SAKIT")
                        && !status.equals("PERJALANAN DINAS") && !status.equals("TUGAS LUAR TANPA SPPD")){
                    boolean isCuti = rekapabsen.isCuti();
                    boolean isFormCheckin = rekapabsen.isFormCheckin();
                    boolean isIzin = rekapabsen.isIzinPribadi();
                    boolean isLupa = rekapabsen.isLupaCheckIn();
                    boolean isSppd = rekapabsen.isSppd();
                    boolean isTugas = rekapabsen.isTugasLuarTanpaSppd();

                    if(status.equals("TANPA KETERANGAN")){
                        if(!isCuti && !isFormCheckin && !isIzin && !isLupa && !isSppd && !isTugas){
                            totalAlpha++;
                        }
                    } else if (status.equals("LUPA CHECK TIME")) {
                        totalLupaChek++;
                        if(isFormCheckin){
                            totalFormCheck++;
                        }
                    } else if (status.equals("TERLAMBAT")) {
                        totalTerlambat+= rekapabsen.getTerlambat();
                    } else if (status.equals("TERLAMBAT & LUPA CHECK TIME")) {
                        totalTerlambat+= rekapabsen.getTerlambat();
                        totalLupaChek++;
                        if(isFormCheckin){
                            totalFormCheck++;
                        }
                    } else if (status.equals("KURANG JAM")) {
                        totalKurangJam+=rekapabsen.getKurangJam();
                        totalKurangKerja+=rekapabsen.getKurangJam();
                    } else if (status.equals("TERLAMBAT & KURANG JAM")) {
                        totalTerlambat+= rekapabsen.getTerlambat();
                        totalKurangJam+=rekapabsen.getKurangJam();
                        totalKurangKerja+=rekapabsen.getKurangJam();
                    } else if (status.equals("CUTI")) {
                        cutiTerpakai++;
                        totalCuti++;
                    } else if (status.equals("IZIN PRIBADI")) {
                        if(rekapabsen.isWholeDay()){
                            cutiTerpakai++;
                            totalIzinHari++;
                        }else {
                            LocalTime jamMasuk = rekapabsen.getJamMasuk();
                            LocalTime jamKeluar = rekapabsen.getJamKeluar();
                            Duration waktuKerja =Duration.between(jamMasuk,jamKeluar);
                            if(waktuKerja.toMinutes() <= 5){
                                cutiTerpakai++;
                                totalIzinHari++;
                            }
                            else {
                                totalIzin += rekapabsen.getKurangJam() + rekapabsen.getTerlambat();
                                totalKurangKerja += rekapabsen.getKurangJam();
                                totalTerlambat += rekapabsen.getTerlambat();
                            }
                        }
                    } else if (status.equals("SAKIT")) {
                        totalSakit++;
                    }
                }
            }
            float jamTerlambatLebih = ((totalTerlambat + totalKurangKerja) / 60) - 8;
            if(jamTerlambatLebih > 0)
                potonganTerlambat = (jamTerlambatLebih / 8) * potonganHarian;
            potonganCheck = (totalLupaChek - totalFormCheck) * potonganHarian;
            potonganAlpha = totalAlpha * potonganHarian;
            int sisaCutiSetelahHitung = 0;
            if(cutiTerpakai > sisaCuti){
                sisaCutiSetelahHitung = 0;
                potonganIzin = (cutiTerpakai - sisaCuti) * potonganHarian;
            }
            else{
                sisaCutiSetelahHitung = sisaCuti - cutiTerpakai;
            }
            totalPotongan = potonganTerlambat + potonganCheck + potonganAlpha + potonganIzin;

            hitungabsen.setTotalTerlambat(totalTerlambat);
            hitungabsen.setTotalKurangJam(totalKurangJam);
            hitungabsen.setTotalKurangKerja(totalKurangKerja);
            hitungabsen.setTotalTanpaKeterangan(totalAlpha);
            hitungabsen.setTotalIzin(totalIzin);
            hitungabsen.setTotalLupaCheck(totalLupaChek);
            hitungabsen.setTotalFormLupa(totalFormCheck);
            hitungabsen.setTotalCuti(totalCuti);
            hitungabsen.setPotonganLupa(potonganCheck);
            hitungabsen.setPotonganTerlambat(potonganTerlambat);
            hitungabsen.setPotonganAlpha(potonganAlpha);
            hitungabsen.setJatahCutiTerpakai(cutiTerpakai);
            hitungabsen.setPotonganIzin(potonganIzin);
            hitungabsen.setTotalPotongan(totalPotongan);
            hitungabsen.setTotalIzinHari(totalIzinHari);
            hitungabsen.setTotalSakit(totalSakit);
            hitungabsen.setSisaCuti(sisaCutiSetelahHitung);
            hitungabsenRepository.save(hitungabsen);
            res.setMsg("SUCCESS");
        }
        catch (Exception ex){
            res.setMsg("ERROR: "+ex.getMessage());
        }

        return res;
    }

    public MessageResponse rekapAbsenEmp(int year, int month, int day, String emp) {
        MessageResponse res = new MessageResponse();

        try{
            Employee employee = employeeRepository.findByEmployeeCode(emp);
            String personId = employee.getPersonIdMesinAbsen();
            String employeCode = employee.getEmployeeCode();

            //dapetin data di mesin absen dari person id
            List<Mesinabsen> mesinabsenList = mesinabsenRepository.findAllByPersonIdAndIsProses(personId,false);
            String bulan = "";
            if(month < 10)
                bulan = "0"+month;
            else
                bulan = ""+month;
            if(mesinabsenList != null) {
                //Rekap data bulan ini
                if(day > 0) {
                    //for (int i = 1; i <= day; i++) {
                        int i = day;
                        String tgl = "";
                        if (i < 10)
                            tgl = year + "-" + bulan + "-0" + i;
                        else
                            tgl = year + "-" + bulan + "-" + i;

                        LocalDate tanggalCek = LocalDate.parse(tgl);
                        String status = "TANPA KETERANGAN";

                        Date tmpdate = new SimpleDateFormat("yyyy-MM-dd").parse(tgl);
                        Format f = new SimpleDateFormat("EEEE");
                        String nmhari = f.format(tmpdate);

                        HariLibur hariLibur = hariliburRepository.findByTanggal(tanggalCek);

                        if (!nmhari.equals("Saturday") && !nmhari.equals("Sunday") && hariLibur == null) {
                            //cari data di list
                            List<Mesinabsen> result = mesinabsenList.stream()
                                    .filter(a -> Objects.equals(a.getDate(), tanggalCek))
                                    .collect(Collectors.toList());
                            //cek jam masuk dan jam keluar
                            LocalTime jamMasuk = LocalTime.of(23, 59, 59);
                            LocalTime jamKeluar = LocalTime.of(0, 0, 1);

                            if (result.size() == 0) {
                                jamMasuk = LocalTime.of(0, 0, 0);
                                jamKeluar = LocalTime.of(0, 0, 0);
                            }

                            for (Mesinabsen mesinabsen : result) {
                                int com1 = jamMasuk.compareTo(mesinabsen.getTime());
                                int com2 = jamKeluar.compareTo(mesinabsen.getTime());
                                if (com1 > 0)
                                    jamMasuk = mesinabsen.getTime();
                                if (com2 < 0)
                                    jamKeluar = mesinabsen.getTime();
                            }
                            //cek menit keterlambatan
                            LocalTime batasTerlambat = LocalTime.of(9, 0, 0);
                            int com3 = jamMasuk.compareTo(batasTerlambat);
                            int mntTerlambat = 0;
                            if (com3 > 0) {
                                Duration duration = Duration.between(batasTerlambat, jamMasuk);
                                mntTerlambat = (int) duration.toMinutes();
                                if(mntTerlambat > 0)
                                    status = "TERLAMBAT";
                            }
                            //cek menit kurangjam
                            Duration waktuKerja = null;
                            LocalTime mulaiKerja = LocalTime.of(8, 0, 0);
                            LocalTime maksKerja = LocalTime.of(18, 1, 0);
                            LocalTime mulaiPulang = LocalTime.of(17, 1, 0);
                            int cmpMulai = mulaiKerja.compareTo(jamMasuk);
                            LocalTime defaultTime = LocalTime.of(0, 0, 0);
                            int cmpMasukDefault = jamMasuk.compareTo(defaultTime);
                            if (cmpMulai > 0 && cmpMasukDefault != 0) {
                                waktuKerja = Duration.between(mulaiKerja, jamKeluar);
                            } else {
                                waktuKerja = Duration.between(jamMasuk, jamKeluar);
                            }

                            int mntKurangJam = 0;
                            if (waktuKerja.toMinutes() < 540) {
                                if (status.equals("TERLAMBAT")) {
                                    if (waktuKerja.toMinutes() <= 5) {
                                        int cmpPulang = mulaiPulang.compareTo(jamMasuk);
                                        if (cmpPulang > 0)
                                            status = "TERLAMBAT & LUPA CHECK TIME";
                                        else
                                            status = "LUPA CHECK TIME";
                                    } else {
                                        int cmpPulang = maksKerja.compareTo(jamKeluar);
                                        if (cmpPulang > 0) {
                                            status = "TERLAMBAT & KURANG JAM";
                                            mntKurangJam = (int) Duration.between(jamKeluar, maksKerja).toMinutes();
                                        }
                                    }
                                } else {
                                    if (waktuKerja.toSeconds() == 0) {
                                        status = "TANPA KETERANGAN";
                                    } else if (waktuKerja.toMinutes() <= 5) {
                                        status = "LUPA CHECK TIME";
                                    } else {
                                        status = "KURANG JAM";
                                        mntKurangJam = (int) (540 - waktuKerja.toMinutes());
                                    }
                                }
                            }

                            if (mntKurangJam >= 540) {
                                if (waktuKerja.toSeconds() > 5) {
                                    status = "LUPA CHECK TIME";
                                } else {
                                    //LocalTime jam = LocalTime.of(0, 0, 0);
                                    //int cmp1 = jamMasuk.compareTo(jam);
                                    //int cmp2 = jamKeluar.compareTo(jam);
                                    //if(cmp1 != 0 && cmp2 != 0)
                                    status = "TANPA KETERANGAN";
                                }
                            }

                            if (waktuKerja.toMinutes() >= 540 && mntTerlambat <= 1) {
                                status = "HADIR SESUAI KETENTUAN";
                            }

                            LocalTime jam = LocalTime.of(0, 0, 0);
                            int cmp1 = jamMasuk.compareTo(jamKeluar);

                            if (cmp1 == 0) {
                                int cmp2 = jamMasuk.compareTo(jam);
                                int cmp3 = jamKeluar.compareTo(jam);

                                if (status.contains("TERLAMBAT")) {
                                    int cmpPulang = mulaiPulang.compareTo(jamMasuk);
                                    if (cmpPulang > 0)
                                        status = "TERLAMBAT & LUPA CHECK TIME";
                                    else
                                        status = "LUPA CHECK TIME";
                                } else if (cmp2 != 0 && cmp3 != 0)
                                    status = "LUPA CHECK TIME";
                            }

                            //cek jika jam masuk lebih dari jam 5
                            int cekJammasuk = mulaiPulang.compareTo(jamMasuk);
                            if(cekJammasuk < 0){
                                status = "LUPA CHECK TIME";
                            }

                            //cek jika hari tersebut ada data di rekap ketidakhadiran
                            Ketidakhadiran ketidakhadiran = ketidakhadiranRepository.findByEmployeeCodeAndTanggal(employeCode, tanggalCek);

                            //cek jika hari tersebut ada data sppd
                            Date tanggalCekDate = Date.from(tanggalCek.atStartOfDay(ZoneId.systemDefault()).toInstant());
                            Sppdnav sppdnav = sppdnavRepository.findByEmployeeCodeAndWaktuSPPD(employeCode,tanggalCekDate);

                            //input data ke tabel rekap
                            Rekapabsen rekapabsen = rekapabsenRepository.findByEmployeeCodeAndTanggal(employeCode, tanggalCek);
                            if (rekapabsen == null)
                                rekapabsen = new Rekapabsen();

                            rekapabsen.setEmployeeCode(employeCode);
                            rekapabsen.setJamMasuk(jamMasuk);
                            rekapabsen.setJamKeluar(jamKeluar);
                            if (ketidakhadiran != null) {
                                String statusSebelum = status;
                                status = ketidakhadiran.getCauseOfAbsence();
                                if (status.equals("CUTI")) {
                                    rekapabsen.setCuti(true);
                                } else if (status.equals("PERJALANAN DINAS")) {
                                    rekapabsen.setSppd(true);
                                } else if (status.equals("SAKIT")) {
                                    rekapabsen.setSakit(true);
                                } else if (status.equals("IZIN PRIBADI")) {
                                    rekapabsen.setIzinPribadi(true);
                                } else if (status.equals("LUPA CHECK IN")) {
                                    mntKurangJam = 0;
                                    status = statusSebelum;
                                    rekapabsen.setLupaCheckIn(true);
                                    rekapabsen.setFormCheckin(ketidakhadiran.isSuratLupaCheckin());
                                } else if (status.equals("TUGAS LUAR TANPA SPPD")) {
                                    rekapabsen.setTugasLuarTanpaSppd(true);
                                }
                                rekapabsen.setKeterangan(ketidakhadiran.getDescription());
                                rekapabsen.setFormCheckin(ketidakhadiran.isSuratLupaCheckin());
                                rekapabsen.setWholeDay(ketidakhadiran.isWholeDay());
                                //rekapabsen.setSppd(true);
                                //rekapabsen.setKeterangan("SPPD No: "+sppdnav.getNoSppd());
                                //status = "PERJALANAN DINAS";
                            }

                            if(sppdnav != null){
                                status = "PERJALANAN DINAS";
                                rekapabsen.setKeterangan("No: "+sppdnav.getNoSppd()+" - "+sppdnav.getKeperluan());
                                rekapabsen.setSppd(true);
                            }

                            //rekapabsen.setWholeDay(ketidakhadiran.isWholeDay());
                            rekapabsen.setKurangJam(mntKurangJam);
                            rekapabsen.setTerlambat(mntTerlambat);
                            rekapabsen.setTanggal(tanggalCek);
                            rekapabsen.setStatus(status);
                            rekapabsenRepository.save(rekapabsen);
                            System.out.println("Successfully insert rekap absen: " + employeCode + " - " + tanggalCek + " ");

                            //update data mesin absen
                        /*for (Mesinabsen mesinabsen : mesinabsenList) {
                            Mesinabsen mesinabsenUpdate = mesinabsenRepository.findAllByPersonIdAndDate(mesinabsen.getPersonId(), mesinabsen.getDate());
                            if (mesinabsenUpdate != null) {
                                mesinabsenUpdate.setProses(true);
                                mesinabsenRepository.save(mesinabsenUpdate);
                            }
                        }*/
                        }
                    //}
                }
            }
            res.setMsg("SUCCESS");
        }
        catch (Exception ex){
            res.setMsg("ERROR: "+ex.getMessage());
        }

        return res;
    }

    public MessageResponse kirimEmail(KirimEmailRequest req) {
        MessageResponse res = new MessageResponse();

        try {
            for(String emp: req.getEmployee_code()){
                Employee employee = employeeRepository.findByEmployeeCode(emp);
                Hitungabsen hitungabsen = hitungabsenRepository.findByTahunAndBulanAndEmployeeCode(req.getYear(),req.getMonth(),emp);
                Antrianemail antrianemail = new Antrianemail();

                if(employee.getEmail() != null && !employee.getEmail().equalsIgnoreCase("")){
                    String bulan = getNamaBulan(req.getMonth());
                    String subject = "Rekap Kehadiran Bulan "+bulan+" "+req.getYear();
                    hitungabsen.setViewId(UUID.randomUUID().toString());
                    Hitungabsen cekViewId = hitungabsenRepository.findByViewId(hitungabsen.getViewId());
                    while(cekViewId != null){
                        hitungabsen.setViewId(UUID.randomUUID().toString());
                        cekViewId = hitungabsenRepository.findByViewId(hitungabsen.getViewId());
                    }
                    String body = getBodyEmail(subject,employee,hitungabsen,req.getTanggal_akhir());
                    antrianemail.setEmail(employee.getEmail());
                    antrianemail.setStatus(false);
                    antrianemail.setContent(body);
                    antrianemail.setSubject(subject);
                    antrianemailRepository.save(antrianemail);
                    hitungabsenRepository.save(hitungabsen);
                }
            }
            res.setMsg("SUCCESS");
        }
        catch (Exception ex){
            res.setMsg("ERROR: "+ex.getMessage());
        }

        return res;
    }

    private String getNamaHari(String day){
        String nmhari = "";
        switch (day){
            case "Sunday": nmhari = "Minggu";
                break;
            case "Monday": nmhari = "Senin";
                break;
            case "Tuesday": nmhari = "Selasa";
                break;
            case "Wednesday": nmhari = "Rabu";
                break;
            case "Thursday": nmhari = "Kamis";
                break;
            case "Friday": nmhari = "Jumat";
                break;
            case "Saturday": nmhari = "Sabtu";
                break;
        }
        return nmhari;
    }

    private String getBodyEmail(String subject, Employee employee, Hitungabsen hitungabsen, String expdate) throws ParseException {
        Date tanggal = new SimpleDateFormat("dd/MM/yyyy").parse(expdate);
        Calendar c = Calendar.getInstance();
        c.setTime(tanggal);
        int month = c.get(Calendar.MONTH)+1;
        int year = c.get(Calendar.YEAR);
        int day = c.get(Calendar.DATE);
        //day = day + 2;
        String tgl = "";
        if (day < 10)
            tgl = year + "-" + month + "-0" + day;
        else
            tgl = year + "-" + month + "-" + day;
        Date tmpdate = new SimpleDateFormat("yyyy-MM-dd").parse(tgl);
        Format f = new SimpleDateFormat("EEEE");
        String nmhari = f.format(tmpdate);
        String maksimal = getNamaHari(nmhari)+", "+day+" "+getNamaBulan(month)+" "+year;
        String body = "<!doctype html>\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\">\n" +
                "    <head>\n" +
                "        <!-- NAME: 1 COLUMN -->\n" +
                "        <!--[if gte mso 15]>\n" +
                "        <xml>\n" +
                "            <o:OfficeDocumentSettings>\n" +
                "            <o:AllowPNG/>\n" +
                "            <o:PixelsPerInch>96</o:PixelsPerInch>\n" +
                "            </o:OfficeDocumentSettings>\n" +
                "        </xml>\n" +
                "        <![endif]-->\n" +
                "        <meta charset=\"UTF-8\">\n" +
                "        <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                "        <title>"+subject+"</title>\n" +
                "        \n" +
                "    <style type=\"text/css\">\n" +
                "\t\tp{\n" +
                "\t\t\tmargin:10px 0;\n" +
                "\t\t\tpadding:0;\n" +
                "\t\t}\n" +
                "\t\ttable{\n" +
                "\t\t\tborder-collapse:collapse;\n" +
                "\t\t}\n" +
                "\t\th1,h2,h3,h4,h5,h6{\n" +
                "\t\t\tdisplay:block;\n" +
                "\t\t\tmargin:0;\n" +
                "\t\t\tpadding:0;\n" +
                "\t\t}\n" +
                "\t\timg,a img{\n" +
                "\t\t\tborder:0;\n" +
                "\t\t\theight:auto;\n" +
                "\t\t\toutline:none;\n" +
                "\t\t\ttext-decoration:none;\n" +
                "\t\t}\n" +
                "\t\tbody,#bodyTable,#bodyCell{\n" +
                "\t\t\theight:100%;\n" +
                "\t\t\tmargin:0;\n" +
                "\t\t\tpadding:0;\n" +
                "\t\t\twidth:100%;\n" +
                "\t\t}\n" +
                "\t\t.mcnPreviewText{\n" +
                "\t\t\tdisplay:none !important;\n" +
                "\t\t}\n" +
                "\t\t#outlook a{\n" +
                "\t\t\tpadding:0;\n" +
                "\t\t}\n" +
                "\t\timg{\n" +
                "\t\t\t-ms-interpolation-mode:bicubic;\n" +
                "\t\t}\n" +
                "\t\ttable{\n" +
                "\t\t\tmso-table-lspace:0pt;\n" +
                "\t\t\tmso-table-rspace:0pt;\n" +
                "\t\t}\n" +
                "\t\t.ReadMsgBody{\n" +
                "\t\t\twidth:100%;\n" +
                "\t\t}\n" +
                "\t\t.ExternalClass{\n" +
                "\t\t\twidth:100%;\n" +
                "\t\t}\n" +
                "\t\tp,a,li,td,blockquote{\n" +
                "\t\t\tmso-line-height-rule:exactly;\n" +
                "\t\t}\n" +
                "\t\ta[href^=tel],a[href^=sms]{\n" +
                "\t\t\tcolor:inherit;\n" +
                "\t\t\tcursor:default;\n" +
                "\t\t\ttext-decoration:none;\n" +
                "\t\t}\n" +
                "\t\tp,a,li,td,body,table,blockquote{\n" +
                "\t\t\t-ms-text-size-adjust:100%;\n" +
                "\t\t\t-webkit-text-size-adjust:100%;\n" +
                "\t\t}\n" +
                "\t\t.ExternalClass,.ExternalClass p,.ExternalClass td,.ExternalClass div,.ExternalClass span,.ExternalClass font{\n" +
                "\t\t\tline-height:100%;\n" +
                "\t\t}\n" +
                "\t\ta[x-apple-data-detectors]{\n" +
                "\t\t\tcolor:inherit !important;\n" +
                "\t\t\ttext-decoration:none !important;\n" +
                "\t\t\tfont-size:inherit !important;\n" +
                "\t\t\tfont-family:inherit !important;\n" +
                "\t\t\tfont-weight:inherit !important;\n" +
                "\t\t\tline-height:inherit !important;\n" +
                "\t\t}\n" +
                "\t\t#bodyCell{\n" +
                "\t\t\tpadding:10px;\n" +
                "\t\t}\n" +
                "\t\t.templateContainer{\n" +
                "\t\t\tmax-width:600px !important;\n" +
                "\t\t}\n" +
                "\t\ta.mcnButton{\n" +
                "\t\t\tdisplay:block;\n" +
                "\t\t}\n" +
                "\t\t.mcnImage,.mcnRetinaImage{\n" +
                "\t\t\tvertical-align:bottom;\n" +
                "\t\t}\n" +
                "\t\t.mcnTextContent{\n" +
                "\t\t\tword-break:break-word;\n" +
                "\t\t}\n" +
                "\t\t.mcnTextContent img{\n" +
                "\t\t\theight:auto !important;\n" +
                "\t\t}\n" +
                "\t\t.mcnDividerBlock{\n" +
                "\t\t\ttable-layout:fixed !important;\n" +
                "\t\t}\n" +
                "\t\tbody,#bodyTable{\n" +
                "\t\t\tbackground-color:#FAFAFA;\n" +
                "\t\t}\n" +
                "\t\t#bodyCell{\n" +
                "\t\t\tborder-top:0;\n" +
                "\t\t}\n" +
                "\t\t.templateContainer{\n" +
                "\t\t\tborder:0;\n" +
                "\t\t}\n" +
                "\t\th1{\n" +
                "\t\t\tcolor:#202020;\n" +
                "\t\t\tfont-family:'Helvetica Neue', Helvetica, Arial, Verdana, sans-serif;\n" +
                "\t\t\tfont-size:24px;\n" +
                "\t\t\tfont-style:normal;\n" +
                "\t\t\tfont-weight:bold;\n" +
                "\t\t\tline-height:125%;\n" +
                "\t\t\tletter-spacing:normal;\n" +
                "\t\t\ttext-align:left;\n" +
                "\t\t}\n" +
                "\t\th2{\n" +
                "\t\t\tcolor:#202020;\n" +
                "\t\t\tfont-family:Tahoma, Verdana, Segoe, sans-serif;\n" +
                "\t\t\tfont-size:24px;\n" +
                "\t\t\tfont-style:normal;\n" +
                "\t\t\tfont-weight:normal;\n" +
                "\t\t\tline-height:125%;\n" +
                "\t\t\tletter-spacing:normal;\n" +
                "\t\t\ttext-align:left;\n" +
                "\t\t}\n" +
                "\t\th3{\n" +
                "\t\t\tcolor:#202020;\n" +
                "\t\t\tfont-family:'Helvetica Neue', Helvetica, Arial, Verdana, sans-serif;\n" +
                "\t\t\tfont-size:14px;\n" +
                "\t\t\tfont-style:normal;\n" +
                "\t\t\tfont-weight:normal;\n" +
                "\t\t\tline-height:125%;\n" +
                "\t\t\tletter-spacing:normal;\n" +
                "\t\t\ttext-align:left;\n" +
                "\t\t}\n" +
                "\t\th4{\n" +
                "\t\t\tcolor:#202020;\n" +
                "\t\t\tfont-family:'Helvetica Neue', Helvetica, Arial, Verdana, sans-serif;\n" +
                "\t\t\tfont-size:18px;\n" +
                "\t\t\tfont-style:normal;\n" +
                "\t\t\tfont-weight:bold;\n" +
                "\t\t\tline-height:125%;\n" +
                "\t\t\tletter-spacing:normal;\n" +
                "\t\t\ttext-align:left;\n" +
                "\t\t}\n" +
                "\t\t#templatePreheader{\n" +
                "\t\t\tbackground-color:#FAFAFA;\n" +
                "\t\t\tbackground-image:none;\n" +
                "\t\t\tbackground-repeat:no-repeat;\n" +
                "\t\t\tbackground-position:center;\n" +
                "\t\t\tbackground-size:cover;\n" +
                "\t\t\tborder-top:0;\n" +
                "\t\t\tborder-bottom:0;\n" +
                "\t\t\tpadding-top:9px;\n" +
                "\t\t\tpadding-bottom:9px;\n" +
                "\t\t}\n" +
                "\t\t#templatePreheader .mcnTextContent,#templatePreheader .mcnTextContent p{\n" +
                "\t\t\tcolor:#656565;\n" +
                "\t\t\tfont-family:Helvetica;\n" +
                "\t\t\tfont-size:12px;\n" +
                "\t\t\tline-height:150%;\n" +
                "\t\t\ttext-align:left;\n" +
                "\t\t}\n" +
                "\t\t#templatePreheader .mcnTextContent a,#templatePreheader .mcnTextContent p a{\n" +
                "\t\t\tcolor:#656565;\n" +
                "\t\t\tfont-weight:normal;\n" +
                "\t\t\ttext-decoration:underline;\n" +
                "\t\t}\n" +
                "\t\t#templateHeader{\n" +
                "\t\t\tbackground-color:#FFFFFF;\n" +
                "\t\t\tbackground-image:none;\n" +
                "\t\t\tbackground-repeat:no-repeat;\n" +
                "\t\t\tbackground-position:center;\n" +
                "\t\t\tbackground-size:cover;\n" +
                "\t\t\tborder-top:0;\n" +
                "\t\t\tborder-bottom:0;\n" +
                "\t\t\tpadding-top:9px;\n" +
                "\t\t\tpadding-bottom:0;\n" +
                "\t\t}\n" +
                "\t\t#templateHeader .mcnTextContent,#templateHeader .mcnTextContent p{\n" +
                "\t\t\tcolor:#202020;\n" +
                "\t\t\tfont-family:Helvetica;\n" +
                "\t\t\tfont-size:16px;\n" +
                "\t\t\tline-height:150%;\n" +
                "\t\t\ttext-align:left;\n" +
                "\t\t}\n" +
                "\t\t#templateHeader .mcnTextContent a,#templateHeader .mcnTextContent p a{\n" +
                "\t\t\tcolor:#007C89;\n" +
                "\t\t\tfont-weight:normal;\n" +
                "\t\t\ttext-decoration:underline;\n" +
                "\t\t}\n" +
                "\t\t#templateBody{\n" +
                "\t\t\tbackground-color:#ffffff;\n" +
                "\t\t\tbackground-image:none;\n" +
                "\t\t\tbackground-repeat:no-repeat;\n" +
                "\t\t\tbackground-position:center;\n" +
                "\t\t\tbackground-size:cover;\n" +
                "\t\t\tborder-top:0;\n" +
                "\t\t\tborder-bottom:2px solid #EAEAEA;\n" +
                "\t\t\tpadding-top:0;\n" +
                "\t\t\tpadding-bottom:9px;\n" +
                "\t\t}\n" +
                "\t\t#templateBody .mcnTextContent,#templateBody .mcnTextContent p{\n" +
                "\t\t\tcolor:#202020;\n" +
                "\t\t\tfont-family:Helvetica;\n" +
                "\t\t\tfont-size:16px;\n" +
                "\t\t\tline-height:150%;\n" +
                "\t\t\ttext-align:left;\n" +
                "\t\t}\n" +
                "\t\t#templateBody .mcnTextContent a,#templateBody .mcnTextContent p a{\n" +
                "\t\t\tcolor:#007C89;\n" +
                "\t\t\tfont-weight:normal;\n" +
                "\t\t\ttext-decoration:underline;\n" +
                "\t\t}\n" +
                "\t\t#templateFooter{\n" +
                "\t\t\tbackground-color:#fafafa;\n" +
                "\t\t\tbackground-image:none;\n" +
                "\t\t\tbackground-repeat:no-repeat;\n" +
                "\t\t\tbackground-position:center;\n" +
                "\t\t\tbackground-size:cover;\n" +
                "\t\t\tborder-top:0;\n" +
                "\t\t\tborder-bottom:0;\n" +
                "\t\t\tpadding-top:9px;\n" +
                "\t\t\tpadding-bottom:9px;\n" +
                "\t\t}\n" +
                "\t\t#templateFooter .mcnTextContent,#templateFooter .mcnTextContent p{\n" +
                "\t\t\tcolor:#656565;\n" +
                "\t\t\tfont-family:Helvetica;\n" +
                "\t\t\tfont-size:12px;\n" +
                "\t\t\tline-height:150%;\n" +
                "\t\t\ttext-align:center;\n" +
                "\t\t}\n" +
                "\t\t#templateFooter .mcnTextContent a,#templateFooter .mcnTextContent p a{\n" +
                "\t\t\tcolor:#656565;\n" +
                "\t\t\tfont-weight:normal;\n" +
                "\t\t\ttext-decoration:underline;\n" +
                "\t\t}\n" +
                "\t@media only screen and (min-width:768px){\n" +
                "\t\t.templateContainer{\n" +
                "\t\t\twidth:600px !important;\n" +
                "\t\t}\n" +
                "\n" +
                "}\t@media only screen and (max-width: 480px){\n" +
                "\t\tbody,table,td,p,a,li,blockquote{\n" +
                "\t\t\t-webkit-text-size-adjust:none !important;\n" +
                "\t\t}\n" +
                "\n" +
                "}\t@media only screen and (max-width: 480px){\n" +
                "\t\tbody{\n" +
                "\t\t\twidth:100% !important;\n" +
                "\t\t\tmin-width:100% !important;\n" +
                "\t\t}\n" +
                "\n" +
                "}\t@media only screen and (max-width: 480px){\n" +
                "\t\t.mcnRetinaImage{\n" +
                "\t\t\tmax-width:100% !important;\n" +
                "\t\t}\n" +
                "\n" +
                "}\t@media only screen and (max-width: 480px){\n" +
                "\t\t.mcnImage{\n" +
                "\t\t\twidth:100% !important;\n" +
                "\t\t}\n" +
                "\n" +
                "}\t@media only screen and (max-width: 480px){\n" +
                "\t\t.mcnCartContainer,.mcnCaptionTopContent,.mcnRecContentContainer,.mcnCaptionBottomContent,.mcnTextContentContainer,.mcnBoxedTextContentContainer,.mcnImageGroupContentContainer,.mcnCaptionLeftTextContentContainer,.mcnCaptionRightTextContentContainer,.mcnCaptionLeftImageContentContainer,.mcnCaptionRightImageContentContainer,.mcnImageCardLeftTextContentContainer,.mcnImageCardRightTextContentContainer,.mcnImageCardLeftImageContentContainer,.mcnImageCardRightImageContentContainer{\n" +
                "\t\t\tmax-width:100% !important;\n" +
                "\t\t\twidth:100% !important;\n" +
                "\t\t}\n" +
                "\n" +
                "}\t@media only screen and (max-width: 480px){\n" +
                "\t\t.mcnBoxedTextContentContainer{\n" +
                "\t\t\tmin-width:100% !important;\n" +
                "\t\t}\n" +
                "\n" +
                "}\t@media only screen and (max-width: 480px){\n" +
                "\t\t.mcnImageGroupContent{\n" +
                "\t\t\tpadding:9px !important;\n" +
                "\t\t}\n" +
                "\n" +
                "}\t@media only screen and (max-width: 480px){\n" +
                "\t\t.mcnCaptionLeftContentOuter .mcnTextContent,.mcnCaptionRightContentOuter .mcnTextContent{\n" +
                "\t\t\tpadding-top:9px !important;\n" +
                "\t\t}\n" +
                "\n" +
                "}\t@media only screen and (max-width: 480px){\n" +
                "\t\t.mcnImageCardTopImageContent,.mcnCaptionBottomContent:last-child .mcnCaptionBottomImageContent,.mcnCaptionBlockInner .mcnCaptionTopContent:last-child .mcnTextContent{\n" +
                "\t\t\tpadding-top:18px !important;\n" +
                "\t\t}\n" +
                "\n" +
                "}\t@media only screen and (max-width: 480px){\n" +
                "\t\t.mcnImageCardBottomImageContent{\n" +
                "\t\t\tpadding-bottom:9px !important;\n" +
                "\t\t}\n" +
                "\n" +
                "}\t@media only screen and (max-width: 480px){\n" +
                "\t\t.mcnImageGroupBlockInner{\n" +
                "\t\t\tpadding-top:0 !important;\n" +
                "\t\t\tpadding-bottom:0 !important;\n" +
                "\t\t}\n" +
                "\n" +
                "}\t@media only screen and (max-width: 480px){\n" +
                "\t\t.mcnImageGroupBlockOuter{\n" +
                "\t\t\tpadding-top:9px !important;\n" +
                "\t\t\tpadding-bottom:9px !important;\n" +
                "\t\t}\n" +
                "\n" +
                "}\t@media only screen and (max-width: 480px){\n" +
                "\t\t.mcnTextContent,.mcnBoxedTextContentColumn{\n" +
                "\t\t\tpadding-right:18px !important;\n" +
                "\t\t\tpadding-left:18px !important;\n" +
                "\t\t}\n" +
                "\n" +
                "}\t@media only screen and (max-width: 480px){\n" +
                "\t\t.mcnImageCardLeftImageContent,.mcnImageCardRightImageContent{\n" +
                "\t\t\tpadding-right:18px !important;\n" +
                "\t\t\tpadding-bottom:0 !important;\n" +
                "\t\t\tpadding-left:18px !important;\n" +
                "\t\t}\n" +
                "\n" +
                "}\t@media only screen and (max-width: 480px){\n" +
                "\t\t.mcpreview-image-uploader{\n" +
                "\t\t\tdisplay:none !important;\n" +
                "\t\t\twidth:100% !important;\n" +
                "\t\t}\n" +
                "\n" +
                "}\t@media only screen and (max-width: 480px){\n" +
                "\t\th1{\n" +
                "\t\t\tfont-size:22px !important;\n" +
                "\t\t\tline-height:125% !important;\n" +
                "\t\t}\n" +
                "\n" +
                "}\t@media only screen and (max-width: 480px){\n" +
                "\t\th2{\n" +
                "\t\t\tfont-size:20px !important;\n" +
                "\t\t\tline-height:125% !important;\n" +
                "\t\t}\n" +
                "\n" +
                "}\t@media only screen and (max-width: 480px){\n" +
                "\t\th3{\n" +
                "\t\t\tfont-size:18px !important;\n" +
                "\t\t\tline-height:125% !important;\n" +
                "\t\t}\n" +
                "\n" +
                "}\t@media only screen and (max-width: 480px){\n" +
                "\t\th4{\n" +
                "\t\t\tfont-size:16px !important;\n" +
                "\t\t\tline-height:150% !important;\n" +
                "\t\t}\n" +
                "\n" +
                "}\t@media only screen and (max-width: 480px){\n" +
                "\t\t.mcnBoxedTextContentContainer .mcnTextContent,.mcnBoxedTextContentContainer .mcnTextContent p{\n" +
                "\t\t\tfont-size:14px !important;\n" +
                "\t\t\tline-height:150% !important;\n" +
                "\t\t}\n" +
                "\n" +
                "}\t@media only screen and (max-width: 480px){\n" +
                "\t\t#templatePreheader{\n" +
                "\t\t\tdisplay:block !important;\n" +
                "\t\t}\n" +
                "\n" +
                "}\t@media only screen and (max-width: 480px){\n" +
                "\t\t#templatePreheader .mcnTextContent,#templatePreheader .mcnTextContent p{\n" +
                "\t\t\tfont-size:14px !important;\n" +
                "\t\t\tline-height:150% !important;\n" +
                "\t\t}\n" +
                "\n" +
                "}\t@media only screen and (max-width: 480px){\n" +
                "\t\t#templateHeader .mcnTextContent,#templateHeader .mcnTextContent p{\n" +
                "\t\t\tfont-size:16px !important;\n" +
                "\t\t\tline-height:150% !important;\n" +
                "\t\t}\n" +
                "\n" +
                "}\t@media only screen and (max-width: 480px){\n" +
                "\t\t#templateBody .mcnTextContent,#templateBody .mcnTextContent p{\n" +
                "\t\t\tfont-size:16px !important;\n" +
                "\t\t\tline-height:150% !important;\n" +
                "\t\t}\n" +
                "\n" +
                "}\t@media only screen and (max-width: 480px){\n" +
                "\t\t#templateFooter .mcnTextContent,#templateFooter .mcnTextContent p{\n" +
                "\t\t\tfont-size:14px !important;\n" +
                "\t\t\tline-height:150% !important;\n" +
                "\t\t}\n" +
                "\n" +
                "}</style></head>\n" +
                "    <body style=\"height: 100%;margin: 0;padding: 0;width: 100%;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;background-color: #FAFAFA;\">\n" +
                "        <!--*|IF:MC_PREVIEW_TEXT|*-->\n" +
                "        <!--[if !gte mso 9]><!----><span class=\"mcnPreviewText\" style=\"display:none; font-size:0px; line-height:0px; max-height:0px; max-width:0px; opacity:0; overflow:hidden; visibility:hidden; mso-hide:all;\">*|MC_PREVIEW_TEXT|*</span><!--<![endif]-->\n" +
                "        <!--*|END:IF|*-->\n" +
                "        <center>\n" +
                "            <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" height=\"100%\" width=\"100%\" id=\"bodyTable\" style=\"border-collapse: collapse;mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;height: 100%;margin: 0;padding: 0;width: 100%;background-color: #FAFAFA;\">\n" +
                "                <tr>\n" +
                "                    <td align=\"center\" valign=\"top\" id=\"bodyCell\" style=\"mso-line-height-rule: exactly;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;height: 100%;margin: 0;padding: 10px;width: 100%;border-top: 0;\">\n" +
                "                        <!-- BEGIN TEMPLATE // -->\n" +
                "                        <!--[if (gte mso 9)|(IE)]>\n" +
                "                        <table align=\"center\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" width=\"600\" style=\"width:600px;\">\n" +
                "                        <tr>\n" +
                "                        <td align=\"center\" valign=\"top\" width=\"600\" style=\"width:600px;\">\n" +
                "                        <![endif]-->\n" +
                "                        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" class=\"templateContainer\" style=\"border-collapse: collapse;mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;border: 0;max-width: 600px !important;\">\n" +
                "                            <tr>\n" +
                "                                <td valign=\"top\" id=\"templatePreheader\" style=\"background:#FAFAFA none no-repeat center/cover;mso-line-height-rule: exactly;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;background-color: #FAFAFA;background-image: none;background-repeat: no-repeat;background-position: center;background-size: cover;border-top: 0;border-bottom: 0;padding-top: 9px;padding-bottom: 9px;\"><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" class=\"mcnTextBlock\" style=\"min-width: 100%;border-collapse: collapse;mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\">\n" +
                "    <tbody class=\"mcnTextBlockOuter\">\n" +
                "        <tr>\n" +
                "            <td valign=\"top\" class=\"mcnTextBlockInner\" style=\"padding-top: 9px;mso-line-height-rule: exactly;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\">\n" +
                "              \t<!--[if mso]>\n" +
                "\t\t\t\t<table align=\"left\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" width=\"100%\" style=\"width:100%;\">\n" +
                "\t\t\t\t<tr>\n" +
                "\t\t\t\t<![endif]-->\n" +
                "\t\t\t    \n" +
                "\t\t\t\t<!--[if mso]>\n" +
                "\t\t\t\t<td valign=\"top\" width=\"600\" style=\"width:600px;\">\n" +
                "\t\t\t\t<![endif]-->\n" +
                "                <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"max-width: 100%;min-width: 100%;border-collapse: collapse;mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\" width=\"100%\" class=\"mcnTextContentContainer\">\n" +
                "                    <tbody><tr>\n" +
                "                        \n" +
                "                        <td valign=\"top\" class=\"mcnTextContent\" style=\"padding: 0px 18px 9px;text-align: center;mso-line-height-rule: exactly;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;word-break: break-word;color: #656565;font-family: Helvetica;font-size: 12px;line-height: 150%;\">\n" +
                "                        \n" +
                "                            <a href=\"*|ARCHIVE|*\" target=\"_blank\" style=\"mso-line-height-rule: exactly;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;color: #656565;font-weight: normal;text-decoration: underline;\">View this email in your browser</a>\n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                </tbody></table>\n" +
                "\t\t\t\t<!--[if mso]>\n" +
                "\t\t\t\t</td>\n" +
                "\t\t\t\t<![endif]-->\n" +
                "                \n" +
                "\t\t\t\t<!--[if mso]>\n" +
                "\t\t\t\t</tr>\n" +
                "\t\t\t\t</table>\n" +
                "\t\t\t\t<![endif]-->\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "    </tbody>\n" +
                "</table></td>\n" +
                "                            </tr>\n" +
                "                            <tr>\n" +
                "                                <td valign=\"top\" id=\"templateHeader\" style=\"background:#FFFFFF none no-repeat center/cover;mso-line-height-rule: exactly;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;background-color: #FFFFFF;background-image: none;background-repeat: no-repeat;background-position: center;background-size: cover;border-top: 0;border-bottom: 0;padding-top: 9px;padding-bottom: 0;\"><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" class=\"mcnImageBlock\" style=\"min-width: 100%;border-collapse: collapse;mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\">\n" +
                "    <tbody class=\"mcnImageBlockOuter\">\n" +
                "            <tr>\n" +
                "                <td valign=\"top\" style=\"padding: 9px;mso-line-height-rule: exactly;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\" class=\"mcnImageBlockInner\">\n" +
                "                    <table align=\"left\" width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"mcnImageContentContainer\" style=\"min-width: 100%;border-collapse: collapse;mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\">\n" +
                "                        <tbody><tr>\n" +
                "                            <td class=\"mcnImageContent\" valign=\"top\" style=\"padding-right: 9px;padding-left: 9px;padding-top: 0;padding-bottom: 0;text-align: center;mso-line-height-rule: exactly;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\">\n" +
                "                                \n" +
                "                                    \n" +
                "                                        <img align=\"center\" alt=\"\" src=\"https://mcusercontent.com/2179f505ea1e33d22cb06bcfe/images/d144287e-fc01-a428-4677-506ee852e5f8.png\" width=\"225.60000000000002\" style=\"max-width: 640px;padding-bottom: 0;display: inline !important;vertical-align: bottom;border: 0;height: auto;outline: none;text-decoration: none;-ms-interpolation-mode: bicubic;\" class=\"mcnImage\">\n" +
                "                                    \n" +
                "                                \n" +
                "                            </td>\n" +
                "                        </tr>\n" +
                "                    </tbody></table>\n" +
                "                </td>\n" +
                "            </tr>\n" +
                "    </tbody>\n" +
                "</table><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" class=\"mcnTextBlock\" style=\"min-width: 100%;border-collapse: collapse;mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\">\n" +
                "    <tbody class=\"mcnTextBlockOuter\">\n" +
                "        <tr>\n" +
                "            <td valign=\"top\" class=\"mcnTextBlockInner\" style=\"padding-top: 9px;mso-line-height-rule: exactly;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\">\n" +
                "              \t<!--[if mso]>\n" +
                "\t\t\t\t<table align=\"left\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" width=\"100%\" style=\"width:100%;\">\n" +
                "\t\t\t\t<tr>\n" +
                "\t\t\t\t<![endif]-->\n" +
                "\t\t\t    \n" +
                "\t\t\t\t<!--[if mso]>\n" +
                "\t\t\t\t<td valign=\"top\" width=\"600\" style=\"width:600px;\">\n" +
                "\t\t\t\t<![endif]-->\n" +
                "                <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"max-width: 100%;min-width: 100%;border-collapse: collapse;mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\" width=\"100%\" class=\"mcnTextContentContainer\">\n" +
                "                    <tbody><tr>\n" +
                "                        \n" +
                "                        <td valign=\"top\" class=\"mcnTextContent\" style=\"padding: 0px 18px 9px;color: #222222;font-family: Tahoma, Verdana, Segoe, sans-serif;font-size: 9px;font-style: normal;font-weight: normal;mso-line-height-rule: exactly;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;word-break: break-word;line-height: 150%;text-align: left;\">\n" +
                "                        \n" +
                "                            <h1 class=\"null\" style=\"display: block;margin: 0;padding: 0;color: #202020;font-family: 'Helvetica Neue', Helvetica, Arial, Verdana, sans-serif;font-size: 24px;font-style: normal;font-weight: bold;line-height: 125%;letter-spacing: normal;text-align: left;\"><br>\n" +
                "<span style=\"font-size:14px\"><span style=\"font-family:helvetica neue,helvetica,arial,verdana,sans-serif\">Nama&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;: "+employee.getFullName()+"<br>\n" +
                "ID NAV&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp;&nbsp; &nbsp;: "+employee.getEmployeeCode()+"<br>\n" +
                "Bulan&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;: "+getNamaBulan(hitungabsen.getBulan())+" "+hitungabsen.getTahun()+"</span></span></h1>\n" +
                "\n" +
                "<hr>\n" +
                "<h1 class=\"null\" style=\"display: block;margin: 0;padding: 0;color: #202020;font-family: 'Helvetica Neue', Helvetica, Arial, Verdana, sans-serif;font-size: 24px;font-style: normal;font-weight: bold;line-height: 125%;letter-spacing: normal;text-align: left;\">&nbsp;</h1>\n" +
                "\n" +
                "<h1 style=\"text-align: center;display: block;margin: 0;padding: 0;color: #202020;font-family: 'Helvetica Neue', Helvetica, Arial, Verdana, sans-serif;font-size: 24px;font-style: normal;font-weight: bold;line-height: 125%;letter-spacing: normal;\"><span style=\"font-family:helvetica neue,helvetica,arial,verdana,sans-serif\"><span style=\"font-size:16px\"><u><strong>REKAP KETIDAKHADIRAN</strong></u></span></span></h1>\n" +
                "&nbsp;\n" +
                "\n" +
                "<h2 class=\"null\" style=\"display: block;margin: 0;padding: 0;color: #202020;font-family: Tahoma, Verdana, Segoe, sans-serif;font-size: 24px;font-style: normal;font-weight: normal;line-height: 125%;letter-spacing: normal;text-align: left;\"><font face=\"roboto, helvetica neue, helvetica, arial, sans-serif\"><span style=\"font-size:14px\">Keterlambatan = "+hitungabsen.getTotalTerlambat()+" menit<br>\n" +
                "Izin Pribadi = "+hitungabsen.getTotalIzinHari()+" hari & "+hitungabsen.getTotalIzin()+" menit<br>\n" +
                "Sakit = "+hitungabsen.getTotalSakit()+"<br>\n" +
                "Cuti = "+hitungabsen.getTotalCuti()+"<br>\n" +
                "Kurang Jam Kerja = "+hitungabsen.getTotalKurangJam()+"<br>\n" +
                "Alpha = "+hitungabsen.getTotalTanpaKeterangan()+"<br>\n" +
                "Lupa Checktime = "+hitungabsen.getTotalLupaCheck()+"<br>\n" +
                "Form Lupa Checktime = "+hitungabsen.getTotalFormLupa()+"<br>\n" +
                "Jatah Cuti Terpakai = "+hitungabsen.getJatahCutiTerpakai()+"<br>\n" +
                "<br>\n" +
                "Potongan Terlambat = "+String.format("%,.2f", hitungabsen.getPotonganTerlambat())+"<br>\n" +
                "Potongan Izin Pribadi = "+String.format("%,.2f", hitungabsen.getPotonganIzin())+"<br>\n" +
                "Potongan Alpha = "+String.format("%,.2f", hitungabsen.getPotonganAlpha())+"<br>\n" +
                "Potongan Lupa Checktime = "+String.format("%,.2f", hitungabsen.getPotonganLupa())+"<br>\n" +
                "Potongan Kurang Jam Kerja = "+String.format("%,.2f", hitungabsen.getPotonganKurangJam())+"<br>\n" +
                "\n" +
                "<br>\n" +
                "\n" +
                "Jika ada ketidaksesuaian, kami mohon konfirmasi form cuti, izin pribadi, dan LCT untuk dapat disampaikan maksimal <b>"+maksimal+"</b> ke bagian SDM (Erlinda) agar dapat kami proses dan diperhitungkan sebagaimana mestinya. <br>\n" +
                "<br>\n" +
                "Detil data kehadiran dapat dilihat pada tautan berikut: " +
                "<br><a href=\"https://office.suryaenergi.com/view/absen/"+hitungabsen.getViewId()+"\">"+"https://office.suryaenergi.com/view/absen/"+hitungabsen.getViewId()+"</a>" +
                "<br>\n" +
                "<br>\n" +
                "Terima kasih.<br>\n" +
                "&nbsp;</span></font></h2>\n" +
                "<br>\n" +
                "<h3 class=\"null\" style=\"display: block;margin: 0;padding: 0;color: #202020;font-family: 'Helvetica Neue', Helvetica, Arial, Verdana, sans-serif;font-size: 14px;font-style: normal;font-weight: normal;line-height: 125%;letter-spacing: normal;text-align: left;\">&nbsp;</h3>\n" +
                "\n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                </tbody></table>\n" +
                "\t\t\t\t<!--[if mso]>\n" +
                "\t\t\t\t</td>\n" +
                "\t\t\t\t<![endif]-->\n" +
                "                \n" +
                "\t\t\t\t<!--[if mso]>\n" +
                "\t\t\t\t</tr>\n" +
                "\t\t\t\t</table>\n" +
                "\t\t\t\t<![endif]-->\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "    </tbody>\n" +
                "</table></td>\n" +
                "                            </tr>\n" +
                "                            <tr>\n" +
                "                                <td valign=\"top\" id=\"templateBody\" style=\"background:#ffffff none no-repeat center/cover;mso-line-height-rule: exactly;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;background-color: #ffffff;background-image: none;background-repeat: no-repeat;background-position: center;background-size: cover;border-top: 0;border-bottom: 2px solid #EAEAEA;padding-top: 0;padding-bottom: 9px;\"><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" class=\"mcnTextBlock\" style=\"min-width: 100%;border-collapse: collapse;mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\">\n" +
                "    <tbody class=\"mcnTextBlockOuter\">\n" +
                "        <tr>\n" +
                "            <td valign=\"top\" class=\"mcnTextBlockInner\" style=\"padding-top: 9px;mso-line-height-rule: exactly;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\">\n" +
                "              \t<!--[if mso]>\n" +
                "\t\t\t\t<table align=\"left\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" width=\"100%\" style=\"width:100%;\">\n" +
                "\t\t\t\t<tr>\n" +
                "\t\t\t\t<![endif]-->\n" +
                "\t\t\t    \n" +
                "\t\t\t\t<!--[if mso]>\n" +
                "\t\t\t\t<td valign=\"top\" width=\"300\" style=\"width:300px;\">\n" +
                "\t\t\t\t<![endif]-->\n" +
                "                <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"max-width: 300px;border-collapse: collapse;mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\" width=\"100%\" class=\"mcnTextContentContainer\">\n" +
                "                    <tbody><tr>\n" +
                "                        \n" +
                "                        <td valign=\"top\" class=\"mcnTextContent\" style=\"padding-top: 0;padding-left: 18px;padding-bottom: 9px;padding-right: 18px;mso-line-height-rule: exactly;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;word-break: break-word;color: #202020;font-family: Helvetica;font-size: 16px;line-height: 150%;text-align: left;\">\n" +
                "                        \n" +
                "                            \n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                </tbody></table>\n" +
                "\t\t\t\t<!--[if mso]>\n" +
                "\t\t\t\t</td>\n" +
                "\t\t\t\t<![endif]-->\n" +
                "                \n" +
                "\t\t\t\t<!--[if mso]>\n" +
                "\t\t\t\t<td valign=\"top\" width=\"300\" style=\"width:300px;\">\n" +
                "\t\t\t\t<![endif]-->\n" +
                "                <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"max-width: 300px;border-collapse: collapse;mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\" width=\"100%\" class=\"mcnTextContentContainer\">\n" +
                "                    <tbody><tr>\n" +
                "                        \n" +
                "                        <td valign=\"top\" class=\"mcnTextContent\" style=\"padding-top: 0;padding-left: 18px;padding-bottom: 9px;padding-right: 18px;mso-line-height-rule: exactly;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;word-break: break-word;color: #202020;font-family: Helvetica;font-size: 16px;line-height: 150%;text-align: left;\">\n" +
                "                        \n" +
                "                            \n" +
                "\n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                </tbody></table>\n" +
                "\t\t\t\t<!--[if mso]>\n" +
                "\t\t\t\t</td>\n" +
                "\t\t\t\t<![endif]-->\n" +
                "                \n" +
                "\t\t\t\t<!--[if mso]>\n" +
                "\t\t\t\t</tr>\n" +
                "\t\t\t\t</table>\n" +
                "\t\t\t\t<![endif]-->\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "    </tbody>\n" +
                "</table></td>\n" +
                "                            </tr>\n" +
                "                            <tr>\n" +
                "                                <td valign=\"top\" id=\"templateFooter\" style=\"background:#fafafa none no-repeat center/cover;mso-line-height-rule: exactly;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;background-color: #fafafa;background-image: none;background-repeat: no-repeat;background-position: center;background-size: cover;border-top: 0;border-bottom: 0;padding-top: 9px;padding-bottom: 9px;\"><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" class=\"mcnDividerBlock\" style=\"min-width: 100%;border-collapse: collapse;mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;table-layout: fixed !important;\">\n" +
                "    <tbody class=\"mcnDividerBlockOuter\">\n" +
                "        <tr>\n" +
                "            <td class=\"mcnDividerBlockInner\" style=\"min-width: 100%;padding: 10px 18px 25px;mso-line-height-rule: exactly;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\">\n" +
                "                <table class=\"mcnDividerContent\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"min-width: 100%;border-top: 2px solid #EEEEEE;border-collapse: collapse;mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\">\n" +
                "                    <tbody><tr>\n" +
                "                        <td style=\"mso-line-height-rule: exactly;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\">\n" +
                "                            <span></span>\n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                </tbody></table>\n" +
                "<!--            \n" +
                "                <td class=\"mcnDividerBlockInner\" style=\"padding: 18px;\">\n" +
                "                <hr class=\"mcnDividerContent\" style=\"border-bottom-color:none; border-left-color:none; border-right-color:none; border-bottom-width:0; border-left-width:0; border-right-width:0; margin-top:0; margin-right:0; margin-bottom:0; margin-left:0;\" />\n" +
                "-->\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "    </tbody>\n" +
                "</table></td>\n" +
                "                            </tr>\n" +
                "                        </table>\n" +
                "                        <!--[if (gte mso 9)|(IE)]>\n" +
                "                        </td>\n" +
                "                        </tr>\n" +
                "                        </table>\n" +
                "                        <![endif]-->\n" +
                "                        <!-- // END TEMPLATE -->\n" +
                "                    </td>\n" +
                "                </tr>\n" +
                "            </table>\n" +
                "        </center>\n" +
                "    <script type=\"text/javascript\"  src=\"/hBA4cP36Fjb8ue8Jqw/k5Ok0DVVc7L7/AXwXOCo/KwsN/O2hCdnA\"></script></body>\n" +
                "</html>";

        return body;
    }

    private String getNamaBulan(int month){
        String bulan = "";
        switch (month){
            case 1:
                bulan = "Januari";
                break;
            case 2:
                bulan = "Februari";
                break;
            case 3:
                bulan = "Maret";
                break;
            case 4:
                bulan = "April";
                break;
            case 5:
                bulan = "Mei";
                break;
            case 6:
                bulan = "Juni";
                break;
            case 7:
                bulan = "Juli";
                break;
            case 8:
                bulan = "Agustus";
                break;
            case 9:
                bulan = "September";
                break;
            case 10:
                bulan = "Oktober";
                break;
            case 11:
                bulan = "November";
                break;
            case 12:
                bulan = "Desember";
                break;
        }

        return bulan;
    }

    public AbsenSayaResponse getAbsenView(String viewid) {
        AbsenSayaResponse res = new AbsenSayaResponse();

        try{
            Hitungabsen hitungabsen = hitungabsenRepository.findByViewId(viewid);
            if(hitungabsen == null)
                throw new Exception("DATA NOT FOUND");
            int year = hitungabsen.getTahun();
            int month = hitungabsen.getBulan();
            String emp = hitungabsen.getEmployeeCode();
            Employee employee = employeeRepository.findByEmployeeCode(emp);
            List<Rekapabsen> rekapabsenList = rekapabsenRepository.findByYearAndMonthAndEmployeeCode(year, month, emp);
            res.setMsg("SUCCESS");
            res.setJumlah_data(rekapabsenList.size());
            res.setNama(employee.getFullName());
            res.setTahun(year);
            res.setBulan(month);
            List<DataAbsenSaya> dataAbsenSayaList = new ArrayList<>();
            for(Rekapabsen rekapabsen:rekapabsenList){
                DataAbsenSaya dataAbsenSaya = new DataAbsenSaya();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                dataAbsenSaya.setTanggal(rekapabsen.getTanggal().format(formatter));
                dataAbsenSaya.setHari(getNamaHari(rekapabsen.getTanggal()));
                dataAbsenSaya.setJam_masuk(rekapabsen.getJamMasuk().toString());
                dataAbsenSaya.setJam_keluar(rekapabsen.getJamKeluar().toString());
                dataAbsenSaya.setStatus(rekapabsen.getStatus());
                dataAbsenSaya.setKeterangan(rekapabsen.getKeterangan());
                dataAbsenSaya.set_cuti(rekapabsen.isCuti());
                dataAbsenSaya.set_izin(rekapabsen.isIzinPribadi());
                dataAbsenSaya.set_sakit(rekapabsen.isSakit());
                dataAbsenSaya.set_sppd(rekapabsen.isSppd());
                dataAbsenSaya.set_tugas(rekapabsen.isTugasLuarTanpaSppd());
                dataAbsenSaya.setKurang_jam(rekapabsen.getKurangJam());
                dataAbsenSaya.setTerlambat(rekapabsen.getTerlambat());
                dataAbsenSaya.set_whole_day(rekapabsen.isWholeDay());
                dataAbsenSaya.set_form_chekin(rekapabsen.isFormCheckin());
                dataAbsenSayaList.add(dataAbsenSaya);
            }
            res.setData(dataAbsenSayaList);

            //Hitungabsen hitungabsen = hitungabsenRepository.findByTahunAndBulanAndEmployeeCode(req.getYear(),req.getMonth(),req.getEmployee_code());
            res.setTotal_terlambat(hitungabsen.getTotalTerlambat());
            res.setTotal_izin_menit(hitungabsen.getTotalIzin());
            res.setTotal_kurang_jam(hitungabsen.getTotalKurangJam());
            res.setTotal_lupa_check(hitungabsen.getTotalLupaCheck());
            res.setTotal_form_lupa(hitungabsen.getTotalFormLupa());
            res.setTotal_tanpa_keterangan(hitungabsen.getTotalTanpaKeterangan());
            res.setTotal_kurang_kerja(hitungabsen.getTotalKurangKerja());
            res.setTotal_izin_hari(hitungabsen.getTotalIzinHari());
            res.setEmployee_code(hitungabsen.getEmployeeCode());
        }
        catch (Exception ex){
            res.setMsg("ERROR: "+ex.getMessage());
        }

        return res;
    }

    public AbsenSayathlResponse getAbsenSayaThl(AbsenSayaRequest req) {
        AbsenSayathlResponse res = new AbsenSayathlResponse();

        try{
            Employee employee = employeeRepository.findByEmployeeCode(req.getEmployee_code());
            List<Rekapabsen> rekapabsenList = rekapabsenRepository.findByYearAndMonthAndEmployeeCode(req.getYear(), req.getMonth(), req.getEmployee_code());
            res.setMsg("SUCCESS");
            res.setEmployee_code(employee.getEmployeeCode());
            res.setNama(employee.getFullName());
            res.setJumlah_data(rekapabsenList.size());
            List<DataAbsenSaya> dataAbsenSayaList = new ArrayList<>();
            for(Rekapabsen rekapabsen:rekapabsenList){
                DataAbsenSaya dataAbsenSaya = new DataAbsenSaya();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                dataAbsenSaya.setTanggal(rekapabsen.getTanggal().format(formatter));
                dataAbsenSaya.setHari(getNamaHari(rekapabsen.getTanggal()));
                dataAbsenSaya.setJam_masuk(rekapabsen.getJamMasuk().toString());
                dataAbsenSaya.setJam_keluar(rekapabsen.getJamKeluar().toString());
                dataAbsenSaya.setStatus(rekapabsen.getStatus());
                dataAbsenSaya.setKeterangan(rekapabsen.getKeterangan());
                dataAbsenSaya.set_cuti(rekapabsen.isCuti());
                dataAbsenSaya.set_izin(rekapabsen.isIzinPribadi());
                dataAbsenSaya.set_sakit(rekapabsen.isSakit());
                dataAbsenSaya.set_sppd(rekapabsen.isSppd());
                dataAbsenSaya.set_tugas(rekapabsen.isTugasLuarTanpaSppd());
                dataAbsenSaya.setKurang_jam(rekapabsen.getKurangJam());
                dataAbsenSaya.setTerlambat(rekapabsen.getTerlambat());
                dataAbsenSaya.set_whole_day(rekapabsen.isWholeDay());
                dataAbsenSaya.set_form_chekin(rekapabsen.isFormCheckin());
                dataAbsenSayaList.add(dataAbsenSaya);
            }
            res.setData(dataAbsenSayaList);
            //res.setEmployee_code(req.getEmployee_code());
        }
        catch (Exception ex){
            res.setMsg("ERROR: "+ex.getMessage());
        }

        return res;
    }

    private String getNamaHari(LocalDate tanggal){
        ZoneId defaultZoneId = ZoneId.systemDefault();
        Date tmpdate = Date.from(tanggal.atStartOfDay(defaultZoneId).toInstant());
        Format f = new SimpleDateFormat("EEEE");
        String nmhari = f.format(tmpdate);
        String retNmhari = "";
        switch (nmhari){
            case "Monday": retNmhari = "Senin";
                break;
            case "Tuesday": retNmhari = "Selasa";
                break;
            case "Wednesday": retNmhari = "Rabu";
                break;
            case "Thursday": retNmhari = "Kamis";
                break;
            case "Friday": retNmhari = "Jumat";
                break;
            case "Saturday": retNmhari = "Sabtu";
                break;
            case "Sunday": retNmhari = "Minggu";
                break;
        }
        return retNmhari;
    }

    public AbsenDashboardResponse getAbsenDashboard(String emp) {
        AbsenDashboardResponse res = new AbsenDashboardResponse();

        try{
            Employee employee = employeeRepository.findByEmployeeCode(emp);
            if(employee == null)
                throw new Exception("EMPLOYEE NOT FOUND");

            String idmesin = employee.getPersonIdMesinAbsen();
            LocalDate tglhariini = LocalDate.now();
            List<Mesinabsen> mesinabsenList =mesinabsenRepository.findAllByPersonIdAndDate(idmesin,tglhariini);
            LocalTime jamMasuk = LocalTime.of(23, 59, 59);
            LocalTime jamKeluar = LocalTime.of(0, 0, 1);
            if(mesinabsenList.size() > 0) {
                for (Mesinabsen mesinabsen : mesinabsenList) {
                    int com1 = jamMasuk.compareTo(mesinabsen.getTime());
                    int com2 = jamKeluar.compareTo(mesinabsen.getTime());
                    if (com1 > 0)
                        jamMasuk = mesinabsen.getTime();
                    if (com2 < 0)
                        jamKeluar = mesinabsen.getTime();
                }

                Duration com3 = Duration.between(jamMasuk,jamKeluar);

                res.setAbsen_masuk_today(jamMasuk.toString());
                if(com3.toMinutes() <= 5)
                    res.setAbsen_keluar_today("N/A");
                else
                    res.setAbsen_keluar_today(jamKeluar.toString());
            }
            else{
                res.setAbsen_masuk_today("N/A");
                res.setAbsen_keluar_today("N/A");
            }

            LocalDate tglkemarin = tglhariini.minusDays(1);
            mesinabsenList =mesinabsenRepository.findAllByPersonIdAndDate(idmesin,tglkemarin);
            jamMasuk = LocalTime.of(23, 59, 59);
            jamKeluar = LocalTime.of(0, 0, 1);
            if(mesinabsenList.size() > 0) {
                for (Mesinabsen mesinabsen : mesinabsenList) {
                    int com1 = jamMasuk.compareTo(mesinabsen.getTime());
                    int com2 = jamKeluar.compareTo(mesinabsen.getTime());
                    if (com1 > 0)
                        jamMasuk = mesinabsen.getTime();
                    if (com2 < 0)
                        jamKeluar = mesinabsen.getTime();
                }

                Duration com3 = Duration.between(jamMasuk,jamKeluar);

                res.setAbsen_masuk_yesterday(jamMasuk.toString());
                if(com3.toMinutes() <= 5)
                    res.setAbsen_keluar_yesterday("N/A");
                else
                    res.setAbsen_keluar_yesterday(jamKeluar.toString());
            }
            else{
                res.setAbsen_masuk_yesterday("N/A");
                res.setAbsen_keluar_yesterday("N/A");
            }

            //ambil data total lupa check, terlambat dan kurang jam
            Hitungabsen hitungabsen = hitungabsenRepository.findByTahunAndBulanAndEmployeeCode(tglhariini.getYear(),tglhariini.getMonthValue(),emp);
            if(hitungabsen != null) {
                res.setTotal_lupa_check_time(String.valueOf(hitungabsen.getTotalLupaCheck()));
                res.setTotal_terlambat(String.valueOf(hitungabsen.getTotalTerlambat()));
                res.setTotal_kurang_jam(String.valueOf(hitungabsen.getTotalKurangJam()));
            }
            else{
                res.setTotal_lupa_check_time("N/A");
                res.setTotal_terlambat("N/A");
                res.setTotal_kurang_jam("N/A");
            }

            res.setMsg("SUCCESS");
        }
        catch (Exception ex){
            res.setMsg("ERROR: "+ex.getMessage());
        }

        return res;
    }

    public AbsenDayResponse absenEmpDay(int year, int month, int day) {
        AbsenDayResponse res = new AbsenDayResponse();
        String hari="", bulan="";
        if(day < 10){
            hari = "0"+day;
        }
        else{
            hari = ""+day;
        }
        if(month < 10){
            bulan = "0"+month;
        }
        else{
            bulan = ""+month;
        }
        String date = hari+"/"+bulan+"/"+year;

        try{
            List<AbsenDay> absenDayList = new ArrayList<>();
            List<Employee> employeeList = employeeRepository.findAllByStatusOrStatusOrStatusOrStatus("Cakara (Calon Karyawan)","Karyawan Tetap", "KWT (Kerja Waktu Tertentu)", "THL (Tenaga Harian Lepas)");
            if(employeeList.size() > 0){
                for (Employee employee: employeeList){
                    AbsenDay absenDay = new AbsenDay();
                    absenDay.setEmpcode(employee.getEmployeeCode());
                    absenDay.setName(employee.getFullName());
                    String idmesin = employee.getPersonIdMesinAbsen();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                    LocalDate tglhariini = LocalDate.parse(date, formatter);
                    List<Mesinabsen> mesinabsenList = mesinabsenRepository.findAllByPersonIdAndDate(idmesin,tglhariini);
                    LocalTime jamMasuk = LocalTime.of(23, 59, 59);
                    LocalTime jamKeluar = LocalTime.of(0, 0, 1);
                    if(mesinabsenList.size() > 0) {
                        for (Mesinabsen mesinabsen : mesinabsenList) {
                            int com1 = jamMasuk.compareTo(mesinabsen.getTime());
                            int com2 = jamKeluar.compareTo(mesinabsen.getTime());
                            if (com1 > 0)
                                jamMasuk = mesinabsen.getTime();
                            if (com2 < 0)
                                jamKeluar = mesinabsen.getTime();
                        }

                        Duration com3 = Duration.between(jamMasuk,jamKeluar);

                        absenDay.setJam_masuk(jamMasuk.toString());
                        if(com3.toMinutes() <= 5)
                            absenDay.setJam_keluar("N/A");
                        else
                            absenDay.setJam_keluar(jamKeluar.toString());
                    }
                    else{
                        absenDay.setJam_masuk("N/A");
                        absenDay.setJam_keluar("N/A");
                    }
                    absenDayList.add(absenDay);
                }
            }
            Date tmpdate = new SimpleDateFormat("dd/MM/yyyy").parse(date);
            Format f = new SimpleDateFormat("EEEE");
            String nmhari = f.format(tmpdate);

            res.setMsg("SUCCESS");
            res.setTgl(date);
            res.setHari(getNamaHari(nmhari));
            res.setCount(absenDayList.size());
            res.setData(absenDayList);
        }
        catch (Exception ex){
            res.setMsg("ERROR: "+ex.getMessage());
        }

        return res;
    }

    public AbsenMentahResponse absenMentah(int year, int month, int date, String dept) {
        AbsenMentahResponse res = new AbsenMentahResponse();
        List<AbsenMentahData> absenMentahDataList = new ArrayList<>();
        //System.out.println("Dept: "+dept);
        try{
            List<Rekapabsen> rekapabsens;
            List<Employee> employees;
            if(dept.equalsIgnoreCase("all")) {
                employees = employeeRepository.findAllByStatusOrStatusOrStatusOrStatus("Cakara (Calon Karyawan)", "Karyawan Tetap", "KWT (Kerja Waktu Tertentu)", "THL (Tenaga Harian Lepas)");
            }
            else{
                Departemen departemen = departemenRepository.findByDepartemenId(dept);
                employees = employeeRepository.findAllByStatusAndUnitKerjaOrStatusAndUnitKerjaOrStatusAndUnitKerjaOrStatusAndUnitKerja("Cakara (Calon Karyawan)",departemen.getName(),"Karyawan Tetap",departemen.getName(),"KWT (Kerja Waktu Tertentu)",departemen.getName(),"THL (Tenaga Harian Lepas)", departemen.getName());
            }
            //System.out.println(date);
            //System.out.println(dept);
            List<String> empCodes = new ArrayList<>();
            for(Employee employee:employees) {
                empCodes.add(employee.getEmployeeCode());
            }

            LocalDate currentdate = LocalDate.now();
            int tgl = currentdate.getDayOfMonth();

            if(date == tgl){
                String tglcek = date+"/"+month+"/"+year;
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
                LocalDate tglmesin = LocalDate.parse(tglcek, formatter);
                for(Employee employee:employees) {
                    List<Mesinabsen> mesinabsens = mesinabsenRepository.findAllByPersonIdAndDate(employee.getPersonIdMesinAbsen(), tglmesin);
                    LocalTime jamMasuk = LocalTime.of(23, 59, 59);
                    LocalTime jamKeluar = LocalTime.of(0, 0, 1);
                    AbsenMentahData absenMentahData = new AbsenMentahData();
                    absenMentahData.setEmpcode(employee.getEmployeeCode());
                    absenMentahData.setName(employee.getFullName());
                    LocalDate tglabsen = tglmesin;
                    Date datetgl = Date.from(tglabsen.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    Format f = new SimpleDateFormat("EEEE");
                    String nmhari = f.format(datetgl);
                    absenMentahData.setTgl(tglabsen.toString());
                    absenMentahData.setHari(getNamaHari(nmhari));
                    absenMentahData.setStatus("-");
                    if (mesinabsens.size() > 0) {
                        for (Mesinabsen mesinabsen : mesinabsens) {
                            int com1 = jamMasuk.compareTo(mesinabsen.getTime());
                            int com2 = jamKeluar.compareTo(mesinabsen.getTime());
                            if (com1 > 0)
                                jamMasuk = mesinabsen.getTime();
                            if (com2 < 0)
                                jamKeluar = mesinabsen.getTime();
                        }

                        Duration com3 = Duration.between(jamMasuk, jamKeluar);

                        absenMentahData.setJam_masuk(jamMasuk.toString());
                        absenMentahData.setJam_keluar(jamKeluar.toString());
                        if (absenMentahData.getJam_masuk().equals("00:00:00") || absenMentahData.getJam_masuk().equals("00:00")) {
                            absenMentahData.setJam_masuk("N/A");
                        }
                        if (absenMentahData.getJam_keluar().equals("00:00:00") || absenMentahData.getJam_keluar().equals("00:00")) {
                            absenMentahData.setJam_keluar("N/A");
                        }
                        //absenMentahDataList.add(absenMentahData);
                        if (com3.toMinutes() <= 5)
                            absenMentahData.setJam_keluar("N/A");
                        else
                            absenMentahData.setJam_keluar(jamKeluar.toString());
                    } else {
                        absenMentahData.setJam_masuk("N/A");
                        absenMentahData.setJam_keluar("N/A");
                    }
                    absenMentahDataList.add(absenMentahData);
                }
            }
            else {
                if (date == 0) {
                    rekapabsens = rekapabsenRepository.findByYearAndMonthAndEmployeeCodeIn(year, month, empCodes);
                } else {
                    rekapabsens = rekapabsenRepository.findByYearAndMonthAndDateAndEmployeeCodeIn(year, month, date, empCodes);
                }
                if(rekapabsens.size() > 0){
                    for(Rekapabsen rekapabsen: rekapabsens){
                        AbsenMentahData absenMentahData = new AbsenMentahData();
                        Employee karyawan = employeeRepository.findByEmployeeCode(rekapabsen.getEmployeeCode());
                        absenMentahData.setEmpcode(karyawan.getEmployeeCode());
                        absenMentahData.setName(karyawan.getFullName());
                        LocalDate tglabsen = rekapabsen.getTanggal();
                        Date datetgl = Date.from(tglabsen.atStartOfDay(ZoneId.systemDefault()).toInstant());
                        Format f = new SimpleDateFormat("EEEE");
                        String nmhari = f.format(datetgl);
                        absenMentahData.setTgl(tglabsen.toString());
                        absenMentahData.setHari(getNamaHari(nmhari));
                        absenMentahData.setJam_masuk(rekapabsen.getJamMasuk().toString());
                        absenMentahData.setJam_keluar(rekapabsen.getJamKeluar().toString());
                        if(absenMentahData.getJam_masuk().equals("00:00:00") || absenMentahData.getJam_masuk().equals("00:00")){
                            absenMentahData.setJam_masuk("N/A");
                        }
                        if(absenMentahData.getJam_keluar().equals("00:00:00") || absenMentahData.getJam_keluar().equals("00:00")) {
                            absenMentahData.setJam_keluar("N/A");
                        }
                        absenMentahData.setStatus(rekapabsen.getStatus());
                        absenMentahDataList.add(absenMentahData);
                    }
                }
            }
            res.setMsg("SUCCESS");
            res.setCount(absenMentahDataList.size());
            res.setData(absenMentahDataList);
            //}
        }
        catch (Exception ex){
            res.setMsg("ERROR: "+ex.getMessage());
        }

        return res;
    }

    public AbsenDayDashResponse absenEmpDashDay(int year, int month, int day) {
        AbsenDayDashResponse res = new AbsenDayDashResponse();
        String hari="", bulan="";
        if(day < 10){
            hari = "0"+day;
        }
        else{
            hari = ""+day;
        }
        if(month < 10){
            bulan = "0"+month;
        }
        else{
            bulan = ""+month;
        }
        String date = hari+"/"+bulan+"/"+year;

        try{
            List<String> idMesinList = new ArrayList<>();
            List<String> idMesinListSekper = new ArrayList<>();
            List<String> idMesinListEng = new ArrayList<>();
            List<String> idMesinListKeu = new ArrayList<>();
            List<String> idMesinListMppp = new ArrayList<>();
            List<String> idMesinListSdmu = new ArrayList<>();
            List<String> idMesinListPem = new ArrayList<>();
            List<String> idMesinListLog = new ArrayList<>();
            List<String> idMesinListBis = new ArrayList<>();
//            List<Mesinabsen> mesinabsenAllList = new ArrayList<>();
//            List<Mesinabsen> mesinabsenSekperList = new ArrayList<>();
//            List<Mesinabsen> mesinabsenEngList = new ArrayList<>();
//            List<Mesinabsen> mesinabsenMpppList = new ArrayList<>();
//            List<Mesinabsen> mesinabsenKeuList = new ArrayList<>();
//            List<Mesinabsen> mesinabsenSdmuList = new ArrayList<>();
//            List<Mesinabsen> mesinabsenBisList = new ArrayList<>();
//            List<Mesinabsen> mesinabsenPemList = new ArrayList<>();
//            List<Mesinabsen> mesinabsenLogList = new ArrayList<>();
            int totalAll = 0;
            int totalSekper = 0;
            int totalEng = 0;
            int totalKeu = 0;
            int totalMppp = 0;
            int totalSdmu = 0;
            int totalPem = 0;
            int totalLog = 0;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate tglhariini = LocalDate.parse(date, formatter);

            //all employee
            List<Employee> employeeList = employeeRepository.findAllByStatusOrStatusOrStatusOrStatus("Cakara (Calon Karyawan)","Karyawan Tetap", "KWT (Kerja Waktu Tertentu)", "THL (Tenaga Harian Lepas)");

            if(employeeList.size() > 0){
                for (Employee employee: employeeList){
                    String idmesin = employee.getPersonIdMesinAbsen();
                    idMesinList.add(idmesin);
                }
//                mesinabsenAllList = mesinabsenRepository.findAllByPersonIdListAndDate(idMesinList,tglhariini);
                totalAll = mesinabsenRepository.countAllByPersonIdAndDate(idMesinList,tglhariini);
            }

            //sekper
            List<Employee> employeeListSekper = employeeRepository.findAllByStatusAndUnitKerjaOrStatusAndUnitKerjaOrStatusAndUnitKerjaOrStatusAndUnitKerja("Cakara (Calon Karyawan)","Bag. Sekretariat Perusahaan","Karyawan Tetap","Bag. Sekretariat Perusahaan","KWT (Kerja Waktu Tertentu)","Bag. Sekretariat Perusahaan","THL (Tenaga Harian Lepas)","Bag. Sekretariat Perusahaan");
            //System.out.println("jumlah pegawai sekper:"+employeeListSekper.size());
            if(employeeListSekper.size() > 0){
                for (Employee employee: employeeListSekper){
                    String idmesin = employee.getPersonIdMesinAbsen();
                    idMesinListSekper.add(idmesin);
                }
//                mesinabsenSekperList = mesinabsenRepository.findAllByPersonIdListAndDate(idMesinListSekper,tglhariini);
                totalSekper = mesinabsenRepository.countAllByPersonIdAndDate(idMesinListSekper,tglhariini);
            }

            //eng
            List<Employee> employeeListEng = employeeRepository.findAllByStatusAndUnitKerjaOrStatusAndUnitKerjaOrStatusAndUnitKerjaOrStatusAndUnitKerja("Cakara (Calon Karyawan)","Dept. Engineering & Pengembangan Teknologi","Karyawan Tetap","Dept. Engineering & Pengembangan Teknologi","KWT (Kerja Waktu Tertentu)","Dept. Engineering & Pengembangan Teknologi","THL (Tenaga Harian Lepas)","Dept. Engineering & Pengembangan Teknologi");
            if(employeeListEng.size() > 0){
                for (Employee employee: employeeListEng){
                    String idmesin = employee.getPersonIdMesinAbsen();
                    idMesinListEng.add(idmesin);
                }
//                mesinabsenEngList = mesinabsenRepository.findAllByPersonIdListAndDate(idMesinListEng,tglhariini);
                totalEng = mesinabsenRepository.countAllByPersonIdAndDate(idMesinListEng,tglhariini);
            }

            //keu
            List<Employee> employeeListKeu = employeeRepository.findAllByStatusAndUnitKerjaOrStatusAndUnitKerjaOrStatusAndUnitKerjaOrStatusAndUnitKerja("Cakara (Calon Karyawan)","Dept. Akuntansi & Keuangan","Karyawan Tetap","Dept. Akuntansi & Keuangan","KWT (Kerja Waktu Tertentu)","Dept. Akuntansi & Keuangan","THL (Tenaga Harian Lepas)","Dept. Akuntansi & Keuangan");
            if(employeeListKeu.size() > 0){
                for (Employee employee: employeeListKeu){
                    String idmesin = employee.getPersonIdMesinAbsen();
                    idMesinListKeu.add(idmesin);
                }
//                mesinabsenKeuList = mesinabsenRepository.findAllByPersonIdListAndDate(idMesinListKeu,tglhariini);
                totalKeu = mesinabsenRepository.countAllByPersonIdAndDate(idMesinListKeu,tglhariini);
            }

            //mppp
            List<Employee> employeeListMppp = employeeRepository.findAllByStatusAndUnitKerjaOrStatusAndUnitKerjaOrStatusAndUnitKerjaOrStatusAndUnitKerja("Cakara (Calon Karyawan)","Dept. Manajemen Proyek, Produksi & Purna Jual","Karyawan Tetap","Dept. Manajemen Proyek, Produksi & Purna Jual","KWT (Kerja Waktu Tertentu)","Dept. Manajemen Proyek, Produksi & Purna Jual","THL (Tenaga Harian Lepas)","Dept. Manajemen Proyek, Produksi & Purna Jual");
            if(employeeListMppp.size() > 0){
                for (Employee employee: employeeListMppp){
                    String idmesin = employee.getPersonIdMesinAbsen();
                    idMesinListMppp.add(idmesin);
                }
//                mesinabsenMpppList = mesinabsenRepository.findAllByPersonIdListAndDate(idMesinListMppp,tglhariini);
                totalMppp = mesinabsenRepository.countAllByPersonIdAndDate(idMesinListMppp,tglhariini);
            }

            //sdmu
            List<Employee> employeeListSdmu = employeeRepository.findAllByStatusAndUnitKerjaOrStatusAndUnitKerjaOrStatusAndUnitKerjaOrStatusAndUnitKerja("Cakara (Calon Karyawan)","Dept. SDM & Umum","Karyawan Tetap","Dept. SDM & Umum","KWT (Kerja Waktu Tertentu)","Dept. SDM & Umum","THL (Tenaga Harian Lepas)","Dept. SDM & Umum");
            if(employeeListSdmu.size() > 0){
                for (Employee employee: employeeListSdmu){
                    String idmesin = employee.getPersonIdMesinAbsen();
                    idMesinListSdmu.add(idmesin);
                }
//                mesinabsenSdmuList = mesinabsenRepository.findAllByPersonIdListAndDate(idMesinListSdmu,tglhariini);
                totalSdmu = mesinabsenRepository.countAllByPersonIdAndDate(idMesinListSdmu,tglhariini);
            }

            //log
            List<Employee> employeeListLog = employeeRepository.findAllByStatusAndUnitKerjaOrStatusAndUnitKerjaOrStatusAndUnitKerjaOrStatusAndUnitKerja("Cakara (Calon Karyawan)","Dept. Logistik","Karyawan Tetap","Dept. Logistik","KWT (Kerja Waktu Tertentu)","Dept. Logistik","THL (Tenaga Harian Lepas)","Dept. Logistik");
            if(employeeListLog.size() > 0){
                for (Employee employee: employeeListLog){
                    String idmesin = employee.getPersonIdMesinAbsen();
                    idMesinListLog.add(idmesin);
                }
//                mesinabsenLogList = mesinabsenRepository.findAllByPersonIdListAndDate(idMesinListLog,tglhariini);
                totalLog = mesinabsenRepository.countAllByPersonIdAndDate(idMesinListLog,tglhariini);
            }

            //pem
            List<Employee> employeeListPem = employeeRepository.findAllByStatusAndUnitKerjaOrStatusAndUnitKerjaOrStatusAndUnitKerjaOrStatusAndUnitKerja("Cakara (Calon Karyawan)","Dept. Pemasaran & Penjualan","Karyawan Tetap","Dept. Pemasaran & Penjualan","KWT (Kerja Waktu Tertentu)","Dept. Pemasaran & Penjualan","THL (Tenaga Harian Lepas)","Dept. Pemasaran & Penjualan");
            if(employeeListPem.size() > 0){
                for (Employee employee: employeeListPem){
                    String idmesin = employee.getPersonIdMesinAbsen();
                    idMesinListPem.add(idmesin);
                }
//                mesinabsenPemList = mesinabsenRepository.findAllByPersonIdListAndDate(idMesinListPem,tglhariini);
                totalPem = mesinabsenRepository.countAllByPersonIdAndDate(idMesinListPem,tglhariini);
            }

            //bisnis
            List<Employee> employeeListBis = employeeRepository.findAllByStatusAndUnitKerjaOrStatusAndUnitKerjaOrStatusAndUnitKerjaOrStatusAndUnitKerja("Cakara (Calon Karyawan)","Dept. Pengembangan Bisnis","Karyawan Tetap","Dept. Pengembangan Bisnis","KWT (Kerja Waktu Tertentu)","Dept. Pengembangan Bisnis","THL (Tenaga Harian Lepas)","Dept. Pengembangan Bisnis");
            if(employeeListBis.size() > 0){
                for (Employee employee: employeeListBis){
                    String idmesin = employee.getPersonIdMesinAbsen();
                    idMesinListBis.add(idmesin);
                }
//                mesinabsenBisList = mesinabsenRepository.findAllByPersonIdListAndDate(idMesinListBis,tglhariini);
                totalLog = mesinabsenRepository.countAllByPersonIdAndDate(idMesinListBis,tglhariini);
            }

            Date tmpdate = new SimpleDateFormat("dd/MM/yyyy").parse(date);
            Format f = new SimpleDateFormat("EEEE");
            String nmhari = f.format(tmpdate);
            res.setMsg("SUCCESS");
            res.setTgl(date);
            res.setHari(getNamaHari(nmhari));
//            res.setSum_check_time(mesinabsenAllList.size());
//            res.setSum_check_time_sekper(mesinabsenSekperList.size());
//            res.setSum_check_time_enginer(mesinabsenEngList.size());
//            res.setSum_check_time_keuangan(mesinabsenKeuList.size());
//            res.setSum_check_time_mppp(mesinabsenMpppList.size());
//            res.setSum_check_time_sdmu(mesinabsenSdmuList.size());
//            res.setSum_check_time_pemasaran(mesinabsenPemList.size());
//            res.setSum_check_time_logistik(mesinabsenLogList.size());
//            res.setSum_check_time_bisnis(mesinabsenBisList.size());
            res.setSum_check_time(totalAll);
            res.setSum_check_time_sekper(totalSekper);
            res.setSum_check_time_enginer(totalEng);
            res.setSum_check_time_keuangan(totalKeu);
            res.setSum_check_time_mppp(totalMppp);
            res.setSum_check_time_sdmu(totalSdmu);
            res.setSum_check_time_pemasaran(totalPem);
            res.setSum_check_time_logistik(totalLog);
            res.setSum_check_time_bisnis(totalLog);
        }
        catch (Exception ex){
            ex.printStackTrace();
            res.setMsg("ERROR: "+ex.getMessage());
        }

        return res;
    }

    public List<EmployeeCheckInApproval> getRequests(String employeeCode, Integer day, Integer month, Integer year) {
        List<EmployeeCheckInApproval> employeeCheckInApprovals = new ArrayList<>();
        try {
            Employee employee = employeeRepository.findByEmployeeCode(employeeCode);
            if (employee == null) {
                return employeeCheckInApprovals;
            }
            LokasiAbsen lokasiAbsen = employee.getLokasiAbsenId();
            if (lokasiAbsen == null) {
                lokasiAbsen = lokasiAbsenRepository.findFirstByIsDefault(true);
            }
            Map<LocalDate, Jamkerja> cachedJamKerja = new HashMap<>();
            for (PendingSwipeRequest pendingSwipeRequest : pendingSwipeRequestRepository.findAllByAssignedReviewerEmployeeCode(employeeCode)) {
                if (day != null && day != pendingSwipeRequest.getDate().getDayOfMonth()) {
                    continue;
                }
                if (month != null && month != pendingSwipeRequest.getDate().getMonthValue()) {
                    continue;
                }
                if (year != null && year != pendingSwipeRequest.getDate().getYear()) {
                    continue;
                }
                Jamkerja jamkerja = cachedJamKerja.computeIfAbsent(pendingSwipeRequest.getDate(), tgl -> jamkerjaRepository.findFirstByTanggal(tgl));
                EmployeeCheckInApproval approval = convertPendingCheckIn(pendingSwipeRequest, jamkerja, lokasiAbsen);
                employeeCheckInApprovals.add(approval);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return employeeCheckInApprovals;
    }

    public EmployeeCheckInApproval getRequest(long id) {
        PendingSwipeRequest pendingSwipeRequest = pendingSwipeRequestRepository.findById(id).orElse(null);
        if (pendingSwipeRequest == null) {
            return null;
        }
        Employee employee = employeeRepository.findByEmployeeCode(pendingSwipeRequest.getEmployeeCode());
        if (employee == null) {
            return null;
        }
        LokasiAbsen lokasiAbsen = employee.getLokasiAbsenId();
        if (lokasiAbsen == null) {
            lokasiAbsen = lokasiAbsenRepository.findFirstByIsDefault(true);
        }
        Jamkerja jamkerja = jamkerjaRepository.findFirstByTanggal(pendingSwipeRequest.getDate());
        return convertPendingCheckIn(pendingSwipeRequest, jamkerja, lokasiAbsen);
    }
}
