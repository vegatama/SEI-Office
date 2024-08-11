package com.suryaenergi.sdm.backendapi.service;

import com.suryaenergi.sdm.backendapi.email.Alignment;
import com.suryaenergi.sdm.backendapi.email.BorderSide;
import com.suryaenergi.sdm.backendapi.email.EdgeInsets;
import com.suryaenergi.sdm.backendapi.email.SizeUnit;
import com.suryaenergi.sdm.backendapi.email.template.EmailTemplateBuilder;
import com.suryaenergi.sdm.backendapi.entity.*;
import com.suryaenergi.sdm.backendapi.pojo.SlipGajiDataEntry;
import com.suryaenergi.sdm.backendapi.pojo.SlipGajiField;
import com.suryaenergi.sdm.backendapi.pojo.SlipGajiItemData;
import com.suryaenergi.sdm.backendapi.repository.*;
import com.suryaenergi.sdm.backendapi.request.SlipGajiTemplateUpdate;
import com.suryaenergi.sdm.backendapi.response.SlipGajiDataResponse;
import com.suryaenergi.sdm.backendapi.response.SlipGajiDetailResponse;
import com.suryaenergi.sdm.backendapi.response.SlipGajiSendEmailResponse;
import com.suryaenergi.sdm.backendapi.response.SlipGajiTemplateListResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFTable;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xmlbeans.StringEnumAbstractBase;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.namespace.QName;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static com.suryaenergi.sdm.backendapi.pojo.Message.ERROR_MESSAGE;
import static com.suryaenergi.sdm.backendapi.pojo.Message.SUCCESS_MESSAGE;

@Service
public class SlipGajiService {

    public static final String DEFAULT_PASSWORD = "123456";

    @Autowired
    private SlipGajiTemplateRepository slipGajiTemplateRepository;
    @Autowired
    private SlipGajiFieldTemplateRepository slipGajiFieldTemplateRepository;
    @Autowired
    private SlipGajiEntryDataRepository slipGajiEntryDataRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Value("classpath:template_input_slip_gaji.xlsx")
    private Resource slipGajiTemplate;
    @Autowired
    private AntrianemailRepository antrianemailRepository;
    @Autowired
    private FrontEndURLService frontEndURLService;

    public SlipGajiTemplateListResponse getTemplateList() {
        Iterable<SlipGajiTemplate> all = slipGajiTemplateRepository.findAll();
        List<SlipGajiItemData> items = new ArrayList<>();
        for (SlipGajiTemplate template : all) {
            SlipGajiItemData item = new SlipGajiItemData();
            List<SlipGajiFieldTemplate> fields = slipGajiFieldTemplateRepository.findAllByTemplate(template.getId());
            item.setId(template.getId());
            item.setName(template.getName());
            item.setBulan(template.getBulan());
            item.setTahun(template.getTahun());
            item.setRevision(template.getRevisi() == null ? 1 : template.getRevisi());
            item.setLastUpdate(template.getWaktuRevisi() == null ? template.getCreatedAt() : template.getWaktuRevisi());
            List<SlipGajiEntryData> dataEntries = slipGajiEntryDataRepository.findAllBySlipFieldIsIn(fields.stream().map(SlipGajiFieldTemplate::getId).toList());
            Map<String, List<SlipGajiEntryData>> groupedData = new HashMap<>();
            if (dataEntries != null) {
                for (SlipGajiEntryData dataEntry : dataEntries) {
                    List<SlipGajiEntryData> dataList = groupedData.computeIfAbsent(dataEntry.getEmployee(), k -> new ArrayList<>());
                    dataList.add(dataEntry);
                }
            }
            if (dataEntries == null || dataEntries.isEmpty()) {
                item.setStatus(SlipGajiItemData.Status.EMPTY);
            } else {
                boolean hasIncomplete = false;
                int completeCount = 0;
                for (Map.Entry<String, List<SlipGajiEntryData>> entry : groupedData.entrySet()) {
                    List<SlipGajiEntryData> dataList = entry.getValue();
                    if (dataList.size() < fields.size()) {
                        hasIncomplete = true;
                        continue;
                    }
                    boolean employeeHasIncomplete = false;
                    for (SlipGajiFieldTemplate field : fields) {
                        boolean hasField = false;
                        for (SlipGajiEntryData dataEntry : dataList) {
                            if (dataEntry.getSlipField().equals(field.getId())) {
                                hasField = true;
                                break;
                            }
                        }
                        if (!hasField) {
                            employeeHasIncomplete = true;
                            break;
                        }
                    }
                    if (employeeHasIncomplete) {
                        hasIncomplete = true;
                        continue;
                    }
                    completeCount++;
                }
                if (hasIncomplete) {
                    if (completeCount > 0) {
                        item.setStatus(SlipGajiItemData.Status.FILLED_SOME);
                    } else {
                        item.setStatus(SlipGajiItemData.Status.INCOMPLETE);
                    }
                } else {
                    item.setStatus(SlipGajiItemData.Status.FILLED);
                }
                item.setCanSend(completeCount > 0);
            }
            items.add(item);
        }
        SlipGajiTemplateListResponse response = new SlipGajiTemplateListResponse();
        response.setMessage(SUCCESS_MESSAGE);
        response.setTemplates(items);
        return response;
    }

