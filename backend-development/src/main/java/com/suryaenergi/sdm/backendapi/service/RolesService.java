package com.suryaenergi.sdm.backendapi.service;

import com.suryaenergi.sdm.backendapi.entity.Roles;
import com.suryaenergi.sdm.backendapi.pojo.RoleData;
import com.suryaenergi.sdm.backendapi.repository.RolesRepository;
import com.suryaenergi.sdm.backendapi.request.RoleAddRequest;
import com.suryaenergi.sdm.backendapi.request.RoleUpdateRequest;
import com.suryaenergi.sdm.backendapi.response.MessageResponse;
import com.suryaenergi.sdm.backendapi.response.RoleDetailResponse;
import com.suryaenergi.sdm.backendapi.response.RoleListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.suryaenergi.sdm.backendapi.pojo.Message.ERROR_MESSAGE;
import static com.suryaenergi.sdm.backendapi.pojo.Message.SUCCESS_MESSAGE;

@Service
public class RolesService {
    @Autowired
    private RolesRepository rolesRepository;


    public RoleDetailResponse getRoleById(String id) {
        RoleDetailResponse res = new RoleDetailResponse();

        try {
            Roles roles = rolesRepository.findByRoleId(id);
            if(roles == null)
                throw new Exception("ROLE NOT FOUND");
            res.setId(roles.getRoleId());
            res.setName(roles.getRoleName());
            res.setPermission(roles.getPermission());
            res.setDescription(roles.getRoleDescription());
            res.set_default(roles.isDefault());
            res.setMsg(SUCCESS_MESSAGE);
        }
        catch (Exception ex){
            res.setMsg(ERROR_MESSAGE + ex.getMessage());
        }

        return res;
    }

    public RoleDetailResponse addNewRole(RoleAddRequest req) {
        RoleDetailResponse res = new RoleDetailResponse();

        try {
            if(req.isRole_default()){
                Roles roleDefault = rolesRepository.findByIsDefault(true);
                if(roleDefault != null){
                    roleDefault.setDefault(false);
                    rolesRepository.save(roleDefault);
                }
            }

            Roles roles = new Roles();
            roles.setRoleId(UUID.randomUUID().toString());
            Roles cekid = rolesRepository.findByRoleId(roles.getRoleId());
            while(cekid != null){
                roles.setRoleId(UUID.randomUUID().toString());
                cekid = rolesRepository.findByRoleId(roles.getRoleId());
            }
            roles.setRoleName(req.getRole_name());
            roles.setRoleDescription(req.getRole_description());
            roles.setPermission(req.getPermission());
            roles.setDefault(req.isRole_default());
            rolesRepository.save(roles);

            res.setId(roles.getRoleId());
            res.setName(roles.getRoleName());
            res.setPermission(roles.getPermission());
            res.setDescription(roles.getRoleDescription());
            res.setMsg(SUCCESS_MESSAGE);
        }
        catch (Exception ex){
            res.setMsg(ERROR_MESSAGE + ex.getMessage());
        }

        return res;
    }

    public RoleListResponse getAllRole() {
        RoleListResponse res = new RoleListResponse();

        try{
            List<RoleData> roleDataList = new ArrayList<>();
            List<Roles> rolesList = (List<Roles>) rolesRepository.findAll();
            if(rolesList.size() > 0){
                for (Roles role: rolesList){
                    RoleData roleData = new RoleData();
                    roleData.setId(role.getRoleId());
                    roleData.setName(role.getRoleName());
                    roleData.setDescription(role.getRoleDescription());
                    roleData.setPermission(role.getPermission());
                    roleData.set_default(role.isDefault());
                    roleDataList.add(roleData);
                }
            }
            res.setMsg(SUCCESS_MESSAGE);
            res.setCount(rolesList.size());
            res.setRoles(roleDataList);
        }
        catch (Exception ex){
            res.setMsg(ERROR_MESSAGE + ex.getMessage());
        }

        return res;
    }

    public RoleDetailResponse updateRole(RoleUpdateRequest req) {
        RoleDetailResponse res = new RoleDetailResponse();

        try{
            if(req.is_default()){
                Roles roleDefault = rolesRepository.findByIsDefault(true);
                if(roleDefault != null){
                    roleDefault.setDefault(false);
                    rolesRepository.save(roleDefault);
                }
            }

            Roles roles = rolesRepository.findByRoleId(req.getRole_id());
            if(roles == null)
                throw new Exception("ROLE NOT FOUND");
            roles.setRoleName(req.getRole_name());
            roles.setRoleDescription(req.getRole_description());
            roles.setPermission(req.getPermission());
            roles.setDefault(req.is_default());
            rolesRepository.save(roles);

            res.setId(roles.getRoleId());
            res.setName(roles.getRoleName());
            res.setPermission(roles.getPermission());
            res.setDescription(roles.getRoleDescription());
            res.set_default(roles.isDefault());
            res.setMsg(SUCCESS_MESSAGE);
        }
        catch (Exception ex){
            res.setMsg(ERROR_MESSAGE + ex.getMessage());
        }

        return res;
    }

    public MessageResponse deleteRole(String id) {
        MessageResponse res = new MessageResponse();

        try {
            Roles roles = rolesRepository.findByRoleId(id);
            if(roles == null)
                throw new Exception("ROLE NOT FOUND");
            rolesRepository.delete(roles);
            res.setMsg(SUCCESS_MESSAGE);
        }
        catch (Exception ex){
            res.setMsg(ERROR_MESSAGE + ex.getMessage());
        }

        return res;
    }
}
