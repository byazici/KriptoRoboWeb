package com.by.robo.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.math.BigDecimal;
import java.util.Set;

import org.junit.After;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.by.robo.enums.BuySell;
import com.by.robo.enums.PairSymbol;
import com.by.robo.enums.TrueFalse;
import com.by.robo.helper.BtcTurkHelper;
import com.by.robo.model.OpenOrdersResult;
import com.by.robo.model.Order;
import com.by.robo.model.OrderResult;
import com.by.robo.model.Tick;
import com.by.robo.model.UserTransactionsResult;

public class BtcHelperTest {
	@Test
	public void testAccountBalance() {
//
//	 Account account = BTCTurkApi.AccountBalance();
//	 assertNotNull(account);
//	 assertNotNull(account.getTRYBalance());
//	 assertNotNull(account.getTRYBlockage());
//	 assertNotNull(account.getTRYNetBalance());
//
//	 assertNotNull(account.getBTCBalance());
//	 assertNotNull(account.getBTCBlockage());
//	 assertNotNull(account.getBTCNetBalance());
	}
	
	@After
	public void justWait(){
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetTick() {
		BtcTurkHelper h = new BtcTurkHelper();
		Tick t = h.getTick(PairSymbol.BTCTRY);
		
		assertNotNull(t);
		assertNotNull(t.getPair());
		assertNotNull(t.getLast());
		assertNotEquals(t.getLast(),  BigDecimal.ZERO);
	}
	
	@Test
	public void testUserTransactions() {
		BtcTurkHelper h = new BtcTurkHelper();
		Set<UserTransactionsResult> set = h.userTransactions(1);
		assertNotNull(h);
		assertNotEquals(set.size(), 0);
	}
	
	@Test
	public void testBuyOrder() {
		BtcTurkHelper h = new BtcTurkHelper();
		Order o = new Order();

		o.setBuySell(BuySell.BUY);
		o.setPairSymbol(PairSymbol.BTCTRY);
		o.setMarketOrder(TrueFalse.FALSE);
		o.setPrice(new BigDecimal("1000"));
		o.setAmount(new BigDecimal("0.001"));

		OrderResult or = h.buyOrder(o);

		assertNotNull(or);
		assertNotNull(or.getOrderId());
		
		Set<OpenOrdersResult> oo = h.openOrders(1, PairSymbol.BTCTRY);
		assertNotNull(oo);
		assertNotEquals(oo.size(), 0);

		String id = or.getOrderId();
		o.setTradeRef(id);
		boolean result = h.cancelOrder(o);
		assertTrue(result);
	}
	
	@Test
	public void testSellOrder() {
		BtcTurkHelper h = new BtcTurkHelper();
		Order o = new Order();

		o.setBuySell(BuySell.SELL);
		o.setPairSymbol(PairSymbol.BTCTRY);
		o.setMarketOrder(TrueFalse.FALSE);
		o.setPrice(new BigDecimal("500000"));
		o.setAmount(new BigDecimal("0.001"));
		OrderResult or = h.sellOrder(o);

		assertNotNull(or);
		assertNotNull(or.getOrderId());
	
		String id = or.getOrderId();
		o.setTradeRef(id);
		boolean result = h.cancelOrder(o);
		assertTrue(result);
	}

	@Test
	@Disabled
	public void testBuyLimitAndCancel() {
		
		//Order newBuyOrder = new Order();
		//newBuyOrder.setIsMarketOrder(false);
		//newBuyOrder.setPrice(new BigDecimal("7000"));
		//newBuyOrder.setAmount(new BigDecimal("0.001"));
		//OrderResult newOrderResult = BTCTurkApi.buyOrder(newBuyOrder);
		//if (newOrderResult != null) System.out.println("Buy Order:" + newOrderResult.toString());

		//Order o = new Order();
		//o.setOrderID("205024");
		//System.out.println("Is Cancelled: " + BTCTurkApi.cancelOrder(o));		
	}

	@Test
	@Disabled

	public void testBuyMarketAndCancel() {
	}

	@Test
	@Disabled
	public void testSellLimitAndCancel() {
		//Order newSellOrder = new Order();
		//newSellOrder.setIsMarketOrder(false);
		//newSellOrder.setPrice(new BigDecimal("100000"));
		//newSellOrder.setAmount(new BigDecimal("0.0001"));
		//OrderResult newOrderResult = BTCTurkApi.sellOrder(newSellOrder);
		//if (newOrderResult != null) System.out.println("Sell Order:" + newOrderResult.toString());
	}

	@Test
	@Disabled
	public void testOpenOrders() {
		//Set<OpenOrdersResult> orderSet = BTCTurkApi.openOrders();
		//System.out.println("open orders : " + orderSet.size());
		//OpenOrdersResult ord0 = (OpenOrdersResult) orderSet.toArray()[0];
		//System.out.println("open order 0 : " + ord0.toString());		
	}
}
