package com.suryaenergi.sdm.backendapi.response;

import com.suryaenergi.sdm.backendapi.pojo.ViewEventData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class ViewRuangMeetingResponse {
    private String message;
    private long id;
    private String name;
    private int capacity;
    private String description;
    private List<ViewEventData> events; // ACTIVE EVENTS ONLY FOR SPECIFIC DATE
}
