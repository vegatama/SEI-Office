package com.suryaenergi.sdm.backendapi.service;

import com.suryaenergi.sdm.backendapi.email.template.EmailTemplateBuilder;
import com.suryaenergi.sdm.backendapi.entity.*;
import com.suryaenergi.sdm.backendapi.notification.VehicleRequestReady;
import com.suryaenergi.sdm.backendapi.pojo.OrderVehicleData;
import com.suryaenergi.sdm.backendapi.pojo.VehiclesData;
import com.suryaenergi.sdm.backendapi.notification.VehiclePendingRequest;
import com.suryaenergi.sdm.backendapi.notification.VehicleRequestAccepted;
import com.suryaenergi.sdm.backendapi.repository.AntrianemailRepository;
import com.suryaenergi.sdm.backendapi.repository.ApprovelogRepository;
import com.suryaenergi.sdm.backendapi.repository.EmployeeRepository;
import com.suryaenergi.sdm.backendapi.repository.ProyeknavRepository;
import com.suryaenergi.sdm.backendapi.repository.VehicleRepository;
import com.suryaenergi.sdm.backendapi.repository.VehicleorderRepository;
import com.suryaenergi.sdm.backendapi.request.ApproveOrderGARequest;
import com.suryaenergi.sdm.backendapi.request.ApproveOrderRequest;
import com.suryaenergi.sdm.backendapi.request.OrderVehicleRequest;
import com.suryaenergi.sdm.backendapi.request.RejectOrderRequest;
import com.suryaenergi.sdm.backendapi.response.MessageResponse;
import com.suryaenergi.sdm.backendapi.response.OrderDetailResponse;
import com.suryaenergi.sdm.backendapi.response.OrderListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.suryaenergi.sdm.backendapi.pojo.Message.SUCCESS_MESSAGE;

@Service
public class OrderService {
    @Autowired
    private VehicleorderRepository vehicleorderRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private ProyeknavRepository proyeknavRepository;
    @Autowired
    private ApprovelogRepository approvelogRepository;
    @Autowired
    private AntrianemailRepository antrianemailRepository;
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private FrontEndURLService frontEndURLService;

    public OrderListResponse getListByMonth(int thn, int bln) {
        OrderListResponse res = new OrderListResponse();
        List<OrderVehicleData> orderVehicleDataList = new ArrayList<>();
        try{
            List<VehicleOrder> vehicleOrders = vehicleorderRepository.findAllByMonth(thn,bln);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            if(vehicleOrders.size() > 0){

                for(VehicleOrder vehicleOrder:vehicleOrders){
                    OrderVehicleData order = new OrderVehicleData();
                    order.setOrder_id(vehicleOrder.getVehicleOrderId());

                    List<String> penumpang = new ArrayList<>();
                    List<Employee> employeeList = vehicleOrder.getUsers();
                    for(Employee employee: employeeList){
                        String nm = employee.getFullName();
                        penumpang.add(nm);
                    }
                    order.setUsers(penumpang);

                    order.setWaktu_berangkat(vehicleOrder.getWaktuBerangkat().format(formatter));
                    order.setTanggal_kembali(vehicleOrder.getTanggalKembali());
                    order.setTujuan(vehicleOrder.getTujuan());
                    order.setKeperluan(vehicleOrder.getKeperluan());
                    Proyeknav proyek = vehicleOrder.getProyek();
                    order.setKode_proyek(proyek.getProjectName()+" ("+proyek.getProjectCode()+")");
                    order.setKeterangan(vehicleOrder.getKeterangan());
                    if(vehicleOrder.getApprovalPimproAtasan() != null) {
                        order.setApproval(vehicleOrder.getApprovalPimproAtasan().getFullName());
                    }

                    if(vehicleOrder.getMobil() != null) {
                        Vehicles vehicle = vehicleOrder.getMobil();
                        VehiclesData dataMobil = new VehiclesData();
                        dataMobil.setVehicle_id(vehicle.getVehicleId());
                        dataMobil.setMerk(vehicle.getMerk());
                        dataMobil.setBbm(vehicle.getBbm());
                        dataMobil.setType(vehicle.getType());
                        dataMobil.setOwnership(vehicle.getOwnership());
                        dataMobil.setTax_expired(vehicle.getTaxExpiredDate());
                        dataMobil.setPlat_number(vehicle.getPlatNumber());
                        dataMobil.setYear(vehicle.getYear());
                        dataMobil.setKeterangan(vehicle.getKeterangan());
                        dataMobil.setCertifcate_expired(vehicle.getCertificateExpiredDate());
                        order.setMobil(dataMobil);
                    } else if (vehicleOrder.getOtherPlatNumber() != null) {
                        VehiclesData dataMobil = new VehiclesData();
                        dataMobil.setPlat_number(vehicleOrder.getOtherPlatNumber());
                        dataMobil.setMerk(vehicleOrder.getOtherMerk());
                        dataMobil.setType(vehicleOrder.getOtherType());
                        dataMobil.setOwnership(vehicleOrder.getOtherOwnership());
                        dataMobil.setYear(vehicleOrder.getOtherYear());
                        dataMobil.setKeterangan(vehicleOrder.getOtherKeterangan());
                        dataMobil.setCertifcate_expired(vehicleOrder.getOtherCertificateExpired());
                        dataMobil.setTax_expired(vehicleOrder.getOtherTaxExpired());
                        dataMobil.setBbm(vehicleOrder.getOtherBbm());
                        order.setMobil(dataMobil);
                    }

                    order.setDriver(vehicleOrder.getDriver());
                    order.setHp_driver(vehicleOrder.getNoHpDriver());
                    if(vehicleOrder.getWaktuKembali() != null)
                        order.setWaktu_kembali(vehicleOrder.getWaktuKembali().format(formatter));
                    order.setStatus(vehicleOrder.getStatus());

                    if(vehicleOrder.getNeedApproval() != null){
                        order.setNeed_approve(vehicleOrder.getNeedApproval().getFullName());
                    }

                    orderVehicleDataList.add(order);
                }
            }
            res.setMsg("SUCCESS");
            res.setCount(orderVehicleDataList.size());
            res.setOrders(orderVehicleDataList);
        }
        catch (Exception ex){
            res.setMsg("ERROR: "+ex.getMessage());
        }

        return res;
    }

