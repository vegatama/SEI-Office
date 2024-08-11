package com.suryaenergi.sdm.backendapi.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FrontEndURLService {

    @Value("${frontend.url}")
    // must not end with a slash
    private String frontEndURL;

    public String siteURL(String path) {
        // make sure the path does not start with a slash
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        return frontEndURL + "/" + path;
    }

    public String getAbsenRequestDetail(Long id) {
        return siteURL("absen/approved/" + id);
    }

    public String getIzinCutiDetailURL(long izinCutiId) {
        return siteURL("izincuti/detailstatuspengajuanizincuti/" + izinCutiId);
    }

    public String getVehicleOrderDetailNeedApproveURL(String vehicleOrderId) {
        return siteURL("order/detail/" + vehicleOrderId + "/carorderna");
    }

    public String getVehicleOrderDetailURL(String vehicleOrderId) {
        return siteURL("order/detail/" + vehicleOrderId);
    }

    public String getSlipGajiPDFURL(String employeeCode, long id) {
        return siteURL("slipgaji/pdf/" + employeeCode + "/" + id);
    }
}
