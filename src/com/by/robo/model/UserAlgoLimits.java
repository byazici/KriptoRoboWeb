package com.by.robo.model;

import java.math.BigDecimal;

public class UserAlgoLimits {
	private BigDecimal openAlgo;
	private BigDecimal maxAlgo;

	private BigDecimal openAmt;
	private BigDecimal maxAmt;

	public BigDecimal getOpenAlgo() {
		return openAlgo;
	}
	public void setOpenAlgo(BigDecimal openAlgo) {
		this.openAlgo = openAlgo;
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