    public OrderListResponse getListByTanggal(int thn, int bln, int tgl) {
        OrderListResponse res = new OrderListResponse();
        List<OrderVehicleData> orderVehicleDataList = new ArrayList<>();
        try{
            List<VehicleOrder> vehicleOrders = vehicleorderRepository.findAllByTanggal(thn,bln,tgl);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            if(vehicleOrders.size() > 0){
                for(VehicleOrder vehicleOrder:vehicleOrders){
                    OrderVehicleData order = new OrderVehicleData();
                    order.setOrder_id(vehicleOrder.getVehicleOrderId());

                    List<String> penumpang = new ArrayList<>();
                    List<Employee> employeeList = vehicleOrder.getUsers();
                    for(Employee employee: employeeList){
                        String nm = employee.getFullName();
                        penumpang.add(nm);
                    }
                    order.setUsers(penumpang);

                    order.setWaktu_berangkat(vehicleOrder.getWaktuBerangkat().format(formatter));
                    order.setTanggal_kembali(vehicleOrder.getTanggalKembali());
                    order.setTujuan(vehicleOrder.getTujuan());
                    order.setKeperluan(vehicleOrder.getKeperluan());
                    Proyeknav proyek = vehicleOrder.getProyek();
                    order.setKode_proyek(proyek.getProjectName()+" ("+proyek.getProjectCode()+")");
                    order.setKeterangan(vehicleOrder.getKeterangan());
                    if(vehicleOrder.getApprovalPimproAtasan() != null) {
                        order.setApproval(vehicleOrder.getApprovalPimproAtasan().getFullName());
                    }

                    if(vehicleOrder.getMobil() != null) {
                        Vehicles vehicle = vehicleOrder.getMobil();
                        VehiclesData dataMobil = new VehiclesData();
                        dataMobil.setVehicle_id(vehicle.getVehicleId());
                        dataMobil.setMerk(vehicle.getMerk());
                        dataMobil.setBbm(vehicle.getBbm());
                        dataMobil.setType(vehicle.getType());
                        dataMobil.setOwnership(vehicle.getOwnership());
                        dataMobil.setTax_expired(vehicle.getTaxExpiredDate());
                        dataMobil.setPlat_number(vehicle.getPlatNumber());
                        dataMobil.setYear(vehicle.getYear());
                        dataMobil.setKeterangan(vehicle.getKeterangan());
                        dataMobil.setCertifcate_expired(vehicle.getCertificateExpiredDate());
                        order.setMobil(dataMobil);
                    } else if (vehicleOrder.getOtherPlatNumber() != null) {
                        VehiclesData dataMobil = new VehiclesData();
                        dataMobil.setPlat_number(vehicleOrder.getOtherPlatNumber());
                        dataMobil.setMerk(vehicleOrder.getOtherMerk());
                        dataMobil.setType(vehicleOrder.getOtherType());
                        dataMobil.setOwnership(vehicleOrder.getOtherOwnership());
                        dataMobil.setYear(vehicleOrder.getOtherYear());
                        dataMobil.setKeterangan(vehicleOrder.getOtherKeterangan());
                        dataMobil.setCertifcate_expired(vehicleOrder.getOtherCertificateExpired());
                        dataMobil.setTax_expired(vehicleOrder.getOtherTaxExpired());
                        dataMobil.setBbm(vehicleOrder.getOtherBbm());
                        order.setMobil(dataMobil);
                    }

                    order.setDriver(vehicleOrder.getDriver());
                    order.setHp_driver(vehicleOrder.getNoHpDriver());
                    if(vehicleOrder.getWaktuKembali() != null)
                        order.setWaktu_kembali(vehicleOrder.getWaktuKembali().format(formatter));
                    order.setStatus(vehicleOrder.getStatus());

                    if(vehicleOrder.getNeedApproval() != null){
                        order.setNeed_approve(vehicleOrder.getNeedApproval().getFullName());
                    }

                    orderVehicleDataList.add(order);
                }
            }
            res.setMsg("SUCCESS");
            res.setCount(orderVehicleDataList.size());
            res.setOrders(orderVehicleDataList);
        }
        catch (Exception ex){
            res.setMsg("ERROR: "+ex.getMessage());
        }

        return res;
    }

    public OrderListResponse getListByPemesan(String pid) {
        OrderListResponse res = new OrderListResponse();
        List<OrderVehicleData> orderVehicleDataList = new ArrayList<>();
        try{
            Employee pemesan = employeeRepository.findByEmployeeId(pid);
            if(pemesan == null)
                throw new Exception("PEMESAN NOT FOUND");
            List<VehicleOrder> vehicleOrders = vehicleorderRepository.findAllByPemesan(pemesan);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            if(vehicleOrders.size() > 0){
                for(VehicleOrder vehicleOrder:vehicleOrders){
                    OrderVehicleData order = new OrderVehicleData();
                    order.setOrder_id(vehicleOrder.getVehicleOrderId());

                    List<String> penumpang = new ArrayList<>();
                    List<Employee> employeeList = vehicleOrder.getUsers();
                    for(Employee employee: employeeList){
                        String nm = employee.getFullName();
                        penumpang.add(nm);
                    }
                    order.setUsers(penumpang);

                    order.setWaktu_berangkat(vehicleOrder.getWaktuBerangkat().format(formatter));
                    order.setTanggal_kembali(vehicleOrder.getTanggalKembali());
                    order.setTujuan(vehicleOrder.getTujuan());
                    order.setKeperluan(vehicleOrder.getKeperluan());
                    Proyeknav proyek = vehicleOrder.getProyek();
                    order.setKode_proyek(proyek.getProjectName()+" ("+proyek.getProjectCode()+")");
                    order.setKeterangan(vehicleOrder.getKeterangan());
                    if(vehicleOrder.getApprovalPimproAtasan() != null) {
                        order.setApproval(vehicleOrder.getApprovalPimproAtasan().getFullName());
                    }

                    if(vehicleOrder.getMobil() != null) {
                        Vehicles vehicle = vehicleOrder.getMobil();
                        VehiclesData dataMobil = new VehiclesData();
                        dataMobil.setVehicle_id(vehicle.getVehicleId());
                        dataMobil.setMerk(vehicle.getMerk());
                        dataMobil.setBbm(vehicle.getBbm());
                        dataMobil.setType(vehicle.getType());
                        dataMobil.setOwnership(vehicle.getOwnership());
                        dataMobil.setTax_expired(vehicle.getTaxExpiredDate());
                        dataMobil.setPlat_number(vehicle.getPlatNumber());
                        dataMobil.setYear(vehicle.getYear());
                        dataMobil.setKeterangan(vehicle.getKeterangan());
                        dataMobil.setCertifcate_expired(vehicle.getCertificateExpiredDate());
                        order.setMobil(dataMobil);
                    } else if (vehicleOrder.getOtherPlatNumber() != null) {
                        VehiclesData dataMobil = new VehiclesData();
                        dataMobil.setPlat_number(vehicleOrder.getOtherPlatNumber());
                        dataMobil.setMerk(vehicleOrder.getOtherMerk());
                        dataMobil.setType(vehicleOrder.getOtherType());
                        dataMobil.setOwnership(vehicleOrder.getOtherOwnership());
                        dataMobil.setYear(vehicleOrder.getOtherYear());
                        dataMobil.setKeterangan(vehicleOrder.getOtherKeterangan());
                        dataMobil.setCertifcate_expired(vehicleOrder.getOtherCertificateExpired());
                        dataMobil.setTax_expired(vehicleOrder.getOtherTaxExpired());
                        dataMobil.setBbm(vehicleOrder.getOtherBbm());
                        order.setMobil(dataMobil);
                    }

                    order.setDriver(vehicleOrder.getDriver());
                    order.setHp_driver(vehicleOrder.getNoHpDriver());
                    if(vehicleOrder.getWaktuKembali() != null)
                        order.setWaktu_kembali(vehicleOrder.getWaktuKembali().format(formatter));
                    order.setStatus(vehicleOrder.getStatus());

                    if(vehicleOrder.getNeedApproval() != null){
                        order.setNeed_approve(vehicleOrder.getNeedApproval().getFullName());
                        order.setNeed_approve_id(vehicleOrder.getNeedApproval().getEmployeeId());
                    }

                    orderVehicleDataList.add(order);
                }
            }
            res.setMsg("SUCCESS");
            res.setCount(orderVehicleDataList.size());
            res.setOrders(orderVehicleDataList);
        }
        catch (Exception ex){
            res.setMsg("ERROR: "+ex.getMessage());
        }

        return res;
    }

