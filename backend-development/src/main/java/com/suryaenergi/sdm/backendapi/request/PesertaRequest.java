package com.suryaenergi.sdm.backendapi.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PesertaRequest {
    private String nama;
    private String bagian;
    private String email_phone;
    private String daftar_id;
}
