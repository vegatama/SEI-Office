package com.suryaenergi.sdm.backendapi.service;

import com.suryaenergi.sdm.backendapi.entity.HariLibur;
import com.suryaenergi.sdm.backendapi.pojo.DataHariLibur;
import com.suryaenergi.sdm.backendapi.repository.HariliburRepository;
import com.suryaenergi.sdm.backendapi.request.HariLiburRequest;
import com.suryaenergi.sdm.backendapi.response.HariLiburResponse;
import com.suryaenergi.sdm.backendapi.response.HariliburListResponse;
import com.suryaenergi.sdm.backendapi.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class HariliburService {
    @Autowired
    private HariliburRepository hariliburRepository;

    public HariliburListResponse getHariliburList(int no, int size) {
        HariliburListResponse res = new HariliburListResponse();

        Pageable paging = PageRequest.of(no,size);
        List<DataHariLibur> dataHariLiburList = new ArrayList<>();
        try{
            Page<HariLibur> hariLiburs = hariliburRepository.findAll(paging);
            if(hariLiburs.hasContent()){
                for(HariLibur hariLibur: hariLiburs){
                    DataHariLibur dataHariLibur = new DataHariLibur();
                    dataHariLibur.setTanggal(hariLibur.getTanggal());
                    dataHariLibur.setId(hariLibur.getHariLiburId());
                    dataHariLibur.setKeterangan(hariLibur.getKeterangan());
                    dataHariLiburList.add(dataHariLibur);
                }
                res.setMsg("SUCCESS");
                res.setCount(dataHariLiburList.size());
                res.setData(dataHariLiburList);
            }
            else {
                res.setMsg("SUCCESS");
                res.setCount(0);
            }
        }
        catch (Exception ex){
            res.setMsg("ERROR: "+ex.getMessage());
        }

        return res;
    }

    public MessageResponse deleteHariLibur(String id) {
        MessageResponse res = new MessageResponse();

        try{
            HariLibur hariLibur = hariliburRepository.findByHariLiburId(id);
            if(hariLibur != null){
                hariliburRepository.delete(hariLibur);
                res.setMsg("SUCCESS");
            }
            else {
                res.setMsg("ERROR: Data not found");
            }
        }
        catch (Exception ex){
            res.setMsg("ERROR: "+ex.getMessage());
        }

        return res;
    }

    public MessageResponse updateHariLibur(HariLiburRequest req) {
        MessageResponse res = new MessageResponse();

        try{
            HariLibur hariLibur = hariliburRepository.findByHariLiburId(req.getId());
            if(hariLibur != null){
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                hariLibur.setTanggal(LocalDate.parse(req.getTgl(),formatter));
                hariLibur.setKeterangan(req.getKeterangan());
                hariliburRepository.save(hariLibur);
                res.setMsg("SUCCESS");
            }
            else {
                res.setMsg("ERROR: Data not found");
            }
        }
        catch (Exception ex){
            res.setMsg("ERROR: "+ex.getMessage());
        }

        return res;
    }

    public MessageResponse addHariLibur(HariLiburRequest req) {
        MessageResponse res = new MessageResponse();

        try{
            HariLibur hariLibur = new HariLibur();
            hariLibur.setHariLiburId(UUID.randomUUID().toString());
            HariLibur cekid = hariliburRepository.findByHariLiburId(hariLibur.getHariLiburId());
            while(cekid != null){
                hariLibur.setHariLiburId(UUID.randomUUID().toString());
                cekid = hariliburRepository.findByHariLiburId(hariLibur.getHariLiburId());
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            hariLibur.setTanggal(LocalDate.parse(req.getTgl(),formatter));
            hariLibur.setKeterangan(req.getKeterangan());
            hariliburRepository.save(hariLibur);
            res.setMsg("SUCCESS");
        }
        catch (Exception ex){
            res.setMsg("ERROR: "+ex.getMessage());
        }

        return res;
    }

    public HariliburListResponse getHariliburListByTahun(int tahun) {
        HariliburListResponse res = new HariliburListResponse();

        List<DataHariLibur> dataHariLiburList = new ArrayList<>();
        try{
            List<HariLibur> hariLiburs = hariliburRepository.findAllByTahun(tahun);
            if(hariLiburs.size() > 0){
                for(HariLibur hariLibur: hariLiburs){
                    DataHariLibur dataHariLibur = new DataHariLibur();
                    dataHariLibur.setTanggal(hariLibur.getTanggal());
                    dataHariLibur.setId(hariLibur.getHariLiburId());
                    dataHariLibur.setKeterangan(hariLibur.getKeterangan());
                    dataHariLiburList.add(dataHariLibur);
                }
                res.setMsg("SUCCESS");
                res.setCount(dataHariLiburList.size());
                res.setData(dataHariLiburList);
            }
            else {
                res.setMsg("SUCCESS");
                res.setCount(0);
            }
        }
        catch (Exception ex){
            res.setMsg("ERROR: "+ex.getMessage());
        }

        return res;
    }

    public HariLiburResponse getHariLiburById(String id) {
        HariLiburResponse res = new HariLiburResponse();
        try{
            HariLibur hariLibur = hariliburRepository.findByHariLiburId(id);
            if(hariLibur != null){
                res.setId(hariLibur.getHariLiburId());
                res.setTanggal(hariLibur.getTanggal().toString());
                res.setKeterangan(hariLibur.getKeterangan());
                res.setMessage("SUCCESS");
            }
            else {
                res.setMessage("ERROR: Data not found");
            }
        }
        catch (Exception ex){
            res.setMessage("ERROR: "+ex.getMessage());
        }

        return res;
    }
}
