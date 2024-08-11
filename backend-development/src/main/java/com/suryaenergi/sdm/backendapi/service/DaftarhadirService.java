package com.suryaenergi.sdm.backendapi.service;

import com.suryaenergi.sdm.backendapi.email.template.EmailTemplateBuilder;
import com.suryaenergi.sdm.backendapi.entity.*;
import com.suryaenergi.sdm.backendapi.pojo.*;
import com.suryaenergi.sdm.backendapi.repository.*;
import com.suryaenergi.sdm.backendapi.request.*;
import com.suryaenergi.sdm.backendapi.response.DaftarHadirResponse;
import com.suryaenergi.sdm.backendapi.response.DaftarhadirListResponse;
import com.suryaenergi.sdm.backendapi.response.MessageResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.suryaenergi.sdm.backendapi.pojo.Message.ERROR_MESSAGE;
import static com.suryaenergi.sdm.backendapi.pojo.Message.SUCCESS_MESSAGE;

@Service
public class DaftarhadirService {
    @Autowired
    private DaftarhadirRepository daftarhadirRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private PesertakegiatanRepository pesertakegiatanRepository;
    @Autowired
    private UndanganRepository undanganRepository;
    @Autowired
    private AntrianemailRepository antrianemailRepository;
    @Autowired
    private RuangMeetingRepository ruangMeetingRepository;
    @Autowired
    private GlobalNotificationRepository globalNotificationRepository;
    @Autowired
    private EmailService emailService;

    public List<ViewEventData> getActiveEvents(long ruangMeetingId) {
        List<Daftarhadir> list = daftarhadirRepository.findActive(ruangMeetingId);
        List<ViewEventData> result = new ArrayList<>(list.size());
        for (Daftarhadir event : list) {
            ViewEventData data = new ViewEventData();
            data.setKegiatan(event.getKegiatan());
            data.setSubyek(event.getSubyek());
            data.setPimpinan(event.getPimpinan());
            data.setStart(event.getTanggal().atTime(event.getWaktuMulai()));
            data.setEnd(event.getTanggal().atTime(event.getWaktuSelesai()));
            result.add(data);
        }
        return result;
    }

    public DaftarhadirListResponse getDaftarHadirByPid(String pid) {
        DaftarhadirListResponse res = new DaftarhadirListResponse();

        try{
            List<DaftarHadirData> daftarHadirData = new ArrayList<>();
            Employee employee = employeeRepository.findByEmployeeId(pid);
            if(employee == null)
                throw new Exception("PEMBUAT NOT FOUND");
            List<Daftarhadir> daftarhadirs = daftarhadirRepository.findAllByPembuat(employee);
            if(daftarhadirs.size() > 0){
                for(Daftarhadir daftarhadir:daftarhadirs){
                    DaftarHadirData hadirData = new DaftarHadirData();
                    RuangMeeting ruangMeeting = daftarhadir.getRuangMeeting();
                    if (ruangMeeting != null) {
                        hadirData.setRuang_meeting(convert(ruangMeeting));
                    }
                    hadirData.setDaftar_hadir_id(daftarhadir.getDaftarId());
                    hadirData.setSubyek(daftarhadir.getSubyek());
                    hadirData.setPimpinan(daftarhadir.getPimpinan());
                    hadirData.setKegiatan(daftarhadir.getKegiatan());
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    hadirData.setTanggal(daftarhadir.getTanggal().format(formatter));
                    DateTimeFormatter formatwaktu = DateTimeFormatter.ofPattern("HH:mm");
                    hadirData.setWaktu_mulai(daftarhadir.getWaktuMulai().format(formatwaktu));
                    hadirData.setWaktu_selesai(daftarhadir.getWaktuSelesai().format(formatwaktu));
                    hadirData.setJumlah_peserta(daftarhadir.getListPeserta().size());
                    hadirData.setTempat(daftarhadir.getTempat());
                    hadirData.setPembuat(daftarhadir.getPembuat().getEmployeeId());
                    if(hadirData.getJumlah_peserta() > 0){
                        List<DataPeserta> dataPesertas = new ArrayList<>();
                        List<Pesertakegiatan> pesertakegiatans = daftarhadir.getListPeserta();
                        for(Pesertakegiatan pesertakegiatan : pesertakegiatans){
                            DataPeserta dataPeserta = new DataPeserta();
                            dataPeserta.setNama(pesertakegiatan.getNama());
                            dataPeserta.setBagian(pesertakegiatan.getBagian());
                            dataPeserta.setEmail_phone(pesertakegiatan.getEmailphone());
                            DateTimeFormatter formatttd = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                            dataPeserta.setTanda_tangan(pesertakegiatan.getCreatedAt().format(formatttd));
                            dataPesertas.add(dataPeserta);
                        }
                        hadirData.setData_peserta(dataPesertas);
                    }
                    daftarHadirData.add(hadirData);
                }
            }
            res.setMsg("SUCCESS");
            res.setCount(daftarHadirData.size());
            res.setHadirs(daftarHadirData);
        }
        catch (Exception ex){
            res.setMsg("ERROR: "+ex.toString());
        }

        return res;
    }

