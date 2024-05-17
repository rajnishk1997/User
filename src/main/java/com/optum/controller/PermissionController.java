package com.optum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.optum.dao.PermissionDao;
import com.optum.dao.ReqRes;
import com.optum.entity.Permission;
import com.optum.entity.ResponseWrapper;
import com.optum.service.PermissionService;

@RestController
@RequestMapping("/permissions")
public class PermissionController {
	 private final PermissionDao permissionRepository;
    @Autowired
    private PermissionService permissionService;
    
   

    @Autowired
    public PermissionController(PermissionDao permissionRepository) {
        this.permissionRepository = permissionRepository;
    }


    @PostMapping("/initialize")
    public ResponseEntity<String> initializePermissions() {
        try {
            permissionService.initializePermissions();
            return ResponseEntity.ok("Permissions initialized successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to initialize permissions: " + e.getMessage());
        }
    }
    
    
    @PostMapping("/add")
    public ResponseEntity<ResponseWrapper<String>> addPermission(@RequestParam String permissionName) {
        Permission existingPermission = permissionRepository.findByPermissionName(permissionName);
        if (existingPermission != null) {
            ReqRes reqRes = new ReqRes(HttpStatus.BAD_REQUEST.value(), "Permission already exists", "");
            return ResponseEntity.badRequest().body(new ResponseWrapper<>(null, reqRes));
        }

        Permission newPermission = new Permission(permissionName);
        permissionRepository.save(newPermission);
        ReqRes reqRes = new ReqRes(HttpStatus.OK.value(), "", "Permission added successfully");
        return ResponseEntity.ok(new ResponseWrapper<>("Permission added successfully", reqRes));
    }

    @PostMapping("/remove")
    public ResponseEntity<ResponseWrapper<String>> removePermission(@RequestParam String permissionName) {
        Permission existingPermission = permissionRepository.findByPermissionName(permissionName);
        if (existingPermission == null) {
            ReqRes reqRes = new ReqRes(HttpStatus.BAD_REQUEST.value(), "Permission does not exist", "");
            return ResponseEntity.badRequest().body(new ResponseWrapper<>(null, reqRes));
        }

        permissionRepository.delete(existingPermission);
        ReqRes reqRes = new ReqRes(HttpStatus.OK.value(), "", "Permission removed successfully");
        return ResponseEntity.ok(new ResponseWrapper<>("Permission removed successfully", reqRes));
    }
}

