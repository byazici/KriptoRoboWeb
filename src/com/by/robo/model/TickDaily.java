package com.by.robo.model;

import java.math.BigDecimal;
import java.util.Date;

public class TickDaily {
	Date TickDate;
	String pair;
	BigDecimal open;
	BigDecimal high;
	BigDecimal low;
	BigDecimal close;
	BigDecimal average;

	public Date getTickDate() {
		return TickDate;
	}
	public void setTickDate(Date tickDate) {
		TickDate = tickDate;
	}
	public String getPair() {
		return pair;
	}
	public void setPair(String pair) {
		this.pair = pair;
	}
	public BigDecimal getOpen() {
		return open;
	}
	public void setOpen(BigDecimal open) {
		this.open = open;
	}
	public BigDecimal getHigh() {
		return high;
	}
	public void setHigh(BigDecimal high) {
		this.high = high;
	}
	public BigDecimal getLow() {
		return low;
	}
	public void setLow(BigDecimal low) {
		this.low = low;
	}
	public BigDecimal getClose() {
		return close;
	}
	public void setClose(BigDecimal close) {
		this.close = close;
	}
	public BigDecimal getAverage() {
		return average;
	}
	public void setAverage(BigDecimal average) {
		this.average = average;
	}
}
