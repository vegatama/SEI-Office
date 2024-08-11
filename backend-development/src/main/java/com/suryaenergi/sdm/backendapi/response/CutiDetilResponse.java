package com.suryaenergi.sdm.backendapi.response;

import com.suryaenergi.sdm.backendapi.pojo.DetilCuti;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CutiDetilResponse {
    private String msg;
    private String employee_code;
    private String name;
    private int jumlah_data;
    private List<DetilCuti> data;
}
