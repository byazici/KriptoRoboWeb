package com.by.robo.model;

import java.math.BigDecimal;
import java.util.Date;

public class BalanceDaily {
	private Date tickDate;
	
	private BigDecimal tryBalance;
	private BigDecimal tryAmount;

	private BigDecimal btcBalance;
	private BigDecimal btcAmount;
	
	private BigDecimal ethBalance;
	private BigDecimal ethAmount;
	private BigDecimal overallBtc;
	
	public Date getTickDate() {
		return tickDate;
	}
	public void setTickDate(Date tickDate) {
		this.tickDate = tickDate;
	}
	public BigDecimal getTryBalance() {
		return tryBalance;
	}
	public void setTryBalance(BigDecimal tryBalance) {
		this.tryBalance = tryBalance;
	}
	public BigDecimal getTryAmount() {
		return tryAmount;
	}
	public void setTryAmount(BigDecimal tryAmount) {
		this.tryAmount = tryAmount;
	}
	public BigDecimal getBtcBalance() {
		return btcBalance;
	}
	public void setBtcBalance(BigDecimal btcBalance) {
		this.btcBalance = btcBalance;
	}
	public BigDecimal getBtcAmount() {
		return btcAmount;
	}
	public void setBtcAmount(BigDecimal btcAmount) {
		this.btcAmount = btcAmount;
	}
	public BigDecimal getEthBalance() {
		return ethBalance;
	}
	public void setEthBalance(BigDecimal ethBalance) {
		this.ethBalance = ethBalance;
	}
	public BigDecimal getEthAmount() {
		return ethAmount;
	}
	public void setEthAmount(BigDecimal ethAmount) {
		this.ethAmount = ethAmount;
	}
	@Override
	public String toString() {
		return "BalanceDaily [tickDate=" + tickDate + ", tryBalance=" + tryBalance + ", tryAmount=" + tryAmount
				+ ", btcBalance=" + btcBalance + ", btcAmount=" + btcAmount + ", ethBalance=" + ethBalance
				+ ", ethAmount=" + ethAmount + "]";
	}
	public BigDecimal getOverallBtc() {
		return overallBtc;
	}
	public void setOverallBtc(BigDecimal overallBtc) {
		this.overallBtc = overallBtc;
	}

}
