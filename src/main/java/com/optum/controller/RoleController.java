package com.optum.controller;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.optum.dao.ReqRes;
import com.optum.entity.Role;
import com.optum.service.RoleService;

@RestController
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping({"/createNewRole"})
    public Role createNewRole(@RequestBody Role role) {
        return roleService.createNewRole(role);
    }
    
    @Transactional
    @GetMapping({"/getAllRoles"})
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ReqRes> updateRole(@PathVariable Integer id, @RequestBody Role role) {
        try {
            roleService.updateRole(id, role);
            return ResponseEntity.ok(new ReqRes(HttpStatus.OK.value(), "", "Role updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ReqRes(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", "Failed to update role"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ReqRes> deleteRole(@PathVariable Integer id) {
        try {
            roleService.deleteRole(id);
            return ResponseEntity.ok(new ReqRes(HttpStatus.OK.value(), "", "Role deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ReqRes(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", "Failed to delete role"));
        }
    }
    
    
}
