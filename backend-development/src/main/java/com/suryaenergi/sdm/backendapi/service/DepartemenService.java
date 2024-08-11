package com.suryaenergi.sdm.backendapi.service;

import com.suryaenergi.sdm.backendapi.entity.Departemen;
import com.suryaenergi.sdm.backendapi.pojo.DataDepartemen;
import com.suryaenergi.sdm.backendapi.repository.DepartemenRepository;
import com.suryaenergi.sdm.backendapi.request.DepartemenRequest;
import com.suryaenergi.sdm.backendapi.response.DeptListResponse;
import com.suryaenergi.sdm.backendapi.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class DepartemenService {
    @Autowired
    private DepartemenRepository departemenRepository;

    public DeptListResponse getDeptList(int no, int size) {
        DeptListResponse res = new DeptListResponse();

        Pageable paging = PageRequest.of(no,size);
        List<DataDepartemen> dataDepartemenList = new ArrayList<>();
        try{
            Page<Departemen> departemenPage = departemenRepository.findAll(paging);
            if(departemenPage.hasContent()){
                for(Departemen departemen:departemenPage){
                    DataDepartemen dataDepartemen = new DataDepartemen();
                    dataDepartemen.setId(departemen.getDepartemenId());
                    dataDepartemen.setName(departemen.getName());
                    dataDepartemenList.add(dataDepartemen);
                }
                res.setMsg("SUCCESS");
                res.setCount(dataDepartemenList.size());
                res.setData(dataDepartemenList);
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

    public MessageResponse addDepartemen(DepartemenRequest req) {
        MessageResponse res = new MessageResponse();

        try{
            Departemen departemen = new Departemen();
            departemen.setDepartemenId(UUID.randomUUID().toString());
            Departemen cekid = departemenRepository.findByDepartemenId(departemen.getDepartemenId());
            while(cekid != null){
                departemen.setDepartemenId(UUID.randomUUID().toString());
                cekid = departemenRepository.findByDepartemenId(departemen.getDepartemenId());
            }
            departemen.setName(req.getName());
            departemenRepository.save(departemen);
            res.setMsg("SUCCESS");
        }
        catch (Exception ex){
            res.setMsg("ERROR: "+ex.getMessage());
        }

        return res;
    }
}
