package com.suryaenergi.sdm.backendapi.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.suryaenergi.sdm.backendapi.entity.Employee;
import com.suryaenergi.sdm.backendapi.entity.Proyeknav;
import com.suryaenergi.sdm.backendapi.entity.VehicleOrder;
import com.suryaenergi.sdm.backendapi.entity.Vehicles;
import com.suryaenergi.sdm.backendapi.repository.EmployeeRepository;
import com.suryaenergi.sdm.backendapi.repository.VehicleorderRepository;

@RestController
@RequestMapping("/dow")
public class WordDownloadController {

    @Autowired
    private VehicleorderRepository vehicleorderRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    

    @GetMapping("/word/{oid}")
    public ResponseEntity<byte[]> downloadWord(@PathVariable String oid) throws IOException {
        // Load template
        ClassPathResource resource = new ClassPathResource("form_order_kendaraan.docx");
        XWPFDocument document = new XWPFDocument(resource.getInputStream());

        VehicleOrder order = vehicleorderRepository.findByVehicleOrderId(oid);

        //debug
        // if (order == null) {
        //     System.out.println("NOT FOUND");
        // }else{
        //     System.out.println("FOUND");
        // }

        
        replacePlaceholders(document, order);

       
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        document.write(outputStream);
        byte[] documentBytes = outputStream.toByteArray();


        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition.builder("attachment").filename("form_order_kendaraan.docx").build());
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        return new ResponseEntity<>(documentBytes, headers, HttpStatus.OK);
    }


    private void replacePlaceholders(XWPFDocument document, VehicleOrder order) {
        Employee pemesan = order.getPemesan();
        Proyeknav proyek = order.getProyek();
        Vehicles mobil = order.getMobil();
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", new Locale("id","ID"));
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter formatter3 = DateTimeFormatter.ofPattern("EEEE", new Locale("id","ID"));
        String tanggalBerangkat = order.getWaktuBerangkat().format(formatter);
        String waktuBerangkat = order.getWaktuBerangkat().format(formatter2);
        String hari = order.getTanggalKembali().format(formatter3);
        String tanggalKembali = order.getTanggalKembali().format(formatter);
        String waktuKembali = null;
        if (order.getStatus().equalsIgnoreCase("DONE")) {
            waktuKembali = order.getJamKembali().format(formatter2);
        }else{
            waktuKembali = "Peminjaman belum selesai";
        }
        String ats = "-";
        if (proyek.getProjectType().equalsIgnoreCase("rutin")) {
            Employee atasan = employeeRepository.findFirstByUserIdNav(pemesan.getAtasanUserId());
            ats = atasan.getFullName();
        }else{
            Employee pimpro = employeeRepository.findFirstByUserIdNav(proyek.getPimpro());
            ats = pimpro.getFullName();
        }
        // debug
        // System.out.println(hari);
        String merk = "-";
        String type = "-";
        String plat = "-";
        String ket = "-";
        if (order.getOtherMerk() != null) {
            merk = order.getOtherMerk();
            type = order.getOtherType();
            plat = order.getOtherPlatNumber();
            ket = order.getOtherKeterangan();
        }else{
            merk = mobil.getMerk();
            type = mobil.getType();
            plat = mobil.getPlatNumber();
            ket = mobil.getKeterangan();
        }
        for (XWPFParagraph paragraph : document.getParagraphs()) {
            for (XWPFRun run : paragraph.getRuns()) {
                String text = run.getText(0);
                if (text != null && !text.isEmpty()) {
                    text = text.replace("${date1}", tanggalBerangkat);
                    text = text.replace("${name}", pemesan.getFullName());
                    text = text.replace("${time1}", waktuBerangkat);
                    text = text.replace("${day}", hari);
                    text = text.replace("${date2}", tanggalBerangkat);
                    text = text.replace("${date3}", tanggalKembali);
                    text = text.replace("${tujuan}", order.getTujuan());
                    text = text.replace("${keperluan}", order.getKeperluan());
                    text = text.replace("${pro}", proyek.getProjectName());
                    text = text.replace("${ats}", ats);
                    text = text.replace("${ktr}", order.getKeterangan());
                    text = text.replace("${kdr}", merk);
                    text = text.replace("${type}", type);
                    text = text.replace("${nom}", plat);
                    text = text.replace("${driver}", order.getDriver());
                    text = text.replace("${nohand}", order.getNoHpDriver());
                    text = text.replace("${rental}", ket);
                    text = text.replace("${time2}", waktuKembali);
                    run.setText(text, 0);
                }
            }
        }
    }
}
