package com.by.robo.model;

public class Result {
	private boolean success;
	private String errorCode;
	private String errorMsg;
	
	public Result(boolean s) {
		success = s;
	}

	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errprCode) {
		this.errorCode = errprCode;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

}
