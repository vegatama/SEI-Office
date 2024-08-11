package com.suryaenergi.sdm.backendapi.service;

import com.suryaenergi.sdm.backendapi.entity.Antrianemail;
import com.suryaenergi.sdm.backendapi.entity.Employee;
import com.suryaenergi.sdm.backendapi.entity.Notifnav;
import com.suryaenergi.sdm.backendapi.entity.Notiftelegram;
import com.suryaenergi.sdm.backendapi.repository.AntrianemailRepository;
import com.suryaenergi.sdm.backendapi.repository.EmployeeRepository;
import com.suryaenergi.sdm.backendapi.repository.NotifnavRepository;
import com.suryaenergi.sdm.backendapi.repository.NotiftelegramRepository;
import com.suryaenergi.sdm.backendapi.response.MessageResponse;
import okhttp3.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

import static com.suryaenergi.sdm.backendapi.pojo.Message.ERROR_MESSAGE;
import static com.suryaenergi.sdm.backendapi.pojo.Message.SUCCESS_MESSAGE;

@Service
public class NotifikasiService {
    @Autowired
    private NotifnavRepository notifnavRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private AntrianemailRepository antrianemailRepository;
    @Autowired
    private NotiftelegramRepository notiftelegramRepository;
    @Value("${navagent.username}")
    private String navUsername;
    @Value("${navagent.password}")
    private String navPassword;
    @Value("${navagent.baseurl}")
    private String navBaseUrl;

    private String getResponseNavAPI(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        String credential = Credentials.basic(navUsername, navPassword);
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization",credential)
                .build();

        Call call = client.newCall(request);
        Response response = call.execute();
        return response.body().string();
    }

    public MessageResponse simpanNotifUM(String user, String dokumen) {
        MessageResponse res = new MessageResponse();

        try{
            Notifnav notifnav = notifnavRepository.findByUserIdAndNoDokumenAndJenisDokumen(user,dokumen,"UM");
            if(notifnav == null) {
                notifnav = new Notifnav();
                notifnav.setJenisDokumen("UM");
                notifnav.setNoDokumen(dokumen);
                notifnav.setUserId(user);
                notifnavRepository.save(notifnav);
                Employee employee = employeeRepository.findFirstByUserIdNav(user);
                if (employee != null) {

                    String apiurl = navBaseUrl + "apiUM?$filter=Nomor%20eq%20'" + dokumen + "'%20and%20Type_Table%20eq%20'Header'";
                    String subject = "NEED APPROVE - Pengajuan Dokumen Uang Muka: " + dokumen;
                    String responseBody = getResponseNavAPI(apiurl);

                    Object obj = new JSONParser().parse(responseBody);
                    JSONObject jo = (JSONObject) obj;
                    if (jo.containsKey("value")) {
                        List<JSONObject> value = (List<JSONObject>) jo.get("value");
                        JSONObject dataUM = value.getFirst();
                        String kode = (String) dataUM.get("Kode_Kegiatan");
                        String nama = (String) dataUM.get("Nama_Kegiatan");
                        String keperluan = (String) dataUM.get("Untuk_Keperluan");
                        String diajukan = (String) dataUM.get("Diajukan_Oleh_Name");
                        Double amount;
                        if (dataUM.get("gVarTotalAmount") instanceof Double) {
                            amount = (Double) dataUM.get("gVarTotalAmount");
                        } else {
                            Long tempamount = (Long) dataUM.get("gVarTotalAmount");
                            amount = tempamount.doubleValue();
                        }

                        if (employee.getEmail() != null && !employee.getEmail().equalsIgnoreCase("") && employee.isNotifEmail()) {
                            String body = getBodyEmail(subject, dokumen, diajukan, kode, nama, keperluan, String.format("%.0f", amount), employee.getFullName(), "Uang Muka");
                            Antrianemail antrianemail = new Antrianemail();
                            antrianemail.setEmail(employee.getEmail());
                            //antrianemail.setEmail("fauzan.azmi@suryaenergi.com");
                            antrianemail.setStatus(false);
                            antrianemail.setContent(body);
                            antrianemail.setSubject(subject);
                            antrianemailRepository.save(antrianemail);
                        }

                        if (employee.getTelegramId() != null && !employee.getTelegramId().equalsIgnoreCase("")) {
                            //tambah antrian telegramnotif
                            String msg = getTelegramMessage(dokumen, diajukan, kode, nama, keperluan, String.format("%.0f", amount), employee.getFullName(), "Uang Muka");
                            Notiftelegram notiftelegram = new Notiftelegram();
                            notiftelegram.setTelegramId(employee.getTelegramId());
                            notiftelegram.setMessage(msg);
                            notiftelegram.setStatus("PENDING");
                            notiftelegramRepository.save(notiftelegram);
                        }
                    }
                }
            }

            res.setMsg(SUCCESS_MESSAGE);
        }
        catch (Exception ex){
            res.setMsg(ERROR_MESSAGE+ex);
        }

        return res;
    }

