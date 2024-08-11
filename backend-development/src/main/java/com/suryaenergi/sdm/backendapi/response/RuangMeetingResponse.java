package com.suryaenergi.sdm.backendapi.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RuangMeetingResponse {
    private String message;
    private long id;
    private String name;
    private int capacity;
    private String description;
    private Long activeEventId;
    private String activeEventName;
}
