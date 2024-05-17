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
@Table(name = "rx_user_role")
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ur_rid")
    private Integer userRoleRid;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "u_rid")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "r_rid")
    private Role role;
    
//    @Column(name = "u_rid")
//    private Integer userRid;
//
//    @Column(name = "r_rid")
//    private Integer roleRid;

    @Column(name = "ur_created_by")
    private Integer createdBy;

    @Column(name = "ur_modified_by")
    private Integer modifiedBy;

    @Column(name = "ur_create_datetime")
    private Date createdDate;

    @Column(name = "ur_modify_datetime")
    private Date modifiedDate;

//	public Integer getId() {
//		return userRoleRid;
//	}
//
//	public void setId(Integer id) {
//		this.userRoleRid = id;
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

	public Integer getUserRoleRid() {
		return userRoleRid;
	}

	public void setUserRoleRid(Integer userRoleRid) {
		this.userRoleRid = userRoleRid;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
	
	

//	public Integer getUserRid() {
//		return userRid;
//	}
//
//	public void setUserRid(Integer userRid) {
//		this.userRid = userRid;
//	}
//
//	public Integer getRoleRid() {
//		return roleRid;
//	}
//
//	public void setRoleRid(Integer roleRid) {
//		this.roleRid = roleRid;
//	}
	
	
    
    
}