    public SlipGajiSendEmailResponse sendToEmails(long id) {
        SlipGajiTemplate template = slipGajiTemplateRepository.findById(id).orElse(null);
        if (template == null) {
            throw new IllegalArgumentException("TEMPLATE NOT FOUND");
        }
        int bulan = template.getBulan();
        int tahun = template.getTahun();
        List<SlipGajiFieldTemplate> fields = slipGajiFieldTemplateRepository.findAllByTemplate(template.getId());
        Map<Long, SlipGajiFieldTemplate> cachedFields = new HashMap<>(fields.size());
        for (SlipGajiFieldTemplate field : fields) {
            cachedFields.put(field.getId(), field);
        }
        List<SlipGajiEntryData> dataEntries = slipGajiEntryDataRepository.findAllBySlipFieldIsIn(cachedFields.keySet());
        Map<String, List<SlipGajiEntryData>> dataMap = new HashMap<>();
        for (SlipGajiEntryData dataEntry : dataEntries) {
            List<SlipGajiEntryData> dataList = dataMap.computeIfAbsent(dataEntry.getEmployee(), k -> new ArrayList<>());
            dataList.add(dataEntry);
        }
        Map<String, Employee> cachedEmployees = new HashMap<>();
        int completeCount = 0;
        int incompleteCount = 0;
        int notSentCount = 0;
        for (Map.Entry<String, List<SlipGajiEntryData>> entry : dataMap.entrySet()) {
            Employee employee = cachedEmployees.computeIfAbsent(entry.getKey(), k -> employeeRepository.findByEmployeeCode(k));
            if (employee == null) {
                continue;
            }
            if (employee.getEmail() == null) {
                notSentCount++;
                continue;
            }
            // check if all fields are sent, if yes, then skip
            boolean needToSend = false;
            for (SlipGajiEntryData data : entry.getValue()) {
                Boolean isSent = data.getIsSent();
                if (isSent == null || !isSent) {
                    needToSend = true;
                    break;
                }
            }
            boolean isComplete = entry.getValue().size() == fields.size();
            if (isComplete) {
                // check if all fields are present
                for (SlipGajiEntryData data : entry.getValue()) {
                    if (!cachedFields.containsKey(data.getSlipField())) {
                        isComplete = false;
                        break;
                    }
                }
            }
            if (!needToSend) {
                // all fields are sent means there is no changes made
                // skip this employee
                notSentCount++;
                continue;
            }
            // send email
            if (isComplete) {
                completeCount++;
            } else {
                incompleteCount++;
                continue;
            }
            for (SlipGajiEntryData data : entry.getValue()) {
                data.setIsSent(true);
                slipGajiEntryDataRepository.save(data);
            }
            String result = generateEmail(employee, template);
            // send email
            Antrianemail antrianemail = new Antrianemail();
//            antrianemail.setSubject("Slip Gaji " + MONTHS[bulan - 1] + " " + tahun);
//            ubah jadi "Slip Gaji <Nama> (Bulan Tahun)"
            String name = template.getName();
            if (name == null || name.isEmpty()) {
                // normal: "Slip Gaji (Bulan Tahun)"
                antrianemail.setSubject("Slip Gaji (" + MONTHS[bulan - 1] + " " + tahun + ")");
            } else {
                antrianemail.setSubject("Slip Gaji " + name + " (" + MONTHS[bulan - 1] + " " + tahun + ")");
            }
            antrianemail.setContent(result);
            antrianemail.setEmail(employee.getEmail());
            antrianemailRepository.save(antrianemail);
        }
        return new SlipGajiSendEmailResponse(SUCCESS_MESSAGE, completeCount, notSentCount, incompleteCount);
    }

    public String getPDFPassword(Employee employee) {
        LocalDate tanggalLahir = employee.getTanggalLahir();
        if (tanggalLahir == null) {
            return DEFAULT_PASSWORD;
        }
        // format: 2 digit tanggal lahir (02 atau 12 contohnya) + 2 digit bulan lahir (01 atau 12 contohnya) + 2 digit terakhir tahun lahir (misal 1990 jadi 90)
        // bulan mulai dari 1 = Januari, 2 = Februari, dst
        int tanggal = tanggalLahir.getDayOfMonth();
        int bulan = tanggalLahir.getMonthValue(); // this is 1-based
        int tahun = tanggalLahir.getYear();
        return String.format("%02d%02d%02d", tanggal, bulan, tahun % 100);
    }

    public String getPDFForEmployee(Employee employee, long id) {
        SlipGajiTemplate template = slipGajiTemplateRepository.findById(id).orElse(null);
        if (template == null) {
            throw new IllegalArgumentException("NOT FOUND");
        }
        List<SlipGajiFieldTemplate> fields = slipGajiFieldTemplateRepository.findAllByTemplate(template.getId());
        Map<Long, SlipGajiFieldTemplate> cachedFields = new HashMap<>(fields.size());
        for (SlipGajiFieldTemplate field : fields) {
            cachedFields.put(field.getId(), field);
        }
        List<SlipGajiEntryData> dataEntries = slipGajiEntryDataRepository.findAllByEmployeeAndSlipFieldIsIn(employee.getEmployeeCode(), fields.stream().map(SlipGajiFieldTemplate::getId).toList());
        return generatePDF(employee, dataEntries, cachedFields, template);
    }

    private static final String[] MONTHS = new String[] {
        "Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember"
    };
    private static final String[] ABBR_MONTHS = new String[] {
        "Jan", "Feb", "Mar", "Apr", "Mei", "Jun", "Jul", "Agu", "Sep", "Okt", "Nov", "Des"
    };

    public String formatDate(LocalDate date) {
        if (date == null || date.isEqual(LocalDate.MIN) || date.isEqual(LocalDate.MAX) || date.isEqual(LocalDate.EPOCH) || date.isEqual(LocalDate.of(1, 1, 1))) {
            // its impossible to have a date that is equal to LocalDate.MIN or LocalDate.MAX
            // probably the date is 0000-00-00 or 9999-99-99 on the database
            return "-";
        }
        int month = date.getMonthValue();
        return date.getDayOfMonth() + " " + MONTHS[month - 1] + " " + date.getYear();
    }

    @AllArgsConstructor
    @Data
    public class DataKolom {
        private final String namaField;
        private final int value;
    }

    @Data
    @AllArgsConstructor
    public class DataBaris {
        private final DataKolom[] kolom;
    }

    @Data
    @AllArgsConstructor
    public class DataTabel {
        private final DataBaris[] baris;
        private final SlipGajiFieldTemplate.Kategori[] kategori;
    }

