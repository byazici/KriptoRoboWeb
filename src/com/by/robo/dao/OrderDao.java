package com.by.robo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.by.robo.enums.BuySell;
import com.by.robo.enums.OrderStatus;
import com.by.robo.enums.PairSymbol;
import com.by.robo.enums.TrueFalse;
import com.by.robo.model.Algo;
import com.by.robo.model.Order;
import com.by.robo.utils.ConnectionFactory;
import com.by.robo.utils.DateUtils;

public final class OrderDao {
	final static Logger logger = LoggerFactory.getLogger(OrderDao.class);

	
	public static int insertOrder(Order order) {
		int orderId = 0;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String query = "insert into orders " 
				+ " (pair_symbol, buy_sell, create_date, price, amount, total, trade_ref, market_order, status, algo_id, descr, user_id) "
				+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		try{
			conn = ConnectionFactory.getConnection();
			prepStmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			int i = 0;

			prepStmt.setString(++i, order.getPairSymbol().getValue());
			prepStmt.setInt(++i, order.getBuySell().getValue());
			prepStmt.setTimestamp(++i, DateUtils.getSqlTimestamp(order.getCreateDate()));
			
			prepStmt.setBigDecimal(++i, order.getPrice());
			prepStmt.setBigDecimal(++i, order.getAmount());
			prepStmt.setBigDecimal(++i, order.getTotal());

			prepStmt.setString(++i, order.getTradeRef());
			prepStmt.setInt(++i, order.getMarketOrder().getValue());
			prepStmt.setInt(++i, order.getStatus().getValue());
			prepStmt.setInt(++i, order.getAlgoId());
			prepStmt.setString(++i, order.getDescr());
			prepStmt.setInt(++i, order.getUserId());
			
			prepStmt.executeUpdate();
			
			ResultSet generatedKeysResultSet = prepStmt.getGeneratedKeys();
			generatedKeysResultSet.next(); 
			orderId = generatedKeysResultSet.getInt(1); 

	    } catch (Exception e) {
	    		logger.error("Error here: ", e);
	    } finally{
	    		ConnectionFactory.closeConnPrep(prepStmt, conn);
	    }
		return orderId;		
	}
	
	public static List<Order> getUserOrders(int userId, OrderStatus status,  PairSymbol symbol){
		List<Order> orderList = null;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String query = null;

		try{
			conn = ConnectionFactory.getConnection();
			
			if (status.equals(OrderStatus.ALL)) {
				query = "select * from orders where user_id = ? and pair_symbol = ? order by id desc";
				prepStmt = conn.prepareStatement(query);
				prepStmt.setInt(1, userId);
				prepStmt.setString(2, symbol.getValue());
			} else {
				query = "select * from orders where user_id = ? and status = ? and pair_symbol = ? order by id desc";
				prepStmt = conn.prepareStatement(query);
				prepStmt.setInt(1, userId);
				prepStmt.setInt(2, status.getValue());
				prepStmt.setString(3, symbol.getValue());
			}

			ResultSet rs = prepStmt.executeQuery();
			orderList = new ArrayList<Order>();
			while (rs.next()) {
				orderList.add(rowToOrder(rs));
			}
	    } catch (Exception e) {
    			logger.error("Error here: ", e);
	    } finally{
	    		ConnectionFactory.closeConnPrep(prepStmt, conn);
	    }
		
		return orderList;		
	}

	public static List<Order> getOrderList(OrderStatus status, PairSymbol pair){
		List<Order> orderList = null;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String query = null;

		try{
			conn = ConnectionFactory.getConnection();
			
			if (status.equals(OrderStatus.ALL)) {
				query = "select * from orders where pair_symbol = ? order by id desc";
				prepStmt = conn.prepareStatement(query);
				prepStmt.setString(1, pair.getValue());

			} else {
				query = "select * from orders where pair_symbol = ? and status = ? order by id desc";
				prepStmt = conn.prepareStatement(query);
				prepStmt.setString(1, pair.getValue());
				prepStmt.setInt(2, status.getValue());
			}

			ResultSet rs = prepStmt.executeQuery();
			orderList = new ArrayList<Order>();
			while (rs.next()) {
				orderList.add(rowToOrder(rs));
			}
	    } catch (Exception e) {
    			logger.error("Error here: ", e);
	    } finally{
	    		ConnectionFactory.closeConnPrep(prepStmt, conn);
	    }
		
		return orderList;		
	}

