package com.suryaenergi.sdm.backendapi.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PODetail {
    private String po_number;
    private String po_project_code;
    private String po_date;
    private String po_qty;
    private String po_status;
    private String po_unit_of_measure;
    private String po_description;
    private String po_direct_unit_cost;
    private String po_vat_percent;
    private String po_amount;
    private String po_amount_include_vat;
    private String po_unit_cost_lcy;
    private String po_buy_from;
    private String po_amount_lcy = "0";
}