    public DataTabel entriesToRows(Map<SlipGajiFieldTemplate, Integer> valueMap) {
        // rows is:
        // Pengeluaran, Pemasukan
        // data pengeluaran 1, data pemasukan 1
        // data pengeluaran 2, data pemasukan 2
        // -, data pemasukan 3 (jika pengeluaran tidak ada, tapi pemasukan ada)
        // jika tidak ada pemasukan, atau pengeluaran, maka tidak perlu ditampilkan
        Map<SlipGajiFieldTemplate.Kategori, List<Map.Entry<SlipGajiFieldTemplate, Integer>>> grouped = new HashMap<>();
        for (Map.Entry<SlipGajiFieldTemplate, Integer> entry : valueMap.entrySet()) {
            SlipGajiFieldTemplate field = entry.getKey();
            List<Map.Entry<SlipGajiFieldTemplate, Integer>> list = grouped.computeIfAbsent(field.getKategori(), k -> new ArrayList<>());
            list.add(entry);
        }
        List<Map.Entry<SlipGajiFieldTemplate.Kategori, List<Map.Entry<SlipGajiFieldTemplate, Integer>>>> entries = new ArrayList<>(grouped.entrySet());
        entries.sort(Comparator.comparingInt(e -> e.getKey().ordinal()));
        int maxRows = 0;
        for (Map.Entry<SlipGajiFieldTemplate.Kategori, List<Map.Entry<SlipGajiFieldTemplate, Integer>>> entry : entries) {
            List<Map.Entry<SlipGajiFieldTemplate, Integer>> list = entry.getValue();
            if (list.size() > maxRows) {
                maxRows = list.size();
            }
        }
        DataBaris[] rows = new DataBaris[maxRows];
        int totalColumns = grouped.size();
        for (int i = 0; i < maxRows; i++) {
            DataKolom[] row = new DataKolom[totalColumns];
            for (int j = 0; j < totalColumns; j++) {
                List<Map.Entry<SlipGajiFieldTemplate, Integer>> list = entries.get(j).getValue();
                list.sort(Comparator.comparingInt(e -> e.getKey().getUrutan()));
                if (i < list.size()) {
                    Map.Entry<SlipGajiFieldTemplate, Integer> item = list.get(i);
                    row[j] = new DataKolom(item.getKey().getNamaField(), item.getValue());
                } else {
                    row[j] = null;
                }
            }
            rows[i] = new DataBaris(row);
        }
        SlipGajiFieldTemplate.Kategori[] kategori = new SlipGajiFieldTemplate.Kategori[totalColumns];
        for (int i = 0; i < totalColumns; i++) {
            kategori[i] = entries.get(i).getKey();
        }
        return new DataTabel(rows, kategori);
    }

    public String intToRupiah(int value) {
        // format into Rp. 1.000.000
        return "Rp. " + String.format("%,d", value).replace(',', '.');
    }

    public String generatePDF(Employee employee, List<SlipGajiEntryData> entries, Map<Long, SlipGajiFieldTemplate> cachedFields, SlipGajiTemplate template) {
        Map<SlipGajiFieldTemplate, Integer> valueMap = new HashMap<>();
        for (SlipGajiEntryData entry : entries) {
            SlipGajiFieldTemplate field = cachedFields.computeIfAbsent(entry.getSlipField(), k -> slipGajiFieldTemplateRepository.findById(k).orElse(null));
            if (field == null) {
                continue;
            }
            valueMap.put(field, entry.getValue());
        }
        int bulan = template.getBulan();
        int tahun = template.getTahun();
        LocalDate mulaiKerja = employee.getMulaiKerja();
        String name = template.getName();
        EmailTemplateBuilder builder = EmailTemplateBuilder.createPDFCompatible(name == null ? "Slip Gaji" : "Slip Gaji: " + name)
                .appendSpacer(SizeUnit.px(25))
                .newBorderlessFullWidthTable()
                .appendRow()
                .appendCell("<b>Nama Pegawai</b>")
                .appendCell(":").withPadding(EdgeInsets.padHorizontal(SizeUnit.px(10)))
                .appendCell(employee.getFullName())
                .appendSpacerCell().withMinColWidth(SizeUnit.px(25))
                .appendCell("<b>Jabatan</b>")
                .appendCell(":").withPadding(EdgeInsets.padHorizontal(SizeUnit.px(10)))
                .appendCell(employee.getJobTitle())
                .appendRow()
                .appendCell("<b>NIK</b>")
                .appendCell(":").withPadding(EdgeInsets.padHorizontal(SizeUnit.px(10)))
                .appendCell(employee.getNik()).withCellAlignment(Alignment.CENTER_LEFT)
                .appendSpacerCell().withMinColWidth(SizeUnit.px(25))
                .appendCell("<b>Periode Gaji</b>")
                .appendCell(":").withPadding(EdgeInsets.padHorizontal(SizeUnit.px(10)))
                .appendCell(MONTHS[bulan - 1] + " " + tahun)
                .appendRow()
                .appendCell("<b>Tanggal Mulai Bekerja</b>")
                .appendCell(":").withPadding(EdgeInsets.padHorizontal(SizeUnit.px(10)))
                .appendCell(formatDate(mulaiKerja)).withCellAlignment(Alignment.CENTER_LEFT)
                .appendSpacerCell().withMinColWidth(SizeUnit.px(25))
                .appendCell("<b>Tanggal Cetak</b>")
                .appendCell(":").withPadding(EdgeInsets.padHorizontal(SizeUnit.px(10)))
                .appendCell(formatDate(LocalDate.now())).withCellAlignment(Alignment.CENTER_LEFT)
                .appendSpacer(SizeUnit.px(25))
                .newBorderlessFullWidthTable(EdgeInsets.padHorizontal(SizeUnit.px(10)).thenVertical(SizeUnit.px(5)))
                .appendHeader();
        Map<SlipGajiFieldTemplate.Kategori, Integer> sumByCategory = new HashMap<>();
        for (Map.Entry<SlipGajiFieldTemplate, Integer> entry : valueMap.entrySet()) {
            SlipGajiFieldTemplate field = entry.getKey();
            SlipGajiFieldTemplate.Kategori kategori = field.getKategori();
            Integer sum = sumByCategory.computeIfAbsent(kategori, k -> 0);
            sumByCategory.put(kategori, sum + entry.getValue());
        }
        DataTabel table = entriesToRows(valueMap);
        SlipGajiFieldTemplate.Kategori[] tableKategori = table.getKategori();
        for (int i = 0; i < tableKategori.length; i++) {
            SlipGajiFieldTemplate.Kategori kategori = tableKategori[i];
            if (i != 0) {
                builder.appendSpacerCell(SizeUnit.px(40));
            }
            builder.appendCell(kategori.getValue()).withColspan(2);
        }
        for (DataBaris row : table.getBaris()) {
            builder.appendRow();
            DataKolom[] rowKolom = row.getKolom();
            for (int i = 0; i < rowKolom.length; i++) {
                DataKolom kolom = rowKolom[i];
                if (i != 0) {
                    builder.appendSpacerCell(SizeUnit.px(40));
                }
                if (kolom == null) {
                    builder.appendCell("").withColspan(2);
                } else {
                    builder.appendCell(kolom.getNamaField()).withCellAlignment(Alignment.CENTER_LEFT);
                    builder.appendCell(intToRupiah(kolom.getValue())).withCellAlignment(Alignment.CENTER_RIGHT);
                }
            }
        }
        builder.appendRow();
        for (int i = 0; i < tableKategori.length; i++) {
            SlipGajiFieldTemplate.Kategori kategori = tableKategori[i];
            if (i != 0) {
                builder.appendSpacerCell(SizeUnit.px(40)).withBorderSides(BorderSide.TOP)
                        .withBackgroundColor(java.awt.Color.LIGHT_GRAY);
            }
            Integer sum = sumByCategory.get(kategori);
            if (sum == null) {
                sum = 0;
            }
            builder.appendCell("Total " + kategori.getValue()).withCellAlignment(Alignment.CENTER_LEFT)
                    .withBorderSides(BorderSide.TOP).withBackgroundColor(java.awt.Color.LIGHT_GRAY);
            builder.appendCell(intToRupiah(sum)).withCellAlignment(Alignment.CENTER_RIGHT)
                    .withBorderSides(BorderSide.TOP).withBackgroundColor(java.awt.Color.LIGHT_GRAY);
        }
        int sum = 0;
        for (Map.Entry<SlipGajiFieldTemplate.Kategori, Integer> entry : sumByCategory.entrySet()) {
            sum += entry.getValue() * entry.getKey().getMultiplier();
        }
        // [kategori] [spacer] [kategori] [spacer] [kategori]
        // [key][value] [spacer] [key][value] [spacer] [key][value]
        double halfColumns = ((tableKategori.length * 4) - 1) / 2.0;
        builder.appendRow("Penerimaan Bersih").withColspan((int) Math.floor(halfColumns))
                .withBackgroundColor(java.awt.Color.ORANGE)
                .withCellAlignment(Alignment.CENTER_LEFT);
        builder.appendCell(intToRupiah(sum)).withColspan((int) Math.ceil(halfColumns))
                .withBackgroundColor(java.awt.Color.ORANGE)
                .withCellAlignment(Alignment.CENTER_RIGHT);
//        if (downloadPDFUrl != null) {
//            builder.appendSpacer(SizeUnit.px(25))
//                    .appendBtnPrimary("Simpan sebagai PDF", downloadPDFUrl)
//                    .append("<i>* Password untuk membuka file PDF anda adalah 2 digit tanggal diikuti 2 digit bulan dan 2 digit terakhir tahun lahir anda</i>");
//        }
        return builder.generate();
    }

