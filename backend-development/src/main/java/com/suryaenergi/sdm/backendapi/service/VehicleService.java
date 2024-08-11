package com.suryaenergi.sdm.backendapi.service;

import com.suryaenergi.sdm.backendapi.entity.Vehicles;
import com.suryaenergi.sdm.backendapi.pojo.VehiclesData;
import com.suryaenergi.sdm.backendapi.repository.VehicleRepository;
import com.suryaenergi.sdm.backendapi.request.VehicleRequest;
import com.suryaenergi.sdm.backendapi.request.VehicleUpdateRequest;
import com.suryaenergi.sdm.backendapi.response.MessageResponse;
import com.suryaenergi.sdm.backendapi.response.VehicleListResponse;
import com.suryaenergi.sdm.backendapi.response.VehicleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class VehicleService {
    @Autowired
    private VehicleRepository vehicleRepository;

    public VehicleListResponse getVehicleList() {
        VehicleListResponse res = new VehicleListResponse();
        List<VehiclesData> vehiclesDataList = new ArrayList<>();

        try{
            List<Vehicles> vehicles = (List<Vehicles>) vehicleRepository.findAll();
            if(vehicles.size() > 0){
                for(Vehicles vehicle:vehicles){
                    VehiclesData vehiclesData = new VehiclesData();
                    vehiclesData.setVehicle_id(vehicle.getVehicleId());
                    vehiclesData.setType(vehicle.getType());
                    vehiclesData.setYear(vehicle.getYear());
                    vehiclesData.setPlat_number(vehicle.getPlatNumber());
                    vehiclesData.setOwnership(vehicle.getOwnership());
                    vehiclesData.setCertifcate_expired(vehicle.getCertificateExpiredDate());
                    vehiclesData.setTax_expired(vehicle.getTaxExpiredDate());
                    vehiclesData.setMerk(vehicle.getMerk());
                    vehiclesData.setBbm(vehicle.getBbm());
                    vehiclesData.setKeterangan(vehicle.getKeterangan());
                    vehiclesDataList.add(vehiclesData);
                }
            }
            res.setMsg("SUCCESS");
            res.setCount(vehiclesDataList.size());
            res.setVehicles(vehiclesDataList);
        }
        catch (Exception ex){
            res.setMsg("ERROR: "+ex.getMessage());
        }

        return res;
    }

    public MessageResponse addVehicle(VehicleRequest req) {
        MessageResponse res = new MessageResponse();

        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            Vehicles vehicle = new Vehicles();
            vehicle.setVehicleId(UUID.randomUUID().toString());
            Vehicles cekid = vehicleRepository.findByVehicleId(vehicle.getVehicleId());
            while (cekid != null){
                vehicle.setVehicleId(UUID.randomUUID().toString());
                cekid = vehicleRepository.findByVehicleId(vehicle.getVehicleId());
            }
            vehicle.setPlatNumber(req.getPlat_number());
            vehicle.setYear(req.getYear());
            vehicle.setMerk(req.getMerk());
            vehicle.setType(req.getType());
            vehicle.setOwnership(req.getOwnership());
            if(req.getCertificate_expired() != null && !req.getCertificate_expired().equalsIgnoreCase(""))
                vehicle.setCertificateExpiredDate(LocalDate.parse(req.getCertificate_expired(),formatter));
            if(req.getTax_expired() != null && !req.getTax_expired().equalsIgnoreCase(""))
                vehicle.setTaxExpiredDate(LocalDate.parse(req.getTax_expired(),formatter));
            vehicle.setBbm(req.getBbm());
            vehicle.setKeterangan(req.getKeterangan());
            vehicleRepository.save(vehicle);
            res.setMsg("SUCCESS");
        }
        catch (Exception ex){
            res.setMsg("ERROR: "+ex.getMessage());
        }

        return res;
    }

    public MessageResponse updateVehicle(VehicleUpdateRequest req) {
        MessageResponse res = new MessageResponse();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try{
            Vehicles vehicle = vehicleRepository.findByVehicleId(req.getVehicle_id());
            if(vehicle == null)
                throw new Exception("VEHICLE NOT FOUND");

            vehicle.setPlatNumber(req.getPlat_number());
            vehicle.setYear(req.getYear());
            vehicle.setMerk(req.getMerk());
            vehicle.setType(req.getType());
            vehicle.setOwnership(req.getOwnership());
            if(req.getCertificate_expired() != null && !req.getCertificate_expired().equalsIgnoreCase(""))
                vehicle.setCertificateExpiredDate(LocalDate.parse(req.getCertificate_expired(),formatter));
            if(req.getTax_expired() != null && !req.getTax_expired().equalsIgnoreCase(""))
                vehicle.setTaxExpiredDate(LocalDate.parse(req.getTax_expired(),formatter));
            vehicle.setBbm(req.getBbm());
            vehicle.setKeterangan(req.getKeterangan());
            vehicleRepository.save(vehicle);
            res.setMsg("SUCCESS");
        }
        catch (Exception ex){
            res.setMsg("ERROR: "+ex.getMessage());
        }

        return res;
    }

    public MessageResponse deleteVehicle(String vehicle_id) {
        MessageResponse res = new MessageResponse();

        try{
            Vehicles vehicle = vehicleRepository.findByVehicleId(vehicle_id);
            if(vehicle == null)
                throw new Exception("VEHICLE NOT FOUND");

            vehicleRepository.delete(vehicle);
            res.setMsg("SUCCESS");
        }
        catch (Exception ex){
            res.setMsg("ERROR: "+ex.getMessage());
        }

        return res;
    }

    public VehicleResponse getVehicleById(String vehicle_id) {
        VehicleResponse res = new VehicleResponse();

        try{
            Vehicles vehicle = vehicleRepository.findByVehicleId(vehicle_id);
            if(vehicle == null)
                throw new Exception("VEHICLE NOT FOUND");

            res.setMsg("SUCCESS");
            res.setVehicle_id(vehicle.getVehicleId());
            res.setPlat_number(vehicle.getPlatNumber());
            res.setMerk(vehicle.getMerk());
            res.setType(vehicle.getType());
            res.setYear(vehicle.getYear());
            res.setOwnership(vehicle.getOwnership());
            res.setCertifcate_expired(vehicle.getCertificateExpiredDate());
            res.setTax_expired(vehicle.getTaxExpiredDate());
            res.setKeterangan(vehicle.getKeterangan());
            res.setBbm(vehicle.getBbm());
        }
        catch (Exception ex){
            res.setMsg("ERROR: "+ex.getMessage());
        }

        return res;
    }
}
