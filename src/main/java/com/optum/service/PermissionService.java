package com.optum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.optum.dao.PermissionDao;
import com.optum.entity.Permission;

@Service
public class PermissionService {

    @Autowired
    private PermissionDao permissionDao;

    public void initializePermissions() {
        // Example: Insert a permission if it doesn't exist
        Permission permission = permissionDao.findByPermissionName("USER_MANAGEMENT");
        if (permission == null) {
            permission = new Permission("USER_MANAGEMENT");
            permissionDao.save(permission);
        }
    }

	public Permission findById(Integer permissionId) {
		// TODO Auto-generated method stub
		return null;
	}
}

