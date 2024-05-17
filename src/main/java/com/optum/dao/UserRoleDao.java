package com.optum.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.optum.entity.UserRole;

public interface UserRoleDao extends JpaRepository<UserRole, Integer> {

}