    @Transactional
    public MessageResponse addOrder(OrderVehicleRequest req) {
        MessageResponse res = new MessageResponse();

        try{
            List<Employee> employees = new ArrayList<>();
            for(String id:req.getPengguna()){
                Employee employee = employeeRepository.findByEmployeeId(id);
                if(employee != null)
                    employees.add(employee);
            }
            VehicleOrder vehicleOrder = new VehicleOrder();
            vehicleOrder.setUsers(employees);
            vehicleOrder.setVehicleOrderId(getNewOrderId());

            /*VehicleOrder cekid = vehicleorderRepository.findByVehicleOrderId(vehicleOrder.getVehicleOrderId());
            while (cekid != null){
                vehicleOrder.setVehicleOrderId(getNewOrderId());
                cekid = vehicleorderRepository.findByVehicleOrderId(vehicleOrder.getVehicleOrderId());
            }*/
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            vehicleOrder.setWaktuBerangkat(LocalDateTime.parse(req.getWaktu_berangkat(),formatter));
            vehicleOrder.setTanggalKembali(LocalDate.parse(req.getTgl_kembali(),formatter2));
            vehicleOrder.setTujuan(req.getTujuan());
            vehicleOrder.setKeperluan(req.getKeperluan());
            vehicleOrder.setKeterangan(req.getKeterangan());
            vehicleOrder.setStatus("Pending Approval");

            Proyeknav proyeknav = proyeknavRepository.findByProjectCode(req.getKode_proyek());
            if(proyeknav == null)
                throw new Exception("PROYEK NOT FOUND");
            vehicleOrder.setProyek(proyeknav);

            Employee needapprove = null;
            Employee pemohon = employeeRepository.findByEmployeeId(req.getPemesan());
            if(proyeknav.getProjectType().equalsIgnoreCase("rutin")){
                if(pemohon.getAtasanUserId() == null || pemohon.getAtasanUserId().equalsIgnoreCase(""))
                    throw new Exception("ATASAN PEMOHON NOT FOUND");
                needapprove = employeeRepository.findFirstByUserIdNav(pemohon.getAtasanUserId());
            }
            else{
                Employee pimpro = employeeRepository.findFirstByUserIdNav(proyeknav.getPimpro());
                if(pimpro == null)
                    throw new Exception("PIMPRO NOT FOUND");
                needapprove = pimpro;
            }

            vehicleOrder.setNeedApproval(needapprove);
            vehicleOrder.setPemesan(pemohon);
            vehicleorderRepository.save(vehicleOrder);

            Approvelog approvelog = new Approvelog();
            approvelog.setFromUser(pemohon);
            approvelog.setToUser(needapprove);
            approvelog.setStatus("SEND APPROVAL");
            approvelogRepository.save(approvelog);

            VehiclePendingRequest notification = new VehiclePendingRequest(pemohon.getFullName(), vehicleOrder.getVehicleOrderId(), proyeknav.getProjectName(), vehicleOrder.getTujuan(), vehicleOrder.getWaktuBerangkat());
            notificationService.pushNotification(notification.build(), needapprove);

            // send email
            emailService.sendEmail(needapprove, "Persetujuan Peminjaman Kendaraan",
                    EmailTemplateBuilder.create("Persetujuan Peminjaman Kendaraan")
                            .append("Halo, ")
                            .append("Anda memiliki permintaan peminjaman kendaraan yang perlu anda tinjau:")
                            .appendEntry("Nama", pemohon.getFullName())
                            .appendEntry("Tujuan", vehicleOrder.getTujuan())
                            .appendEntry("Waktu Berangkat", vehicleOrder.getWaktuBerangkat().format(formatter))
                            .append("Silahkan tinjau permintaan peminjaman kendaraan tersebut di aplikasi.")
                            .appendBtnPrimary("Lihat Detail di Aplikasi", frontEndURLService.getVehicleOrderDetailNeedApproveURL(vehicleOrder.getVehicleOrderId())).generate());


            res.setMsg("SUCCESS");
        }
        catch (Exception ex){
            res.setMsg("ERROR: "+ex.getMessage());
        }

        return res;
    }

    @Transactional
    public MessageResponse updateOrder(OrderVehicleRequest req) {
        MessageResponse res = new MessageResponse();

        try{
            VehicleOrder vehicleOrder = vehicleorderRepository.findByVehicleOrderId(req.getId());
            if(vehicleOrder == null)
                throw new Exception("ORDER NOT FOUND");

            List<Employee> employees = new ArrayList<>();
            for(String id:req.getPengguna()){
                Employee employee = employeeRepository.findByEmployeeId(id);
                if(employee != null)
                    employees.add(employee);
            }
            vehicleOrder.setUsers(employees);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            vehicleOrder.setWaktuBerangkat(LocalDateTime.parse(req.getWaktu_berangkat(),formatter));
            vehicleOrder.setTanggalKembali(LocalDate.parse(req.getTgl_kembali(),formatter2));
            vehicleOrder.setTujuan(req.getTujuan());
            vehicleOrder.setKeperluan(req.getKeperluan());
            vehicleOrder.setKeterangan(req.getKeterangan());

            Proyeknav proyeknav = proyeknavRepository.findByProjectCode(req.getKode_proyek());
            if(proyeknav == null)
                throw new Exception("PROYEK NOT FOUND");
            vehicleOrder.setProyek(proyeknav);

            Employee previousNeedApprove = vehicleOrder.getNeedApproval();
            Employee needapprove = null;
            Employee pemohon = employeeRepository.findByEmployeeId(req.getPemesan());
            if(proyeknav.getProjectType().equalsIgnoreCase("rutin")){
                if(pemohon.getAtasanUserId() == null || pemohon.getAtasanUserId().equalsIgnoreCase(""))
                    throw new Exception("ATASAN PEMOHON NOT FOUND");
                needapprove = employeeRepository.findFirstByUserIdNav(pemohon.getAtasanUserId());
            }
            else{
                Employee pimpro = employeeRepository.findFirstByUserIdNav(proyeknav.getPimpro());
                if(pimpro == null)
                    throw new Exception("PIMPRO NOT FOUND");
                needapprove = pimpro;
            }

            if (previousNeedApprove != null && !previousNeedApprove.getEmployeeId().equalsIgnoreCase(needapprove.getEmployeeId())) {
                VehiclePendingRequest notification = new VehiclePendingRequest(pemohon.getFullName(), vehicleOrder.getVehicleOrderId(), proyeknav.getProjectName(), vehicleOrder.getTujuan(), vehicleOrder.getWaktuBerangkat());
                notificationService.pushNotification(notification.build(), needapprove);

                // send email
                emailService.sendEmail(needapprove, "Persetujuan Peminjaman Kendaraan",
                        EmailTemplateBuilder.create("Persetujuan Peminjaman Kendaraan")
                                .append("Halo, ")
                                .append("Anda memiliki permintaan peminjaman kendaraan yang perlu anda tinjau:")
                                .appendEntry("Nama", pemohon.getFullName())
                                .appendEntry("Tujuan", vehicleOrder.getTujuan())
                                .appendEntry("Waktu Berangkat", vehicleOrder.getWaktuBerangkat().format(formatter))
                                .append("Silahkan tinjau permintaan peminjaman kendaraan tersebut di aplikasi.")
                                .appendBtnPrimary("Lihat Detail di Aplikasi", frontEndURLService.getVehicleOrderDetailNeedApproveURL(vehicleOrder.getVehicleOrderId())).generate());
            }

            vehicleOrder.setNeedApproval(needapprove);
            vehicleOrder.setPemesan(pemohon);
            vehicleorderRepository.save(vehicleOrder);

            Approvelog approvelog = new Approvelog();
            approvelog.setFromUser(pemohon);
            approvelog.setToUser(needapprove);
            approvelog.setStatus("SEND APPROVAL");
            approvelogRepository.save(approvelog);

            res.setMsg("SUCCESS");
        }
        catch (Exception ex){
            res.setMsg("ERROR: "+ex.getMessage());
        }
        return res;
    }