    public MessageResponse simpanNotifDPB(String user, String dokumen) {
        MessageResponse res = new MessageResponse();

        try{
            Notifnav notifnav = notifnavRepository.findByUserIdAndNoDokumenAndJenisDokumen(user,dokumen,"DPB/J");
            if(notifnav == null) {
                notifnav = new Notifnav();
                notifnav.setJenisDokumen("DPB/J");
                notifnav.setNoDokumen(dokumen);
                notifnav.setUserId(user);
                notifnavRepository.save(notifnav);
                Employee employee = employeeRepository.findFirstByUserIdNav(user);
                if (employee != null) {

                    String apiurl = navBaseUrl + "apiDpbHead?$filter=No%20eq%20'" + dokumen + "'";
                    String subject = "NEED APPROVE - Pengajuan Dokumen DPB/J: " + dokumen;
                    String responseBody = getResponseNavAPI(apiurl);

                    Object obj = new JSONParser().parse(responseBody);
                    JSONObject jo = (JSONObject) obj;
                    if (jo.containsKey("value")) {
                        List<JSONObject> value = (List<JSONObject>) jo.get("value");
                        JSONObject dataUM = value.getFirst();
                        String kode = (String) dataUM.get("Kode_Proyek");
                        String nama = (String) dataUM.get("Project_Name");
                        String keperluan = (String) dataUM.get("Additional_Type");
                        String diajukan = (String) dataUM.get("Pemohon_PimPro");
                        Double amount;
                        if (dataUM.get("Amount") instanceof Double) {
                            amount = (Double) dataUM.get("Amount");
                        } else {
                            Long tempamount = (Long) dataUM.get("Amount");
                            amount = tempamount.doubleValue();
                        }

                        if (employee.getEmail() != null && !employee.getEmail().equalsIgnoreCase("") && employee.isNotifEmail()) {
                            String body = getBodyEmail(subject, dokumen, diajukan, kode, nama, keperluan, String.format("%.0f", amount), employee.getFullName(), "DPB/J");
                            Antrianemail antrianemail = new Antrianemail();
                            antrianemail.setEmail(employee.getEmail());
                            //antrianemail.setEmail("fauzan.azmi@suryaenergi.com");
                            antrianemail.setStatus(false);
                            antrianemail.setContent(body);
                            antrianemail.setSubject(subject);
                            antrianemailRepository.save(antrianemail);
                        }

                        if (employee.getTelegramId() != null && !employee.getTelegramId().equalsIgnoreCase("")) {
                            //tambah antrian telegramnotif
                            String msg = getTelegramMessage(dokumen, diajukan, kode, nama, keperluan, String.format("%.0f", amount), employee.getFullName(), "DPB/J");
                            Notiftelegram notiftelegram = new Notiftelegram();
                            notiftelegram.setTelegramId(employee.getTelegramId());
                            notiftelegram.setMessage(msg);
                            notiftelegram.setStatus("PENDING");
                            notiftelegramRepository.save(notiftelegram);
                        }
                    }
                }
            }

            res.setMsg(SUCCESS_MESSAGE);
        }
        catch (Exception ex){
            res.setMsg(ERROR_MESSAGE+ex);
        }

        return res;
    }

