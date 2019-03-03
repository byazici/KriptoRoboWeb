package com.by.robo.model;

import java.math.BigDecimal;
import java.util.Date;

public class Tick {
	private BigDecimal last;
	private Long timestamp;
	private BigDecimal high;
	private Date date;
	private BigDecimal bid;
	private BigDecimal volume;
	private BigDecimal low;
	private BigDecimal ask;
	private BigDecimal open;
	private BigDecimal average;
	
	private String pair;
	
//	public Date getDate() {
//		return date;
//	}
	public void setDate(Date date) {
		this.date = date;
	}	
//	public BigDecimal getHigh() {
//		return high;
//	}
	public void setHigh(BigDecimal high) {
		this.high = high;
	}
	public BigDecimal getLast() {
		return last;
	}
	public void setLast(BigDecimal last) {
		this.last = last;
	}
	public Long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
//	public BigDecimal getBid() {
//		return bid;
//	}
	public void setBid(BigDecimal bid) {
		this.bid = bid;
	}
//	public BigDecimal getVolume() {
//		return volume;
//	}
	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}
//	public BigDecimal getLow() {
//		return low;
//	}
	public void setLow(BigDecimal low) {
		this.low = low;
	}
//	public BigDecimal getAsk() {
//		return ask;
//	}
	public void setAsk(BigDecimal ask) {
		this.ask = ask;
	}
//	public BigDecimal getOpen() {
//		return open;
//	}
	public void setOpen(BigDecimal open) {
		this.open = open;
	}
//	public BigDecimal getAverage() {
//		return average;
//	}
	public void setAverage(BigDecimal average) {
		this.average = average;
	}
	@Override
	public String toString() {
		return "Tick [last=" + last + ", timestamp=" + timestamp + ", high=" + high + ", date=" + date + ", bid=" + bid
				+ ", volume=" + volume + ", low=" + low + ", ask=" + ask + ", open=" + open + ", average=" + average
				+ ", pair=" + pair + "]";
	}
	public String getPair() {
		return pair;
	}
	public void setPair(String pair) {
		this.pair = pair;
	}
}
