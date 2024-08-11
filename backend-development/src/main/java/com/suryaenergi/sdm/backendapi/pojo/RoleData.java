package com.suryaenergi.sdm.backendapi.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleData {
    private String id;
    private String name;
    private String description;
    private String permission;
    private boolean is_default;
}
