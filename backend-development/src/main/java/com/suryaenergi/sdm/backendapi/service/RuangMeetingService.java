package com.suryaenergi.sdm.backendapi.service;

import com.suryaenergi.sdm.backendapi.entity.Daftarhadir;
import com.suryaenergi.sdm.backendapi.entity.RuangMeeting;
import com.suryaenergi.sdm.backendapi.pojo.RuangMeetingData;
import com.suryaenergi.sdm.backendapi.repository.DaftarhadirRepository;
import com.suryaenergi.sdm.backendapi.repository.RuangMeetingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class RuangMeetingService {

    @Autowired
    private RuangMeetingRepository ruangMeetingRepository;

    @Autowired
    private DaftarhadirRepository daftarhadirRepository;

    public List<RuangMeetingData> getAllRuangMeeting() {
        List<RuangMeetingData> ruangMeetingDataList = new ArrayList<>();
        ruangMeetingRepository.findAll().forEach(ruangMeeting -> {
            RuangMeetingData ruangMeetingData = new RuangMeetingData();
            ruangMeetingData.setId(ruangMeeting.getId());
            ruangMeetingData.setName(ruangMeeting.getName());
            ruangMeetingData.setCapacity(ruangMeeting.getCapacity());
            ruangMeetingData.setDescription(ruangMeeting.getDescription());
            Daftarhadir daftarhadir = daftarhadirRepository.getCurrentMeeting(ruangMeeting, LocalDate.now(), LocalTime.now());
            if (daftarhadir != null) {
                ruangMeetingData.setActiveEventId(daftarhadir.getId());
                ruangMeetingData.setActiveEventName(daftarhadir.getSubyek());
                LocalDate tanggal = daftarhadir.getTanggal();
                LocalTime waktuMulai = daftarhadir.getWaktuMulai();
                LocalTime waktuSelesai = daftarhadir.getWaktuSelesai();
                ruangMeetingData.setActiveEventStart(LocalDateTime.of(tanggal, waktuMulai));
                ruangMeetingData.setActiveEventEnd(LocalDateTime.of(tanggal, waktuSelesai));
            }
            ruangMeetingDataList.add(ruangMeetingData);
        });
        return ruangMeetingDataList;
    }

    public RuangMeetingData getRuangMeeting(long id) {
        RuangMeetingData ruangMeetingData = new RuangMeetingData();
        ruangMeetingData.setId(id);
//        ruangMeetingRepository.findById(id).ifPresent(ruangMeeting -> {
//            ruangMeetingData.setName(ruangMeeting.getName());
//            ruangMeetingData.setCapacity(ruangMeeting.getCapacity());
//            ruangMeetingData.setDescription(ruangMeeting.getDescription());
//            List<Daftarhadir> daftarhadirList = daftarhadirRepository.findAllByRuangMeetingAndTanggal(ruangMeeting, LocalDate.now());
//            for (Daftarhadir daftarhadir : daftarhadirList) {
//                if (daftarhadir.getWaktuMulai().isBefore(LocalTime.now()) && daftarhadir.getWaktuSelesai().isAfter(LocalTime.now())) {
//                    ruangMeetingData.setActiveEventId(daftarhadir.getId());
//                    ruangMeetingData.setActiveEventName(daftarhadir.getSubyek());
//                    LocalDate tanggal = daftarhadir.getTanggal();
//                    LocalTime waktuMulai = daftarhadir.getWaktuMulai();
//                    LocalTime waktuSelesai = daftarhadir.getWaktuSelesai();
//                    ruangMeetingData.setActiveEventStart(LocalDateTime.of(tanggal, waktuMulai));
//                    ruangMeetingData.setActiveEventEnd(LocalDateTime.of(tanggal, waktuSelesai));
//                    break;
//                }
//            }
//        });
        RuangMeeting ruangMeeting = ruangMeetingRepository.findById(id).orElse(null);
        if (ruangMeeting == null) {
            throw new RuntimeException("NOT_FOUND");
        }
        ruangMeetingData.setName(ruangMeeting.getName());
        ruangMeetingData.setCapacity(ruangMeeting.getCapacity());
        ruangMeetingData.setDescription(ruangMeeting.getDescription());
        List<Daftarhadir> daftarhadirList = daftarhadirRepository.findAllByRuangMeetingAndTanggal(ruangMeeting, LocalDate.now());
        for (Daftarhadir daftarhadir : daftarhadirList) {
            if (daftarhadir.getWaktuMulai().isBefore(LocalTime.now()) && daftarhadir.getWaktuSelesai().isAfter(LocalTime.now())) {
                ruangMeetingData.setActiveEventId(daftarhadir.getId());
                ruangMeetingData.setActiveEventName(daftarhadir.getSubyek());
                LocalDate tanggal = daftarhadir.getTanggal();
                LocalTime waktuMulai = daftarhadir.getWaktuMulai();
                LocalTime waktuSelesai = daftarhadir.getWaktuSelesai();
                ruangMeetingData.setActiveEventStart(LocalDateTime.of(tanggal, waktuMulai));
                ruangMeetingData.setActiveEventEnd(LocalDateTime.of(tanggal, waktuSelesai));
                break;
            }
        }
        return ruangMeetingData;
    }

    public void updateRuangMeeting(RuangMeetingData ruangMeetingData) {
        ruangMeetingRepository.findById(ruangMeetingData.getId()).ifPresent(ruangMeeting -> {
            ruangMeeting.setName(ruangMeetingData.getName());
            ruangMeeting.setCapacity(ruangMeetingData.getCapacity());
            ruangMeeting.setDescription(ruangMeetingData.getDescription());
            ruangMeetingRepository.save(ruangMeeting);
        });
    }

    public void deleteRuangMeeting(long id) {
        ruangMeetingRepository.deleteById(id);
    }

    public void addRuangMeeting(RuangMeetingData ruangMeetingData) {
        RuangMeeting ruangMeeting = new RuangMeeting();
        ruangMeeting.setName(ruangMeetingData.getName());
        ruangMeeting.setCapacity(ruangMeetingData.getCapacity());
        ruangMeeting.setDescription(ruangMeetingData.getDescription());
        ruangMeetingRepository.save(ruangMeeting);
    }
}
