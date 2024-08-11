package com.suryaenergi.sdm.backendapi.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DpbLineRealisasi {
    private String item_no;
    private String item_description;
    private String qty;
    private String unit_of_measure;
    private String direct_unit_cost;
    private String amount;
    private String amount_inc_vat;
    private String vat_percent;
    private String unit_cost_lcy;
    private List<PODetail> purchases;
}
