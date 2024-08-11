package com.suryaenergi.sdm.backendapi.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RuangMeetingData {
    private long id;
    private String name;
    private int capacity;
    private String description;
    private Long activeEventId;
    private String activeEventName;
    private LocalDateTime activeEventStart;
    private LocalDateTime activeEventEnd;
}
