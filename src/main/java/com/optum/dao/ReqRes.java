package com.optum.dao;

import com.optum.entity.JwtResponse;

public class ReqRes {
	
	 	private int statusCode;
	    private String error;
	    private String message;
	    
	    public ReqRes() {
	    }
	    
		public ReqRes(int statusCode, String error, String message) {
			super();
			this.statusCode = statusCode;
			this.error = error;
			this.message = message;
		}
		public ReqRes(String string, JwtResponse jwtResponse) {
			// TODO Auto-generated constructor stub
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
	    

}
