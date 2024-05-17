package com.optum.entity;

public class RegistrationResponse<T> {

	
	private int statusCode;
    private String error;
    private String message;
    private String userName; 
    private String password;
	public RegistrationResponse(int statusCode, String error, String message, String userName, String password) {
		super();
		this.statusCode = statusCode;
		this.error = error;
		this.message = message;
		this.userName = userName;
		this.password = password;
	}
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public String getError() {
		return error;
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
    
    
}
