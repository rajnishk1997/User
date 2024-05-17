package com.optum.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
@Entity
@Table(name = "rx_permission")
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "p_rid")
    private int permissionRid;
    
    @ManyToOne
    @JoinColumn(name = "r_rid")
    private Role role;

    @Column(name = "p_permission_name")
    private String permissionName;
    
    @Column(name = "p_created_by")
    private Integer createdBy;

    @Column(name = "p_modified_by")
    private Integer modifiedBy;

    @Column(name = "p_create_datetime")
    private Date createdDate;

    @Column(name = "p_modify_datetime")
    private Date modifiedDate;

    
    // Constructors
    public Permission() {
    }

    public Permission(String permissionName) {
        this.permissionName = permissionName;
    }

   // Getters and Setters
//    public int getId() {
//        return permissionRid;
//    }
//
//    public void setId(int id) {
//        this.permissionRid = id;
//    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

	public int getPermissionRid() {
		return permissionRid;
	}

	public void setPermissionRid(int permissionRid) {
		this.permissionRid = permissionRid;
	}

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

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
    
    
}