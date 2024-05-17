package com.optum.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.optum.entity.*;

public interface PermissionDao extends JpaRepository<Permission, Integer> {

	Permission findByPermissionName(String string);
    // You can define custom query methods here if needed

	List<Permission> findAllByPermissionNameIn(List<String> asList);
}
