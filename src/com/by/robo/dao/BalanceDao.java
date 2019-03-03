package com.by.robo.dao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.by.robo.model.Account;
import com.by.robo.model.BalanceDaily;
import com.by.robo.utils.ConnectionFactory;

public final class BalanceDao {
	private final static Logger logger = LoggerFactory.getLogger(BalanceDao.class);

	public static boolean insertDailyBalance(int userId, Date d, Account acc, BigDecimal btcLastPrice, BigDecimal ethLastPrice) {
		Boolean result = false;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String query = "insert into balance_daily"
				+ " (tick_date, try_balance, try_amount, btc_balance, btc_amount, eth_balance, eth_amount, user_id, overall_btc)"
				+ "	values (?,?,?,?,?,?,?,?,?)";

		try{
			conn = ConnectionFactory.getConnection();
			prepStmt = conn.prepareStatement(query);
			
			// tüm varlığı BTC cinsinden yazıyoruz.
			BigDecimal overallBtc = acc.getBTCBalance();
			overallBtc = overallBtc.add(acc.getTRYBalance().divide(btcLastPrice, 6, RoundingMode.HALF_UP));
			overallBtc = overallBtc.add(acc.getETHBalance().multiply(ethLastPrice).divide(btcLastPrice, 6, RoundingMode.HALF_UP));

			java.sql.Date tickDate = new java.sql.Date(d.getTime());
			int i = 0;
			prepStmt.setDate(++i, tickDate);
			prepStmt.setBigDecimal(++i, acc.getTRYBalance());
			prepStmt.setBigDecimal(++i, acc.getTRYBalance());
			prepStmt.setBigDecimal(++i, acc.getBTCBalance());
			prepStmt.setBigDecimal(++i, acc.getBTCBalance().multiply(btcLastPrice));
			prepStmt.setBigDecimal(++i, acc.getETHBalance());
			prepStmt.setBigDecimal(++i, acc.getETHBalance().multiply(ethLastPrice));
			prepStmt.setInt(++i, userId);
			prepStmt.setBigDecimal(++i, overallBtc);
			prepStmt.executeUpdate();
			
			result = true;
	    } catch (Exception e) {
	    		logger.error("Error here: ", e);
	    } finally{
	    		ConnectionFactory.closeConnPrep(prepStmt, conn);
	    }
		return result;		
	}

	public static List<BalanceDaily> getBalanceList(int userId) {
		List<BalanceDaily> balanceList = null;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String query = null;

		try{
			conn = ConnectionFactory.getConnection();
			query = "select * from balance_daily where user_id = ? order by tick_date desc Limit 30";
			prepStmt = conn.prepareStatement(query);
			prepStmt.setInt(1, userId);

			ResultSet rs = prepStmt.executeQuery();
			balanceList = new ArrayList<BalanceDaily>();
			while (rs.next()) {
				BalanceDaily b = new BalanceDaily();
				b.setTickDate(rs.getDate("tick_date"));
				b.setTryBalance(rs.getBigDecimal("try_balance"));
				b.setTryAmount(rs.getBigDecimal("try_amount"));
				b.setBtcBalance(rs.getBigDecimal("btc_balance"));
				b.setBtcAmount(rs.getBigDecimal("btc_amount"));
				b.setEthBalance(rs.getBigDecimal("eth_balance"));
				b.setEthAmount(rs.getBigDecimal("eth_amount"));
				b.setOverallBtc(rs.getBigDecimal("overall_btc"));
				
				balanceList.add(b);
			}
	    } catch (Exception e) {
    			logger.error("Error here: ", e);
	    } finally{
	    		ConnectionFactory.closeConnPrep(prepStmt, conn);
	    }
		
		return balanceList;	
	}
}