    public String generateEmail(Employee employee, SlipGajiTemplate template) {
        String name = template.getName();
        String subject = name == null || name.isEmpty() ? "Slip Gaji " + MONTHS[template.getBulan() - 1] + " " + template.getTahun() : "Slip Gaji " + name + " " + MONTHS[template.getBulan() - 1] + " " + template.getTahun();
        return EmailTemplateBuilder.create(subject)
                .append("Halo " + employee.getFullName() + ",")
                .append("Berikut adalah slip gaji anda untuk bulan " + MONTHS[template.getBulan() - 1] + " " + template.getTahun())
                .appendEntry("Slip Gaji", name == null ? "(Tidak ada nama)" : name)
                .appendEntry("Password PDF", employee.getTanggalLahir() == null ? DEFAULT_PASSWORD : "Dua digit tanggal lahir diikuti dua digit bulan lahir dan dua digit terakhir tahun lahir")
                .append("Silakan klik tombol di bawah ini untuk melihat slip gaji anda.")
                .appendBtnPrimary("Lihat Slip Gaji", frontEndURLService.getSlipGajiPDFURL(employee.getEmployeeCode(), template.getId()))
                .generate();
    }

    public SlipGajiDetailResponse getDetail(long id) {
        SlipGajiTemplate template = slipGajiTemplateRepository.findById(id).orElse(null);
        if (template == null) {
            SlipGajiDetailResponse response = new SlipGajiDetailResponse();
            response.setMessage(ERROR_MESSAGE + "Template not found");
            return response;
        }
        SlipGajiDetailResponse response = new SlipGajiDetailResponse();
        response.setMessage(SUCCESS_MESSAGE);
        response.setId(template.getId());
        response.setName(template.getName());
        response.setBulan(template.getBulan());
        response.setTahun(template.getTahun());
        response.setFields(getSlipGajiFields(template));
        response.setRevision(template.getRevisi() == null ? 1 : template.getRevisi());
        return response;
    }

