package com.by.robo.model;

import java.math.BigDecimal;

public class UserStats {
	private BigDecimal rlzAlgo;
	private BigDecimal openAlgo;
	private BigDecimal profit;
	private BigDecimal rate;
	private BigDecimal maxAlgo;
	private BigDecimal maxAmt;
	private BigDecimal openAmt;
	public BigDecimal getRlzAlgo() {
		return rlzAlgo;
	}
	public void setRlzAlgo(BigDecimal rlzAlgo) {
		this.rlzAlgo = rlzAlgo;
	}
	public BigDecimal getOpenAlgo() {
		return openAlgo;
	}
	public void setOpenAlgo(BigDecimal openAlgo) {
		this.openAlgo = openAlgo;
	}
	public BigDecimal getProfit() {
		return profit;
	}
	public void setProfit(BigDecimal profit) {
		this.profit = profit;
	}
	public BigDecimal getRate() {
		return rate;
	}
	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}
	public BigDecimal getMaxAlgo() {
		return maxAlgo;
	}
	public void setMaxAlgo(BigDecimal maxAlgo) {
		this.maxAlgo = maxAlgo;
	}
	public BigDecimal getMaxAmt() {
		return maxAmt;
	}
	public void setMaxAmt(BigDecimal maxAmt) {
		this.maxAmt = maxAmt;
	}
	public BigDecimal getOpenAmt() {
		return openAmt;
	}
	public void setOpenAmt(BigDecimal openAmt) {
		this.openAmt = openAmt;
	}

}