    public MessageResponse simpanNotifSPPD(String user, String dokumen) {
        MessageResponse res = new MessageResponse();
        //System.out.println("User: "+user);
        try{
            Notifnav notifnav = notifnavRepository.findByUserIdAndNoDokumenAndJenisDokumen(user,dokumen,"SPPD");
            if(notifnav == null) {
                notifnav = new Notifnav();
                notifnav.setJenisDokumen("SPPD");
                notifnav.setNoDokumen(dokumen);
                notifnav.setUserId(user);
                notifnavRepository.save(notifnav);
                Employee employee = employeeRepository.findFirstByUserIdNav(user);
                if (employee != null) {
                    String apiurl = navBaseUrl + "api_sppd_list?$filter=No%20eq%20'" + dokumen + "'";
                    String subject = "NEED APPROVE - Pengajuan Dokumen SPPD: " + dokumen;
                    String responseBody = getResponseNavAPI(apiurl);

                    Object obj = new JSONParser().parse(responseBody);
                    JSONObject jo = (JSONObject) obj;
                    if (jo.containsKey("value")) {
                        List<JSONObject> value = (List<JSONObject>) jo.get("value");
                        JSONObject dataUM = value.getFirst();
                        String kode = (String) dataUM.get("Kode_Kegiatan");
                        String nama = (String) dataUM.get("Nama_Kegiatan");
                        String keperluan = (String) dataUM.get("Untuk_Keperluan");
                        String diajukan = (String) dataUM.get("Diajukan_Oleh");
                        Double amount;
                        if (dataUM.get("Akomodasi_Lumpsum_IDR") instanceof Double) {
                            amount = (Double) dataUM.get("Akomodasi_Lumpsum_IDR");
                        } else {
                            Long tempamount = (Long) dataUM.get("Akomodasi_Lumpsum_IDR");
                            amount = tempamount.doubleValue();
                        }
                        if (dataUM.get("Akomodasi_Uang_Muka_IDR") instanceof Double) {
                            amount += (Double) dataUM.get("Akomodasi_Uang_Muka_IDR");
                        } else {
                            Long tempamount = (Long) dataUM.get("Akomodasi_Uang_Muka_IDR");
                            amount += tempamount.doubleValue();
                        }
                        if (dataUM.get("Transportasi_Lumpsum_IDR") instanceof Double) {
                            amount += (Double) dataUM.get("Transportasi_Lumpsum_IDR");
                        } else {
                            Long tempamount = (Long) dataUM.get("Transportasi_Lumpsum_IDR");
                            amount += tempamount.doubleValue();
                        }
                        if (dataUM.get("Transportasi_Uang_Muka_IDR") instanceof Double) {
                            amount += (Double) dataUM.get("Transportasi_Uang_Muka_IDR");
                        } else {
                            Long tempamount = (Long) dataUM.get("Transportasi_Uang_Muka_IDR");
                            amount += tempamount.doubleValue();
                        }

                        if (employee.getEmail() != null && !employee.getEmail().equalsIgnoreCase("") && employee.isNotifEmail()) {
                            String body = getBodyEmail(subject, dokumen, diajukan, kode, nama, keperluan, String.format("%.0f", amount), employee.getFullName(), "SPPD");
                            Antrianemail antrianemail = new Antrianemail();
                            antrianemail.setEmail(employee.getEmail());
                            //antrianemail.setEmail("fauzan.azmi@suryaenergi.com");
                            antrianemail.setStatus(false);
                            antrianemail.setContent(body);
                            antrianemail.setSubject(subject);
                            antrianemailRepository.save(antrianemail);
                        }

                        if (employee.getTelegramId() != null && !employee.getTelegramId().equalsIgnoreCase("")) {
                            //tambah antrian telegramnotif
                            String msg = getTelegramMessage(dokumen, diajukan, kode, nama, keperluan, String.format("%.0f", amount), employee.getFullName(), "SPPD");
                            Notiftelegram notiftelegram = new Notiftelegram();
                            notiftelegram.setTelegramId(employee.getTelegramId());
                            notiftelegram.setMessage(msg);
                            notiftelegram.setStatus("PENDING");
                            notiftelegramRepository.save(notiftelegram);
                        }
                    }
                }
            }

            res.setMsg(SUCCESS_MESSAGE);
        }
        catch (Exception ex){
            res.setMsg(ERROR_MESSAGE+ex);
        }

        return res;
    }
    private String getTelegramMessage(String dokumen, String diajukan, String kode, String nama, String keperluan, String jumlah, String fullName, String jndok) {
        String text = "Yth. Bpk/Ibu <b>"+fullName+"</b>.%0A%0A" +
                "Terdapat pengajuan dokumen NAV "+jndok+" yang membutuhkan approval dari Bpk/Ibu. " +
                "Adapun rincian dari dokumen tersebut adalah sebagai berikut:";
        text += "%0A%0A=====================================%0A" +
                "<b>No. Dokumen:</b> "+dokumen+"%0A%0A" +
                "<b>Kode Kegiatan:</b> "+kode+"%0A%0A" +
                "<b>Nama Kegiatan:</b> "+nama+"%0A%0A"+
                "<b>Diajukan Oleh:</b> "+diajukan+"%0A%0A"+
                "<b>Jumlah:</b> "+jumlah+"%0A%0A"+
                "<b>Untuk Keperluan:</b> "+keperluan+"%0A%0A"+
                "=====================================%0A%0A";
        text += "Silahkan Bpk/Ibu melakukan Approve / Reject untuk dokumen tersebut di aplikasi NAV, atau melalui pranala berikut: https://dynamicssei.com/bc140/";

        return text;
    }
    private String getBodyEmail(String subject, String nodok, String diajukan, String kode, String nama, String keperluan, String amount, String nmkaryawan, String jndok) {
        String body = "<!doctype html>\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\">\n" +
                "    <head>\n" +
                "        <!-- NAME: 1 COLUMN -->\n" +
                "        <!--[if gte mso 15]>\n" +
                "        <xml>\n" +
                "            <o:OfficeDocumentSettings>\n" +
                "            <o:AllowPNG/>\n" +
                "            <o:PixelsPerInch>96</o:PixelsPerInch>\n" +
                "            </o:OfficeDocumentSettings>\n" +
                "        </xml>\n" +
                "        <![endif]-->\n" +
                "        <meta charset=\"UTF-8\">\n" +
                "        <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                "        <title>"+subject+"</title>\n" +
                "        \n" +
                "    <style type=\"text/css\">\n" +
                "\t\tp{\n" +
                "\t\t\tmargin:10px 0;\n" +
                "\t\t\tpadding:0;\n" +
                "\t\t}\n" +
                "\t\ttable{\n" +
                "\t\t\tborder-collapse:collapse;\n" +
                "\t\t}\n" +
                "\t\th1,h2,h3,h4,h5,h6{\n" +
                "\t\t\tdisplay:block;\n" +
                "\t\t\tmargin:0;\n" +
                "\t\t\tpadding:0;\n" +
                "\t\t}\n" +
                "\t\timg,a img{\n" +
                "\t\t\tborder:0;\n" +
                "\t\t\theight:auto;\n" +
                "\t\t\toutline:none;\n" +
                "\t\t\ttext-decoration:none;\n" +
                "\t\t}\n" +
                "\t\tbody,#bodyTable,#bodyCell{\n" +
                "\t\t\theight:100%;\n" +
                "\t\t\tmargin:0;\n" +
                "\t\t\tpadding:0;\n" +
                "\t\t\twidth:100%;\n" +
                "\t\t}\n" +
                "\t\t.mcnPreviewText{\n" +
                "\t\t\tdisplay:none !important;\n" +
                "\t\t}\n" +
                "\t\t#outlook a{\n" +
                "\t\t\tpadding:0;\n" +
                "\t\t}\n" +
                "\t\timg{\n" +
                "\t\t\t-ms-interpolation-mode:bicubic;\n" +
                "\t\t}\n" +
                "\t\ttable{\n" +
                "\t\t\tmso-table-lspace:0pt;\n" +
                "\t\t\tmso-table-rspace:0pt;\n" +
                "\t\t}\n" +
                "\t\t.ReadMsgBody{\n" +
                "\t\t\twidth:100%;\n" +
                "\t\t}\n" +
                "\t\t.ExternalClass{\n" +
                "\t\t\twidth:100%;\n" +
                "\t\t}\n" +
                "\t\tp,a,li,td,blockquote{\n" +
                "\t\t\tmso-line-height-rule:exactly;\n" +
                "\t\t}\n" +
                "\t\ta[href^=tel],a[href^=sms]{\n" +
                "\t\t\tcolor:inherit;\n" +
                "\t\t\tcursor:default;\n" +
                "\t\t\ttext-decoration:none;\n" +
                "\t\t}\n" +
                "\t\tp,a,li,td,body,table,blockquote{\n" +
                "\t\t\t-ms-text-size-adjust:100%;\n" +
                "\t\t\t-webkit-text-size-adjust:100%;\n" +
                "\t\t}\n" +
                "\t\t.ExternalClass,.ExternalClass p,.ExternalClass td,.ExternalClass div,.ExternalClass span,.ExternalClass font{\n" +
                "\t\t\tline-height:100%;\n" +
                "\t\t}\n" +
                "\t\ta[x-apple-data-detectors]{\n" +
                "\t\t\tcolor:inherit !important;\n" +
                "\t\t\ttext-decoration:none !important;\n" +
                "\t\t\tfont-size:inherit !important;\n" +
                "\t\t\tfont-family:inherit !important;\n" +
                "\t\t\tfont-weight:inherit !important;\n" +
                "\t\t\tline-height:inherit !important;\n" +
                "\t\t}\n" +
                "\t\t#bodyCell{\n" +
                "\t\t\tpadding:10px;\n" +
                "\t\t}\n" +
                "\t\t.templateContainer{\n" +
                "\t\t\tmax-width:600px !important;\n" +
                "\t\t}\n" +
                "\t\ta.mcnButton{\n" +
                "\t\t\tdisplay:block;\n" +
                "\t\t}\n" +
                "\t\t.mcnImage,.mcnRetinaImage{\n" +
                "\t\t\tvertical-align:bottom;\n" +
                "\t\t}\n" +
                "\t\t.mcnTextContent{\n" +
                "\t\t\tword-break:break-word;\n" +
                "\t\t}\n" +
                "\t\t.mcnTextContent img{\n" +
                "\t\t\theight:auto !important;\n" +
                "\t\t}\n" +
                "\t\t.mcnDividerBlock{\n" +
                "\t\t\ttable-layout:fixed !important;\n" +
                "\t\t}\n" +
                "\t\tbody,#bodyTable{\n" +
                "\t\t\tbackground-color:#FAFAFA;\n" +
                "\t\t}\n" +
                "\t\t#bodyCell{\n" +
                "\t\t\tborder-top:0;\n" +
                "\t\t}\n" +
                "\t\t.templateContainer{\n" +
                "\t\t\tborder:0;\n" +
                "\t\t}\n" +
                "\t\th1{\n" +
                "\t\t\tcolor:#202020;\n" +
                "\t\t\tfont-family:'Helvetica Neue', Helvetica, Arial, Verdana, sans-serif;\n" +
                "\t\t\tfont-size:24px;\n" +
                "\t\t\tfont-style:normal;\n" +
                "\t\t\tfont-weight:bold;\n" +
                "\t\t\tline-height:125%;\n" +
                "\t\t\tletter-spacing:normal;\n" +
                "\t\t\ttext-align:left;\n" +
                "\t\t}\n" +
                "\t\th2{\n" +
                "\t\t\tcolor:#202020;\n" +
                "\t\t\tfont-family:Tahoma, Verdana, Segoe, sans-serif;\n" +
                "\t\t\tfont-size:24px;\n" +
                "\t\t\tfont-style:normal;\n" +
                "\t\t\tfont-weight:normal;\n" +
                "\t\t\tline-height:125%;\n" +
                "\t\t\tletter-spacing:normal;\n" +
                "\t\t\ttext-align:left;\n" +
                "\t\t}\n" +
                "\t\th3{\n" +
                "\t\t\tcolor:#202020;\n" +
                "\t\t\tfont-family:'Helvetica Neue', Helvetica, Arial, Verdana, sans-serif;\n" +
                "\t\t\tfont-size:14px;\n" +
                "\t\t\tfont-style:normal;\n" +
                "\t\t\tfont-weight:normal;\n" +
                "\t\t\tline-height:125%;\n" +
                "\t\t\tletter-spacing:normal;\n" +
                "\t\t\ttext-align:left;\n" +
                "\t\t}\n" +
                "\t\th4{\n" +
                "\t\t\tcolor:#202020;\n" +
                "\t\t\tfont-family:'Helvetica Neue', Helvetica, Arial, Verdana, sans-serif;\n" +
                "\t\t\tfont-size:18px;\n" +
                "\t\t\tfont-style:normal;\n" +
                "\t\t\tfont-weight:bold;\n" +
                "\t\t\tline-height:125%;\n" +
                "\t\t\tletter-spacing:normal;\n" +
                "\t\t\ttext-align:left;\n" +
                "\t\t}\n" +
                "\t\t#templatePreheader{\n" +
                "\t\t\tbackground-color:#FAFAFA;\n" +
                "\t\t\tbackground-image:none;\n" +
                "\t\t\tbackground-repeat:no-repeat;\n" +
                "\t\t\tbackground-position:center;\n" +
                "\t\t\tbackground-size:cover;\n" +
                "\t\t\tborder-top:0;\n" +
                "\t\t\tborder-bottom:0;\n" +
                "\t\t\tpadding-top:9px;\n" +
                "\t\t\tpadding-bottom:9px;\n" +
                "\t\t}\n" +
                "\t\t#templatePreheader .mcnTextContent,#templatePreheader .mcnTextContent p{\n" +
                "\t\t\tcolor:#656565;\n" +
                "\t\t\tfont-family:Helvetica;\n" +
                "\t\t\tfont-size:12px;\n" +
                "\t\t\tline-height:150%;\n" +
                "\t\t\ttext-align:left;\n" +
                "\t\t}\n" +
                "\t\t#templatePreheader .mcnTextContent a,#templatePreheader .mcnTextContent p a{\n" +
                "\t\t\tcolor:#656565;\n" +
                "\t\t\tfont-weight:normal;\n" +
                "\t\t\ttext-decoration:underline;\n" +
                "\t\t}\n" +
                "\t\t#templateHeader{\n" +
                "\t\t\tbackground-color:#FFFFFF;\n" +
                "\t\t\tbackground-image:none;\n" +
                "\t\t\tbackground-repeat:no-repeat;\n" +
                "\t\t\tbackground-position:center;\n" +
                "\t\t\tbackground-size:cover;\n" +
                "\t\t\tborder-top:0;\n" +
                "\t\t\tborder-bottom:0;\n" +
                "\t\t\tpadding-top:9px;\n" +
                "\t\t\tpadding-bottom:0;\n" +
                "\t\t}\n" +
                "\t\t#templateHeader .mcnTextContent,#templateHeader .mcnTextContent p{\n" +
                "\t\t\tcolor:#202020;\n" +
                "\t\t\tfont-family:Helvetica;\n" +
                "\t\t\tfont-size:16px;\n" +
                "\t\t\tline-height:150%;\n" +
                "\t\t\ttext-align:left;\n" +
                "\t\t}\n" +
                "\t\t#templateHeader .mcnTextContent a,#templateHeader .mcnTextContent p a{\n" +
                "\t\t\tcolor:#007C89;\n" +
                "\t\t\tfont-weight:normal;\n" +
                "\t\t\ttext-decoration:underline;\n" +
                "\t\t}\n" +
                "\t\t#templateBody{\n" +
                "\t\t\tbackground-color:#ffffff;\n" +
                "\t\t\tbackground-image:none;\n" +
                "\t\t\tbackground-repeat:no-repeat;\n" +
                "\t\t\tbackground-position:center;\n" +
                "\t\t\tbackground-size:cover;\n" +
                "\t\t\tborder-top:0;\n" +
                "\t\t\tborder-bottom:2px solid #EAEAEA;\n" +
                "\t\t\tpadding-top:0;\n" +
                "\t\t\tpadding-bottom:9px;\n" +
                "\t\t}\n" +
                "\t\t#templateBody .mcnTextContent,#templateBody .mcnTextContent p{\n" +
                "\t\t\tcolor:#202020;\n" +
                "\t\t\tfont-family:Helvetica;\n" +
                "\t\t\tfont-size:16px;\n" +
                "\t\t\tline-height:150%;\n" +
                "\t\t\ttext-align:left;\n" +
                "\t\t}\n" +
                "\t\t#templateBody .mcnTextContent a,#templateBody .mcnTextContent p a{\n" +
                "\t\t\tcolor:#007C89;\n" +
                "\t\t\tfont-weight:normal;\n" +
                "\t\t\ttext-decoration:underline;\n" +
                "\t\t}\n" +
                "\t\t#templateFooter{\n" +
                "\t\t\tbackground-color:#fafafa;\n" +
                "\t\t\tbackground-image:none;\n" +
                "\t\t\tbackground-repeat:no-repeat;\n" +
                "\t\t\tbackground-position:center;\n" +
                "\t\t\tbackground-size:cover;\n" +
                "\t\t\tborder-top:0;\n" +
                "\t\t\tborder-bottom:0;\n" +
                "\t\t\tpadding-top:9px;\n" +
                "\t\t\tpadding-bottom:9px;\n" +
                "\t\t}\n" +
                "\t\t#templateFooter .mcnTextContent,#templateFooter .mcnTextContent p{\n" +
                "\t\t\tcolor:#656565;\n" +
                "\t\t\tfont-family:Helvetica;\n" +
                "\t\t\tfont-size:12px;\n" +
                "\t\t\tline-height:150%;\n" +
                "\t\t\ttext-align:center;\n" +
                "\t\t}\n" +
                "\t\t#templateFooter .mcnTextContent a,#templateFooter .mcnTextContent p a{\n" +
                "\t\t\tcolor:#656565;\n" +
                "\t\t\tfont-weight:normal;\n" +
                "\t\t\ttext-decoration:underline;\n" +
                "\t\t}\n" +
                "\t@media only screen and (min-width:768px){\n" +
                "\t\t.templateContainer{\n" +
                "\t\t\twidth:600px !important;\n" +
                "\t\t}\n" +
                "\n" +
                "}\t@media only screen and (max-width: 480px){\n" +
                "\t\tbody,table,td,p,a,li,blockquote{\n" +
                "\t\t\t-webkit-text-size-adjust:none !important;\n" +
                "\t\t}\n" +
                "\n" +
                "}\t@media only screen and (max-width: 480px){\n" +
                "\t\tbody{\n" +
                "\t\t\twidth:100% !important;\n" +
                "\t\t\tmin-width:100% !important;\n" +
                "\t\t}\n" +
                "\n" +
                "}\t@media only screen and (max-width: 480px){\n" +
                "\t\t.mcnRetinaImage{\n" +
                "\t\t\tmax-width:100% !important;\n" +
                "\t\t}\n" +
                "\n" +
                "}\t@media only screen and (max-width: 480px){\n" +
                "\t\t.mcnImage{\n" +
                "\t\t\twidth:100% !important;\n" +
                "\t\t}\n" +
                "\n" +
                "}\t@media only screen and (max-width: 480px){\n" +
                "\t\t.mcnCartContainer,.mcnCaptionTopContent,.mcnRecContentContainer,.mcnCaptionBottomContent,.mcnTextContentContainer,.mcnBoxedTextContentContainer,.mcnImageGroupContentContainer,.mcnCaptionLeftTextContentContainer,.mcnCaptionRightTextContentContainer,.mcnCaptionLeftImageContentContainer,.mcnCaptionRightImageContentContainer,.mcnImageCardLeftTextContentContainer,.mcnImageCardRightTextContentContainer,.mcnImageCardLeftImageContentContainer,.mcnImageCardRightImageContentContainer{\n" +
                "\t\t\tmax-width:100% !important;\n" +
                "\t\t\twidth:100% !important;\n" +
                "\t\t}\n" +
                "\n" +
                "}\t@media only screen and (max-width: 480px){\n" +
                "\t\t.mcnBoxedTextContentContainer{\n" +
                "\t\t\tmin-width:100% !important;\n" +
                "\t\t}\n" +
                "\n" +
                "}\t@media only screen and (max-width: 480px){\n" +
                "\t\t.mcnImageGroupContent{\n" +
                "\t\t\tpadding:9px !important;\n" +
                "\t\t}\n" +
                "\n" +
                "}\t@media only screen and (max-width: 480px){\n" +
                "\t\t.mcnCaptionLeftContentOuter .mcnTextContent,.mcnCaptionRightContentOuter .mcnTextContent{\n" +
                "\t\t\tpadding-top:9px !important;\n" +
                "\t\t}\n" +
                "\n" +
                "}\t@media only screen and (max-width: 480px){\n" +
                "\t\t.mcnImageCardTopImageContent,.mcnCaptionBottomContent:last-child .mcnCaptionBottomImageContent,.mcnCaptionBlockInner .mcnCaptionTopContent:last-child .mcnTextContent{\n" +
                "\t\t\tpadding-top:18px !important;\n" +
                "\t\t}\n" +
                "\n" +
                "}\t@media only screen and (max-width: 480px){\n" +
                "\t\t.mcnImageCardBottomImageContent{\n" +
                "\t\t\tpadding-bottom:9px !important;\n" +
                "\t\t}\n" +
                "\n" +
                "}\t@media only screen and (max-width: 480px){\n" +
                "\t\t.mcnImageGroupBlockInner{\n" +
                "\t\t\tpadding-top:0 !important;\n" +
                "\t\t\tpadding-bottom:0 !important;\n" +
                "\t\t}\n" +
                "\n" +
                "}\t@media only screen and (max-width: 480px){\n" +
                "\t\t.mcnImageGroupBlockOuter{\n" +
                "\t\t\tpadding-top:9px !important;\n" +
                "\t\t\tpadding-bottom:9px !important;\n" +
                "\t\t}\n" +
                "\n" +
                "}\t@media only screen and (max-width: 480px){\n" +
                "\t\t.mcnTextContent,.mcnBoxedTextContentColumn{\n" +
                "\t\t\tpadding-right:18px !important;\n" +
                "\t\t\tpadding-left:18px !important;\n" +
                "\t\t}\n" +
                "\n" +
                "}\t@media only screen and (max-width: 480px){\n" +
                "\t\t.mcnImageCardLeftImageContent,.mcnImageCardRightImageContent{\n" +
                "\t\t\tpadding-right:18px !important;\n" +
                "\t\t\tpadding-bottom:0 !important;\n" +
                "\t\t\tpadding-left:18px !important;\n" +
                "\t\t}\n" +
                "\n" +
                "}\t@media only screen and (max-width: 480px){\n" +
                "\t\t.mcpreview-image-uploader{\n" +
                "\t\t\tdisplay:none !important;\n" +
                "\t\t\twidth:100% !important;\n" +
                "\t\t}\n" +
                "\n" +
                "}\t@media only screen and (max-width: 480px){\n" +
                "\t\th1{\n" +
                "\t\t\tfont-size:22px !important;\n" +
                "\t\t\tline-height:125% !important;\n" +
                "\t\t}\n" +
                "\n" +
                "}\t@media only screen and (max-width: 480px){\n" +
                "\t\th2{\n" +
                "\t\t\tfont-size:20px !important;\n" +
                "\t\t\tline-height:125% !important;\n" +
                "\t\t}\n" +
                "\n" +
                "}\t@media only screen and (max-width: 480px){\n" +
                "\t\th3{\n" +
                "\t\t\tfont-size:18px !important;\n" +
                "\t\t\tline-height:125% !important;\n" +
                "\t\t}\n" +
                "\n" +
                "}\t@media only screen and (max-width: 480px){\n" +
                "\t\th4{\n" +
                "\t\t\tfont-size:16px !important;\n" +
                "\t\t\tline-height:150% !important;\n" +
                "\t\t}\n" +
                "\n" +
                "}\t@media only screen and (max-width: 480px){\n" +
                "\t\t.mcnBoxedTextContentContainer .mcnTextContent,.mcnBoxedTextContentContainer .mcnTextContent p{\n" +
                "\t\t\tfont-size:14px !important;\n" +
                "\t\t\tline-height:150% !important;\n" +
                "\t\t}\n" +
                "\n" +
                "}\t@media only screen and (max-width: 480px){\n" +
                "\t\t#templatePreheader{\n" +
                "\t\t\tdisplay:block !important;\n" +
                "\t\t}\n" +
                "\n" +
                "}\t@media only screen and (max-width: 480px){\n" +
                "\t\t#templatePreheader .mcnTextContent,#templatePreheader .mcnTextContent p{\n" +
                "\t\t\tfont-size:14px !important;\n" +
                "\t\t\tline-height:150% !important;\n" +
                "\t\t}\n" +
                "\n" +
                "}\t@media only screen and (max-width: 480px){\n" +
                "\t\t#templateHeader .mcnTextContent,#templateHeader .mcnTextContent p{\n" +
                "\t\t\tfont-size:16px !important;\n" +
                "\t\t\tline-height:150% !important;\n" +
                "\t\t}\n" +
                "\n" +
                "}\t@media only screen and (max-width: 480px){\n" +
                "\t\t#templateBody .mcnTextContent,#templateBody .mcnTextContent p{\n" +
                "\t\t\tfont-size:16px !important;\n" +
                "\t\t\tline-height:150% !important;\n" +
                "\t\t}\n" +
                "\n" +
                "}\t@media only screen and (max-width: 480px){\n" +
                "\t\t#templateFooter .mcnTextContent,#templateFooter .mcnTextContent p{\n" +
                "\t\t\tfont-size:14px !important;\n" +
                "\t\t\tline-height:150% !important;\n" +
                "\t\t}\n" +
                "\n" +
                "}</style></head>\n" +
                "    <body style=\"height: 100%;margin: 0;padding: 0;width: 100%;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;background-color: #FAFAFA;\">\n" +
                "        <!--*|IF:MC_PREVIEW_TEXT|*-->\n" +
                "        <!--[if !gte mso 9]><!----><span class=\"mcnPreviewText\" style=\"display:none; font-size:0px; line-height:0px; max-height:0px; max-width:0px; opacity:0; overflow:hidden; visibility:hidden; mso-hide:all;\">*|MC_PREVIEW_TEXT|*</span><!--<![endif]-->\n" +
                "        <!--*|END:IF|*-->\n" +
                "        <center>\n" +
                "            <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" height=\"100%\" width=\"100%\" id=\"bodyTable\" style=\"border-collapse: collapse;mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;height: 100%;margin: 0;padding: 0;width: 100%;background-color: #FAFAFA;\">\n" +
                "                <tr>\n" +
                "                    <td align=\"center\" valign=\"top\" id=\"bodyCell\" style=\"mso-line-height-rule: exactly;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;height: 100%;margin: 0;padding: 10px;width: 100%;border-top: 0;\">\n" +
                "                        <!-- BEGIN TEMPLATE // -->\n" +
                "                        <!--[if (gte mso 9)|(IE)]>\n" +
                "                        <table align=\"center\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" width=\"600\" style=\"width:600px;\">\n" +
                "                        <tr>\n" +
                "                        <td align=\"center\" valign=\"top\" width=\"600\" style=\"width:600px;\">\n" +
                "                        <![endif]-->\n" +
                "                        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" class=\"templateContainer\" style=\"border-collapse: collapse;mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;border: 0;max-width: 600px !important;\">\n" +
                "                            <tr>\n" +
                "                                <td valign=\"top\" id=\"templatePreheader\" style=\"background:#FAFAFA none no-repeat center/cover;mso-line-height-rule: exactly;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;background-color: #FAFAFA;background-image: none;background-repeat: no-repeat;background-position: center;background-size: cover;border-top: 0;border-bottom: 0;padding-top: 9px;padding-bottom: 9px;\"><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" class=\"mcnTextBlock\" style=\"min-width: 100%;border-collapse: collapse;mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\">\n" +
                "    <tbody class=\"mcnTextBlockOuter\">\n" +
                "        <tr>\n" +
                "            <td valign=\"top\" class=\"mcnTextBlockInner\" style=\"padding-top: 9px;mso-line-height-rule: exactly;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\">\n" +
                "              \t<!--[if mso]>\n" +
                "\t\t\t\t<table align=\"left\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" width=\"100%\" style=\"width:100%;\">\n" +
                "\t\t\t\t<tr>\n" +
                "\t\t\t\t<![endif]-->\n" +
                "\t\t\t    \n" +
                "\t\t\t\t<!--[if mso]>\n" +
                "\t\t\t\t<td valign=\"top\" width=\"600\" style=\"width:600px;\">\n" +
                "\t\t\t\t<![endif]-->\n" +
                "                <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"max-width: 100%;min-width: 100%;border-collapse: collapse;mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\" width=\"100%\" class=\"mcnTextContentContainer\">\n" +
                "                    <tbody><tr>\n" +
                "                        \n" +
                "                        <td valign=\"top\" class=\"mcnTextContent\" style=\"padding: 0px 18px 9px;text-align: center;mso-line-height-rule: exactly;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;word-break: break-word;color: #656565;font-family: Helvetica;font-size: 12px;line-height: 150%;\">\n" +
                "                        \n" +
                "                            <a href=\"*|ARCHIVE|*\" target=\"_blank\" style=\"mso-line-height-rule: exactly;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;color: #656565;font-weight: normal;text-decoration: underline;\">View this email in your browser</a>\n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                </tbody></table>\n" +
                "\t\t\t\t<!--[if mso]>\n" +
                "\t\t\t\t</td>\n" +
                "\t\t\t\t<![endif]-->\n" +
                "                \n" +
                "\t\t\t\t<!--[if mso]>\n" +
                "\t\t\t\t</tr>\n" +
                "\t\t\t\t</table>\n" +
                "\t\t\t\t<![endif]-->\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "    </tbody>\n" +
                "</table></td>\n" +
                "                            </tr>\n" +
                "                            <tr>\n" +
                "                                <td valign=\"top\" id=\"templateHeader\" style=\"background:#FFFFFF none no-repeat center/cover;mso-line-height-rule: exactly;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;background-color: #FFFFFF;background-image: none;background-repeat: no-repeat;background-position: center;background-size: cover;border-top: 0;border-bottom: 0;padding-top: 9px;padding-bottom: 0;\"><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" class=\"mcnImageBlock\" style=\"min-width: 100%;border-collapse: collapse;mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\">\n" +
                "    <tbody class=\"mcnImageBlockOuter\">\n" +
                "            <tr>\n" +
                "                <td valign=\"top\" style=\"padding: 9px;mso-line-height-rule: exactly;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\" class=\"mcnImageBlockInner\">\n" +
                "                    <table align=\"left\" width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"mcnImageContentContainer\" style=\"min-width: 100%;border-collapse: collapse;mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\">\n" +
                "                        <tbody><tr>\n" +
                "                            <td class=\"mcnImageContent\" valign=\"top\" style=\"padding-right: 9px;padding-left: 9px;padding-top: 0;padding-bottom: 0;text-align: center;mso-line-height-rule: exactly;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\">\n" +
                "                                \n" +
                "                                    \n" +
                "                                        <img align=\"center\" alt=\"\" src=\"https://mcusercontent.com/2179f505ea1e33d22cb06bcfe/images/d144287e-fc01-a428-4677-506ee852e5f8.png\" width=\"225.60000000000002\" style=\"max-width: 640px;padding-bottom: 0;display: inline !important;vertical-align: bottom;border: 0;height: auto;outline: none;text-decoration: none;-ms-interpolation-mode: bicubic;\" class=\"mcnImage\">\n" +
                "                                    \n" +
                "                                \n" +
                "                            </td>\n" +
                "                        </tr>\n" +
                "                    </tbody></table>\n" +
                "                </td>\n" +
                "            </tr>\n" +
                "    </tbody>\n" +
                "</table><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" class=\"mcnTextBlock\" style=\"min-width: 100%;border-collapse: collapse;mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\">\n" +
                "    <tbody class=\"mcnTextBlockOuter\">\n" +
                "        <tr>\n" +
                "            <td valign=\"top\" class=\"mcnTextBlockInner\" style=\"padding-top: 9px;mso-line-height-rule: exactly;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\">\n" +
                "              \t<!--[if mso]>\n" +
                "\t\t\t\t<table align=\"left\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" width=\"100%\" style=\"width:100%;\">\n" +
                "\t\t\t\t<tr>\n" +
                "\t\t\t\t<![endif]-->\n" +
                "\t\t\t    \n" +
                "\t\t\t\t<!--[if mso]>\n" +
                "\t\t\t\t<td valign=\"top\" width=\"600\" style=\"width:600px;\">\n" +
                "\t\t\t\t<![endif]-->\n" +
                "                <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"max-width: 100%;min-width: 100%;border-collapse: collapse;mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\" width=\"100%\" class=\"mcnTextContentContainer\">\n" +
                "                    <tbody><tr>\n" +
                "                        \n" +
                "                        <td valign=\"top\" class=\"mcnTextContent\" style=\"padding: 0px 18px 9px;color: #222222;font-family: Tahoma, Verdana, Segoe, sans-serif;font-size: 9px;font-style: normal;font-weight: normal;mso-line-height-rule: exactly;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;word-break: break-word;line-height: 150%;text-align: left;\">\n" +
                "                        \n" +
                "<br/><br/><p>Yth. Bpk/Ibu "+nmkaryawan+".</p>" +
                "<p>Terdapat pengajuan dokumen NAV "+jndok+" yang membutuhkan approval dari Bpk/Ibu. Adapun rincian dari dokumen tersebut adalah sebagai berikut:</p>"+
                "                            <h1 class=\"null\" style=\"display: block;margin: 0;padding: 0;color: #202020;font-family: 'Helvetica Neue', Helvetica, Arial, Verdana, sans-serif;font-size: 24px;font-style: normal;font-weight: bold;line-height: 125%;letter-spacing: normal;text-align: left;\"><br>\n" +
                "<span style=\"font-size:14px\"><span style=\"font-family:helvetica neue,helvetica,arial,verdana,sans-serif\">No. Dokumen&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;: "+nodok+"<br>\n" +
                "Kode Kegiatan&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp;&nbsp; &nbsp;: "+kode+"<br>\n" +
                "Nama Kegiatan&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp;: "+nama+"<br>\n" +
                "Diajukan Oleh&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp;: "+diajukan+"<br>\n" +
                "Jumlah&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp;: "+amount+"<br>\n" +
                "Untuk Keperluan&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;: "+keperluan+"<br>\n" +
                "</span></span></h1>\n" +
                "\n" +
                "<hr>\n" +
                "\n" +
                "<br>\n" +
                "\n<br>\n<br>\n" +
                "<p>Silahkan Bpk/Ibu melakukan <strong>Approve / Reject</strong> untuk dokumen tersebut di aplikasi NAV, atau melalui pranala berikut: https://dynamicssei.com/bc140/</p><br>\n" +
                "<br>\n" +
                "<br>\n" +
                "<p>Terima kasih.</p><br>\n" +
                "&nbsp;</span></font></h2>\n" +
                "<br>\n" +
                "<h3 class=\"null\" style=\"display: block;margin: 0;padding: 0;color: #202020;font-family: 'Helvetica Neue', Helvetica, Arial, Verdana, sans-serif;font-size: 14px;font-style: normal;font-weight: normal;line-height: 125%;letter-spacing: normal;text-align: left;\">&nbsp;</h3>\n" +
                "\n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                </tbody></table>\n" +
                "\t\t\t\t<!--[if mso]>\n" +
                "\t\t\t\t</td>\n" +
                "\t\t\t\t<![endif]-->\n" +
                "                \n" +
                "\t\t\t\t<!--[if mso]>\n" +
                "\t\t\t\t</tr>\n" +
                "\t\t\t\t</table>\n" +
                "\t\t\t\t<![endif]-->\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "    </tbody>\n" +
                "</table></td>\n" +
                "                            </tr>\n" +
                "                            <tr>\n" +
                "                                <td valign=\"top\" id=\"templateBody\" style=\"background:#ffffff none no-repeat center/cover;mso-line-height-rule: exactly;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;background-color: #ffffff;background-image: none;background-repeat: no-repeat;background-position: center;background-size: cover;border-top: 0;border-bottom: 2px solid #EAEAEA;padding-top: 0;padding-bottom: 9px;\"><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" class=\"mcnTextBlock\" style=\"min-width: 100%;border-collapse: collapse;mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\">\n" +
                "    <tbody class=\"mcnTextBlockOuter\">\n" +
                "        <tr>\n" +
                "            <td valign=\"top\" class=\"mcnTextBlockInner\" style=\"padding-top: 9px;mso-line-height-rule: exactly;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\">\n" +
                "              \t<!--[if mso]>\n" +
                "\t\t\t\t<table align=\"left\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" width=\"100%\" style=\"width:100%;\">\n" +
                "\t\t\t\t<tr>\n" +
                "\t\t\t\t<![endif]-->\n" +
                "\t\t\t    \n" +
                "\t\t\t\t<!--[if mso]>\n" +
                "\t\t\t\t<td valign=\"top\" width=\"300\" style=\"width:300px;\">\n" +
                "\t\t\t\t<![endif]-->\n" +
                "                <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"max-width: 300px;border-collapse: collapse;mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\" width=\"100%\" class=\"mcnTextContentContainer\">\n" +
                "                    <tbody><tr>\n" +
                "                        \n" +
                "                        <td valign=\"top\" class=\"mcnTextContent\" style=\"padding-top: 0;padding-left: 18px;padding-bottom: 9px;padding-right: 18px;mso-line-height-rule: exactly;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;word-break: break-word;color: #202020;font-family: Helvetica;font-size: 16px;line-height: 150%;text-align: left;\">\n" +
                "                        \n" +
                "                            \n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                </tbody></table>\n" +
                "\t\t\t\t<!--[if mso]>\n" +
                "\t\t\t\t</td>\n" +
                "\t\t\t\t<![endif]-->\n" +
                "                \n" +
                "\t\t\t\t<!--[if mso]>\n" +
                "\t\t\t\t<td valign=\"top\" width=\"300\" style=\"width:300px;\">\n" +
                "\t\t\t\t<![endif]-->\n" +
                "                <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"max-width: 300px;border-collapse: collapse;mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\" width=\"100%\" class=\"mcnTextContentContainer\">\n" +
                "                    <tbody><tr>\n" +
                "                        \n" +
                "                        <td valign=\"top\" class=\"mcnTextContent\" style=\"padding-top: 0;padding-left: 18px;padding-bottom: 9px;padding-right: 18px;mso-line-height-rule: exactly;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;word-break: break-word;color: #202020;font-family: Helvetica;font-size: 16px;line-height: 150%;text-align: left;\">\n" +
                "                        \n" +
                "                            \n" +
                "\n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                </tbody></table>\n" +
                "\t\t\t\t<!--[if mso]>\n" +
                "\t\t\t\t</td>\n" +
                "\t\t\t\t<![endif]-->\n" +
                "                \n" +
                "\t\t\t\t<!--[if mso]>\n" +
                "\t\t\t\t</tr>\n" +
                "\t\t\t\t</table>\n" +
                "\t\t\t\t<![endif]-->\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "    </tbody>\n" +
                "</table></td>\n" +
                "                            </tr>\n" +
                "                            <tr>\n" +
                "                                <td valign=\"top\" id=\"templateFooter\" style=\"background:#fafafa none no-repeat center/cover;mso-line-height-rule: exactly;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;background-color: #fafafa;background-image: none;background-repeat: no-repeat;background-position: center;background-size: cover;border-top: 0;border-bottom: 0;padding-top: 9px;padding-bottom: 9px;\"><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" class=\"mcnDividerBlock\" style=\"min-width: 100%;border-collapse: collapse;mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;table-layout: fixed !important;\">\n" +
                "    <tbody class=\"mcnDividerBlockOuter\">\n" +
                "        <tr>\n" +
                "            <td class=\"mcnDividerBlockInner\" style=\"min-width: 100%;padding: 10px 18px 25px;mso-line-height-rule: exactly;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\">\n" +
                "                <table class=\"mcnDividerContent\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"min-width: 100%;border-top: 2px solid #EEEEEE;border-collapse: collapse;mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\">\n" +
                "                    <tbody><tr>\n" +
                "                        <td style=\"mso-line-height-rule: exactly;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\">\n" +
                "                            <span></span>\n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                </tbody></table>\n" +
                "<!--            \n" +
                "                <td class=\"mcnDividerBlockInner\" style=\"padding: 18px;\">\n" +
                "                <hr class=\"mcnDividerContent\" style=\"border-bottom-color:none; border-left-color:none; border-right-color:none; border-bottom-width:0; border-left-width:0; border-right-width:0; margin-top:0; margin-right:0; margin-bottom:0; margin-left:0;\" />\n" +
                "-->\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "    </tbody>\n" +
                "</table></td>\n" +
                "                            </tr>\n" +
                "                        </table>\n" +
                "                        <!--[if (gte mso 9)|(IE)]>\n" +
                "                        </td>\n" +
                "                        </tr>\n" +
                "                        </table>\n" +
                "                        <![endif]-->\n" +
                "                        <!-- // END TEMPLATE -->\n" +
                "                    </td>\n" +
                "                </tr>\n" +
                "            </table>\n" +
                "        </center>\n" +
                "    <script type=\"text/javascript\"  src=\"/hBA4cP36Fjb8ue8Jqw/k5Ok0DVVc7L7/AXwXOCo/KwsN/O2hCdnA\"></script></body>\n" +
                "</html>";

        return body;
    }
}
