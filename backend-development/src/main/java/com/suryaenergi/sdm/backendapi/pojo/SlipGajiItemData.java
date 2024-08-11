package com.suryaenergi.sdm.backendapi.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SlipGajiItemData {
    private long id;
    private String name;
    private int tahun;
    private int bulan;
    private Status status;
    private int revision;
    private LocalDateTime lastUpdate;
    private boolean canSend;

    public enum Status {
        EMPTY,
        FILLED, INCOMPLETE, FILLED_SOME
    }
}
