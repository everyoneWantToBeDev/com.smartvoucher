package com.smartvoucher.webEcommercesmartvoucher.controller;

import com.smartvoucher.webEcommercesmartvoucher.dto.RolesUsersDTO;
import com.smartvoucher.webEcommercesmartvoucher.payload.ResponseObject;
import com.smartvoucher.webEcommercesmartvoucher.service.IRoleUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/role_user")
public class RoleUserController {
    private final IRoleUserService roleUserService;

    @Autowired
    public RoleUserController(final IRoleUserService roleUserService) {
        this.roleUserService = roleUserService;
    }

    @PostMapping("/api/insert")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<ResponseObject> insertRoleUser(@RequestBody RolesUsersDTO rolesUsersDTO){
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject(
                        200,
                        "update is completed",
                        roleUserService.insert(rolesUsersDTO)
                )
        );
    }

    @DeleteMapping("/api/delete")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<ResponseObject> delete(@RequestBody RolesUsersDTO rolesUsersDTO){
        this.roleUserService.delete(rolesUsersDTO);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject(
                        200,
                        "update is completed",
                        ""
                )
        );
    }

}