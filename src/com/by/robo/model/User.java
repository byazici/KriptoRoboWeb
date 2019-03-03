package com.by.robo.model;

import java.math.BigDecimal;
import java.util.Date;

import com.by.robo.enums.UserStatus;

public class User {
	private String firstName;
	private String LastName;
	private String userName;
	
	private int id;
	private String userMail;
	private String userPass;

	private String publicKey;
	private String privateKey;
	private int roles;
	private UserStatus status;
	private int wrongLogin;
	private Date lastLogin;
	
	private String tckn;
	private String birthYear;
	
	private int maxAlgo;
	private BigDecimal maxAmt;
	
	public int getMaxAlgo() {
		return maxAlgo;
	}
	public void setMaxAlgo(int maxAlgo) {
		this.maxAlgo = maxAlgo;
	}
	public BigDecimal getMaxAmt() {
		return maxAmt;
	}
	public void setMaxAmt(BigDecimal maxAmt) {
		this.maxAmt = maxAmt;
	}
	public String getTckn() {
		return tckn;
	}
	public void setTckn(String tckn) {
		this.tckn = tckn;
	}
	public String getBirthYear() {
		return birthYear;
	}
	public void setBirthYear(String birthYear) {
		this.birthYear = birthYear;
	}
	public Date getLastLogin() {
		return lastLogin;
	}
	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}
	public int getWrongLogin() {
		return wrongLogin;
	}
	public void setWrongLogin(int wrongLogin) {
		this.wrongLogin = wrongLogin;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return LastName;
	}
	public void setLastName(String lastName) {
		LastName = lastName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserMail() {
		return userMail;
	}
	public void setUserMail(String userMail) {
		this.userMail = userMail;
	}
	public String getPublicKey() {
		return publicKey;
	}
	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}
	public String getPrivateKey() {
		return privateKey;
	}
	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}
	public int getRoles() {
		return roles;
	}
	public void setRoles(int roles) {
		this.roles = roles;
	}
	public UserStatus getStatus() {
		return status;
	}
	public void setStatus(UserStatus status) {
		this.status = status;
	}
	public String getUserPass() {
		return userPass;
	}
	public void setUserPass(String userPass) {
		this.userPass = userPass;
	}
	
	@Override
	public String toString() {
		return "User [firstName=" + firstName + ", LastName=" + LastName + ", userName=" + userName + ", id=" + id
				+ ", userMail=" + userMail + ", publicKey=" + publicKey + ", roles=" + roles + ", status=" + status
				+ ", wrongLogin=" + wrongLogin + ", lastLogin=" + lastLogin + "]";
	}

}