    private String getNewOrderId() {
        Date tanggal = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(tanggal);
        int month = c.get(Calendar.MONTH)+1;
        int year = c.get(Calendar.YEAR);
        List<VehicleOrder> vehicleOrders = vehicleorderRepository.findAllByOrderByCreatedAtDesc(year,month);
        int jumlahData = vehicleOrders.size();
        jumlahData++;
        DateFormat df = new SimpleDateFormat("yy");
        DateFormat dfm = new SimpleDateFormat("MM");
        String twoDigitYear = df.format(c.getTime());
        String twoDigitMonth = dfm.format(c.getTime());
        String formatted = String.format("%05d", jumlahData);
        return "GAK-"+twoDigitYear+twoDigitMonth+"-"+formatted;
    }

    public OrderListResponse getListNeedApprove(String pid) {
        OrderListResponse res = new OrderListResponse();
        List<OrderVehicleData> orderVehicleDataList = new ArrayList<>();
        try{
            Employee pemesan = employeeRepository.findByEmployeeId(pid);
            if(pemesan == null)
                throw new Exception("EMPLOYEE NOT FOUND");
            List<VehicleOrder> vehicleOrders = vehicleorderRepository.findAllByNeedApproval(pemesan);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            if(vehicleOrders.size() > 0){
                for(VehicleOrder vehicleOrder:vehicleOrders){
                    OrderVehicleData order = new OrderVehicleData();
                    order.setOrder_id(vehicleOrder.getVehicleOrderId());

                    List<String> penumpang = new ArrayList<>();
                    List<Employee> employeeList = vehicleOrder.getUsers();
                    for(Employee employee: employeeList){
                        String nm = employee.getFullName();
                        penumpang.add(nm);
                    }
                    order.setUsers(penumpang);

                    order.setWaktu_berangkat(vehicleOrder.getWaktuBerangkat().format(formatter));
                    order.setTanggal_kembali(vehicleOrder.getTanggalKembali());
                    order.setTujuan(vehicleOrder.getTujuan());
                    order.setKeperluan(vehicleOrder.getKeperluan());
                    Proyeknav proyek = vehicleOrder.getProyek();
                    order.setKode_proyek(proyek.getProjectName()+" ("+proyek.getProjectCode()+")");
                    order.setKeterangan(vehicleOrder.getKeterangan());
                    if(vehicleOrder.getApprovalPimproAtasan() != null) {
                        order.setApproval(vehicleOrder.getApprovalPimproAtasan().getFullName());
                    }

                    if(vehicleOrder.getMobil() != null) {
                        Vehicles vehicle = vehicleOrder.getMobil();
                        VehiclesData dataMobil = new VehiclesData();
                        dataMobil.setVehicle_id(vehicle.getVehicleId());
                        dataMobil.setMerk(vehicle.getMerk());
                        dataMobil.setBbm(vehicle.getBbm());
                        dataMobil.setType(vehicle.getType());
                        dataMobil.setOwnership(vehicle.getOwnership());
                        dataMobil.setTax_expired(vehicle.getTaxExpiredDate());
                        dataMobil.setPlat_number(vehicle.getPlatNumber());
                        dataMobil.setYear(vehicle.getYear());
                        dataMobil.setKeterangan(vehicle.getKeterangan());
                        dataMobil.setCertifcate_expired(vehicle.getCertificateExpiredDate());
                        order.setMobil(dataMobil);
                    } else if (vehicleOrder.getOtherPlatNumber() != null) {
                        VehiclesData dataMobil = new VehiclesData();
                        dataMobil.setPlat_number(vehicleOrder.getOtherPlatNumber());
                        dataMobil.setMerk(vehicleOrder.getOtherMerk());
                        dataMobil.setType(vehicleOrder.getOtherType());
                        dataMobil.setOwnership(vehicleOrder.getOtherOwnership());
                        dataMobil.setYear(vehicleOrder.getOtherYear());
                        dataMobil.setKeterangan(vehicleOrder.getOtherKeterangan());
                        dataMobil.setCertifcate_expired(vehicleOrder.getOtherCertificateExpired());
                        dataMobil.setTax_expired(vehicleOrder.getOtherTaxExpired());
                        dataMobil.setBbm(vehicleOrder.getOtherBbm());
                        order.setMobil(dataMobil);
                    }

                    order.setDriver(vehicleOrder.getDriver());
                    order.setHp_driver(vehicleOrder.getNoHpDriver());
                    if(vehicleOrder.getWaktuKembali() != null)
                        order.setWaktu_kembali(vehicleOrder.getWaktuKembali().format(formatter));
                    order.setStatus(vehicleOrder.getStatus());

                    if(vehicleOrder.getNeedApproval() != null){
                        order.setNeed_approve(vehicleOrder.getNeedApproval().getFullName());
                    }

                    orderVehicleDataList.add(order);
                }
            }
            res.setMsg("SUCCESS");
            res.setCount(orderVehicleDataList.size());
            res.setOrders(orderVehicleDataList);
        }
        catch (Exception ex){
            res.setMsg("ERROR: "+ex.getMessage());
        }

        return res;
    }

    public OrderListResponse getListApproved(String pid) {
        OrderListResponse res = new OrderListResponse();
        List<OrderVehicleData> orderVehicleDataList = new ArrayList<>();
        try{
            Employee pemesan = employeeRepository.findByEmployeeId(pid);
            if(pemesan == null)
                throw new Exception("EMPLOYEE NOT FOUND");
            List<VehicleOrder> vehicleOrders = vehicleorderRepository.findAllByApprovalPimproAtasan(pemesan);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            if(vehicleOrders.size() > 0){
                for(VehicleOrder vehicleOrder:vehicleOrders){
                    OrderVehicleData order = new OrderVehicleData();
                    order.setOrder_id(vehicleOrder.getVehicleOrderId());

                    List<String> penumpang = new ArrayList<>();
                    List<Employee> employeeList = vehicleOrder.getUsers();
                    for(Employee employee: employeeList){
                        String nm = employee.getFullName();
                        penumpang.add(nm);
                    }
                    order.setUsers(penumpang);

                    order.setWaktu_berangkat(vehicleOrder.getWaktuBerangkat().format(formatter));
                    order.setTanggal_kembali(vehicleOrder.getTanggalKembali());
                    order.setTujuan(vehicleOrder.getTujuan());
                    order.setKeperluan(vehicleOrder.getKeperluan());
                    Proyeknav proyek = vehicleOrder.getProyek();
                    order.setKode_proyek(proyek.getProjectName()+" ("+proyek.getProjectCode()+")");
                    order.setKeterangan(vehicleOrder.getKeterangan());
                    if(vehicleOrder.getApprovalPimproAtasan() != null) {
                        order.setApproval(vehicleOrder.getApprovalPimproAtasan().getFullName());
                    }

                    if(vehicleOrder.getMobil() != null) {
                        Vehicles vehicle = vehicleOrder.getMobil();
                        VehiclesData dataMobil = new VehiclesData();
                        dataMobil.setVehicle_id(vehicle.getVehicleId());
                        dataMobil.setMerk(vehicle.getMerk());
                        dataMobil.setBbm(vehicle.getBbm());
                        dataMobil.setType(vehicle.getType());
                        dataMobil.setOwnership(vehicle.getOwnership());
                        dataMobil.setTax_expired(vehicle.getTaxExpiredDate());
                        dataMobil.setPlat_number(vehicle.getPlatNumber());
                        dataMobil.setYear(vehicle.getYear());
                        dataMobil.setKeterangan(vehicle.getKeterangan());
                        dataMobil.setCertifcate_expired(vehicle.getCertificateExpiredDate());
                        order.setMobil(dataMobil);
                    } else if (vehicleOrder.getOtherPlatNumber() != null) {
                        VehiclesData dataMobil = new VehiclesData();
                        dataMobil.setPlat_number(vehicleOrder.getOtherPlatNumber());
                        dataMobil.setMerk(vehicleOrder.getOtherMerk());
                        dataMobil.setType(vehicleOrder.getOtherType());
                        dataMobil.setOwnership(vehicleOrder.getOtherOwnership());
                        dataMobil.setYear(vehicleOrder.getOtherYear());
                        dataMobil.setKeterangan(vehicleOrder.getOtherKeterangan());
                        dataMobil.setCertifcate_expired(vehicleOrder.getOtherCertificateExpired());
                        dataMobil.setTax_expired(vehicleOrder.getOtherTaxExpired());
                        dataMobil.setBbm(vehicleOrder.getOtherBbm());
                        order.setMobil(dataMobil);
                    }

                    order.setDriver(vehicleOrder.getDriver());
                    order.setHp_driver(vehicleOrder.getNoHpDriver());
                    if(vehicleOrder.getWaktuKembali() != null)
                        order.setWaktu_kembali(vehicleOrder.getWaktuKembali().format(formatter));
                    order.setStatus(vehicleOrder.getStatus());

                    if(vehicleOrder.getNeedApproval() != null){
                        order.setNeed_approve(vehicleOrder.getNeedApproval().getFullName());
                    }

                    orderVehicleDataList.add(order);
                }
            }
            res.setMsg("SUCCESS");
            res.setCount(orderVehicleDataList.size());
            res.setOrders(orderVehicleDataList);
        }
        catch (Exception ex){
            res.setMsg("ERROR: "+ex.getMessage());
        }

        return res;
    }

