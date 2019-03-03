package com.by.robo.model;

import java.math.BigDecimal;
import java.util.Date;

public class OrderResult {
	private String orderId;
	private Date datetime;
	private String type;
	private BigDecimal price;
	private BigDecimal amount;
	private boolean success;
	private String errMsg;
	private int dbId;

	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String id) {
		this.orderId = id;
	}
	public Date getDatetime() {
		return datetime;
	}
	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	@Override
	public String toString() {
		return "OrderResult [orderId=" + orderId + ", datetime=" + datetime + ", type=" + type + ", price=" + price
				+ ", amount=" + amount + "]";
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	public int getDbId() {
		return dbId;
	}
	public void setDbId(int dbId) {
		this.dbId = dbId;
	}
}
