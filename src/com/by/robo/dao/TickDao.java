package com.by.robo.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.by.robo.enums.PairSymbol;
import com.by.robo.model.Algo;
import com.by.robo.model.Tick;
import com.by.robo.server.AlgoServer;
import com.by.robo.utils.ConnectionFactory;
import com.by.robo.utils.DateUtils;

public final class TickDao {
	final static Logger logger = LoggerFactory.getLogger(TickDao.class);
	
	public static boolean insertTick(Date d, Tick t) throws SQLException{
		Boolean result = false;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String query = "insert into tick (tick_date, pair, last) values (?, ?, ?)";
		
		try{
			conn = ConnectionFactory.getConnection();
			prepStmt = conn.prepareStatement(query);
			prepStmt.setTimestamp(1, DateUtils.getSqlTimestamp(d));
			prepStmt.setString (2, t.getPair());
			prepStmt.setBigDecimal(3, t.getLast());
			prepStmt.execute();
			
			result = true;
	    } catch (Exception e) {
	    		if (e.getMessage().indexOf("Duplicate entry") == -1) logger.error("Error here: ", e);
	    } finally{
	    		ConnectionFactory.closeConnPrep(prepStmt, conn);
	    }
		return result;
	}
	
	public static List<Tick> getTicksByCount(int count){
		List<Tick> tickList = null;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String query = null;

		try{
			conn = ConnectionFactory.getConnection();
			query = "select tick_date, pair, last from tick order by id LIMIT ?";
			prepStmt = conn.prepareStatement(query);
			prepStmt.setInt(1,count);

			ResultSet rs = prepStmt.executeQuery();
			tickList = new ArrayList<Tick>();
			while (rs.next()) {
				Tick tick = new Tick();
				tick.setDate(rs.getDate("tick_date"));
				tick.setPair(rs.getString("pair"));
				tick.setLast(rs.getBigDecimal("last"));;
				
				tickList.add(tick);
			}
	    } catch (Exception e) {
    			logger.error("Error here: ", e);
	    } finally{
	    		ConnectionFactory.closeConnPrep(prepStmt, conn);
	    }
		
		return tickList;	
	}
	
	public static List<Tick> getTicskByDate(Date date){
		List<Tick> tickList = null;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String query = null;

		try{
			conn = ConnectionFactory.getConnection();
			query = "select tick_date, pair, last from tick where tick_date = ? order by id";
			prepStmt = conn.prepareStatement(query);
			prepStmt.setDate(1, DateUtils.getSqlDate(date));

			ResultSet rs = prepStmt.executeQuery();
			tickList = new ArrayList<Tick>();
			while (rs.next()) {
				Tick tick = new Tick();
				tick.setDate(rs.getDate("tick_date"));
				tick.setPair(rs.getString("pair"));
				tick.setLast(rs.getBigDecimal("last"));
				
				tickList.add(tick);
			}
	    } catch (Exception e) {
    			logger.error("Error here: ", e);
	    } finally{
	    		ConnectionFactory.closeConnPrep(prepStmt, conn);
	    }
		
		return tickList;	
	}
	
	public static List<Tick> getTicskByDates(PairSymbol p, Date date, Date date2){
		List<Tick> tickList = null;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String query = null;

		try{
			conn = ConnectionFactory.getConnection();
			query = "select tick_date, pair, last from tick where pair = ? and tick_date >= ? and tick_date <= ? order by id";
			prepStmt = conn.prepareStatement(query);

			prepStmt.setString(1, p.getValue());
			prepStmt.setDate(1, DateUtils.getSqlDate(date));
			prepStmt.setDate(2, DateUtils.getSqlDate(date2));

			ResultSet rs = prepStmt.executeQuery();
			tickList = new ArrayList<Tick>();
			while (rs.next()) {
				Tick tick = new Tick();
				tick.setDate(rs.getDate("tick_date"));
				tick.setPair(rs.getString("pair"));
				tick.setLast(rs.getBigDecimal("last"));
				
				tickList.add(tick);
			}
	    } catch (Exception e) {
    			logger.error("Error here: ", e);
	    } finally{
	    		ConnectionFactory.closeConnPrep(prepStmt, conn);
	    }
		
		return tickList;	
	}	
	