    public DaftarHadirResponse getDaftarHadir(String hid) {
        DaftarHadirResponse res = new DaftarHadirResponse();

        try{
            Daftarhadir daftarhadir = daftarhadirRepository.findByDaftarId(hid);
            if(daftarhadir == null)
                throw new Exception("DAFTAR HADIR NOT FOUND");
            RuangMeeting ruangMeeting = daftarhadir.getRuangMeeting();
            if (ruangMeeting != null) {
                res.setRuang_meeting(convert(ruangMeeting));
            }
            res.setMsg(SUCCESS_MESSAGE);
            res.setDaftar_hadir_id(daftarhadir.getDaftarId());
            res.setPimpinan(daftarhadir.getPimpinan());
            res.setSubyek(daftarhadir.getSubyek());
            res.setKegiatan(daftarhadir.getKegiatan());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            res.setTanggal(daftarhadir.getTanggal().format(formatter));
            res.setJumlah_peserta(daftarhadir.getListPeserta().size());
            DateTimeFormatter formatwaktu = DateTimeFormatter.ofPattern("HH:mm");
            res.setWaktu_mulai(daftarhadir.getWaktuMulai().format(formatwaktu));
            res.setWaktu_selesai(daftarhadir.getWaktuSelesai().format(formatwaktu));
            res.setTempat(daftarhadir.getTempat());
            res.setRisalah(daftarhadir.getRisalah());
            res.setKeterangan(daftarhadir.getKeterangan());
            if(daftarhadir.getNotulen() != null) {
                DataEmployee dataNotulis = new DataEmployee();
                Employee notulis = daftarhadir.getNotulen();
                dataNotulis.setId(notulis.getEmployeeId());
                dataNotulis.setEmail(notulis.getEmail());
                dataNotulis.setName(notulis.getFullName());
                dataNotulis.setNik(notulis.getNik());
                dataNotulis.setDirektorat(notulis.getDirektorat());
                dataNotulis.setBagian_fungsi(notulis.getBagianFungsi());
                dataNotulis.setEmployee_code(notulis.getEmployeeCode());
                dataNotulis.setJob_title(notulis.getJobTitle());
                dataNotulis.setUnit_kerja(notulis.getUnitKerja());
                dataNotulis.setStatus(notulis.getStatus());
                res.setNotulis(dataNotulis);
            }

            res.setPembuat_id(daftarhadir.getPembuat().getEmployeeId());

            List<Undangan> undanganList = daftarhadir.getListUndangan();
            List<String> undangan = new ArrayList<>();
            if(undanganList.size() > 0){
                for(Undangan daftarUndangan:undanganList) {
                    String email = daftarUndangan.getEmail();
                    undangan.add(email);
                }
            }
            res.setUndangan(undangan);

            List<DataPeserta> dataPesertas = new ArrayList<>();
            if(res.getJumlah_peserta() > 0){
                List<Pesertakegiatan> pesertakegiatans = daftarhadir.getListPeserta();
                for(Pesertakegiatan pesertakegiatan : pesertakegiatans){
                    DataPeserta dataPeserta = new DataPeserta();
                    dataPeserta.setNama(pesertakegiatan.getNama());
                    dataPeserta.setBagian(pesertakegiatan.getBagian());
                    dataPeserta.setEmail_phone(pesertakegiatan.getEmailphone());
                    DateTimeFormatter formatttd = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                    dataPeserta.setTanda_tangan(pesertakegiatan.getCreatedAt().format(formatttd));
                    dataPesertas.add(dataPeserta);
                }
            }
            res.setData_peserta(dataPesertas);
        }
        catch (Exception ex){
            res.setMsg(ERROR_MESSAGE+ ex);
        }

        return res;
    }

