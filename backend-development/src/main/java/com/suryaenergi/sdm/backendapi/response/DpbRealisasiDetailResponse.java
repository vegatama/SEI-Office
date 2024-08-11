package com.suryaenergi.sdm.backendapi.response;

import com.suryaenergi.sdm.backendapi.pojo.DpbLineRealisasi;
import com.suryaenergi.sdm.backendapi.pojo.ProyekDpbDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DpbRealisasiDetailResponse {
    private String msg;
    private ProyekDpbDetail dpb_detail;
    private List<DpbLineRealisasi> dpb_lines;
}