	public static LinkedList<String> getTickHourGraph(PairSymbol p, int interval){
		LinkedList<String> ticks = null;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String query = null;
		 interval = (interval == 0) ? 24 : interval;

		try{
			conn = ConnectionFactory.getConnection();
			query = "select " + 
					" tick_date," + 
					" open," +
					" high," + 
					" low," + 
					" close" +
					" from tick_hourly t" + 
					" where pair = ? " + 
					" order by tick_date desc " +
					" LIMIT ?";
			
			prepStmt = conn.prepareStatement(query);
			prepStmt.setString(1, p.getValue());
			prepStmt.setInt(2, interval);

			ResultSet rs = prepStmt.executeQuery();
			ticks = new  LinkedList<String>();
			while (rs.next()) {
				Date d = rs.getTimestamp("tick_date");
				Calendar c = Calendar.getInstance();
				c.setTime(d);

				ticks.add(String.format("[new Date(%s, %s, %s, %s, %s, %s), %f, %f, %f, %f],\n", 
						c.get(Calendar.YEAR),
						c.get(Calendar.MONTH),
						c.get(Calendar.DAY_OF_MONTH),
						c.get(Calendar.HOUR_OF_DAY),
						"00",
						"00",
						
						rs.getBigDecimal("low"),
						rs.getBigDecimal("open"),
						rs.getBigDecimal("close"),
						rs.getBigDecimal("high")
						// ,rs.getBigDecimal("avg")
						));
			}
	    } catch (Exception e) {
    			logger.error("Error here: ", e);
	    } finally{
	    		ConnectionFactory.closeConnPrep(prepStmt, conn);
	    }
		
		return ticks;	
	}	
	
	public static LinkedList<String> getTickDayGraph(PairSymbol p, int interval){
		LinkedList<String> ticks = null;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String query = null;
		 interval = (interval == 0) ? 24 : interval;

		try{
			conn = ConnectionFactory.getConnection();
			query = "select " + 
					" tick_date," + 
					" open," +
					" high," + 
					" low," + 
					" close" +
					" from tick_daily t" + 
					" where pair = ? " + 
					" order by tick_date desc " +
					" LIMIT ?";
			
			prepStmt = conn.prepareStatement(query);
			prepStmt.setString(1, p.getValue());
			prepStmt.setInt(2, interval);

			ResultSet rs = prepStmt.executeQuery();
			ticks = new  LinkedList<String>();
			while (rs.next()) {
				Date d = rs.getTimestamp("tick_date");
				Calendar c = Calendar.getInstance();
				c.setTime(d);

				ticks.add(String.format("[new Date(%s, %s, %s, %s, %s, %s), %f, %f, %f, %f],\n", 
						c.get(Calendar.YEAR),
						c.get(Calendar.MONTH),
						c.get(Calendar.DAY_OF_MONTH),
						"00",
						"00",
						"00",
						
						rs.getBigDecimal("low"),
						rs.getBigDecimal("open"),
						rs.getBigDecimal("close"),
						rs.getBigDecimal("high")
						// ,rs.getBigDecimal("avg")
						));
			}
	    } catch (Exception e) {
    			logger.error("Error here: ", e);
	    } finally{
	    		ConnectionFactory.closeConnPrep(prepStmt, conn);
	    }
		
		return ticks;	
	}		
	
	public static boolean insertTickDaily(Date d, PairSymbol symbol) throws SQLException{
		Boolean result = false;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String query = 
			" insert into tick_daily (tick_date, pair, open, high, low, close, average)" + 
			" select" + 
			"	date(tick_date) as tick_date," + 
			"	pair," + 
			"	(select last from tick where pair = ? and date(tick_date) = ? order by tick_date limit 1)," + 
			"	max(last)," + 
			"	min(last)," + 
			"	(select last from tick where pair = ? and date(tick_date) = ? order by tick_date desc limit 1)," + 
			"	(max(last) + min(last) + " + 
			"	(select last from tick where pair = ? and date(tick_date) = ? order by tick_date limit 1) +" + 
			"	(select last from tick where pair = ? and date(tick_date) = ? order by tick_date desc limit 1)) / 4" + 
			" from tick t  " + 
			" where date(tick_date) = ?" + 
			"  and pair = ?" + 
			" group by date(tick_date), pair";

		try{
			String pair = symbol.getValue();
			java.sql.Date tickDate = new java.sql.Date(d.getTime());
			
			conn = ConnectionFactory.getConnection();
			prepStmt = conn.prepareStatement(query);
			prepStmt.setString (1, pair);
			prepStmt.setDate (2,  tickDate);
			prepStmt.setString (3, pair);
			prepStmt.setDate (4,  tickDate);
			prepStmt.setString (5, pair);
			prepStmt.setDate (6,  tickDate);
			prepStmt.setString (7, pair);
			prepStmt.setDate (8,  tickDate);
			prepStmt.setDate (9,  tickDate);
			prepStmt.setString (10, pair);
			prepStmt.execute();
			
			result = true;
	    } catch (Exception e) {
	    		if (e.getMessage().indexOf("Duplicate entry") == -1) logger.error("Error here: ", e);
	    } finally{
	    		ConnectionFactory.closeConnPrep(prepStmt, conn);
	    }
		return result;
	}	
	
