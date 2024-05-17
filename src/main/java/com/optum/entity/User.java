package com.optum.entity;

import javax.persistence.*;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "rx_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "u_rid")
    private int userRid;

    @Column(name = "u_username")
    private String userName;
    @Column(name = "u_first_name")
    private String userFirstName;
    @Column(name = "u_middle_name")
    private String userMiddleName;
    @Column(name = "u_last_name")
    private String userLastName;
    @Column(name = "u_full_name")
    private String userFullName;
    @Column(name = "u_password")
    private String userPassword;
    @Column(name = "u_mobile")
    private String userMobile;
    @Column(name = "u_email")
    private String userEmail;
    @Column(name = "u_designation")
    private String UserDesignation;
    @Column(name = "u_employee_id")
    private String userEmployeeId;
    @Column(name = "u_roleName")
    private String roleNames;
    @Column(name = "u_isactive")
    private boolean isActiveUser = false;
    
    @Column(name = "u_created_by")
    private Integer createdBy;

    @Column(name = "u_modified_by")
    private Integer modifiedBy;

    @Column(name = "u_create_datetime")
    private Date createdDate;

    @Column(name = "u_modify_datetime")
    private Date modifiedDate;

    //@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;
    
    private Integer currentUserId;

//    public int getId() {
//        return userRid;
//    }
//
//    public void setId(int id) {
//        this.userRid = id;
//    }

    public String getUserEmployeeId() {
		return userEmployeeId;
	}
    

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setUserEmployeeId(String userEmployeeId) {
		this.userEmployeeId = userEmployeeId;
	}

	public String getUserFirstName() {
        return userFirstName;
    }

    public String getUserMiddleName() {
		return userMiddleName;
	}

	public void setUserMiddleName(String userMiddleName) {
		this.userMiddleName = userMiddleName;
	}

	public String getUserMobile() {
		return userMobile;
	}

	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserDesignation() {
		return UserDesignation;
	}

	public void setUserDesignation(String userDesignation) {
		UserDesignation = userDesignation;
	}

	public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

	public int getUserRid() {
		return userRid;
	}

	public void setUserRid(int userRid) {
		this.userRid = userRid;
	}

	public String getUserFullName() {
		return userFullName;
	}

	public void setUserFullName(String userFullName) {
		this.userFullName = userFullName;
	}

	public String getRoleNames() {
		return roleNames;
	}

	public void setRoleNames(String roleNames) {
		this.roleNames = roleNames;
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

	public Integer getCurrentUserId() {
		return currentUserId;
	}

	public void setCurrentUserId(Integer currentUserId) {
		this.currentUserId = currentUserId;
	}

	public boolean isActiveUser() {
		return isActiveUser;
	}

	public void setActiveUser(boolean isActiveUser) {
		this.isActiveUser = isActiveUser;
	}
	
	
    
}