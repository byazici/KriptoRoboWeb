package com.by.robo.model;

import com.by.robo.enums.TokenStatus;

public class UserToken {
	private int id;
	private int userId;
	private String token;
	private 	TokenStatus status;
	
	public UserToken() {
		
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public TokenStatus getStatus() {
		return status;
	}
	public void setStatus(TokenStatus status) {
		this.status = status;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	
	
}
