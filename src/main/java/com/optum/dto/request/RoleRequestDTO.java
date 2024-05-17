package com.optum.dto.request;

import java.util.Set;

public class RoleRequestDTO {
    private String roleName;
    private String roleDescription;
    private Set<String> permissionNames; // Assuming only permission names are required in requests
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getRoleDescription() {
		return roleDescription;
	}
	public void setRoleDescription(String roleDescription) {
		this.roleDescription = roleDescription;
	}
	public Set<String> getPermissionNames() {
		return permissionNames;
	}
	public void setPermissionNames(Set<String> permissionNames) {
		this.permissionNames = permissionNames;
	}

   
}

