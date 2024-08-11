package com.suryaenergi.sdm.backendapi.service;

import com.suryaenergi.sdm.backendapi.entity.*;
import com.suryaenergi.sdm.backendapi.pojo.*;
import com.suryaenergi.sdm.backendapi.repository.*;
import com.suryaenergi.sdm.backendapi.request.ProyekListRequest;
import com.suryaenergi.sdm.backendapi.response.DpbRealisasiDetailResponse;
import com.suryaenergi.sdm.backendapi.response.PoListResponse;
import com.suryaenergi.sdm.backendapi.response.ProyekDetailResponse;
import com.suryaenergi.sdm.backendapi.response.ProyekListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ProyekService {
    @Autowired
    private ProyeknavRepository proyeknavRepository;
    @Autowired
    private BudgetnavRepository budgetnavRepository;
    @Autowired
    private DpbheadRepository dpbheadRepository;
    @Autowired
    private DpblineRepository dpblineRepository;
    @Autowired
    private PonavRepository ponavRepository;
    @Autowired
    private GlaccountRepository glaccountRepository;

    public ProyekListResponse getAllProyek(int no, int size) {
        ProyekListResponse res = new ProyekListResponse();
        Pageable paging = PageRequest.of(no,size);
        List<ProyekData> proyekDataList = new ArrayList<>();
        try{
            Page<Proyeknav> proyekPage = proyeknavRepository.findAll(paging);
            if(proyekPage.hasContent()){
                DateTimeFormatter formattgl = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                for (Proyeknav proyek: proyekPage){
                    ProyekData proyekData = new ProyekData();
                    proyekData.setProject_code(proyek.getProjectCode());
                    proyekData.setProject_name(proyek.getProjectName());
                    proyekData.setPimpro(proyek.getPimpro());
                    proyekData.setProject_type(proyek.getProjectType());
                    proyekData.setStatus(proyek.getStatus());
                    proyekData.setProject_value(new BigDecimal(proyek.getNilaiProject()).toPlainString());
                    if(proyek.getTglMulai() != null && !proyek.getTglMulai().equals("")) {
                        proyekData.setTgl_mulai(proyek.getTglMulai().format(formattgl));
                    }
                    if(proyek.getTglAkhir() != null && !proyek.getTglAkhir().equals("")) {
                        proyekData.setTgl_akhir(proyek.getTglAkhir().format(formattgl));
                    }
                    proyekData.setCarry_over(proyekData.isCarry_over());
                    proyekDataList.add(proyekData);
                }
                res.setMsg("SUCCESS");
                res.setCount(proyekDataList.size());
                res.setProyeks(proyekDataList);
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

    public ProyekListResponse getAllProyekNoPage(ProyekListRequest req) {
        ProyekListResponse res = new ProyekListResponse();
        List<ProyekData> proyekDataList = new ArrayList<>();
        try{
            List<Proyeknav> proyeknavList = proyeknavRepository.findAllByStatus(req.getStatus());
            if(proyeknavList.size() > 0){
                DateTimeFormatter formattgl = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                for (Proyeknav proyek: proyeknavList){
                    ProyekData proyekData = new ProyekData();
                    proyekData.setProject_code(proyek.getProjectCode());
                    proyekData.setProject_name(proyek.getProjectName());
                    proyekData.setPimpro(proyek.getPimpro());
                    proyekData.setProject_type(proyek.getProjectType());
                    proyekData.setStatus(proyek.getStatus());
                    proyekData.setProject_value(new BigDecimal(proyek.getNilaiProject()).toPlainString());
                    if(proyek.getTglMulai() != null && !proyek.getTglMulai().equals("")) {
                        proyekData.setTgl_mulai(proyek.getTglMulai().format(formattgl));
                    }
                    if(proyek.getTglAkhir() != null && !proyek.getTglAkhir().equals("")) {
                        proyekData.setTgl_akhir(proyek.getTglAkhir().format(formattgl));
                    }
                    proyekData.setCarry_over(proyekData.isCarry_over());
                    proyekDataList.add(proyekData);
                }
                res.setMsg("SUCCESS");
                res.setCount(proyekDataList.size());
                res.setProyeks(proyekDataList);
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

    public ProyekDetailResponse getDetailProyek(String kdproyek) {
        ProyekDetailResponse res = new ProyekDetailResponse();
        DateTimeFormatter formattgl = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DecimalFormat decimalFormat = new DecimalFormat("#.0#");

        try{
            //dapetin detail proyek
            Proyeknav proyeknav = proyeknavRepository.findByProjectCode(kdproyek);
            if(proyeknav != null){
                ProyekData proyekData = new ProyekData();
                proyekData.setProject_code(proyeknav.getProjectCode());
                proyekData.setProject_name(proyeknav.getProjectName());
                proyekData.setProject_type(proyeknav.getProjectType());
                proyekData.setProject_value(decimalFormat.format(proyeknav.getNilaiProject()));
                if(proyeknav.getClosingDate() != null) {
                    proyekData.setClosing_date(proyeknav.getClosingDate().format(formattgl));
                }
                proyekData.setCarry_over(proyeknav.isCarryOver());
                proyekData.setPimpro(proyeknav.getPimpro());
                proyekData.setStatus(proyeknav.getStatus());
                if(proyeknav.getTglMulai() != null && !proyeknav.getTglMulai().equals(""))
                    proyekData.setTgl_mulai(proyeknav.getTglMulai().format(formattgl));
                if(proyeknav.getTglAkhir() != null && !proyeknav.getTglAkhir().equals(""))
                    proyekData.setTgl_akhir(proyeknav.getTglAkhir().format(formattgl));
                res.setProyek(proyekData);
            }

            //dapetin data budgets
            List<ProyekBudgetDetail> budgetList = new ArrayList<>();
            List<Budgetnav> budgetnavList = budgetnavRepository.findByProjectCode(kdproyek);
            if(budgetnavList.size() > 0){
                for(int i=510; i<=890; i+=10) {
                    String finalI = i+".00";
                    List<Budgetnav> result = budgetnavList.stream()
                            .filter(a -> Objects.equals(a.getGlAccount(), finalI))
                            .collect(Collectors.toList());
                    if(result.size() > 0){
                        ProyekBudgetDetail proyekBudgetDetail = new ProyekBudgetDetail();
                        Glaccountnav glaccountnav = glaccountRepository.findByGlNo(finalI);
                        proyekBudgetDetail.setGl_account(finalI);
                        if(glaccountnav != null)
                            proyekBudgetDetail.setGl_name(glaccountnav.getGlName());
                        for(Budgetnav budgetnav:result){
                            if(budgetnav.getType().equalsIgnoreCase("Budget") && budgetnav.getAmount() > 0){
                                proyekBudgetDetail.setBudget(decimalFormat.format(budgetnav.getAmount()));
                            }
                            else if(budgetnav.getType().equalsIgnoreCase("Realisasi") && budgetnav.getAmount() > 0){
                                proyekBudgetDetail.setRealisasi(decimalFormat.format(budgetnav.getAmount()));
                            }
                            else if(budgetnav.getType().equalsIgnoreCase("Penggunaan") && budgetnav.getAmount() > 0){
                                proyekBudgetDetail.setPenggunaan(decimalFormat.format(budgetnav.getAmount()));
                            }
                        }
                        budgetList.add(proyekBudgetDetail);
                    }
                }

                /*List<Glaccountnav> glaccountnav = glaccountRepository.findByAccountType("Begin-Total");
                if(glaccountnav.size() > 0){
                    for(Glaccountnav glacc:glaccountnav){
                        if(glacc.getGlNo() >= "")
                    }
                }*/

                res.setBudgets(budgetList);
            }

            //dapetin data dpb
            List<ProyekDpbDetail> proyekDpbDetailList = new ArrayList<>();
            List<Dpbheadnav> dpbheadnavList = dpbheadRepository.findByProjectCode(kdproyek);
            if(dpbheadnavList.size() > 0){
                for(Dpbheadnav dpbheadnav:dpbheadnavList) {
                    ProyekDpbDetail proyekDpbDetail = new ProyekDpbDetail();
                    proyekDpbDetail.setDocument_no(dpbheadnav.getDocumentNo());
                    if(dpbheadnav.getDocumentDate() != null && !dpbheadnav.getDocumentDate().equals(""))
                        proyekDpbDetail.setDocument_date(dpbheadnav.getDocumentDate().format(formattgl));
                    proyekDpbDetail.setAmount(decimalFormat.format(dpbheadnav.getAmount()));
                    proyekDpbDetail.setStatus(dpbheadnav.getStatus());
                    if(dpbheadnav.getTglDibutuhkan() != null && !dpbheadnav.getTglDibutuhkan().equals(""))
                        proyekDpbDetail.setTgl_dibutuhkan(dpbheadnav.getTglDibutuhkan().format(formattgl));
                    proyekDpbDetail.setAmount_inc_vat(decimalFormat.format(dpbheadnav.getAmountIncVat()));
                    proyekDpbDetailList.add(proyekDpbDetail);
                }
                res.setDpbs(proyekDpbDetailList);
            }
            res.setMsg("SUCCESS");
        }
        catch (Exception ex){
            res.setMsg("ERROR: "+ex.getMessage());
        }

        return res;
    }

    public DpbRealisasiDetailResponse getDetailDpb(String nodpb) {
        DpbRealisasiDetailResponse res = new DpbRealisasiDetailResponse();
        DateTimeFormatter formattgl = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DecimalFormat decimalFormat = new DecimalFormat("#.0#");

        try{
            Dpbheadnav dpbheadnav = dpbheadRepository.findFirstByDocumentNo(nodpb);
            if(dpbheadnav != null){
                ProyekDpbDetail proyekDpbDetail = new ProyekDpbDetail();
                proyekDpbDetail.setDocument_no(dpbheadnav.getDocumentNo());
                if(dpbheadnav.getDocumentDate() != null && !dpbheadnav.getDocumentDate().equals(""))
                    proyekDpbDetail.setDocument_date(dpbheadnav.getDocumentDate().format(formattgl));
                if(dpbheadnav.getTglDibutuhkan() != null && !dpbheadnav.getTglDibutuhkan().equals(""))
                    proyekDpbDetail.setTgl_dibutuhkan(dpbheadnav.getTglDibutuhkan().format(formattgl));
                proyekDpbDetail.setAmount(decimalFormat.format(dpbheadnav.getAmount()));
                proyekDpbDetail.setStatus(dpbheadnav.getStatus());
                proyekDpbDetail.setAmount_inc_vat(decimalFormat.format(dpbheadnav.getAmountIncVat()));
                proyekDpbDetail.setProject_code(dpbheadnav.getProjectCode());
                proyekDpbDetail.setProject_name(dpbheadnav.getProjectName());
                res.setDpb_detail(proyekDpbDetail);
            }

            List<Dpblinenav> dpblinenavs = dpblineRepository.findAllByDocumentNo(nodpb);
            List<DpbLineRealisasi> dpbLineRealisasiList = new ArrayList<>();
            if(dpblinenavs.size() > 0){
                for(Dpblinenav dpblinenav:dpblinenavs){
                    DpbLineRealisasi dpbLineRealisasi = new DpbLineRealisasi();
                    dpbLineRealisasi.setItem_no(dpblinenav.getItemNo());
                    dpbLineRealisasi.setItem_description(dpblinenav.getItemDesc());
                    dpbLineRealisasi.setQty(decimalFormat.format(dpblinenav.getQty()));
                    dpbLineRealisasi.setUnit_of_measure(dpblinenav.getUnitMeasure());
                    dpbLineRealisasi.setDirect_unit_cost(decimalFormat.format(dpblinenav.getDirectUnitCost()));
                    dpbLineRealisasi.setAmount(decimalFormat.format(dpblinenav.getAmount()));
                    dpbLineRealisasi.setAmount_inc_vat(decimalFormat.format(dpblinenav.getAmountIncVat()));
                    dpbLineRealisasi.setVat_percent(decimalFormat.format(dpblinenav.getVatPercent()));
                    dpbLineRealisasi.setUnit_cost_lcy(decimalFormat.format(dpblinenav.getUnitCostLcy()));

                    //get data PO
                    List<PONav> poNavs = ponavRepository.findAllByPrNoAndPurchaseNo(dpblinenav.getDocumentNo(),dpblinenav.getItemNo());
                    if(poNavs.size() > 0){
                        List<PODetail> poDetailList = new ArrayList<>();
                        for(PONav poNav: poNavs){
                            PODetail poDetail = new PODetail();
                            poDetail.setPo_number(poNav.getDocumentNo());
                            if(poNav.getDocumentDate() != null && !poNav.getDocumentDate().equals(""))
                                poDetail.setPo_date(poNav.getDocumentDate().format(formattgl));
                            poDetail.setPo_qty(decimalFormat.format(poNav.getQty()));
                            poDetail.setPo_status(poNav.getStatus());
                            poDetail.setPo_unit_of_measure(poNav.getUnitOfMeasure());
                            poDetail.setPo_description(poNav.getDescription());
                            poDetail.setPo_direct_unit_cost(decimalFormat.format(poNav.getDirectUnitCost()));
                            poDetail.setPo_vat_percent(decimalFormat.format(poNav.getVatPercent()));
                            poDetail.setPo_amount(decimalFormat.format(poNav.getAmount()));
                            poDetail.setPo_amount_include_vat(decimalFormat.format(poNav.getAmountIncVat()));
                            poDetail.setPo_unit_cost_lcy(decimalFormat.format(poNav.getUnitCostLcy()));
                            poDetail.setPo_buy_from(poNav.getBuyFrom());
                            if(poNav.getAmountLcy() != null)
                                poDetail.setPo_amount_lcy(decimalFormat.format(poNav.getAmountLcy()));
                            poDetailList.add(poDetail);
                        }
                        dpbLineRealisasi.setPurchases(poDetailList);
                    }
                    dpbLineRealisasiList.add(dpbLineRealisasi);
                }
                res.setDpb_lines(dpbLineRealisasiList);
            }
            res.setMsg("SUCCESS");
        }
        catch (Exception ex){
            res.setMsg("ERROR: "+ex.getMessage());
        }

        return res;
    }

    public PoListResponse getAllPo() {
        PoListResponse res = new PoListResponse();
        List<PODetail> poDetailList = new ArrayList<>();
        DateTimeFormatter formattgl = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DecimalFormat decimalFormat = new DecimalFormat("#.0#");
        try{
            List<PONav> poNavs = ponavRepository.findAllByOrderByDocumentDateDesc();
            if(poNavs.size() > 0){
                for(PONav poNav:poNavs) {
                    PODetail poDetail = new PODetail();
                    poDetail.setPo_number(poNav.getDocumentNo());
                    if(poNav.getDocumentDate() != null && !poNav.getDocumentDate().equals(""))
                        poDetail.setPo_date(poNav.getDocumentDate().format(formattgl));
                    poDetail.setPo_qty(decimalFormat.format(poNav.getQty()));
                    poDetail.setPo_status(poNav.getStatus());
                    poDetail.setPo_unit_of_measure(poNav.getUnitOfMeasure());
                    poDetail.setPo_description(poNav.getDescription());
                    poDetail.setPo_direct_unit_cost(decimalFormat.format(poNav.getDirectUnitCost()));
                    poDetail.setPo_vat_percent(decimalFormat.format(poNav.getVatPercent()));
                    poDetail.setPo_amount(decimalFormat.format(poNav.getAmount()));
                    poDetail.setPo_amount_include_vat(decimalFormat.format(poNav.getAmountIncVat()));
                    poDetail.setPo_unit_cost_lcy(decimalFormat.format(poNav.getUnitCostLcy()));
                    poDetail.setPo_buy_from(poNav.getBuyFrom());
                    poDetail.setPo_project_code(poNav.getProjectCode());
                    if(poNav.getAmountLcy() != null)
                        poDetail.setPo_amount_lcy(decimalFormat.format(poNav.getAmountLcy()));
                    poDetailList.add(poDetail);
                }
            }
            res.setMsg("SUCCESS");
            res.setCount(poDetailList.size());
            res.setPurchases(poDetailList);
        }
        catch (Exception ex){
            res.setMsg("ERROR: "+ex.getMessage());
        }

        return res;
    }

    public ProyekListResponse getAllProyekWithTipeTahun(String tipe, int tahun) {
        ProyekListResponse res = new ProyekListResponse();
        List<ProyekData> proyekDataList = new ArrayList<>();
        try{
            List<Proyeknav> proyeknavs = proyeknavRepository.findAllByProjectTypeAndTahun(tipe,tahun);
            if(proyeknavs.size() > 0){
                DateTimeFormatter formattgl = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                for (Proyeknav proyek: proyeknavs){
                    ProyekData proyekData = new ProyekData();
                    proyekData.setProject_code(proyek.getProjectCode());
                    proyekData.setProject_name(proyek.getProjectName());
                    proyekData.setPimpro(proyek.getPimpro());
                    proyekData.setProject_type(proyek.getProjectType());
                    proyekData.setStatus(proyek.getStatus());
                    proyekData.setProject_value(new BigDecimal(proyek.getNilaiProject()).toPlainString());
                    if(proyek.getTglMulai() != null && !proyek.getTglMulai().equals("")) {
                        proyekData.setTgl_mulai(proyek.getTglMulai().format(formattgl));
                    }
                    if(proyek.getTglAkhir() != null && !proyek.getTglAkhir().equals("")) {
                        proyekData.setTgl_akhir(proyek.getTglAkhir().format(formattgl));
                    }
                    proyekData.setCarry_over(proyekData.isCarry_over());
                    proyekDataList.add(proyekData);
                }
            }
            res.setMsg("SUCCESS");
            res.setCount(proyekDataList.size());
            res.setProyeks(proyekDataList);
        }
        catch (Exception ex){
            res.setMsg("ERROR: "+ex.getMessage());
        }

        return res;
    }
}