	private static Order rowToOrder(ResultSet rs) throws SQLException {
		Order order = new Order();
		order.setId(rs.getInt("id"));
		order.setPairSymbol(PairSymbol.setValue(rs.getString("pair_symbol")));
		order.setBuySell(BuySell.setValue(rs.getInt("buy_sell")));
		order.setCreateDate(rs.getTimestamp("create_date"));
		order.setRlzDate(rs.getTimestamp("rlz_date"));
		order.setCancelDate(rs.getTimestamp("cancel_date"));
		order.setPrice(rs.getBigDecimal("price"));
		order.setAmount(rs.getBigDecimal("amount"));
		order.setTotal(rs.getBigDecimal("total"));
		order.setRlzTotal(rs.getBigDecimal("rlz_total"));
		order.setTradeRef(rs.getString("trade_ref"));
		order.setMarketOrder(TrueFalse.setValue(rs.getInt("market_order")));
		order.setStatus(OrderStatus.setValue(rs.getInt("status")));
		order.setAlgoId(rs.getInt("algo_id"));
		order.setDescr(rs.getString("descr"));
		order.setUserId(rs.getInt("user_id"));
		
		return order;
	}

	public static Order getOrder(int id){
		Order order = null;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String query = null;

		try{
			query = "select * from orders where id = ?";

			conn = ConnectionFactory.getConnection();
			prepStmt= conn.prepareStatement(query);
			prepStmt.setInt(1, id);

			ResultSet rs = prepStmt.executeQuery();
			if (rs.next()) {
				order = rowToOrder(rs);
			}
	    } catch (Exception e) {
    			logger.error("Error here: ", e);
	    } finally{
	    		ConnectionFactory.closeConnPrep(prepStmt, conn);
	    }
		
		return order;
	}
	
	public static Order getAlgoOrder(Algo algo, BuySell buysell){
		Order order = null;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String query = null;

		try{
			query = "select * from orders where id = ?";

			conn = ConnectionFactory.getConnection();
			prepStmt= conn.prepareStatement(query);
			
			if (buysell.equals(BuySell.BUY)) {
				prepStmt.setInt(1, algo.getBuyId());
			} else {
				prepStmt.setInt(1, algo.getSellId());
			}

			ResultSet rs = prepStmt.executeQuery();
			if (rs.next()) {
				order = rowToOrder(rs);
			}
	    } catch (Exception e) {
    			logger.error("Error here: ", e);
	    } finally{
	    		ConnectionFactory.closeConnPrep(prepStmt, conn);
	    }
		
		return order;
	}	
	
	public static boolean updateOrder(Order order) {
		Boolean result = false;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String query = 
			" update orders set	"+
			"	rlz_date 	= ?,	"+
			"	cancel_date = ?,	"+
			"	rlz_total	= ?,	"+
			"	status		= ?,	"+
			"	descr 		= ? 	"+
			" where id      = ? ";
		
		try{
			conn = ConnectionFactory.getConnection();
			prepStmt = conn.prepareStatement(query);
			
			if (order.getRlzDate() == null) {
				prepStmt.setNull(1, Types.DATE);
			} else {
				prepStmt.setTimestamp(1, DateUtils.getSqlTimestamp(order.getRlzDate()));
			}
			
			if (order.getCancelDate() == null) {
				prepStmt.setNull(2, Types.DATE);
			} else {
				prepStmt.setTimestamp(2, DateUtils.getSqlTimestamp(order.getCancelDate()));
			}			
			
			if (order.getRlzTotal() == null) {
				prepStmt.setNull(3, Types.DECIMAL);
			} else {
				prepStmt.setBigDecimal(3, order.getRlzTotal());
			}			

			if (order.getStatus().getValue() == 0) {
				prepStmt.setNull(4, Types.INTEGER);
			} else {
				prepStmt.setInt(4, order.getStatus().getValue());
			}			
			
			if (order.getDescr() == null) {
				prepStmt.setNull(5, Types.VARCHAR);
			} else {
				prepStmt.setString(5, order.getDescr());
			}						

			prepStmt.setInt(6, order.getId());
			prepStmt.executeUpdate();
			
			result = true;
	    } catch (Exception e) {
	    		logger.error("Error here: ", e);
	    } finally{
	    		ConnectionFactory.closeConnPrep(prepStmt, conn);
	    }
		return result;
	}
}