    public SlipGajiDataResponse getData(long id) {
        SlipGajiTemplate template = slipGajiTemplateRepository.findById(id).orElse(null);
        if (template == null) {
            throw new IllegalArgumentException("NOT FOUND");
        }
        List<SlipGajiFieldTemplate> fields = slipGajiFieldTemplateRepository.findAllByTemplate(template.getId());
        List<Long> fieldIds = fields.stream().map(SlipGajiFieldTemplate::getId).toList();
        List<SlipGajiEntryData> dataEntries = slipGajiEntryDataRepository.findAllBySlipFieldIsIn(fieldIds);
        SlipGajiDataResponse response = new SlipGajiDataResponse();
        response.setMessage(SUCCESS_MESSAGE);
        int bulan = template.getBulan();
        int tahun = template.getTahun();
        response.setId(template.getId());
        response.setName(template.getName());
        response.setBulan(bulan);
        response.setTahun(tahun);
        List<SlipGajiDataEntry> data = new ArrayList<>();
        Map<String, List<SlipGajiEntryData>> dataMap = new HashMap<>();
        for (SlipGajiEntryData dataEntry : dataEntries) {
            List<SlipGajiEntryData> dataList = dataMap.computeIfAbsent(dataEntry.getEmployee(), k -> new ArrayList<>());
            dataList.add(dataEntry);
        }
        Map<String, Employee> cachedEmployees = new HashMap<>();
        for (Map.Entry<String, List<SlipGajiEntryData>> entry : dataMap.entrySet()) {
            SlipGajiDataEntry entryItem = new SlipGajiDataEntry();
            Employee employee = cachedEmployees.computeIfAbsent(entry.getKey(), k -> employeeRepository.findByEmployeeCode(k));
            entryItem.setEmployeeCode(employee.getEmployeeCode());
            entryItem.setEmployeeName(employee.getFullName());
            List<SlipGajiDataEntry.EntryData> dataList = new ArrayList<>();
            boolean isSentAll = !entry.getValue().isEmpty();
            boolean isComplete = entry.getValue().size() == fields.size();
            List<Long> emptyFields = new ArrayList<>(fieldIds);
            for (SlipGajiEntryData item : entry.getValue()) {
                SlipGajiDataEntry.EntryData dataItem = new SlipGajiDataEntry.EntryData();
                dataItem.setIdField(item.getSlipField());
                dataItem.setValue(item.getValue());
                dataList.add(dataItem);
                emptyFields.remove(item.getSlipField());
                if (item.getIsSent() == null || !item.getIsSent()) {
                    isSentAll = false;
                }
                if (item.getValue() == null) {
                    isComplete = false;
                }
            }
            entryItem.setSent(isSentAll);
            entryItem.setComplete(isComplete && emptyFields.isEmpty());
            entryItem.setDataList(dataList);
            data.add(entryItem);
        }
        response.setData(data);
        response.setFields(getSlipGajiFields(template));
        return response;
    }

    private List<SlipGajiField> getSlipGajiFields(SlipGajiTemplate template) {
        List<SlipGajiField> fields = new ArrayList<>();
        List<SlipGajiFieldTemplate> fieldTemplates = slipGajiFieldTemplateRepository.findAllByTemplate(template.getId());
        for (SlipGajiFieldTemplate field : fieldTemplates) {
            SlipGajiField item = new SlipGajiField();
            item.setId(field.getId());
            item.setCategory(field.getKategori());
            item.setName(field.getNamaField());
            item.setOrder(field.getUrutan());
            fields.add(item);
        }
        return fields;
    }

    public void deleteTemplate(long id) {
        SlipGajiTemplate template = slipGajiTemplateRepository.findById(id).orElse(null);
        if (template == null) {
            throw new IllegalArgumentException("NOT FOUND");
        }
        List<SlipGajiFieldTemplate> fields = slipGajiFieldTemplateRepository.findAllByTemplate(id);
        List<Long> fieldIds = fields.stream().map(SlipGajiFieldTemplate::getId).toList();
        // delete all entries
        slipGajiEntryDataRepository.deleteAllBySlipFieldIsIn(fieldIds);
        // delete all fields
        slipGajiFieldTemplateRepository.deleteAll(fields);
        // delete the template
        slipGajiTemplateRepository.delete(template);
    }

    @Transactional
    public void createOrUpdateTemplate(SlipGajiTemplateUpdate update) {
        SlipGajiTemplate existing;
        if (update.getId() == null) {
            existing = new SlipGajiTemplate();
        } else {
            existing = slipGajiTemplateRepository.findById(update.getId()).orElse(null);
            if (existing == null) {
                throw new IllegalArgumentException("TEMPLATE NOT FOUND");
            }
            Integer revision = existing.getRevisi();
            if (revision == null) {
                revision = 1;
            }
            existing.setRevisi(revision + 1);
            existing.setWaktuRevisi(LocalDateTime.now());
        }
        slipGajiTemplateRepository.save(existing);
        List<SlipGajiFieldTemplate> existingFields = slipGajiFieldTemplateRepository.findAllByTemplate(existing.getId());
        List<SlipGajiFieldTemplate> toDelete = new ArrayList<>(existingFields);
        boolean hasChanges = false;
        if (!Objects.equals(existing.getName(), update.getName())) {
            existing.setName(update.getName());
            hasChanges = true;
        }
        if (existing.getBulan() != update.getBulan()) {
            existing.setBulan(update.getBulan());
            hasChanges = true;
        }
        if (existing.getTahun() != update.getTahun()) {
            existing.setTahun(update.getTahun());
            hasChanges = true;
        }
        for (SlipGajiField field : update.getFields()) {
            Long id = field.getId();
            SlipGajiFieldTemplate existingField = null;
            boolean shouldSave = false;
            if (id != null) {
                // find existing field
                existingField = existingFields.stream()
                        .filter(f -> f.getId().equals(id))
                        .findFirst()
                        .orElse(null);
                if (existingField != null) {
                    toDelete.remove(existingField);
                }
            }
            if (existingField == null) {
                existingField = new SlipGajiFieldTemplate();
                existingField.setTemplate(existing.getId());
                shouldSave = true;
            }
            if (field.getCategory() != null && field.getCategory() != existingField.getKategori()) {
                existingField.setKategori(field.getCategory());
                shouldSave = true;
            }
            if (field.getName() != null && !field.getName().equals(existingField.getNamaField())) {
                existingField.setNamaField(field.getName());
                shouldSave = true;
            }
            if (field.getOrder() != null && !field.getOrder().equals(existingField.getUrutan())) {
                existingField.setUrutan(field.getOrder());
                shouldSave = true;
            }
            if (shouldSave) {
                slipGajiFieldTemplateRepository.save(existingField);
                hasChanges = true;
            }
        }
        if (!toDelete.isEmpty()) {
            hasChanges = true;
            slipGajiEntryDataRepository.deleteAllBySlipFieldIsIn(toDelete.stream().map(SlipGajiFieldTemplate::getId).toList());
            slipGajiFieldTemplateRepository.deleteAll(toDelete);
        }
        // fetch again all fields
        existingFields = slipGajiFieldTemplateRepository.findAllByTemplate(existing.getId());
        // if empty, then do delete the template
        if (existingFields.isEmpty()) {
            slipGajiTemplateRepository.delete(existing);
        } else {
            if (!hasChanges) {
                throw new IllegalArgumentException("NO CHANGES");
            }
        }
    }