    public OrderDetailResponse getOrderDetail(String oid) {
        OrderDetailResponse res = new OrderDetailResponse();

        try{
            VehicleOrder vehicleOrder = vehicleorderRepository.findByVehicleOrderId(oid);
            if(vehicleOrder == null)
                throw new Exception("ORDER NOT FOUND");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            res.setOrder_id(vehicleOrder.getVehicleOrderId());

            List<String> penumpang = new ArrayList<>();
            List<Employee> employeeList = vehicleOrder.getUsers();
            List<OrderDetailResponse.User> userList = new ArrayList<>();
            for(Employee employee: employeeList){
                String nm = employee.getFullName()+" ("+employee.getUnitKerja()+")";
                penumpang.add(nm);
                OrderDetailResponse.User user = new OrderDetailResponse.User(employee.getEmployeeId(), employee.getFullName());
                userList.add(user);
            }
            res.setUsers(penumpang);
            res.setUserList(userList);

            res.setWaktu_berangkat(vehicleOrder.getWaktuBerangkat().format(formatter));

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            res.setBerangkat_date(vehicleOrder.getWaktuBerangkat().format(dateFormatter));
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            res.setBerangkat_time(vehicleOrder.getWaktuBerangkat().format(timeFormatter));

            res.setTanggal_kembali(vehicleOrder.getTanggalKembali());
            res.setJam_kembali(vehicleOrder.getJamKembali());
            res.setTujuan(vehicleOrder.getTujuan());
            res.setKeperluan(vehicleOrder.getKeperluan());
            Proyeknav proyek = vehicleOrder.getProyek();
            res.setKode_proyek(proyek.getProjectName()+" ("+proyek.getProjectCode()+")");
            res.setProject_code(proyek.getProjectCode());
            res.setKeterangan(vehicleOrder.getKeterangan());
            if(vehicleOrder.getApprovalPimproAtasan() != null) {
                res.setApproval(vehicleOrder.getApprovalPimproAtasan().getFullName());
            }

            if(vehicleOrder.getMobil() != null) {
                Vehicles vehicle = vehicleOrder.getMobil();
                VehiclesData dataMobil = new VehiclesData();
                dataMobil.setVehicle_id(vehicle.getVehicleId());
                dataMobil.setMerk(vehicle.getMerk());
                dataMobil.setBbm(vehicle.getBbm());
                dataMobil.setType(vehicle.getType());
                dataMobil.setOwnership(vehicle.getOwnership());
                dataMobil.setTax_expired(vehicle.getTaxExpiredDate());
                dataMobil.setPlat_number(vehicle.getPlatNumber());
                dataMobil.setYear(vehicle.getYear());
                dataMobil.setKeterangan(vehicle.getKeterangan());
                dataMobil.setCertifcate_expired(vehicle.getCertificateExpiredDate());
                res.setMobil(dataMobil);
            } else if (vehicleOrder.getOtherPlatNumber() != null) {
                VehiclesData dataMobil = new VehiclesData();
                dataMobil.setPlat_number(vehicleOrder.getOtherPlatNumber());
                dataMobil.setMerk(vehicleOrder.getOtherMerk());
                dataMobil.setType(vehicleOrder.getOtherType());
                dataMobil.setOwnership(vehicleOrder.getOtherOwnership());
                dataMobil.setYear(vehicleOrder.getOtherYear());
                dataMobil.setKeterangan(vehicleOrder.getOtherKeterangan());
                dataMobil.setCertifcate_expired(vehicleOrder.getOtherCertificateExpired());
                dataMobil.setTax_expired(vehicleOrder.getOtherTaxExpired());
                dataMobil.setBbm(vehicleOrder.getOtherBbm());
                res.setMobil(dataMobil);
            }

            res.setDriver(vehicleOrder.getDriver());
            res.setHp_driver(vehicleOrder.getNoHpDriver());
            if(vehicleOrder.getWaktuKembali() != null)
                res.setWaktu_kembali(vehicleOrder.getWaktuKembali().format(formatter));
            res.setStatus(vehicleOrder.getStatus());

            if(vehicleOrder.getNeedApproval() != null){
                res.setNeed_approve(vehicleOrder.getNeedApproval().getFullName());
                res.setNeed_approve_id(vehicleOrder.getNeedApproval().getEmployeeId());
            }

            res.setMsg("SUCCESS");
        }
        catch (Exception ex){
            res.setMsg("ERROR: "+ex.getMessage());
        }

        return res;
    }

