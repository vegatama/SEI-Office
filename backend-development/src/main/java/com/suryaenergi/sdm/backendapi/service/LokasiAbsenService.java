package com.suryaenergi.sdm.backendapi.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suryaenergi.sdm.backendapi.entity.LokasiAbsen;
import com.suryaenergi.sdm.backendapi.pojo.LokasiAbsenData;
import com.suryaenergi.sdm.backendapi.repository.LokasiAbsenRepository;
import com.suryaenergi.sdm.backendapi.request.LokasiAbsenRequest;
import com.suryaenergi.sdm.backendapi.response.LokasiAbsenListResponse;
import com.suryaenergi.sdm.backendapi.response.LokasiAbsenResponse;
import com.suryaenergi.sdm.backendapi.response.MessageResponse;

@Service
public class LokasiAbsenService {
    
    @Autowired
    private LokasiAbsenRepository lokasiAbsenRepository;

    public MessageResponse addLokasi(LokasiAbsenRequest request){
        
        MessageResponse res = new MessageResponse();
        try {
            LokasiAbsen lokasiAbsen = new LokasiAbsen();
            lokasiAbsen.setLokasiAbsen(request.getLokasi_absen());
            lokasiAbsen.setLatitude(request.getLatitude());
            lokasiAbsen.setLongitude(request.getLongitude());
            lokasiAbsen.setRadius(request.getRadius());
            lokasiAbsen.setIsDefault(request.getIsDefault());
            lokasiAbsenRepository.save(lokasiAbsen);
            res.setMsg("SUCCESS");
        } catch (Exception ex) {
            res.setMsg("ERROR: "+ex.getMessage());
        }
        return res;
    }

    public LokasiAbsenListResponse getLokasiList (){
        LokasiAbsenListResponse res = new LokasiAbsenListResponse();
        List<LokasiAbsenData> lokasiList = new ArrayList<>();
        try {
            List<LokasiAbsen> listLokasi = (List<LokasiAbsen>) lokasiAbsenRepository.findAll();
            if (listLokasi.size() > 0) {
                for(LokasiAbsen lokasiAbsen:listLokasi){
                    LokasiAbsenData lokasiAbsenData = new LokasiAbsenData();
                    lokasiAbsenData.setId(lokasiAbsen.getId());
                    lokasiAbsenData.setLokasi_absen(lokasiAbsen.getLokasiAbsen());
                    lokasiAbsenData.setLatitude(lokasiAbsen.getLatitude());
                    lokasiAbsenData.setLongitude(lokasiAbsen.getLongitude());
                    lokasiAbsenData.setRadius(lokasiAbsen.getRadius());
                    lokasiAbsenData.setIsDefault(lokasiAbsen.getIsDefault());
                    lokasiList.add(lokasiAbsenData);
                }
            }
            res.setMsg("SUCCESS");
            res.setCount(lokasiList.size());
            res.setLokasiAbsen(lokasiList);
        } catch (Exception ex) {
            res.setMsg("ERROR: "+ex.getMessage());
        }
        return res;
    }

    public MessageResponse updateLokasi(LokasiAbsenRequest req){
        MessageResponse res = new MessageResponse();
        try {
            LokasiAbsen lokasiAbsen = lokasiAbsenRepository.findLokasiById(req.getId());

            if (lokasiAbsen == null) {
                throw new Exception("NOT FOUND");
            }
            lokasiAbsen.setLokasiAbsen(req.getLokasi_absen());
            lokasiAbsen.setLatitude(req.getLatitude());
            lokasiAbsen.setLongitude(req.getLongitude());
            lokasiAbsen.setRadius(req.getRadius());
            lokasiAbsen.setIsDefault(req.getIsDefault());
            lokasiAbsenRepository.save(lokasiAbsen);
            res.setMsg("SUCCESS");
        } catch (Exception ex) {
            res.setMsg("ERROR: "+ex.getMessage());
        }
        return res; 
    }

    public MessageResponse deleteLokasi(Long id){
        MessageResponse res = new MessageResponse();
        try {
            LokasiAbsen lokasiAbsen = lokasiAbsenRepository.findLokasiById(id);
            if (lokasiAbsen == null) {
                throw new Exception("NOT FOUND");
            }
            lokasiAbsenRepository.delete(lokasiAbsen);
            res.setMsg("SUCCESS");
        } catch (Exception ex) {
            res.setMsg("ERROR: "+ex.getMessage());
        }
        return res;
    }

    public LokasiAbsenResponse getLokasiById(Long id){
        LokasiAbsenResponse res = new LokasiAbsenResponse();
        try {
            LokasiAbsen lokasiAbsen = lokasiAbsenRepository.findLokasiById(id);
            if (lokasiAbsen == null) {
                throw new Exception("NOT FOUND");
            }
            res.setMsg("SUCCESS");
            res.setId(lokasiAbsen.getId());
            res.setLokasi_absen(lokasiAbsen.getLokasiAbsen());
            res.setLatitude(lokasiAbsen.getLatitude());
            res.setLongitude(lokasiAbsen.getLongitude());
            res.setRadius(lokasiAbsen.getRadius());
            res.setIsDefault(lokasiAbsen.getIsDefault());
        } catch (Exception ex) {
            res.setMsg("ERROR: "+ex.getMessage());
        }
        return res;
    }
}
