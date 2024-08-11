package com.suryaenergi.sdm.backendapi.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.suryaenergi.sdm.backendapi.pojo.JatahCutiGroup;
import com.suryaenergi.sdm.backendapi.pojo.JatahCutiSimpleGroup;
import com.suryaenergi.sdm.backendapi.repository.IzinCutiRepository;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.suryaenergi.sdm.backendapi.entity.Employee;
import com.suryaenergi.sdm.backendapi.entity.JatahCuti;
import com.suryaenergi.sdm.backendapi.pojo.JatahCutiData;
import com.suryaenergi.sdm.backendapi.repository.EmployeeRepository;
import com.suryaenergi.sdm.backendapi.repository.JatahCutiRepository;

@Service
public class JatahCutiService {
    public static final String KETERANGAN_INPUT_DATA_JATAH_CUTI = "INPUT_DATA";
    public static final String KETERANGAN_PEMAKAIAN_CUTI = "PEMAKAIAN_CUTI";
    public static final String KETERANGAN_PEMBATALAN_CUTI = "PEMBATALAN_CUTI";
    public static final String KETERANGAN_PEMAKAIAN_CUTI_TIDAK_DISETUJUI = "PEMAKAIAN_CUTI_TIDAK_DI_SETUJUI";
    public static final String KETERANGAN_AKUMULASI_IZIN = "AKUMULASI_IZIN";
    @Autowired
    private JatahCutiRepository jatahCutiRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private IzinCutiRepository izinCutiRepository;

    @Value("classpath:template_input_jatah_cuti.xlsx")
    private Resource jatahCutiTemplate;

    public byte[] downloadTemplate() throws IOException {
        // load com.suryaenergi.sdm.backendapi.template.template_input_jatah_cuti.xlsx
        byte[] read = jatahCutiTemplate.getContentAsByteArray();
        Iterable<Employee> employees = employeeRepository.findAll();
        Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(read));
        Sheet sheet = workbook.getSheetAt(1); // 2nd sheet adalah data pegawai
        int row = 1;
        for (Employee employee : employees) {
            sheet.createRow(row).createCell(0).setCellValue(employee.getEmployeeCode());
            sheet.getRow(row).createCell(1).setCellValue(employee.getFullName());
            row++;
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        return outputStream.toByteArray();
    }

