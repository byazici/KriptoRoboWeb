package com.by.robo.model;

import java.math.BigDecimal;
import java.util.Date;

public class OpenOrdersResult {
//	[
//	  {
//	    "id":"55b708549c8d054130d80d71",
//	    "datetime":"2015-07-28T04:43:00.271Z",
//	    "type":"SellBtc",
//	    "price":820.02,
//	    "amount":4.65915461
//	  },
//	  {
//	    "id":"55b9f6039c8d0530ac9926dd",
//	    "datetime":"2015-07-30T10:01:39.619Z",
//	    "type":"BuyBtc",
//	    "price":790.61,
//	    "amount":10.42124175
//	  },
//	]

	private String id;
	private Date datetime;
	private String type;
	private BigDecimal price;
	private BigDecimal amount;
	private String pairSymbol; 
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getPairSymbol() {
		return pairSymbol;
	}
	public void setPairSymbol(String pairSymbol) {
		this.pairSymbol = pairSymbol;
	}
	@Override
	public String toString() {
		return "OpenOrdersResult [id=" + id + ", datetime=" + datetime + ", type=" + type + ", price=" + price
				+ ", amount=" + amount + ", pairSymbol=" + pairSymbol + "]";
	}
}
