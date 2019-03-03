package com.by.robo.web;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.by.robo.dao.OrderDao;
import com.by.robo.enums.OrderStatus;
import com.by.robo.enums.PairSymbol;
import com.by.robo.model.Order;
import com.by.robo.model.User;

public class OrderWao {
	final static Logger logger = LoggerFactory.getLogger(OrderWao.class);

	public static List<Order> getOrderList(User user, OrderStatus status){
		List<Order> orderList = null;		
		List<Order> orderListETH = null;		
		orderList = OrderDao.getUserOrders(user.getId(), status, PairSymbol.BTCTRY);
		orderListETH = OrderDao.getUserOrders(user.getId(), status, PairSymbol.ETHTRY);
		
		orderList.addAll(orderListETH);
		return orderList;
	}
}
