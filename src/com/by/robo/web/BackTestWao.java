package com.by.robo.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.by.robo.helper.BackTestHelper;
import com.by.robo.model.BackTestResult;
import com.by.robo.model.User;

public class BackTestWao {
	final static Logger logger = LoggerFactory.getLogger(BackTestWao.class);

	public static BackTestResult getBackTest(User user) {
		BackTestResult result = new BackTestHelper().getBackTest(user, null, null, null, null, null); // TODO
		
		return result;
	}
	
//	public static List<Order> getOrderList(User user, OrderStatus status){
//		List<Order> orderList = null;		
//		List<Order> orderListETH = null;		
//		orderList = OrderDao.getUserOrders(user.getId(), status, PairSymbol.BTCTRY);
//		orderListETH = OrderDao.getUserOrders(user.getId(), status, PairSymbol.ETHTRY);
//		
//		orderList.addAll(orderListETH);
//		return orderList;
//	}

}
