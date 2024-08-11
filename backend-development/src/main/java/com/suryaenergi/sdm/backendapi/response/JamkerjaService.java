package com.suryaenergi.sdm.backendapi.response;

import com.suryaenergi.sdm.backendapi.entity.Jamkerja;
import com.suryaenergi.sdm.backendapi.repository.JamkerjaRepository;
import com.suryaenergi.sdm.backendapi.request.JamKerjaRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class JamkerjaService {
    @Autowired
    private JamkerjaRepository jamkerjaRepository;

    public MessageResponse addJamKerja(JamKerjaRequest req) {
        MessageResponse res = new MessageResponse();

        try {
            Jamkerja jamkerja = new Jamkerja();
            jamkerja.setJamKerjaId(UUID.randomUUID().toString());
            Jamkerja cekid = jamkerjaRepository.findByJamKerjaId(jamkerja.getJamKerjaId());
            while(cekid != null) {
                jamkerja.setJamKerjaId(UUID.randomUUID().toString());
                cekid = jamkerjaRepository.findByJamKerjaId(jamkerja.getJamKerjaId());
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            jamkerja.setTanggal(LocalDate.parse(req.getTanggal(),formatter));
            jamkerja.setKeterangan(req.getKeterangan());
            jamkerja.setJamMasuk(LocalTime.parse(req.getJam_masuk()));
            jamkerja.setJamKeluar(LocalTime.parse(req.getJam_keluar()));
            jamkerjaRepository.save(jamkerja);
            res.setMsg("SUCCESS");
        }
        catch (Exception ex){
            res.setMsg("ERROR: "+ex.getMessage());
        }

        return res;
    }
}
