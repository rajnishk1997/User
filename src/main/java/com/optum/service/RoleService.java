package com.optum.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.optum.dao.RoleDao;
import com.optum.entity.Role;

@Service
public class RoleService {

    @Autowired
    private RoleDao roleRepository;

    public Role createNewRole(Role role) {
    	// Extract roleName from the incoming Role object
        String roleName = role.getRoleName();

        // Set the roleName to the incoming Role object (optional)
        role.setRoleName(roleName);

        return roleRepository.save(role);
    }

    public void updateRole(Integer id, Role role) {
        Role existingRole = roleRepository.findById(id).orElseThrow(() -> new RuntimeException("Role not found"));
        existingRole.setRoleName(role.getRoleName());
        // Update other properties if needed
        roleRepository.save(existingRole);
    }

    public void deleteRole(Integer id) {
        Role existingRole = roleRepository.findById(id).orElseThrow(() -> new RuntimeException("Role not found"));
        roleRepository.delete(existingRole);
    }
    
    @Transactional
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
}
