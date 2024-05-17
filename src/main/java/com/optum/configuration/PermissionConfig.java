package com.optum.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.Transactional;

import com.optum.dao.PermissionDao;
import com.optum.entity.Permission;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

@Configuration
@PropertySource({"classpath:application.properties", "classpath:permission.properties"})
public class PermissionConfig {

    private final PermissionDao permissionRepository;

    @Value("${permissions}")
    private String permissions;

    public PermissionConfig(PermissionDao permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @PostConstruct
    @Transactional
    public void init() {
        List<String> permissionNames = Arrays.asList(permissions.split(", "));
        for (String permissionName : permissionNames) {
            Permission permission = permissionRepository.findByPermissionName(permissionName);
            if (permission == null) {
                permission = new Permission(permissionName);
                permissionRepository.save(permission);
            }
        }
    }
}

