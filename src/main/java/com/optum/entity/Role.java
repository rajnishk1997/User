package com.optum.entity;

import javax.persistence.*;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "rx_role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "r_rid")
    private int roleRid;
    
    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "u_rid")
    private User user;

    @Column(name = "r_role_name")
    private String roleName;
    
    @Column(name = "r_role_description")
    private String roleDescription;


    //@OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Permission> permissions;
    
    @Column(name = "r_created_by")
    private Integer createdBy;

    @Column(name = "r_modified_by")
    private Integer modifiedBy;

    @Column(name = "r_create_datetime")
    private Date createdDate;

    @Column(name = "r_modify_datetime")
    private Date modifiedDate;

    public Role(String roleName2) {
		// TODO Auto-generated constructor stub
	}

	public Role() {
		// TODO Auto-generated constructor stub
	}

//	public int getRoleId() {
//        return roleRid;
//    }
//
//    public void setRoleId(int id) {
//        this.roleRid = id;
//    }

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

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

	public int getRoleRid() {
		return roleRid;
	}

	public void setRoleRid(int roleRid) {
		this.roleRid = roleRid;
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
    
	
    
}