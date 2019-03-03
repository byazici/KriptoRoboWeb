package com.by.robo.model;

import java.math.BigDecimal;

public class Account {
	private BigDecimal TRYBalance;
	private BigDecimal TRYBlockage;
	private BigDecimal TRYNetBalance;

	private BigDecimal BTCBalance;
	private BigDecimal BTCBlockage;
	private BigDecimal BTCNetBalance;

	private BigDecimal ETHBalance;
	private BigDecimal ETHBlockage;
	private BigDecimal ETHNetBalance;

	public Account(){
	}
	
	public BigDecimal getTRYBalance() {
		return TRYBalance;
	}

	public BigDecimal getBTCBalance() {
		return BTCBalance;
	}

	public BigDecimal getTRYBlockage() {
		return TRYBlockage;
	}

	public BigDecimal getBTCBlockage() {
		return BTCBlockage;
	}

	public void setTRYBalance(BigDecimal tRYBalance) {
		TRYBalance = tRYBalance;
	}

	public void setTRYBlockage(BigDecimal tRYBlockage) {
		TRYBlockage = tRYBlockage;
	}

	public void setBTCBalance(BigDecimal bTCBalance) {
		BTCBalance = bTCBalance;
	}

	public void setBTCBlockage(BigDecimal bTCBlockage) {
		BTCBlockage = bTCBlockage;
	}

	public BigDecimal getTRYNetBalance() {
		return TRYNetBalance;
	}

	public void setTRYNetBalance(BigDecimal tRYNetBalance) {
		TRYNetBalance = tRYNetBalance;
	}

	public BigDecimal getBTCNetBalance() {
		return BTCNetBalance;
	}

	public void setBTCNetBalance(BigDecimal bTCNetBalance) {
		BTCNetBalance = bTCNetBalance;
	}

	public BigDecimal getETHBalance() {
		return ETHBalance;
	}

	public void setETHBalance(BigDecimal eTHBalance) {
		ETHBalance = eTHBalance;
	}

	public BigDecimal getETHBlockage() {
		return ETHBlockage;
	}

	public void setETHBlockage(BigDecimal eTHBlockage) {
		ETHBlockage = eTHBlockage;
	}

	public BigDecimal getETHNetBalance() {
		return ETHNetBalance;
	}

	public void setETHNetBalance(BigDecimal eTHNetBalance) {
		ETHNetBalance = eTHNetBalance;
	}

	@Override
	public String toString() {
		return "Account [TRYBalance=" + TRYBalance + ", TRYBlockage=" + TRYBlockage + ", TRYNetBalance=" + TRYNetBalance
				+ ", BTCBalance=" + BTCBalance + ", BTCBlockage=" + BTCBlockage + ", BTCNetBalance=" + BTCNetBalance
				+ ", ETHBalance=" + ETHBalance + ", ETHBlockage=" + ETHBlockage + ", ETHNetBalance=" + ETHNetBalance
				+ "]";
	}
}
