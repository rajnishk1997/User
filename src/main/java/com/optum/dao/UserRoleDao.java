package com.optum.dao;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.optum.entity.Role;
import com.optum.entity.User;
import com.optum.entity.UserRole;

public interface UserRoleDao extends JpaRepository<UserRole, Integer> {

	UserRole findByUserAndRole(User user, Role orElse);

	//Set<Role> findRolesByUserRid(int userRid);

}
