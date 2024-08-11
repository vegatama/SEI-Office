package com.suryaenergi.sdm.backendapi.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ViewEventData {
    private LocalDateTime start;
    private LocalDateTime end;
    private String subyek;
    private String kegiatan;
    private String pimpinan;
}