    private void copyStyle(Cell from, Cell to) {
        // copy only color, font
        to.setCellStyle(from.getCellStyle());
        // set border around
        CellStyle cellStyle = to.getCellStyle();
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
    }

    private static final QName typeQname = new QName("", "t");
    private class AccountingFormatEnum extends StringEnumAbstractBase {
        public AccountingFormatEnum(String s, int i) {
            super(s, i);
        }
    }

    @Transactional
    public byte[] generateTemplateFile(long id) throws IOException, XmlException {
        SlipGajiTemplate template = slipGajiTemplateRepository.findById(id).orElse(null);
        if (template == null) {
            throw new IllegalArgumentException("NOT FOUND");
        }
        int bulan = template.getBulan();
        int tahun = template.getTahun();
        List<SlipGajiFieldTemplate> fields = slipGajiFieldTemplateRepository.findAllByTemplate(template.getId());
        fields.sort(Comparator.<SlipGajiFieldTemplate>comparingInt(value -> value.getKategori().ordinal()).thenComparingInt(SlipGajiFieldTemplate::getUrutan));
        byte[] read = slipGajiTemplate.getContentAsByteArray();
        Iterable<Employee> employees = employeeRepository.findAll();
        XSSFWorkbook workbook = new XSSFWorkbook(new ByteArrayInputStream(read));
        XSSFSheet sheet = workbook.getSheetAt(1); // 2nd sheet adalah data pegawai
        int row = 1;
        for (Employee employee : employees) {
            sheet.createRow(row).createCell(0).setCellValue(employee.getEmployeeCode());
            sheet.getRow(row).createCell(1).setCellValue(employee.getFullName());
            row++;
        }
        DataFormat dataFormat = workbook.createDataFormat();
        short formatIndex = dataFormat.getFormat("_-Rp* #,##0_-;-Rp* #,##0_-;_-Rp* \"-\"_-;_-@_-");
        CellStyle currencyCellStyle = workbook.createCellStyle();
        currencyCellStyle.setDataFormat(formatIndex);
        List<Long> fieldIds = fields.stream().map(SlipGajiFieldTemplate::getId).toList();
        List<SlipGajiEntryData> dataEntries = slipGajiEntryDataRepository.findAllBySlipFieldIsIn(fieldIds);
        sheet = workbook.getSheetAt(0); // 1st sheet adalah field
        Row nameRow = sheet.getRow(5);
        if (nameRow == null) {
            nameRow = sheet.createRow(5);
        }
        Cell nameCell = nameRow.getCell(2);
        if (nameCell == null) {
            nameCell = nameRow.createCell(2);
        }
        // change the revision cell value at C7
        Row revisionRow = sheet.getRow(6);
        if (revisionRow == null) {
            revisionRow = sheet.createRow(6);
        }
        Cell revisionCell = revisionRow.getCell(2);
        if (revisionCell == null) {
            revisionCell = revisionRow.createCell(2);
        }
        String name = template.getName();
        if (name != null) {
            nameCell.setCellValue(name);
        } else {
            nameCell.setCellValue("(Tidak ada nama)");
        }
        revisionCell.setCellValue(template.getRevisi() == null ? 1 : template.getRevisi());
        Row infoRow = sheet.getRow(7);
        if (infoRow == null) {
            infoRow = sheet.createRow(7);
        }
        Cell infoCell = infoRow.getCell(2);
        if (infoCell == null) {
            infoCell = infoRow.createCell(2);
        }
        infoCell.setCellValue(MONTHS[bulan - 1] + "/" + tahun);
        int minColumnWidth = 20;
        minColumnWidth *= 256;
        // COPY STYLE FROM D8
        // CATEGORY HEADER START: E8
        // FIELD HEADER START: E9
        // DATA START: E10
        Map<SlipGajiFieldTemplate.Kategori, List<SlipGajiFieldTemplate>> fieldMap = new HashMap<>();
        for (SlipGajiFieldTemplate field : fields) {
            SlipGajiFieldTemplate.Kategori kategori = field.getKategori();
            List<SlipGajiFieldTemplate> fieldList = fieldMap.computeIfAbsent(kategori, k -> new ArrayList<>());
            fieldList.add(field);
        }
        List<Map.Entry<SlipGajiFieldTemplate.Kategori, List<SlipGajiFieldTemplate>>> entries = new ArrayList<>(fieldMap.entrySet());
        entries.sort(Comparator.comparingInt(e -> e.getKey().ordinal())); // make sure it's sorted by ordinal
        int column = 4; // start from E
        Cell styleAnchor = sheet.getRow(9).getCell(3); // D10
        Cell hiddenStyleAnchor = sheet.getRow(11).getCell(3); // D12
        for (Map.Entry<SlipGajiFieldTemplate.Kategori, List<SlipGajiFieldTemplate>> entry : entries) {
            // fields are columns
            Row categoryRow = sheet.getRow(9);
            Row fieldsRow = sheet.getRow(10);
            Row hiddenRow = sheet.getRow(11);
            Row dataRow = sheet.getRow(12);
            if (categoryRow == null) {
                categoryRow = sheet.createRow(9);
            }
            if (fieldsRow == null) {
                fieldsRow = sheet.createRow(10);
            }
            if (hiddenRow == null) {
                hiddenRow = sheet.createRow(11);
            }
            if (dataRow == null) {
                dataRow = sheet.createRow(12);
            }
            // merge category cell
            Cell categoryCell = categoryRow.createCell(column);
            categoryCell.setCellValue(entry.getKey().getValue());
            int fromColumn = column;
            for (SlipGajiFieldTemplate field : entry.getValue()) {
                Cell fieldCell = fieldsRow.getCell(column);
                if (fieldCell == null) {
                    fieldCell = fieldsRow.createCell(column);
                }
                fieldCell.setCellValue(field.getNamaField());
                Cell hiddenCell = hiddenRow.getCell(column);
                if (hiddenCell == null) {
                    hiddenCell = hiddenRow.createCell(column);
                }
                Cell dataCell = dataRow.getCell(column);
                if (dataCell == null) {
                    dataCell = dataRow.createCell(column);
                }
                dataCell.setCellStyle(currencyCellStyle);
                copyStyle(styleAnchor, fieldCell);
                copyStyle(hiddenStyleAnchor, hiddenCell);
                hiddenCell.setCellValue("(" + column + ")");
                column++;
            }
            // merge category cell
//            CellRangeAddress region = new CellRangeAddress(7, 7, fromColumn, column - 1);
//            sheet.addMergedRegion(region);
            // check if its single cell, then don't merge
            CellRangeAddress region = new CellRangeAddress(9, 9, fromColumn, column - 1);
            if (fromColumn != column - 1) {
                sheet.addMergedRegion(region);
                RegionUtil.setBorderTop(BorderStyle.THIN, region, sheet);
                RegionUtil.setBorderBottom(BorderStyle.THIN, region, sheet);
                RegionUtil.setBorderLeft(BorderStyle.THIN, region, sheet);
                RegionUtil.setBorderRight(BorderStyle.THIN, region, sheet);
            } else {
                Cell catCell = categoryRow.getCell(fromColumn);
                if (catCell == null) {
                    catCell = categoryRow.createCell(fromColumn);
                }
                copyStyle(styleAnchor, catCell);
            }
            // set style
            copyStyle(styleAnchor, categoryCell);
        }
        // resize columns or fit to content
        for (int i = 4; i < column; i++) {
            sheet.autoSizeColumn(i);
            int width = sheet.getColumnWidth(i);
            if (width < minColumnWidth) {
                sheet.setColumnWidth(i, minColumnWidth);
            }
        }
        // in the template, there's excel table that ranges from B10 to D10
        // expand it to the right by the number of fields
        List<XSSFTable> tables = sheet.getTables();
        // we only have one table
        XSSFTable table = tables.get(0);

        Map<String, List<SlipGajiEntryData>> dataMap = new HashMap<>();
        for (SlipGajiEntryData entry : dataEntries) {
            List<SlipGajiEntryData> dataList = dataMap.computeIfAbsent(entry.getEmployee(), k -> new ArrayList<>());
            dataList.add(entry);
        }

        // change the table range
        CellReference topLeft = new CellReference(11, 1);// B12
        int totalEntries = dataMap.size();
        // shift the row by the number of entries
        if (totalEntries > 0) {
            sheet.shiftRows(12, sheet.getLastRowNum(), totalEntries, true, true);
        }
        // shift the table range
        CellReference bottomRight = new CellReference(12 + totalEntries, 3 + fields.size()); // D10 + number of fields
        table.setArea(new AreaReference(topLeft, bottomRight, workbook.getSpreadsheetVersion()));
        // rename columns
        CTTable ctTable = table.getCTTable();
        CTTableColumns ctTableColumns = ctTable.getTableColumns();
        List<CTTableColumn> ctTableColumnList = ctTableColumns.getTableColumnList();
        for (int i = 0; i < fields.size(); i++) {
            CTTableColumn ctTableColumn = ctTableColumnList.get(i);
            ctTableColumn.setName("(" + (i + 1) + ")");
        }

        // write existing entries to the table
        List<Map.Entry<String, List<SlipGajiEntryData>>> employeeEntries = new ArrayList<>(dataMap.entrySet());
        employeeEntries.sort(Map.Entry.comparingByKey());
        Map<String, Employee> cachedEmployees = new HashMap<>();
        for (int i = 0; i < employeeEntries.size(); i++) {
            Map.Entry<String, List<SlipGajiEntryData>> entry = employeeEntries.get(i);
            Employee employee = cachedEmployees.computeIfAbsent(entry.getKey(), k -> employeeRepository.findByEmployeeCode(k));
            List<SlipGajiEntryData> dataList = entry.getValue();
            Row dataRow = sheet.getRow(12 + i); // start from row 13
            if (dataRow == null) {
                dataRow = sheet.createRow(12 + i);
            }
            Cell numberCell = dataRow.getCell(1); // B
            if (numberCell == null) {
                numberCell = dataRow.createCell(1);
            }
            Cell employeeCodeCell = dataRow.getCell(2); // C
            if (employeeCodeCell == null) {
                employeeCodeCell = dataRow.createCell(2);
            }
            employeeCodeCell.setCellValue(employee.getEmployeeCode());
            Cell employeeNameCell = dataRow.getCell(3); // D
            if (employeeNameCell == null) {
                employeeNameCell = dataRow.createCell(3);
            }
            List<Long> notFilledFields = new ArrayList<>(fieldIds);
            for (SlipGajiEntryData dataEntry : dataList) {
                int fieldIndex = fieldIds.indexOf(dataEntry.getSlipField());
                if (fieldIndex == -1) {
                    continue;
                }
                notFilledFields.remove(dataEntry.getSlipField());
                int colIndex = fieldIndex + 4; // start from E
                Cell cell = dataRow.getCell(colIndex);
                if (cell == null) {
                    cell = dataRow.createCell(colIndex);
                }
                cell.setCellStyle(currencyCellStyle);
                cell.setCellValue(dataEntry.getValue());
            }
            for (Long fieldId : notFilledFields) {
                int fieldIndex = fieldIds.indexOf(fieldId);
                if (fieldIndex == -1) {
                    continue;
                }
                int colIndex = fieldIndex + 4; // start from E
                Cell cell = dataRow.getCell(colIndex);
                if (cell == null) {
                    cell = dataRow.createCell(colIndex);
                }
                cell.setCellStyle(currencyCellStyle);
            }
            if (i != 0) { // copy formula from previous row
                Row previousRow = sheet.getRow(12 + i - 1);
                Cell previousNumberCell = previousRow.getCell(1);
                if (previousNumberCell != null) {
                    numberCell.setCellFormula(previousNumberCell.getCellFormula());
                }
                Cell previousEmployeeNameCell = previousRow.getCell(3);
                if (previousEmployeeNameCell != null) {
                    employeeNameCell.setCellFormula(previousEmployeeNameCell.getCellFormula());
                }
            }
        }
        XSSFFormulaEvaluator.evaluateAllFormulaCells(workbook);
        // create data validation for employee code from 2nd sheet
        if (totalEntries > 0) {
            CTExtensionList extLst = sheet.getCTWorksheet().getExtLst();
            CTExtension[] extArray = extLst.getExtArray();
            CTExtension ext = extArray[0];
            StringWriter writer = new StringWriter();
            ext.save(writer);
            String xml = writer.toString();
            xml = xml.replace("<xm:sqref>C13</xm:sqref>", "<xm:sqref>C13:C" + (13 + totalEntries) + "</xm:sqref>");
            XmlObject xmlObject = XmlObject.Factory.parse(xml);
            ext.set(xmlObject);
        }
        // end of data validation
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();
        return out.toByteArray();
    }

