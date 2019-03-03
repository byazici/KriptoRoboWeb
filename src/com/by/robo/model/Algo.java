package com.by.robo.model;

import java.math.BigDecimal;
import java.util.Date;

import com.by.robo.enums.AlgoStatus;
import com.by.robo.enums.PairSymbol;
import com.by.robo.enums.TrueFalse;

public class Algo {

	private int id;
	private String name;
	private PairSymbol pairSynbol;

	private Date createDate;
	private Date lastCreate;
	private Date expireDate;
	
	private BigDecimal maxAmount;
	private BigDecimal buyRate;
	private BigDecimal sellRate;
	private BigDecimal trigRate;
	
	private TrueFalse repeated;
	private int refId;

	private int duration; // hours
	private int priceDuration; // hours
	private AlgoStatus status;
	
	private BigDecimal buyPrice;
	private BigDecimal trigPrice;
	private BigDecimal sellPrice;
	private BigDecimal avgPrice;
	private BigDecimal basePrice;
	
	private String descr;
	
	private int buyId;
	private int sellId;	
	
	private int userId;
	private BigDecimal buyAmt;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Date getBuyRlzDate() {
		return buyRlzDate;
	}

	public void setBuyRlzDate(Date buyRlzDate) {
		this.buyRlzDate = buyRlzDate;
	}

	public Date getSellRlzDate() {
		return sellRlzDate;
	}

	public void setSellRlzDate(Date sellRlzDate) {
		this.sellRlzDate = sellRlzDate;
	}

	private BigDecimal profit; 
	
	private Date buyRlzDate;
	private Date sellRlzDate;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public PairSymbol getPairSynbol() {
		return pairSynbol;
	}

	public void setPairSynbol(PairSymbol pairSynbol) {
		this.pairSynbol = pairSynbol;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public BigDecimal getMaxAmount() {
		return maxAmount;
	}

	public void setMaxAmount(BigDecimal maxAmount) {
		this.maxAmount = maxAmount;
	}

	public BigDecimal getBuyRate() {
		return buyRate;
	}

	public void setBuyRate(BigDecimal buyRate) {
		this.buyRate = buyRate;
	}

	public BigDecimal getSellRate() {
		return sellRate;
	}

	public void setSellRate(BigDecimal sellRate) {
		this.sellRate = sellRate;
	}

	public BigDecimal getTrigRate() {
		return trigRate;
	}

	public void setTrigRate(BigDecimal trigRate) {
		this.trigRate = trigRate;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public AlgoStatus getStatus() {
		return status;
	}

	public void setStatus(AlgoStatus status) {
		this.status = status;
	}

	public TrueFalse getRepeated() {
		return repeated;
	}

	public void setRepeated(TrueFalse repeat) {
		this.repeated = repeat;
	}

	public Date getLastCreate() {
		return lastCreate;
	}

	public void setLastCreate(Date lastCreate) {
		this.lastCreate = lastCreate;
	}

	public int getPriceDuration() {
		return priceDuration;
	}

	public void setPriceDuration(int priceDuration) {
		this.priceDuration = priceDuration;
	}
	
	

	public BigDecimal getBuyPrice() {
		return buyPrice;
	}

	public void setBuyPrice(BigDecimal buyPrice) {
		this.buyPrice = buyPrice;
	}

	public BigDecimal getTrigPrice() {
		return trigPrice;
	}

	public void setTrigPrice(BigDecimal trigPrice) {
		this.trigPrice = trigPrice;
	}

	public BigDecimal getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(BigDecimal sellPrice) {
		this.sellPrice = sellPrice;
	}

	@Override
	public String toString() {
		return "Algo [id=" + id + ", name=" + name + ", pairSynbol=" + pairSynbol + ", createDate=" + createDate
				+ ", lastCreate=" + lastCreate + ", expireDate=" + expireDate + ", maxAmount=" + maxAmount
				+ ", buyRate=" + buyRate + ", sellRate=" + sellRate + ", trigRate=" + trigRate + ", repeated="
				+ repeated + ", refId=" + refId + ", duration=" + duration + ", priceDuration=" + priceDuration
				+ ", status=" + status + ", buyPrice=" + buyPrice + ", trigPrice=" + trigPrice + ", sellPrice="
				+ sellPrice + ", avgPrice=" + avgPrice + ", basePrice=" + basePrice + ", descr=" + descr + ", buyId="
				+ buyId + ", sellId=" + sellId + ", userId=" + userId + ", buyAmt=" + buyAmt + ", profit=" + profit
				+ ", buyRlzDate=" + buyRlzDate + ", sellRlzDate=" + sellRlzDate + "]";
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public int getBuyId() {
		return buyId;
	}

	public void setBuyId(int buyId) {
		this.buyId = buyId;
	}

	public int getSellId() {
		return sellId;
	}

	public void setSellId(int sellId) {
		this.sellId = sellId;
	}

	public BigDecimal getAvgPrice() {
		return avgPrice;
	}

	public void setAvgPrice(BigDecimal avgPrice) {
		this.avgPrice = avgPrice;
	}

	public int getRefId() {
		return refId;
	}

	public void setRefId(int refId) {
		this.refId = refId;
	}

	public BigDecimal getProfit() {
		return profit;
	}

	public void setProfit(BigDecimal profit) {
		this.profit = profit;
	}

	public BigDecimal getBuyAmt() {
		return buyAmt;
	}

	public void setBuyAmt(BigDecimal buyAmt) {
		this.buyAmt = buyAmt;
	}

	public BigDecimal getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(BigDecimal basePrice) {
		this.basePrice = basePrice;
	}
}