	public static boolean insertTickHourly(Date d, PairSymbol symbol) throws SQLException{
		Boolean result = false;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String query = 
				"insert into tick_hourly (tick_date, pair, open, high, low, close)\n" + 
				" select " + 
				"	DATE_FORMAT(tick_date, '%Y-%m-%d %H:00:00'), " + 
				"	pair, " + 
				"	(select last from tick where pair = ? and Date(tick_date) = ? and hour(tick_date) = ? order by tick_date limit 1), " + 
				"	max(last), " + 
				"	min(last), " + 
				"	(select last from tick where pair = ? and Date(tick_date) = ? and hour(tick_date) = ? order by tick_date desc limit 1)" + 
				" from tick t  " + 
				" where date(tick_date) = ? " + 
				"   and hour(tick_date) = ? " +
				"   and pair = ?";

		try{
			String pair = symbol.getValue();
			java.sql.Date tickDate = DateUtils.getSqlDate(d);
			int h = DateUtils.getHour(d);
			
			conn = ConnectionFactory.getConnection();
			prepStmt = conn.prepareStatement(query);
			prepStmt.setString (1, pair);
			prepStmt.setDate(2, tickDate);
			prepStmt.setInt(3, h);
			
			prepStmt.setString (4, pair);
			prepStmt.setDate (5, tickDate);
			prepStmt.setInt(6, h);

			prepStmt.setDate (7, tickDate);
			prepStmt.setInt(8, h);
			prepStmt.setString(9, pair);
			prepStmt.execute();
			
			result = true;
	    } catch (Exception e) {
	    		if (e.getMessage().indexOf("Duplicate entry") == -1) logger.error("Error here: ", e);
	    } finally{
	    		ConnectionFactory.closeConnPrep(prepStmt, conn);
	    }
		return result;
	}		

	public static BigDecimal getAvgPrice(Algo algo) {
		BigDecimal avgPrice = null;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String query = " select round(("
				+ "		(select last from tick where pair = ? and tick_date between ? and ? order by tick_date limit 1)  + "
				+ "		(select max(last) from tick where pair = ? and tick_date between ? and ?) + "
				+ "		(select min(last) from tick where pair = ? and tick_date between ? and ?) + "
				+ "		(select last from tick where pair = ? and tick_date between ? and ? order by tick_date desc limit 1) "
				+ "		) / 4, 2) as avg_price ";		

		try{
			String pair = algo.getPairSynbol().getValue();
			Date d = AlgoServer.getAlgoDate();
			int p = algo.getPriceDuration();

			java.sql.Timestamp date1 = new java.sql.Timestamp(DateUtils.getPrevPeriod(d, p).getTime());
			java.sql.Timestamp date2 = new java.sql.Timestamp(DateUtils.getPeriodStart(d, p).getTime());

			conn = ConnectionFactory.getConnection();
			prepStmt = conn.prepareStatement(query);
			prepStmt.setString (1, pair);
			prepStmt.setTimestamp(2,  date1);
			prepStmt.setTimestamp (3,  date2);

			prepStmt.setString (4, pair);
			prepStmt.setTimestamp (5,  date1);
			prepStmt.setTimestamp (6,  date2);

			prepStmt.setString (7, pair);
			prepStmt.setTimestamp (8,  date1);
			prepStmt.setTimestamp (9,  date2);

			prepStmt.setString (10, pair);
			prepStmt.setTimestamp (11,  date1);
			prepStmt.setTimestamp (12,  date2);

			ResultSet rs = prepStmt.executeQuery();
			if (rs.next()) {
				avgPrice = rs.getBigDecimal("avg_price");
			}
	    } catch (Exception e) {
    			logger.error("Error here: ", e);
	    } finally{
	    		ConnectionFactory.closeConnPrep(prepStmt, conn);
	    }
		
		return avgPrice;	
	}	
	
	public static Tick getLastMinus24hours(PairSymbol pair) {
		Tick tick = null;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String query = "select tick_date, pair, last from tick where pair = ? AND tick_date = " + 
				" (select min(tick_date) from tick where pair = ? AND tick_date >= DATE_ADD(?, INTERVAL -1 DAY))";

		try{
			conn = ConnectionFactory.getConnection();
			prepStmt = conn.prepareStatement(query);
			prepStmt.setString (1, pair.getValue());
			prepStmt.setString (2, pair.getValue());
			prepStmt.setTimestamp(3, DateUtils.getSqlTimestamp(AlgoServer.getAlgoDate()));

			ResultSet rs = prepStmt.executeQuery();
			if (rs.next()) {
				tick = new Tick();
				tick.setDate(rs.getDate("tick_date"));
				tick.setPair(rs.getString("pair"));
				tick.setLast(rs.getBigDecimal("last"));
			}
	    } catch (Exception e) {
    			logger.error("Error here: ", e);
	    } finally{
	    		ConnectionFactory.closeConnPrep(prepStmt, conn);
	    }
		
		return tick;	
	}	
}