    @Transactional
    public DaftarHadirResponse addDaftarHadir(DaftarHadirRequest req) {
        DaftarHadirResponse res = new DaftarHadirResponse();
        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter formatwaktu = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime parsedStartTime = LocalTime.parse(req.getWaktu_mulai(), formatwaktu);
            LocalDate parsedDate = LocalDate.parse(req.getTanggal(), formatter);
            LocalTime parsedEndTime = LocalTime.parse(req.getWaktu_selesai(), formatwaktu);
            Daftarhadir daftarhadir = new Daftarhadir();
            Long ruangMeeting = req.getRuang_meeting();
            if (ruangMeeting != null && ruangMeeting != -1) {
                RuangMeeting ruang = ruangMeetingRepository.findById(ruangMeeting).orElse(null);
                if (ruang == null) {
                    res.setMsg(ERROR_MESSAGE + "RUANG_MEETING_NOT_FOUND");
                    return res;
                }
                // cek jika sudah ada kegiatan di ruang tersebut
                if (validateRuangMeeting(ruang, parsedDate, parsedStartTime, parsedEndTime, null)) {
                    res.setMsg(ERROR_MESSAGE + "RUANG_MEETING_ALREADY_BOOKED");
                    return res;
                }
                req.setTempat(ruang.getName());
                daftarhadir.setRuangMeeting(ruang);
            }
            daftarhadir.setKegiatan(req.getKegiatan());
            daftarhadir.setDaftarId(RandomStringUtils.randomAlphanumeric(6));
            Daftarhadir cekid = daftarhadirRepository.findByDaftarId(daftarhadir.getDaftarId());
            while (cekid != null){
                daftarhadir.setDaftarId(RandomStringUtils.randomAlphanumeric(6));
                cekid = daftarhadirRepository.findByDaftarId(daftarhadir.getDaftarId());
            }
            daftarhadir.setPimpinan(req.getPimpinan());
            daftarhadir.setTanggal(parsedDate);
            daftarhadir.setWaktuMulai(parsedStartTime);
            daftarhadir.setWaktuSelesai(parsedEndTime);
            daftarhadir.setSubyek(req.getSubyek());
            daftarhadir.setKeterangan(req.getKeterangan());
            Employee pembuat = employeeRepository.findByEmployeeId(req.getPembuat());
            if(pembuat == null)
                throw new Exception("PEMBUAT NOT FOUND");
            daftarhadir.setPembuat(pembuat);
            daftarhadir.setTempat(req.getTempat());

            Employee notulis = null;
            if(req.getNotulis() != null && !req.getNotulis().isEmpty()){
                notulis = employeeRepository.findByEmployeeId(req.getNotulis());
                if(notulis == null)
                    throw new Exception("NOTULIS NOT FOUND");
            }
            daftarhadir.setNotulen(notulis);
            daftarhadir.setIsiUndangan(req.getIsi_undangan());
            daftarhadirRepository.save(daftarhadir);

            if(req.getUndangan() != null && !req.getUndangan().isEmpty()){
                for(String email: req.getUndangan()){
                    Undangan undangan = new Undangan();
                    undangan.setEmail(email);
                    undangan.setDaftarhadir(daftarhadir);
                    undanganRepository.save(undangan);
                    Antrianemail antrianemail = new Antrianemail();
                    antrianemail.setEmail(email);
                    antrianemail.setStatus(false);
                    antrianemail.setContent(req.getIsi_undangan());
                    antrianemail.setSubject("Undangan Kegiatan: "+req.getSubyek());
                    antrianemailRepository.save(antrianemail);
                }
            }

            res.setMsg(SUCCESS_MESSAGE);
            res.setSubyek(daftarhadir.getSubyek());
            res.setPimpinan(daftarhadir.getPimpinan());
            res.setDaftar_hadir_id(daftarhadir.getDaftarId());
            res.setKegiatan(daftarhadir.getKegiatan());
            res.setTanggal(req.getTanggal());
            res.setWaktu_mulai(req.getWaktu_mulai());
            res.setWaktu_selesai(req.getWaktu_selesai());
            res.setTempat(req.getTempat());
            res.setKeterangan(daftarhadir.getKeterangan());
            if(daftarhadir.getListPeserta() != null) {
                res.setJumlah_peserta(daftarhadir.getListPeserta().size());
                if (res.getJumlah_peserta() > 0) {
                    List<DataPeserta> dataPesertas = new ArrayList<>();
                    List<Pesertakegiatan> pesertakegiatans = daftarhadir.getListPeserta();
                    for (Pesertakegiatan pesertakegiatan : pesertakegiatans) {
                        DataPeserta dataPeserta = new DataPeserta();
                        dataPeserta.setNama(pesertakegiatan.getNama());
                        dataPeserta.setBagian(pesertakegiatan.getBagian());
                        dataPeserta.setEmail_phone(pesertakegiatan.getEmailphone());
                        DateTimeFormatter formatttd = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                        dataPeserta.setTanda_tangan(pesertakegiatan.getCreatedAt().format(formatttd));
                        dataPesertas.add(dataPeserta);
                    }
                    res.setData_peserta(dataPesertas);
                }
            }

            //send notification
            if(daftarhadir.getRuangMeeting() != null) {
                List<GlobalNotificationSettings> glnList = globalNotificationRepository.findAllByReceiveEventNotification(true);
                if (glnList.size() > 0) {
                    for (GlobalNotificationSettings gln : glnList) {
                        // send email
                        emailService.sendEmail(gln.getEmployee(), "Reservasi Ruang Meeting",
                                EmailTemplateBuilder.create("Reservasi Ruang Meeting")
                                        .append("User telah melakukan penambahan Reservasi Ruang Meeting.")
                                        .appendEntry("Kegiatan", daftarhadir.getKegiatan())
                                        .appendEntry("Subyek",daftarhadir.getSubyek())
                                        .appendEntry("Tempat", daftarhadir.getTempat())
                                        .appendEntry("Pimpinan", daftarhadir.getPimpinan())
                                        .appendEntry("User/Pemohon", daftarhadir.getPembuat().getFullName())
                                        .appendEntry("Tanggal", daftarhadir.getTanggal())
                                        .appendEntry("Waktu", daftarhadir.getWaktuMulai() + " s/d " + daftarhadir.getWaktuSelesai())
                                        .appendEntry("Keterangan", daftarhadir.getKeterangan())
                                        .generate());
                    }
                }
            }
        }
        catch (Exception ex){
            res.setMsg(ERROR_MESSAGE+ ex.getMessage());
        }


