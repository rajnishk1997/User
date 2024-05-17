package com.optum.dao;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import com.optum.entity.RolePermission;

public interface RolePermissionDao extends JpaRepository<RolePermission, Integer>  {

	//Set<RolePermission> findByRoleId(int roleRid);
	 Set<RolePermission> findByRoleRoleRid(int roleRid);

	

	

}
