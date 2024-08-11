package com.suryaenergi.sdm.backendapi.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JatahCutiGroup {
    private long jumlah_cuti;
    private String employee_id;
    private int tahun;
}