        return res;
    }

    protected boolean validateRuangMeeting(RuangMeeting ruangMeeting, LocalDate tanggal, LocalTime waktuMulai, LocalTime waktuSelesai, Daftarhadir exclude) {
        if (ruangMeeting == null) {
            return false;
        }
        TimeRange range = new TimeRange(waktuMulai.atDate(tanggal), waktuSelesai.atDate(tanggal));
        List<Daftarhadir> active = daftarhadirRepository.findAllByRuangMeetingAndTanggal(ruangMeeting, tanggal);
        for (Daftarhadir event : active) {
            if (exclude != null && event.getId().equals(exclude.getId())) {
                continue;
            }
            TimeRange eventRange = new TimeRange(event.getWaktuMulai().atDate(event.getTanggal()), event.getWaktuSelesai().atDate(event.getTanggal()));
            if (range.overlaps(eventRange)) {
                return true;
            }
        }
        return false;
    }

    public DaftarHadirResponse editDaftarHadir(DaftarHadirUpdateRequest req) {
        DaftarHadirResponse res = new DaftarHadirResponse();

        try{
            Daftarhadir daftarhadir = daftarhadirRepository.findByDaftarId(req.getDaftar_id());
            if(daftarhadir == null)
                throw new Exception("DAFTAR HADIR NOT FOUND");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter formatwaktu = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime parsedStartTime = LocalTime.parse(req.getWaktu_mulai(), formatwaktu);
            LocalDate parsedDate = LocalDate.parse(req.getTanggal(), formatter);
            LocalTime parsedEndTime = LocalTime.parse(req.getWaktu_selesai(), formatwaktu);

            Long ruangMeeting = req.getRuang_meeting_id();
            if (ruangMeeting != null && ruangMeeting != -1) {
                RuangMeeting ruang = ruangMeetingRepository.findById(ruangMeeting).orElse(null);
                if (ruang == null) {
                    res.setMsg(ERROR_MESSAGE + "RUANG_MEETING_NOT_FOUND");
                    return res;
                }
                // cek jika sudah ada kegiatan di ruang tersebut
                if (validateRuangMeeting(ruang, parsedDate, parsedStartTime, parsedEndTime, daftarhadir)) {
                    res.setMsg(ERROR_MESSAGE + "RUANG_MEETING_ALREADY_BOOKED");
                    return res;
                }
                req.setTempat(ruang.getName());
                daftarhadir.setRuangMeeting(ruang);
            } else {
                daftarhadir.setRuangMeeting(null);
            }

            daftarhadir.setPimpinan(req.getPimpinan());
            daftarhadir.setSubyek(req.getSubyek());
            daftarhadir.setKegiatan(req.getKegiatan());
            daftarhadir.setWaktuMulai(parsedStartTime);
            daftarhadir.setWaktuSelesai(parsedEndTime);
            daftarhadir.setTanggal(parsedDate);
            daftarhadir.setTempat(req.getTempat());
            daftarhadir.setKeterangan(req.getKeterangan());

            Employee notulis = null;
//            if(!req.getNotulis().equalsIgnoreCase("")){
            if (req.getNotulis() != null && !req.getNotulis().isEmpty()) {
                notulis = employeeRepository.findByEmployeeId(req.getNotulis());
                if(notulis == null)
                    throw new Exception("NOTULIS NOT FOUND");
            }
            daftarhadir.setNotulen(notulis);

            daftarhadirRepository.save(daftarhadir);

            res.setMsg(SUCCESS_MESSAGE);
            res.setSubyek(daftarhadir.getSubyek());
            res.setPimpinan(daftarhadir.getPimpinan());
            res.setDaftar_hadir_id(daftarhadir.getDaftarId());
            res.setKegiatan(daftarhadir.getKegiatan());
            res.setTanggal(req.getTanggal());
            res.setWaktu_mulai(req.getWaktu_mulai());
            res.setWaktu_selesai(req.getWaktu_selesai());
            res.setJumlah_peserta(daftarhadir.getListPeserta().size());
            res.setTempat(req.getTempat());
            res.setKeterangan(daftarhadir.getKeterangan());
            if(res.getJumlah_peserta() > 0){
                List<DataPeserta> dataPesertas = new ArrayList<>();
                List<Pesertakegiatan> pesertakegiatans = daftarhadir.getListPeserta();
                for(Pesertakegiatan pesertakegiatan : pesertakegiatans){
                    DataPeserta dataPeserta = new DataPeserta();
                    dataPeserta.setNama(pesertakegiatan.getNama());
                    dataPeserta.setBagian(pesertakegiatan.getBagian());
                    dataPeserta.setEmail_phone(pesertakegiatan.getEmailphone());
                    DateTimeFormatter formatttd = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                    dataPeserta.setTanda_tangan(pesertakegiatan.getCreatedAt().format(formatttd));
                    dataPesertas.add(dataPeserta);
                }
                res.setData_peserta(dataPesertas);
            }

            //send notification
            if(daftarhadir.getRuangMeeting() != null) {
                List<GlobalNotificationSettings> glnList = globalNotificationRepository.findAllByReceiveEventNotification(true);
                if (glnList.size() > 0) {
                    for (GlobalNotificationSettings gln : glnList) {
                        // send email
                        emailService.sendEmail(gln.getEmployee(), "Update Reservasi Ruang Meeting",
                                EmailTemplateBuilder.create("Update Reservasi Ruang Meeting")
                                        .append("User telah melakukan penambahan Reservasi Ruang Meeting.")
                                        .appendEntry("Kegiatan", daftarhadir.getKegiatan())
                                        .appendEntry("Subyek",daftarhadir.getSubyek())
                                        .appendEntry("Tempat", daftarhadir.getTempat())
                                        .appendEntry("Pimpinan", daftarhadir.getPimpinan())
                                        .appendEntry("User/Pemohon", daftarhadir.getPembuat().getFullName())
                                        .appendEntry("Tanggal", daftarhadir.getTanggal())
                                        .appendEntry("Waktu", daftarhadir.getWaktuMulai() + " s/d " + daftarhadir.getWaktuSelesai())
                                        .appendEntry("Keterangan", daftarhadir.getKeterangan())
                                        .generate());
                    }
                }
            }
        }
        catch (Exception ex){
            res.setMsg(ERROR_MESSAGE+ ex);
        }

        return res;
    }

    public MessageResponse deleteDaftarHadir(String hid) {
        MessageResponse res = new MessageResponse();

        try{
            Daftarhadir daftarhadir = daftarhadirRepository.findByDaftarId(hid);
            if(daftarhadir == null)
                throw new Exception("DAFTAR HADIR NOT FOUND");
            daftarhadirRepository.delete(daftarhadir);
            res.setMsg(SUCCESS_MESSAGE);

            //send notification
            if(daftarhadir.getRuangMeeting() != null) {
                List<GlobalNotificationSettings> glnList = globalNotificationRepository.findAllByReceiveEventNotification(true);
                if (glnList.size() > 0) {
                    for (GlobalNotificationSettings gln : glnList) {
                        // send email
                        emailService.sendEmail(gln.getEmployee(), "Penghapusan Data Reservasi Ruang Meeting",
                                EmailTemplateBuilder.create("Penghapusan Data Reservasi Ruang Meeting")
                                        .append("User telah melakukan penambahan Reservasi Ruang Meeting.")
                                        .appendEntry("Kegiatan", daftarhadir.getKegiatan())
                                        .appendEntry("Subyek",daftarhadir.getSubyek())
                                        .appendEntry("Tempat", daftarhadir.getTempat())
                                        .appendEntry("Pimpinan", daftarhadir.getPimpinan())
                                        .appendEntry("User/Pemohon", daftarhadir.getPembuat().getFullName())
                                        .appendEntry("Tanggal", daftarhadir.getTanggal())
                                        .appendEntry("Waktu", daftarhadir.getWaktuMulai() + " s/d " + daftarhadir.getWaktuSelesai())
                                        .appendEntry("Keterangan", daftarhadir.getKeterangan())
                                        .generate());
                    }
                }
            }
        }
        catch (Exception ex){
            res.setMsg(ERROR_MESSAGE+ ex);
        }

        return res;
    }

    public MessageResponse addPeserta(PesertaRequest req) {
        MessageResponse res = new MessageResponse();

        try{
            Daftarhadir daftarhadir = daftarhadirRepository.findByDaftarId(req.getDaftar_id());
            if (daftarhadir == null)
                throw new Exception("DAFTAR HADIR NOT FOUND");

            List<Pesertakegiatan> pesertakegiatans = daftarhadir.getListPeserta();
            List<String> emails = new ArrayList<>();
            for(Pesertakegiatan pesertakegiatan:pesertakegiatans){
                emails.add(pesertakegiatan.getEmailphone());
            }
            if(!emails.contains(req.getEmail_phone())) {
                Pesertakegiatan peserta = new Pesertakegiatan();
                peserta.setDaftarhadir(daftarhadir);
                peserta.setNama(req.getNama());
                peserta.setBagian(req.getBagian());
                peserta.setEmailphone(req.getEmail_phone());
                pesertakegiatanRepository.save(peserta);
            }

            res.setMsg(SUCCESS_MESSAGE);
        }
        catch (Exception ex){
            res.setMsg(ERROR_MESSAGE+ ex);
        }

        return res;
    }

    private RuangMeetingData convert(RuangMeeting ruangMeeting) {
        RuangMeetingData ruangMeetingData = new RuangMeetingData();
        ruangMeetingData.setId(ruangMeeting.getId());
        ruangMeetingData.setName(ruangMeeting.getName());
        ruangMeetingData.setCapacity(ruangMeeting.getCapacity());
        ruangMeetingData.setDescription(ruangMeeting.getDescription());
        return ruangMeetingData;
    }

    public DaftarhadirListResponse getListDaftarHadir() {
        DaftarhadirListResponse res = new DaftarhadirListResponse();

        try{
            List<DaftarHadirData> daftarHadirData = new ArrayList<>();
            List<Daftarhadir> daftarhadirs = (List<Daftarhadir>) daftarhadirRepository.findAll();
            if(daftarhadirs.size() > 0){
                for(Daftarhadir daftarhadir:daftarhadirs){
                    DaftarHadirData hadirData = new DaftarHadirData();
                    RuangMeeting ruangMeeting = daftarhadir.getRuangMeeting();
                    if (ruangMeeting != null) {
                        hadirData.setRuang_meeting(convert(ruangMeeting));
                    }
                    hadirData.setDaftar_hadir_id(daftarhadir.getDaftarId());
                    hadirData.setSubyek(daftarhadir.getSubyek());
                    hadirData.setPimpinan(daftarhadir.getPimpinan());
                    hadirData.setKegiatan(daftarhadir.getKegiatan());
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    hadirData.setTanggal(daftarhadir.getTanggal().format(formatter));
                    DateTimeFormatter formatwaktu = DateTimeFormatter.ofPattern("HH:mm");
                    hadirData.setWaktu_mulai(daftarhadir.getWaktuMulai().format(formatwaktu));
                    hadirData.setWaktu_selesai(daftarhadir.getWaktuSelesai().format(formatwaktu));
                    hadirData.setJumlah_peserta(daftarhadir.getListPeserta().size());
                    hadirData.setTempat(daftarhadir.getTempat());
                    hadirData.setPembuat(daftarhadir.getPembuat().getEmployeeId());
                    hadirData.setRisalah(daftarhadir.getRisalah());
                    hadirData.setKeterangan(daftarhadir.getKeterangan());
                    if(daftarhadir.getNotulen() != null) {
                        hadirData.setNotulis(daftarhadir.getNotulen().getEmployeeId());
                    }
                    if(hadirData.getJumlah_peserta() > 0){
                        List<DataPeserta> dataPesertas = new ArrayList<>();
                        List<Pesertakegiatan> pesertakegiatans = daftarhadir.getListPeserta();
                        for(Pesertakegiatan pesertakegiatan : pesertakegiatans){
                            DataPeserta dataPeserta = new DataPeserta();
                            dataPeserta.setNama(pesertakegiatan.getNama());
                            dataPeserta.setBagian(pesertakegiatan.getBagian());
                            dataPeserta.setEmail_phone(pesertakegiatan.getEmailphone());
                            DateTimeFormatter formatttd = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                            dataPeserta.setTanda_tangan(pesertakegiatan.getCreatedAt().format(formatttd));
                            dataPesertas.add(dataPeserta);
                        }
                        hadirData.setData_peserta(dataPesertas);
                    }
                    daftarHadirData.add(hadirData);
                }
            }
            res.setMsg("SUCCESS");
            res.setCount(daftarHadirData.size());
            res.setHadirs(daftarHadirData);
        }
        catch (Exception ex){
            res.setMsg("ERROR: "+ex.toString());
        }

        return res;
    }

    public DaftarHadirResponse updateRisalah(RisalahUpdateRequest req) {
        DaftarHadirResponse res = new DaftarHadirResponse();

        try{
            Daftarhadir daftarhadir = daftarhadirRepository.findByDaftarId(req.getDaftar_hadir_id());
            if(daftarhadir == null)
                throw new Exception("DAFTAR HADIR NOT FOUND");
            daftarhadir.setRisalah(req.getRisalah());
            daftarhadirRepository.save(daftarhadir);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter formatwaktu = DateTimeFormatter.ofPattern("HH:mm");
            res.setMsg(SUCCESS_MESSAGE);
            res.setSubyek(daftarhadir.getSubyek());
            res.setPimpinan(daftarhadir.getPimpinan());
            res.setDaftar_hadir_id(daftarhadir.getDaftarId());
            res.setKegiatan(daftarhadir.getKegiatan());
            res.setTanggal(daftarhadir.getTanggal().format(formatter));
            res.setWaktu_mulai(daftarhadir.getWaktuMulai().format(formatwaktu));
            res.setWaktu_selesai(daftarhadir.getWaktuSelesai().format(formatwaktu));
            res.setJumlah_peserta(daftarhadir.getListPeserta().size());
            res.setTempat(daftarhadir.getTempat());
            if(res.getJumlah_peserta() > 0){
                List<DataPeserta> dataPesertas = new ArrayList<>();
                List<Pesertakegiatan> pesertakegiatans = daftarhadir.getListPeserta();
                for(Pesertakegiatan pesertakegiatan : pesertakegiatans){
                    DataPeserta dataPeserta = new DataPeserta();
                    dataPeserta.setNama(pesertakegiatan.getNama());
                    dataPeserta.setBagian(pesertakegiatan.getBagian());
                    dataPeserta.setEmail_phone(pesertakegiatan.getEmailphone());
                    DateTimeFormatter formatttd = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                    dataPeserta.setTanda_tangan(pesertakegiatan.getCreatedAt().format(formatttd));
                    dataPesertas.add(dataPeserta);
                }
                res.setData_peserta(dataPesertas);
            }
        }
        catch (Exception ex){
            res.setMsg("ERROR: "+ex.toString());
        }

        return res;
    }

    public MessageResponse finishKegiatan(DaftarHadirFinish req) {
        MessageResponse res = new MessageResponse();

        try{
            DateTimeFormatter formatwaktu = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime parsedEndTime = LocalTime.parse(req.getWaktu_selesai(), formatwaktu);
            Daftarhadir daftarhadir = daftarhadirRepository.findByDaftarId(req.getDaftar_hadir_id());
            if(daftarhadir == null)
                throw new Exception("DAFTAR HADIR NOT FOUND");
            daftarhadir.setWaktuSelesai(parsedEndTime);
            daftarhadirRepository.save(daftarhadir);
            res.setMsg("SUCCESS");

            //send notification
            if(daftarhadir.getRuangMeeting() != null) {
                List<GlobalNotificationSettings> glnList = globalNotificationRepository.findAllByReceiveEventNotification(true);
                if (glnList.size() > 0) {
                    for (GlobalNotificationSettings gln : glnList) {
                        // send email
                        emailService.sendEmail(gln.getEmployee(), "Update Reservasi Ruang Meeting",
                                EmailTemplateBuilder.create("Update Reservasi Ruang Meeting")
                                        .append("User telah melakukan penambahan Reservasi Ruang Meeting.")
                                        .appendEntry("Kegiatan", daftarhadir.getKegiatan())
                                        .appendEntry("Subyek",daftarhadir.getSubyek())
                                        .appendEntry("Tempat", daftarhadir.getTempat())
                                        .appendEntry("Pimpinan", daftarhadir.getPimpinan())
                                        .appendEntry("User/Pemohon", daftarhadir.getPembuat().getFullName())
                                        .appendEntry("Tanggal", daftarhadir.getTanggal())
                                        .appendEntry("Waktu", daftarhadir.getWaktuMulai() + " s/d " + daftarhadir.getWaktuSelesai())
                                        .appendEntry("Keterangan", daftarhadir.getKeterangan())
                                        .generate());
                    }
                }
            }
        }
        catch (Exception ex){
            res.setMsg("ERROR: "+ex.toString());
        }

        return res;
    }
}
