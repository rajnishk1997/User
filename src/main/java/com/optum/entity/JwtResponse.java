package com.optum.entity;

import java.util.Set;

public class JwtResponse {

   // private User user;
	private int statusCode;
    private String error;
    private String message;
    private String userName;
    private String userEmail;
    private String jwtToken;
    private Integer currentUserRid;
    private Set<Role> roleNames;
    private Set<Permission> permissions;
    
//    public JwtResponse(int statusCode, String error, String message, String jwtToken, User user) {
//        this.statusCode = statusCode;
//        this.error = error;
//        this.message = message;
//        this.jwtToken = jwtToken;
//        this.userName = user.getUserName();
//        this.userEmail = user.getUserEmail();
//        this.currentUserRid=user.getUserRid();
//    }
    
    

    public int getStatusCode() {
		return statusCode;
	}

	public JwtResponse(int statusCode, String error, String message, String jwtToken,
		 Set<Role> roleNames, Set<Permission> permissions, User user) {
	super();
	this.statusCode = statusCode;
	this.error = error;
	this.message = message;
	this.jwtToken = jwtToken;
	this.roleNames = roleNames;
	this.permissions = permissions;
	this.userName = user.getUserName();
  this.userEmail = user.getUserEmail();
  this.currentUserRid=user.getUserRid();
}

	public Set<Role> getRoleNames() {
		return roleNames;
	}

	public void setRoleNames(Set<Role> roleNames) {
		this.roleNames = roleNames;
	}

	public Set<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getError() {
		return error;
	}

	public Integer getCurrentUserRid() {
		return currentUserRid;
	}

	public void setCurrentUserRid(Integer currentUserRid) {
		this.currentUserRid = currentUserRid;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}
