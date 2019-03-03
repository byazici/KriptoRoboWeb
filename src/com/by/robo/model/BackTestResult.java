package com.by.robo.model;

import java.math.BigDecimal;

public class BackTestResult {
	
	private int buyOrderCount;
	private int sellOrderCount;
	
	private BigDecimal sumProfit;
	private BigDecimal sumCommAmt;
	private BigDecimal sumProfitNet;

	
	public int getBuyOrderCount() {
		return buyOrderCount;
	}



	public void setBuyOrderCount(int buyOrderCount) {
		this.buyOrderCount = buyOrderCount;
	}



	public int getSellOrderCount() {
		return sellOrderCount;
	}



	public void setSellOrderCount(int sellOrderCount) {
		this.sellOrderCount = sellOrderCount;
	}



	public BigDecimal getSumProfit() {
		return sumProfit;
	}



	public void setSumProfit(BigDecimal sumProfit) {
		this.sumProfit = sumProfit;
	}



	public BigDecimal getSumCommAmt() {
		return sumCommAmt;
	}



	public void setSumCommAmt(BigDecimal sumCommAmt) {
		this.sumCommAmt = sumCommAmt;
	}



	public BigDecimal getSumProfitNet() {
		return sumProfitNet;
	}



	public void setSumProfitNet(BigDecimal sumProfitNet) {
		this.sumProfitNet = sumProfitNet;
	}



	@Override
	public String toString() {
		return "BackTestResult [buyOrderCount=" + buyOrderCount + ", sellOrderCount=" + sellOrderCount + ", sumProfit="
				+ sumProfit + ", sumCommAmt=" + sumCommAmt + ", sumProfitNet=" + sumProfitNet + "]";
	}
}
