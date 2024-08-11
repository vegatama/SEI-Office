package com.suryaenergi.sdm.backendapi.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDetailResponse {
    private String msg;
    private String id;
    private String name;
    private String description;
    private String permission;
    private boolean is_default;
}
