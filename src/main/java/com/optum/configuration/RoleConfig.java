package com.optum.configuration;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import com.optum.dao.PermissionDao;
import com.optum.dao.RoleDao;
import com.optum.entity.Role;
import com.optum.entity.Permission;

public class RoleConfig {
	private final RoleDao roleRepository;
	 private final PermissionDao permissionRepository;

    // Inject role names from properties file using @Value annotation
    @Value("${role}")
    private String roleNames;

    // Inject RoleRepository
    @Autowired
    public RoleConfig(RoleDao roleRepository, PermissionDao permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    // Initialize roles on application startup
    @PostConstruct
    @Transactional
    public void init() {
    	// Split the role names string into a list
    	List<String> roles = Arrays.asList(roleNames.split(","));

    	// Check if each role exists in the repository, if not, create and save it
    	for (String roleName : roles) {
    	    Optional<Role> roleOptional = roleRepository.findByRoleName(roleName);
    	    if (!roleOptional.isPresent()) {
    	        Role role = new Role(roleName);
    	        roleRepository.save(role);
    	    }
    	}

    }
    
    
    public Role getAdminRole() {
        return roleRepository.findById(1) // I've used findById(1) assuming the ID of the Admin role is 1.
                .orElseThrow(() -> new RuntimeException("Admin role not found"));
    }
    
    @PostConstruct
    public void assignAllPermissionsToAdminRole() {
        Role adminRole = getAdminRole();
        Set<Permission> allPermissions = new HashSet<>(permissionRepository.findAll());
        adminRole.setPermissions(allPermissions);
        roleRepository.save(adminRole);
    }
}
