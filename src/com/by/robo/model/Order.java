package com.by.robo.model;

import java.math.BigDecimal;
import java.util.Date;

import com.by.robo.enums.BuySell;
import com.by.robo.enums.OrderStatus;
import com.by.robo.enums.PairSymbol;
import com.by.robo.enums.TrueFalse;

public class Order {
	
	private int id;
	private PairSymbol pairSymbol;
	private BuySell buySell;

	private Date createDate;
	private Date rlzDate;
	private Date cancelDate;
	
	private BigDecimal price;
	private BigDecimal amount;
	private BigDecimal total;
	private BigDecimal rlzTotal;
	
	private String tradeRef;
	private TrueFalse marketOrder;
	
	private OrderStatus status;
	private int algoId;
	
	private String descr;
	
	private int userId;
	
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Order(PairSymbol pairSymbol, BuySell buySell, Date createDate, BigDecimal price, BigDecimal amount,
			TrueFalse marketOrder, OrderStatus status, int algoId, String descr, int userId) {
		super();
		this.pairSymbol = pairSymbol;
		this.buySell = buySell;
		this.createDate = createDate;
		this.price = price;
		this.amount = amount;
		this.marketOrder = marketOrder;
		this.status = status;
		this.setAlgoId(algoId);
		this.setDescr(descr);
		this.setUserId(userId);
	}
	
	public Order() {
		//
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public PairSymbol getPairSymbol() {
		return pairSymbol;
	}
	public void setPairSymbol(PairSymbol pairSymbol) {
		this.pairSymbol = pairSymbol;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getRlzDate() {
		return rlzDate;
	}
	public void setRlzDate(Date rlzDate) {
		this.rlzDate = rlzDate;
	}
	public Date getCancelDate() {
		return cancelDate;
	}
	public void setCancelDate(Date cancelDate) {
		this.cancelDate = cancelDate;
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
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	public BigDecimal getRlzTotal() {
		return rlzTotal;
	}
	public void setRlzTotal(BigDecimal rlzTotal) {
		this.rlzTotal = rlzTotal;
	}
	public String getTradeRef() {
		return tradeRef;
	}
	public void setTradeRef(String tradeRef) {
		this.tradeRef = tradeRef;
	}
	public OrderStatus getStatus() {
		return status;
	}
	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public TrueFalse getMarketOrder() {
		return marketOrder;
	}
	public void setMarketOrder(TrueFalse marketOrder) {
		this.marketOrder = marketOrder;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", pairSymbol=" + pairSymbol + ", buySell=" + buySell + ", createDate=" + createDate
				+ ", rlzDate=" + rlzDate + ", cancelDate=" + cancelDate + ", price=" + price + ", amount=" + amount
				+ ", total=" + total + ", rlzTotal=" + rlzTotal + ", tradeRef=" + tradeRef + ", marketOrder="
				+ marketOrder + ", status=" + status + ", algoId=" + algoId + ", descr=" + descr + ", userId=" + userId
				+ "]";
	}

	public int getAlgoId() {
		return algoId;
	}


	public void setAlgoId(int algoId) {
		this.algoId = algoId;
	}


	public String getDescr() {
		return descr;
	}


	public void setDescr(String descr) {
		this.descr = descr;
	}


	public BuySell getBuySell() {
		return buySell;
	}


	public void setBuySell(BuySell buySell) {
		this.buySell = buySell;
	}
	
}
