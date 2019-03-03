package com.by.robo.model;

import java.math.BigDecimal;
import java.util.Date;

public class UserTransactionsResult {
//	  {
//		    "id":"55c9b4ea3fbe186b4c089d09",
//		    "date":"2015-08-11T11:40:17.278",
//		    "operation":"buy",
//		    "btc":1.9449023,
//		    "currency":-1428.57,
//		    "price":734.52
//		  },

	private String id;
	private Date date;
	private String operation;
	private BigDecimal btc;
	private String currency;
	private BigDecimal price;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public BigDecimal getBtc() {
		return btc;
	}
	public void setBtc(BigDecimal btc) {
		this.btc = btc;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	@Override
	public String toString() {
		return "UserTransactionsResult [id=" + id + ", date=" + date + ", operation=" + operation + ", btc=" + btc
				+ ", currency=" + currency + ", price=" + price + "]";
	}
}