    @Transactional
    public MessageResponse approveOrder(ApproveOrderRequest req, boolean isUpdate) {
        MessageResponse res = new MessageResponse();
        try{
            VehicleOrder vehicleOrder = vehicleorderRepository.findByVehicleOrderId(req.getOrder_id());
            if(vehicleOrder == null)
                throw new Exception("ORDER NOT FOUND");

            if (vehicleOrder.getApprovalPimproAtasan() != null) {
                // telah diapprove oleh pimpinan proyek, berarti approve ini adalah approve dari GA (General Affair)
                if (req.getAssignedDriverName() == null || req.getAssignedDriverPhone() == null || req.getAssignedDriverName().isEmpty() || req.getAssignedDriverPhone().isEmpty()) {
                    throw new Exception("DRIVER NOT ASSIGNED");
                }

                // assign driver
                vehicleOrder.setDriver(req.getAssignedDriverName());
                vehicleOrder.setNoHpDriver(req.getAssignedDriverPhone());

                if (req.getAssignedVehicleId() == null) {
                    if (req.getAssignedOtherPlatNumber() == null) {
                        throw new Exception("VEHICLE NOT ASSIGNED");
                    }
                    vehicleOrder.setMobil(null);
                    // custom vehicle
                    vehicleOrder.setOtherPlatNumber(req.getAssignedOtherPlatNumber());
                    vehicleOrder.setOtherMerk(req.getAssignedOtherMerk());
                    vehicleOrder.setOtherType(req.getAssignedOtherTipe());
                    vehicleOrder.setOtherYear(req.getAssignedOtherTahun());
                    vehicleOrder.setOtherBbm(req.getAssignedOtherBBM());
                    vehicleOrder.setOtherOwnership(req.getAssignedOtherPemilik());
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    if (req.getAssignedOtherPKB() != null && !req.getAssignedOtherPKB().isEmpty()) {
                        vehicleOrder.setOtherCertificateExpired(LocalDate.parse(req.getAssignedOtherPKB(), df));
                        vehicleOrder.setOtherTaxExpired(LocalDate.parse(req.getAssignedOtherPKB(), df));
                    }
                    vehicleOrder.setOtherKeterangan(req.getAssignedOtherKeterangan());

                } else {
                    // assign vehicle
                    Vehicles vehicle = vehicleRepository.findByVehicleId(req.getAssignedVehicleId());
                    if (vehicle == null) {
                        throw new Exception("VEHICLE NOT FOUND");
                    }
                    vehicleOrder.setMobil(vehicle);
                    vehicleOrder.setOtherPlatNumber(null);
                    vehicleOrder.setOtherMerk(null);
                    vehicleOrder.setOtherType(null);
                    vehicleOrder.setOtherYear(null);
                    vehicleOrder.setOtherBbm(null);
                    vehicleOrder.setOtherOwnership(null);
                    vehicleOrder.setOtherCertificateExpired(null);
                    vehicleOrder.setOtherTaxExpired(null);
                    vehicleOrder.setOtherKeterangan(null);
                }

                res.setMsg(SUCCESS_MESSAGE);
                vehicleOrder.setStatus("APPROVED");
                vehicleorderRepository.save(vehicleOrder);

                List<Employee> users = vehicleOrder.getUsers();
                boolean pemohonAdalahUser = false;
                for (Employee user : users) {
                    if (user.getEmployeeCode().equals(vehicleOrder.getPemesan().getEmployeeCode())) {
                        pemohonAdalahUser = true;
                    }
                    // send notification
                    VehicleRequestReady notification = new VehicleRequestReady(vehicleOrder.getVehicleOrderId(), vehicleOrder.getProyek().getProjectName(), vehicleOrder.getDriver(), vehicleOrder.getNoHpDriver());
                    notificationService.pushNotification(notification.build(), user);
                    // send email
                    if (!isUpdate) {
                        emailService.sendEmail(user, "Persetujuan Peminjaman Kendaraan",
                                EmailTemplateBuilder.create("Persetujuan Peminjaman Kendaraan")
                                        .append("Halo, ")
                                        .append("Peminjaman kendaraan anda telah disetujui oleh Koordinator Kendaraan.")
                                        .appendEntry("Tujuan", vehicleOrder.getTujuan())
                                        .appendEntry("Waktu Berangkat", vehicleOrder.getWaktuBerangkat().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                                        .appendEntry("Driver", vehicleOrder.getDriver())
                                        .appendEntry("No. HP Driver", vehicleOrder.getNoHpDriver())
                                        .append("Silahkan hubungi driver untuk informasi lebih lanjut.")
                                        .appendBtnPrimary("Lihat Detail di Aplikasi", frontEndURLService.getVehicleOrderDetailURL(vehicleOrder.getVehicleOrderId()))
                                        .appendBtnSecondary("Whatsapp Driver", getWhatsappURL(vehicleOrder.getNoHpDriver())).generate());
                    } else {
                        emailService.sendEmail(user, "Persetujuan Peminjaman Kendaraan",
                                EmailTemplateBuilder.create("Persetujuan Peminjaman Kendaraan")
                                        .append("Halo, ")
                                        .append("Peminjaman kendaraan anda telah diupdate oleh Koordinator Kendaraan.")
                                        .appendEntry("Tujuan", vehicleOrder.getTujuan())
                                        .appendEntry("Waktu Berangkat", vehicleOrder.getWaktuBerangkat().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                                        .appendEntry("Driver", vehicleOrder.getDriver())
                                        .appendEntry("No. HP Driver", vehicleOrder.getNoHpDriver())
                                        .append("Silahkan hubungi driver untuk informasi lebih lanjut.")
                                        .appendBtnPrimary("Lihat Detail di Aplikasi", frontEndURLService.getVehicleOrderDetailURL(vehicleOrder.getVehicleOrderId()))
                                        .appendBtnSecondary("Whatsapp Driver", getWhatsappURL(vehicleOrder.getNoHpDriver())).generate());
                    }
                }

                if (!pemohonAdalahUser) {
                    // send notification to pemohon
                    VehicleRequestReady notification = new VehicleRequestReady(vehicleOrder.getVehicleOrderId(), vehicleOrder.getProyek().getProjectName(), vehicleOrder.getDriver(), vehicleOrder.getNoHpDriver());
                    notificationService.pushNotification(notification.build(), vehicleOrder.getPemesan());
                    // send email to pemohon
                    if (!isUpdate) {
                        emailService.sendEmail(vehicleOrder.getPemesan(), "Persetujuan Peminjaman Kendaraan",
                                EmailTemplateBuilder.create("Persetujuan Peminjaman Kendaraan")
                                        .append("Halo, ")
                                        .append("Peminjaman kendaraan anda telah disetujui oleh Koordinator Kendaraan.")
                                        .appendEntry("Tujuan", vehicleOrder.getTujuan())
                                        .appendEntry("Waktu Berangkat", vehicleOrder.getWaktuBerangkat().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                                        .appendEntry("Driver", vehicleOrder.getDriver())
                                        .appendEntry("No. HP Driver", vehicleOrder.getNoHpDriver())
                                        .append("Silahkan hubungi driver untuk informasi lebih lanjut.")
                                        .appendBtnPrimary("Lihat Detail di Aplikasi", frontEndURLService.getVehicleOrderDetailURL(vehicleOrder.getVehicleOrderId()))
                                        .appendBtnSecondary("Whatsapp Driver", getWhatsappURL(vehicleOrder.getNoHpDriver())).generate());
                    } else {
                        emailService.sendEmail(vehicleOrder.getPemesan(), "Persetujuan Peminjaman Kendaraan",
                                EmailTemplateBuilder.create("Persetujuan Peminjaman Kendaraan")
                                        .append("Halo, ")
                                        .append("Peminjaman kendaraan anda telah diupdate oleh Koordinator Kendaraan.")
                                        .appendEntry("Tujuan", vehicleOrder.getTujuan())
                                        .appendEntry("Waktu Berangkat", vehicleOrder.getWaktuBerangkat().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                                        .appendEntry("Driver", vehicleOrder.getDriver())
                                        .appendEntry("No. HP Driver", vehicleOrder.getNoHpDriver())
                                        .append("Silahkan hubungi driver untuk informasi lebih lanjut.")
                                        .appendBtnPrimary("Lihat Detail di Aplikasi", frontEndURLService.getVehicleOrderDetailURL(vehicleOrder.getVehicleOrderId()))
                                        .appendBtnSecondary("Whatsapp Driver", getWhatsappURL(vehicleOrder.getNoHpDriver())).generate());
                    }
                }

                if (!isUpdate) {
                    Approvelog approvelog = new Approvelog();
                    approvelog.setFromUser(vehicleOrder.getApprovalPimproAtasan());
                    approvelog.setToUser(vehicleOrder.getPemesan());
                    approvelog.setStatus("APPROVED");
                    approvelogRepository.save(approvelog);
                }

                return res;
            }

            if (isUpdate) {
                throw new Exception("CANNOT UPDATE DRIVER DETAIL FOR UNAPPROVED ORDER");
            }

            Employee employee = employeeRepository.findByEmployeeId(req.getApproval_id());
            if(employee == null)
                throw new Exception("APPROVAL NOT FOUND");
            vehicleOrder.setApprovalPimproAtasan(employee);

            Employee pemohon = vehicleOrder.getPemesan();

            Employee previousNeedApprove = vehicleOrder.getNeedApproval();

            // Employee userGA = employeeRepository.findFirstByUserIdNav("HCG-05");
            Employee userGA = employeeRepository.findFirstByUserIdNav("ATS2-48");
//            Employee userGA = employeeRepository.findByEmployeeCode("EMP458"); // digantikan sementara untuk keperluan testing
            Approvelog approvelog = new Approvelog();
            approvelog.setFromUser(employee);
            approvelog.setToUser(userGA);
            approvelog.setStatus("APPROVED");
            approvelogRepository.save(approvelog);

            vehicleOrder.setNeedApproval(userGA);
            vehicleorderRepository.save(vehicleOrder);

            // send to users
            List<Employee> users = vehicleOrder.getUsers();
            boolean pemohonAdalahUser = false;
            for (Employee user : users) {
                if (user.getEmployeeCode().equals(pemohon.getEmployeeCode())) {
                    pemohonAdalahUser = true;
                }
                // send notification
                VehicleRequestAccepted notification = new VehicleRequestAccepted(previousNeedApprove.getFullName(), vehicleOrder.getVehicleOrderId(), vehicleOrder.getProyek().getProjectName());
                notificationService.pushNotification(notification.build(), user);
                // send email
                emailService.sendEmail(user, "Persetujuan Peminjaman Kendaraan",
                        EmailTemplateBuilder.create("Persetujuan Peminjaman Kendaraan")
                                .append("Halo, ")
                                .append("Peminjaman kendaraan anda telah disetujui oleh atasan anda.")
                                .appendEntry("Tujuan", vehicleOrder.getTujuan())
                                .appendEntry("Waktu Berangkat", vehicleOrder.getWaktuBerangkat().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                                .append("Pengajuan berikutnya akan ditinjau oleh Koordinator Kendaraan.")
                                .appendBtnPrimary("Lihat Detail di Aplikasi", frontEndURLService.getVehicleOrderDetailURL(vehicleOrder.getVehicleOrderId())).generate());
            }

            if (!pemohonAdalahUser) {
                // send notification to pemohon
                VehicleRequestAccepted notification = new VehicleRequestAccepted(previousNeedApprove.getFullName(), vehicleOrder.getVehicleOrderId(), vehicleOrder.getProyek().getProjectName());
                notificationService.pushNotification(notification.build(), pemohon);
                // send email to pemohon
                emailService.sendEmail(pemohon, "Persetujuan Peminjaman Kendaraan",
                        EmailTemplateBuilder.create("Persetujuan Peminjaman Kendaraan")
                                .append("Halo, ")
                                .append("Peminjaman kendaraan anda telah disetujui oleh atasan anda.")
                                .appendEntry("Tujuan", vehicleOrder.getTujuan())
                                .appendEntry("Waktu Berangkat", vehicleOrder.getWaktuBerangkat().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                                .append("Pengajuan berikutnya akan ditinjau oleh Koordinator Kendaraan.")
                                .appendBtnPrimary("Lihat Detail di Aplikasi", frontEndURLService.getVehicleOrderDetailURL(vehicleOrder.getVehicleOrderId())).generate());
            }

            VehiclePendingRequest notificationGA = new VehiclePendingRequest(pemohon.getFullName(), vehicleOrder.getVehicleOrderId(), vehicleOrder.getProyek().getProjectName(), vehicleOrder.getTujuan(), vehicleOrder.getWaktuBerangkat());
            notificationService.pushNotification(notificationGA.build(), userGA);

            // send email
            emailService.sendEmail(userGA, "Persetujuan Peminjaman Kendaraan",
                    EmailTemplateBuilder.create("Persetujuan Peminjaman Kendaraan")
                            .append("Halo, ")
                            .append("Anda memiliki permintaan peminjaman kendaraan yang perlu anda tinjau:")
                            .appendEntry("Nama", pemohon.getFullName())
                            .appendEntry("Tujuan", vehicleOrder.getTujuan())
                            .appendEntry("Waktu Berangkat", vehicleOrder.getWaktuBerangkat().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                            .appendBtnPrimary("Lihat Detail di Aplikasi", frontEndURLService.getVehicleOrderDetailNeedApproveURL(vehicleOrder.getVehicleOrderId())).generate());

            res.setMsg(SUCCESS_MESSAGE);
        }
        catch (Exception ex){
            res.setMsg("ERROR: "+ex.getMessage());
        }

        return res;
    }

    public String getWhatsappURL(String number) {
        if (number == null) {
            return null;
        }
        return "https://wa.me/"+generalizeWhatsappNumber(number);
    }

    public String generalizeWhatsappNumber(String number) {
        // must be in format 62xxxxxxxxxx
        if (number.startsWith("+")) {
            number = number.substring(1);
        }
        if (number.startsWith("0")) {
            number = "62" + number.substring(1);
        }
        if (!number.startsWith("62")) {
            number = "62" + number;
        }
        return number;
    }

    @Transactional
    public MessageResponse rejectOrder(RejectOrderRequest req) {
        MessageResponse res = new MessageResponse();
        try{
            VehicleOrder vehicleOrder = vehicleorderRepository.findByVehicleOrderId(req.getOrder_id());
            if(vehicleOrder == null)
                throw new Exception("ORDER NOT FOUND");
            Employee employee = employeeRepository.findByEmployeeId(req.getReject_id());
            if(employee == null)
                throw new Exception("REJECTER NOT FOUND");
            vehicleOrder.setApprovalPimproAtasan(null);
            vehicleOrder.setNeedApproval(null);
            vehicleOrder.setStatus("REJECTED");
            vehicleorderRepository.save(vehicleOrder);

            List<Employee> users = vehicleOrder.getUsers();
            for(Employee user : users){
                String subject = "Informasi Pengajuan Kendaraan";
                String body = "<h1>Pengajuan Kendaraan</h1>" +
                        "<p>Pengajuan ditolak karena</p>" + req.getAlasan();
                Antrianemail antrianemail = new Antrianemail();
                antrianemail.setEmail(user.getEmail());
                antrianemail.setStatus(false);
                antrianemail.setSubject(subject);
                antrianemail.setContent(body);
                antrianemailRepository.save(antrianemail);
            }

            Employee userPemesan = vehicleOrder.getPemesan();
            Approvelog approvelog = new Approvelog();
            approvelog.setFromUser(employee);
            approvelog.setToUser(userPemesan);
            approvelog.setStatus("REJECTED");
            approvelogRepository.save(approvelog);
            res.setMsg(SUCCESS_MESSAGE);
        }
        catch (Exception ex){
            res.setMsg("ERROR: "+ex.getMessage());
        }

        return res;
    }

    public MessageResponse deleteOrder(String oid) {
        MessageResponse res = new MessageResponse();
        try{
            VehicleOrder vehicleOrder = vehicleorderRepository.findByVehicleOrderId(oid);
            if(vehicleOrder == null)
                throw new Exception("ORDER NOT FOUND");
            vehicleorderRepository.delete(vehicleOrder);
            res.setMsg(SUCCESS_MESSAGE);
        }
        catch (Exception ex){
            res.setMsg("ERROR: "+ex.getMessage());
        }

        return res;
    }

    public OrderListResponse getListApprovedByGA(){
        OrderListResponse res = new OrderListResponse();
        List<OrderVehicleData> orderVehicleDataList = new ArrayList<>();
        try {
            List<VehicleOrder> vehicleOrders = vehicleorderRepository.findAllByStatus("APPROVED", "DONE");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            if(vehicleOrders.size() > 0){
                for(VehicleOrder vehicleOrder:vehicleOrders){
                    OrderVehicleData order = new OrderVehicleData();
                    order.setOrder_id(vehicleOrder.getVehicleOrderId());

                    List<String> penumpang = new ArrayList<>();
                    List<Employee> employeeList = vehicleOrder.getUsers();
                    for(Employee employee: employeeList){
                        String nm = employee.getFullName();
                        penumpang.add(nm);
                    }
                    order.setUsers(penumpang);

                    order.setWaktu_berangkat(vehicleOrder.getWaktuBerangkat().format(formatter));
                    order.setTanggal_kembali(vehicleOrder.getTanggalKembali());
                    order.setTujuan(vehicleOrder.getTujuan());
                    order.setKeperluan(vehicleOrder.getKeperluan());
                    Proyeknav proyek = vehicleOrder.getProyek();
                    order.setKode_proyek(proyek.getProjectName()+" ("+proyek.getProjectCode()+")");
                    order.setKeterangan(vehicleOrder.getKeterangan());
                    if(vehicleOrder.getApprovalPimproAtasan() != null) {
                        order.setApproval(vehicleOrder.getApprovalPimproAtasan().getFullName());
                    }

                    if(vehicleOrder.getMobil() != null) {
                        Vehicles vehicle = vehicleOrder.getMobil();
                        VehiclesData dataMobil = new VehiclesData();
                        dataMobil.setVehicle_id(vehicle.getVehicleId());
                        dataMobil.setMerk(vehicle.getMerk());
                        dataMobil.setBbm(vehicle.getBbm());
                        dataMobil.setType(vehicle.getType());
                        dataMobil.setOwnership(vehicle.getOwnership());
                        dataMobil.setTax_expired(vehicle.getTaxExpiredDate());
                        dataMobil.setPlat_number(vehicle.getPlatNumber());
                        dataMobil.setYear(vehicle.getYear());
                        dataMobil.setKeterangan(vehicle.getKeterangan());
                        dataMobil.setCertifcate_expired(vehicle.getCertificateExpiredDate());
                        order.setMobil(dataMobil);
                    } else if (vehicleOrder.getOtherPlatNumber() != null) {
                        VehiclesData dataMobil = new VehiclesData();
                        dataMobil.setPlat_number(vehicleOrder.getOtherPlatNumber());
                        dataMobil.setMerk(vehicleOrder.getOtherMerk());
                        dataMobil.setType(vehicleOrder.getOtherType());
                        dataMobil.setOwnership(vehicleOrder.getOtherOwnership());
                        dataMobil.setYear(vehicleOrder.getOtherYear());
                        dataMobil.setKeterangan(vehicleOrder.getOtherKeterangan());
                        dataMobil.setCertifcate_expired(vehicleOrder.getOtherCertificateExpired());
                        dataMobil.setTax_expired(vehicleOrder.getOtherTaxExpired());
                        dataMobil.setBbm(vehicleOrder.getOtherBbm());
                        order.setMobil(dataMobil);
                    }

                    order.setDriver(vehicleOrder.getDriver());
                    order.setHp_driver(vehicleOrder.getNoHpDriver());
                    if(vehicleOrder.getWaktuKembali() != null)
                        order.setWaktu_kembali(vehicleOrder.getWaktuKembali().format(formatter));
                    order.setStatus(vehicleOrder.getStatus());

                    if(vehicleOrder.getNeedApproval() != null){
                        order.setNeed_approve(vehicleOrder.getNeedApproval().getFullName());
                        order.setNeed_approve_id(vehicleOrder.getNeedApproval().getEmployeeId());
                    }

                    orderVehicleDataList.add(order);
                }
            }
            res.setMsg("SUCCESS");
            res.setCount(orderVehicleDataList.size());
            res.setOrders(orderVehicleDataList);

        } catch (Exception ex) {
            res.setMsg("ERROR: "+ex.getMessage());
        }
        return res;
    }

    public MessageResponse doneOrder(OrderVehicleRequest req){
        MessageResponse res = new MessageResponse();
        try {
            VehicleOrder order = vehicleorderRepository.findByVehicleOrderId(req.getId());
            if (order == null) {
                throw new Exception("NOT FOUND");
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("HH:mm");
            LocalDate date = LocalDate.parse(req.getTgl_kembali(), formatter);
            LocalTime time = LocalTime.parse(req.getJam_kembali(), formatter2);
            order.setTanggalKembali(date);
            order.setJamKembali(time);
            order.setStatus("DONE");
            vehicleorderRepository.save(order);
            res.setMsg("SUCCESS");

        } catch (Exception ex) {
            res.setMsg("ERROR: "+ex.getMessage());
        }
        return res;
    }

    //approveGA

    /**
     * @deprecated Fungsi sudah diwakilkan oleh approve order
     * @see OrderService#approveOrder(ApproveOrderRequest)
     * @param req
     * @return
     */
     @Transactional @Deprecated
    public MessageResponse approveGA(ApproveOrderGARequest req){
        MessageResponse res = new MessageResponse();
        try {
            VehicleOrder order = vehicleorderRepository.findByVehicleOrderId(req.getOrder_id());
            if (order == null) {
                throw new Exception("Order Not Found");
            }
            Employee userGA = employeeRepository.findByEmployeeId(req.getApproval_id());
            if (userGA == null) {
                throw new Exception("User GA Not Found");
            }
            Vehicles mobil = vehicleRepository.findByVehicleId(req.getVehicleId());
            if (mobil == null) {
                throw new Exception("Vehicle Not Found");
            }
            order.setMobil(mobil);
            order.setApprovalPimproAtasan(userGA);
            order.setStatus("APPROVED");
            order.setNeedApproval(null);
            order.setDriver(req.getDriver());
            order.setNoHpDriver(req.getHpDriver());
            //approvallog
            vehicleorderRepository.save(order);
            List<Employee> users = order.getUsers();
            for(Employee user : users){
                String subject = "Informasi Pengajuan Kendaraan";
                String body = "<h1>Pengajuan Kendaraan</h1>" +
                                "<p>Pengajuan telah diapprove</p>";
                Antrianemail antrianemail = new Antrianemail();
                //kirim email
                antrianemail.setEmail(user.getEmail());
                antrianemail.setStatus(false);
                antrianemail.setSubject(subject);
                antrianemail.setContent(body);
                antrianemailRepository.save(antrianemail);
            }
            res.setMsg("SUCCESS");
        } catch (Exception ex) {
            res.setMsg("ERROR: "+ex.getMessage());
        }
        return res;
    }


    /**
     * @deprecated Sudah diwakilkan oleh method reject
     * @see OrderService#rejectOrder(RejectOrderRequest)
     * @param req
     * @return
     */
    //rejectGA
    @Transactional @Deprecated
    public MessageResponse rejectGA(RejectOrderRequest req){
        MessageResponse res = new MessageResponse();
        try {
            Employee userGA = employeeRepository.findByEmployeeId(req.getReject_id());
            if (userGA == null) {
                throw new Exception("REJECTER NOT FOUND");
            }
            VehicleOrder order = vehicleorderRepository.findByVehicleOrderId(req.getOrder_id());
            if (order == null) {
                throw new Exception("ORDER NOT FOUND");
            }
            order.setNeedApproval(null);
            order.setStatus("REJECTED");
            order.setApprovalPimproAtasan(null);
            vehicleorderRepository.save(order);

            List<Employee> users = order.getUsers();
            for(Employee user : users){
                String subject = "Informasi Pengajuan Kendaraan";
                String body = "<h1>Pengajuan Kendaraan</h1>" +
                                "<p>Pengajuan ditolak karena</p>" + req.getAlasan();
                Antrianemail antrianemail = new Antrianemail();
                antrianemail.setEmail(user.getEmail());
                antrianemail.setStatus(false);
                antrianemail.setSubject(subject);
                antrianemail.setContent(body);
                antrianemailRepository.save(antrianemail);
            }
            res.setMsg("SUCCESS");

        } catch (Exception ex) {
            // TODO: handle exception
            res.setMsg("ERROR: "+ex.getMessage());
        }
        return res;
    }
}
