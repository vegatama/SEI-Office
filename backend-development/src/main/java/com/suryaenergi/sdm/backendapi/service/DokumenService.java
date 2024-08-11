package com.suryaenergi.sdm.backendapi.service;

import com.suryaenergi.sdm.backendapi.entity.Dokumen;
import com.suryaenergi.sdm.backendapi.repository.DokumenRepository;
import com.suryaenergi.sdm.backendapi.request.DokumenAddRequest;
import com.suryaenergi.sdm.backendapi.response.DokumenDetailResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

import static com.suryaenergi.sdm.backendapi.pojo.Message.ERROR_MESSAGE;
import static com.suryaenergi.sdm.backendapi.pojo.Message.SUCCESS_MESSAGE;

@Service
public class DokumenService {
    @Autowired
    private DokumenRepository dokumenRepository;

    public DokumenDetailResponse getDokumenDetail(String dokid) {
        DokumenDetailResponse res = new DokumenDetailResponse();

        try {
            Dokumen dokumen = dokumenRepository.findByDokumenId(dokid);
            if(dokumen == null)
                throw new Exception("DOKUMEN NOT FOUND");

            res.setMsg(SUCCESS_MESSAGE);
            res.setDokumen_id(dokumen.getDokumenId());
            res.setNama_dokumen(dokumen.getNamaDokumen());
            res.setNo_dokumen(dokumen.getNoDokumen());
            res.setRevisi(dokumen.getRevisi());
            res.setFile_dokumen(dokumen.getFileDokumen());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            res.setTgl_pengesehan(dokumen.getTanggalPengesahan().format(formatter));
        }
        catch (Exception ex){
            res.setMsg(ERROR_MESSAGE+ex.getMessage());
        }

        return res;
    }

    public DokumenDetailResponse addDokumen(DokumenAddRequest req) {
        DokumenDetailResponse res = new DokumenDetailResponse();

        try{

        }
        catch (Exception ex){
            res.setMsg(ERROR_MESSAGE+ex.getMessage());
        }

        return res;
    }
}