    public void loadTemplate(byte[] fileContents) throws IOException {
        Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(fileContents));
        Sheet sheet = workbook.getSheetAt(0); // 1st sheet adalah data jatah cuti
        // Kode Pegawai Column C starts from row 9
        // Tahun Column E starts from row 9
        // Jatah Cuti Column F starts from row 9
        int row = 9;
        while (sheet.getRow(row) != null && sheet.getRow(row).getCell(2) != null) {
            String empcode = sheet.getRow(row).getCell(2).getStringCellValue();
            try {
                int tahun = (int) sheet.getRow(row).getCell(4).getNumericCellValue();
                int jumlahHari = (int) sheet.getRow(row).getCell(5).getNumericCellValue();
                setJatahCuti(empcode, tahun, jumlahHari);
            } catch (Exception e) {
                throw new RuntimeException("ERROR_READING_EXCEL: Gagal membaca baris ke-" + row+" untuk kode pegawai "+empcode);
            }
            row++;
        }
        workbook.close();
    }

    public void addJatahCuti(String empcode, int tahun, int jumlahHari, String keterangan, String referrer) {
        if (jumlahHari == 0) {
            return;
        }
        Employee employee = employeeRepository.findByEmployeeCode(empcode);
        if (employee == null) {
            throw new RuntimeException("EMPLOYEE_NOT_FOUND");
        }
        JatahCuti jatahCuti = new JatahCuti();
        jatahCuti.setEmployeeId(employee.getEmployeeId());
        jatahCuti.setTahun(tahun);
        jatahCuti.setJumlahCuti(jumlahHari);
        jatahCuti.setKeterangan(keterangan);
        jatahCuti.setReferrer(referrer);
        jatahCutiRepository.save(jatahCuti);
    }

    public void setJatahCuti(String empcode, int tahun, int jumlahHari) {
        Employee employee = employeeRepository.findByEmployeeCode(empcode);
        if (employee == null) {
            throw new RuntimeException("EMPLOYEE_NOT_FOUND");
        }
        int sumJatahCuti = jatahCutiRepository.sumJatahCutiByEmployeeIdAndTahun(employee.getEmployeeId(), tahun).orElse(0L).intValue();
        int diff = jumlahHari - sumJatahCuti;
        if (diff == 0) {
            return;
        }
        JatahCuti jatahCuti = new JatahCuti();
        jatahCuti.setEmployeeId(employee.getEmployeeId());
        jatahCuti.setTahun(tahun);
        jatahCuti.setJumlahCuti(diff);
        jatahCuti.setKeterangan(KETERANGAN_INPUT_DATA_JATAH_CUTI);
    }

    public int sumJatahCutiBalance(String empcode, int tahun) {
        Employee employee = employeeRepository.findByEmployeeCode(empcode);
        if (employee == null) {
            throw new RuntimeException("EMPLOYEE_NOT_FOUND");
        }
        return jatahCutiRepository.sumJatahCutiByEmployeeIdAndTahun(employee.getEmployeeId(), tahun).orElse(0L).intValue();
    }

    public List<JatahCutiData> getJatahCutiList(String empcode, int tahun, Long after) {
        Employee employee = employeeRepository.findByEmployeeCode(empcode);
        if (employee == null) {
            throw new RuntimeException("EMPLOYEE_NOT_FOUND");
        }
        List<JatahCuti> jatahCutiList = after != null ? jatahCutiRepository.findJatahCutiByEmployeeIdAndTahunAfter(employee.getEmployeeId(), tahun, after) : jatahCutiRepository.findJatahCutiByEmployeeIdAndTahunAfter(employee.getEmployeeId(), tahun);
        List<JatahCutiData> jatahCutiDataList = new ArrayList<>();
        Map<Long, String> cache = new HashMap<>();
        for (JatahCuti jatahCuti : jatahCutiList) {
            JatahCutiData jatahCutiData = new JatahCutiData();
            jatahCutiData.setId(jatahCuti.getId());
            jatahCutiData.setEmployeeId(jatahCuti.getEmployeeId());
            jatahCutiData.setEmpcode(employee.getEmployeeCode());
            jatahCutiData.setEmployeeName(employee.getFullName());
            jatahCutiData.setTahun(jatahCuti.getTahun());
            jatahCutiData.setJumlahCuti(jatahCuti.getJumlahCuti());
            jatahCutiData.setKeterangan(jatahCuti.getKeterangan());
            jatahCutiData.setReferrer(jatahCuti.getReferrer());
            jatahCutiData.setCreatedDate(jatahCuti.getCreatedAt());
            jatahCutiData.setUpdatedDate(jatahCuti.getUpdatedAt());
            try {
                long izinCutiId = Long.parseLong(jatahCuti.getReferrer());
                String izinCuti = cache.computeIfAbsent(izinCutiId, id -> izinCutiRepository.izinCutiJenisName(izinCutiId));
                if (izinCuti != null) {
                    jatahCutiData.setReferrerName(izinCuti);
                }
            } catch (NumberFormatException ignored) {
            }
            jatahCutiDataList.add(jatahCutiData);
        }
        return jatahCutiDataList;
    }

    public List<JatahCutiData> groupJatahCutiByEmployeeIdAndTahun() {
        List<JatahCutiGroup> jatahCutiList = jatahCutiRepository.sumGroupByEmployeeIdAndTahun();
        List<JatahCutiData> jatahCutiDataList = new ArrayList<>();
        for (JatahCutiGroup jatahCuti : jatahCutiList) {
            JatahCutiData jatahCutiData = new JatahCutiData();
            Employee employee = employeeRepository.findByEmployeeId(jatahCuti.getEmployee_id());
            if (employee == null) {
                continue;
            }
            jatahCutiData.setEmployeeId(jatahCuti.getEmployee_id());
            jatahCutiData.setEmployeeName(employee.getFullName());
            jatahCutiData.setEmpcode(employee.getEmployeeCode());
            jatahCutiData.setTahun(jatahCuti.getTahun());
            jatahCutiData.setJumlahCuti((int) jatahCuti.getJumlah_cuti());
            jatahCutiDataList.add(jatahCutiData);
        }
        return jatahCutiDataList;
    }

    public List<JatahCutiData> groupJatahCutiByEmployeeIdAndTahun(int year) {
        List<JatahCutiGroup> jatahCutiList = jatahCutiRepository.sumGroupByEmployeeIdAndTahun(year);
        List<JatahCutiData> jatahCutiDataList = new ArrayList<>();
        for (JatahCutiGroup jatahCuti : jatahCutiList) {
            JatahCutiData jatahCutiData = new JatahCutiData();
            Employee employee = employeeRepository.findByEmployeeId(jatahCuti.getEmployee_id());
            if (employee == null) {
                continue;
            }
            jatahCutiData.setEmployeeId(jatahCuti.getEmployee_id());
            jatahCutiData.setEmployeeName(employee.getFullName());
            jatahCutiData.setEmpcode(employee.getEmployeeCode());
            jatahCutiData.setTahun(jatahCuti.getTahun());
            jatahCutiData.setJumlahCuti((int) jatahCuti.getJumlah_cuti());
            jatahCutiDataList.add(jatahCutiData);
        }
        return jatahCutiDataList;
    }

    public List<JatahCutiData> getJatahCutiByEmployee(String empcode) {
        Employee employee = employeeRepository.findByEmployeeCode(empcode);
        if (employee == null) {
            throw new RuntimeException("EMPLOYEE_NOT_FOUND");
        }
        List<JatahCutiSimpleGroup> jatahCuti = jatahCutiRepository.sumGroupByEmployeeIdAndTahun(employee.getEmployeeId());
        List<JatahCutiData> jatahCutiDataList = new ArrayList<>();
        for (JatahCutiSimpleGroup jatahCuti1 : jatahCuti) {
            JatahCutiData jatahCutiData = new JatahCutiData();
            jatahCutiData.setEmployeeId(employee.getEmployeeId());
            jatahCutiData.setEmployeeName(employee.getFullName());
            jatahCutiData.setEmpcode(employee.getEmployeeCode());
            jatahCutiData.setTahun(jatahCuti1.getTahun() == null ? 0 : jatahCuti1.getTahun());
            Long jumlahCuti = jatahCuti1.getJumlah_cuti();
            jatahCutiData.setJumlahCuti(jumlahCuti == null ? 0 : jumlahCuti.intValue());
            jatahCutiDataList.add(jatahCutiData);
        }
        return jatahCutiDataList;
    }

    public JatahCutiData getJatahCutiByEmployeeAndTahun(String empcode, int year) {
        Employee employee = employeeRepository.findByEmployeeCode(empcode);
        if (employee == null) {
            throw new RuntimeException("EMPLOYEE_NOT_FOUND");
        }
        JatahCutiSimpleGroup jatahCuti = jatahCutiRepository.sumGroupByEmployeeIdAndTahun(employee.getEmployeeId(), year);
        if (jatahCuti == null) {
            JatahCutiData jatahCutiData = new JatahCutiData();
            jatahCutiData.setEmployeeId(employee.getEmployeeId());
            jatahCutiData.setEmployeeName(employee.getFullName());
            jatahCutiData.setEmpcode(employee.getEmployeeCode());
            jatahCutiData.setTahun(year);
            jatahCutiData.setJumlahCuti(0);
            return jatahCutiData;
        }
        JatahCutiData jatahCutiData = new JatahCutiData();
        jatahCutiData.setEmployeeId(employee.getEmployeeId());
        jatahCutiData.setEmployeeName(employee.getFullName());
        jatahCutiData.setEmpcode(employee.getEmployeeCode());
        jatahCutiData.setTahun(jatahCuti.getTahun() == null ? 0 : jatahCuti.getTahun());
        Long jumlahCuti = jatahCuti.getJumlah_cuti();
        jatahCutiData.setJumlahCuti(jumlahCuti == null ? 0 : jumlahCuti.intValue());
        return jatahCutiData;
    }

}
