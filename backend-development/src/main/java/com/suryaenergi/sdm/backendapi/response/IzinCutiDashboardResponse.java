package com.suryaenergi.sdm.backendapi.response;

import com.suryaenergi.sdm.backendapi.pojo.IzinCutiData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IzinCutiDashboardResponse {
    private String message;
    private long akumulasiIzin;
    private int sisaCuti;
    private IzinCutiData cutiAktif;
}