    @Transactional
    public void loadData(byte[] bytes, long id) throws IOException {
        SlipGajiTemplate template = slipGajiTemplateRepository.findById(id).orElse(null);
        if (template == null) {
            throw new IllegalArgumentException("TEMPLATE NOT FOUND");
        }
        List<SlipGajiFieldTemplate> fields = slipGajiFieldTemplateRepository.findAllByTemplate(template.getId());
        // delete all entries
//        slipGajiEntryDataRepository.deleteAllBySlipFieldIsIn(fields.stream().map(SlipGajiFieldTemplate::getId).toList());
        XSSFWorkbook workbook = new XSSFWorkbook(new ByteArrayInputStream(bytes));
        XSSFSheet sheet = workbook.getSheetAt(0);
        Row revisionRow = sheet.getRow(6);
        if (revisionRow == null) {
            return;
        }
        Cell revisionCell = revisionRow.getCell(2);
        if (revisionCell == null) {
            return;
        }
        Row infoRow = sheet.getRow(7);
        if (infoRow == null) {
            return;
        }
        Cell infoCell = infoRow.getCell(2);
        if (infoCell == null) {
            return;
        }
        int bulan = template.getBulan();
        int tahun = template.getTahun();
        String infoCheck = MONTHS[bulan - 1] + "/" + tahun;
        if (!infoCheck.equals(infoCell.getStringCellValue())) {
            throw new IllegalArgumentException("INFO MISMATCH");
        }
        int revision = (int) revisionCell.getNumericCellValue();
        Integer revisi = template.getRevisi();
        if (revisi == null) {
            revisi = 1;
        }
        if (revision != revisi) {
            throw new IllegalArgumentException("TEMPLATE VERSION MISMATCH");
        }
        int row = 12; // start from row 11
        int col = 4; // start from column E
        List<XSSFTable> tables = sheet.getTables();
        XSSFTable table = tables.get(0);
        // find the maximum row
        AreaReference area = table.getArea();
        CellReference lastCell = area.getLastCell();
        int maxRow = lastCell.getRow();
        DataFormatter dataFormatter = new DataFormatter();
        // clear all entries
//        slipGajiEntryDataRepository.deleteAllBySlipFieldIsIn(fields.stream().map(SlipGajiFieldTemplate::getId).toList());
        Map<SlipGajiFieldTemplate.Kategori, List<SlipGajiFieldTemplate>> fieldMap = new HashMap<>();
        for (SlipGajiFieldTemplate field : fields) {
            SlipGajiFieldTemplate.Kategori kategori = field.getKategori();
            List<SlipGajiFieldTemplate> fieldList = fieldMap.computeIfAbsent(kategori, k -> new ArrayList<>());
            fieldList.add(field);
        }
        List<Map.Entry<SlipGajiFieldTemplate.Kategori, List<SlipGajiFieldTemplate>>> entries = new ArrayList<>(fieldMap.entrySet());
        entries.sort(Comparator.comparingInt(e -> e.getKey().ordinal())); // make sure it's sorted by ordinal
        for (; row <= maxRow; row++) {
            Row dataRow = sheet.getRow(row);
            if (dataRow == null) {
                continue;
            }
            // EmployeeCode at Column C
            Cell employeeCodeCell = dataRow.getCell(2);
            if (employeeCodeCell == null) {
                continue;
            }
            String employeeCode = employeeCodeCell.getStringCellValue();
            if (employeeCode == null || employeeCode.isEmpty()) {
                continue;
            }
            Employee employee = employeeRepository.findByEmployeeCode(employeeCode);
            if (employee == null) {
                throw new IllegalArgumentException("EMPLOYEE NOT FOUND: " + employeeCode);
            }
            int index = 0;
            for (Map.Entry<SlipGajiFieldTemplate.Kategori, List<SlipGajiFieldTemplate>> entryData : entries) {
                List<SlipGajiFieldTemplate> fieldList = entryData.getValue();
                fieldList.sort(Comparator.comparingInt(SlipGajiFieldTemplate::getUrutan));
                for (SlipGajiFieldTemplate field : fieldList) {
                    Cell cell = dataRow.getCell(col + index);
                    if (cell == null) {
                        SlipGajiEntryData entry = slipGajiEntryDataRepository.findFirstByEmployeeAndSlipField(employee.getEmployeeCode(), field.getId());
                        if (entry != null) {
                            slipGajiEntryDataRepository.delete(entry);
                        }
                        index++;
                        continue;
                    }
                    int value;
                    if (cell.getCellType() == CellType.NUMERIC) {
                        value = (int) cell.getNumericCellValue();
                    } else {
                        String stringValue = dataFormatter.formatCellValue(cell);
                        if (stringValue == null || stringValue.isEmpty()) {
                            continue;
                        }
                        try {
                            value = Integer.parseInt(stringValue);
                        } catch (NumberFormatException e) {
                            throw new IllegalArgumentException("INVALID VALUE AT ROW " + row + " COLUMN " + (col + index) + ": " + stringValue);
                        }
                    }
                    SlipGajiEntryData entry = slipGajiEntryDataRepository.findFirstByEmployeeAndSlipField(employee.getEmployeeCode(), field.getId());
                    if (entry == null) {
                        entry = new SlipGajiEntryData();
                        entry.setEmployee(employee.getEmployeeCode());
                        entry.setSlipField(field.getId());
                    } else {
                        if (entry.getValue() != null && entry.getValue() == value) {
                            index++;
                            continue;
                        }
                    }
                    entry.setEmployee(employee.getEmployeeCode());
                    entry.setSlipField(field.getId());
                    entry.setIsSent(false);
                    entry.setValue(value);
                    slipGajiEntryDataRepository.save(entry);
                    index++;
                }
            }
        }
    }


}
