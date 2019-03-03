package com.by.robo.helper;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.by.robo.dao.AlgoDao;
import com.by.robo.dao.OrderDao;
import com.by.robo.dao.UserDao;
import com.by.robo.enums.BuySell;
import com.by.robo.enums.OrderStatus;
import com.by.robo.enums.PairSymbol;
import com.by.robo.model.Algo;
import com.by.robo.model.OpenOrdersResult;
import com.by.robo.model.Order;
import com.by.robo.model.OrderResult;
import com.by.robo.model.User;
import com.by.robo.server.AlgoServer;

public class OrderHelper {
	final static Logger logger = LoggerFactory.getLogger(OrderHelper.class);

	public boolean cancelOrder(Order order) {
		boolean result = false;
		BtcTurkHelper btcHelper = new BtcTurkHelper();
		
		if (!order.getStatus().equals(OrderStatus.OPEN)) {
			logger.error("Order status is not enough to cancel: " + order.toString());
		} else if (!btcHelper.cancelOrder(order)) {
			logger.error("Order cancelling at Trader is failed: " + order.toString());
		} else {
			order.setCancelDate(AlgoServer.getAlgoDate());
			order.setStatus(OrderStatus.CANCEL);
			order.setDescr("CANCELLED.");
			if (!OrderDao.updateOrder(order)) {
				logger.error("Order cancelled at Trader but not on us! " + order.toString());
			} else {
				result = true;
			}
		}
		
		return result;
	}	
	
	/**
	 * Elimizdeki pair lerle açık emirleri karşılaştırıyoruz.
	 * Eğer açık emirlerde yoksa gerçekleşmiş olarak güncelliyoruz.
	 * 
	 * Dikkat! 
	 * btc den iptal edilen emirler algılanmıyor. Emir silme işlemini uygulamadan yapmak gerekir.
	 * 
	 */
	public void updateRlzOrders(Date d, PairSymbol symbol){
		List<User> users = UserDao.getActiveUsers();
		
		for (User user : users) {
			updateRlzOrdersByUser(user.getId(), d, symbol);
		}  
	}
	
	
	
	public void updateRlzOrdersByUser(int userId, Date d, PairSymbol symbol){
		BtcTurkHelper btcHelper = new BtcTurkHelper();
		List<Order> orderList = null;
		
		try {			
			//orderList = OrderDao.getOrderList(OrderStatus.OPEN, symbol);	// vendor new orders
			orderList = OrderDao.getUserOrders(userId, OrderStatus.OPEN, symbol);	// vendor new orders
			

			// db de açık emir varsa vendor dan kontrol ediyoruz.
			if (orderList != null && orderList.size() > 0) {
				Set<OpenOrdersResult> openOrders = btcHelper.openOrders(userId, symbol);		// vendor open orders
				
				for (Order order: orderList) {
					// hepsini açık varsay.
					boolean found = false;	 
				
					// vendor açık emirlerde bulamazsak gerçekleşti yapacağız.
					for (OpenOrdersResult openOrder: openOrders) {	// vendor open orders
						if (openOrder.getId().equals(order.getTradeRef())) {
							found = true;
							continue;
						}
					}

					// açık emirlerde bulunamadı, demek ki gerçekleşmiş.
					if (!found) {
						order.setStatus(OrderStatus.RLZ);
						order.setDescr("REALIZED");
						order.setRlzDate(d);
						
						Algo a = AlgoDao.getAlgo(order.getAlgoId());
						if (a == null) {
							logger.error("Algo bulunamadı! " + order.getAlgoId());
						} else {
							
							// TODO: burada gerçek gerçekleşme fiyatını almamız gerekiyor (user transactions)
							if (order.getBuySell() == BuySell.BUY) {
								order.setRlzTotal(a.getBuyPrice().multiply(order.getAmount())); 
							} else {
								order.setRlzTotal(a.getSellPrice().multiply(order.getAmount()));
							}
						}

						OrderDao.updateOrder(order);
						logger.info("Realized: " + order.toString());
					}				
				}				
			}
		
		} catch (Exception e) {
			logger.error("error here", e);
		}		
	}

	public Order getBuyAlgoOrder(Algo algo) {
		return OrderDao.getAlgoOrder(algo, BuySell.BUY);
	}

	public Order getSellOrderByAlgo(Algo algo) {
		return OrderDao.getAlgoOrder(algo, BuySell.SELL);
	}

	public OrderResult createOrder(Order o) {
		int orderId = 0;
		OrderResult oResult = null;
		BtcTurkHelper helper = new BtcTurkHelper();
		
		if (o.getBuySell().equals(BuySell.BUY)) {
			oResult = helper.buyOrder(o);
		} else {
			oResult = helper.sellOrder(o);
		}
		
		if (oResult == null) {
			logger.error("error here");			
		} else {
			if (oResult.isSuccess()) {
				o.setTradeRef(oResult.getOrderId());
				o.setTotal(oResult.getAmount().multiply(oResult.getPrice()));
				//o.setCancelDate(DateUtils.getCurrentDate());
				//o.setAmount(oResult.getAmount());

				orderId = OrderDao.insertOrder(o);
				if (orderId == 0) {
					logger.error("db save error. Cancel order manuelly: " + oResult.getOrderId());
					oResult.setSuccess(false);
					oResult.setErrMsg("db save error. Cancel order manuelly: " + oResult.getOrderId());
				} else {
					oResult.setDbId(orderId);
				}
			}
		}

		return oResult;
	}	
}
