package com.optum.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "rx_role_permission")
public class RolePermission {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "rp_rid")
    private int rolePermissionRid;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "r_rid")
    private Role role;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "p_rid")
    private Permission permission;

//	 @Column(name = "r_rid")
//	    private int roleId;
//	 
//	 @Column(name = "p_rid")
//	    private int permissionId;
    
    @Column(name = "rp_created_by")
    private Integer createdBy;

    @Column(name = "rp_modified_by")
    private Integer modifiedBy;

    @Column(name = "rp_create_datetime")
    private Date createdDate;

    @Column(name = "rp_modify_datetime")
    private Date modifiedDate;

//	public int getId() {
//		return rolePermissionRid;
//	}

//	public void setId(int id) {
//		this.rolePermissionRid = id;
//	}
	

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(Integer modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public int getRolePermissionRid() {
		return rolePermissionRid;
	}

	public void setRolePermissionRid(int rolePermissionRid) {
		this.rolePermissionRid = rolePermissionRid;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Permission getPermission() {
		return permission;
	}

	public void setPermission(Permission permission) {
		this.permission = permission;
	}
	
	

//	public int getRoleId() {
//		return roleId;
//	}
//
//	public void setRoleId(int roleId) {
//		this.roleId = roleId;
//	}
//
//	public int getPermissionId() {
//		return permissionId;
//	}
//
//	public void setPermissionId(int permissionId) {
//		this.permissionId = permissionId;
//	}
	
	

    

}
