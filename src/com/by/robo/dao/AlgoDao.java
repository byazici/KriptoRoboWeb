package com.by.robo.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.by.robo.enums.AlgoStatus;
import com.by.robo.enums.PairSymbol;
import com.by.robo.enums.TrueFalse;
import com.by.robo.model.Algo;
import com.by.robo.model.Tick;
import com.by.robo.model.User;
import com.by.robo.utils.ConnectionFactory;
import com.by.robo.utils.DateUtils;

public final class AlgoDao {
	final static Logger logger = LoggerFactory.getLogger(AlgoDao.class);

	public static boolean insertAlgo(Algo algo) {
		Boolean result = false;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String query = "insert into algo ("
				+ " name, pair_symbol, create_date, last_create, "
				+ " expire_date, max_amount, buy_rate, buy_price, "
				+ " sell_rate, sell_price, trig_rate, trig_price, avg_price,"
				+ " repeated, ref_id, duration, price_duration, status, descr, user_id)"
				+ "	values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		try{
			conn = ConnectionFactory.getConnection();
			prepStmt = conn.prepareStatement(query);
			int i = 0;

			prepStmt.setString(++i, algo.getName());
			prepStmt.setString(++i, algo.getPairSynbol().getValue());
			prepStmt.setTimestamp(++i, DateUtils.getSqlTimestamp(algo.getCreateDate()));
			prepStmt.setTimestamp(++i, DateUtils.getSqlTimestamp(algo.getCreateDate()));
			prepStmt.setTimestamp(++i, DateUtils.getSqlTimestamp(algo.getExpireDate()));
			prepStmt.setBigDecimal(++i, algo.getMaxAmount());
			prepStmt.setBigDecimal(++i, algo.getBuyRate());
			prepStmt.setBigDecimal(++i, algo.getBuyPrice());
			prepStmt.setBigDecimal(++i, algo.getSellRate());
			prepStmt.setBigDecimal(++i, algo.getSellPrice());
			prepStmt.setBigDecimal(++i, algo.getTrigRate());
			prepStmt.setBigDecimal(++i, algo.getTrigPrice());
			prepStmt.setBigDecimal(++i, algo.getAvgPrice());
			prepStmt.setInt(++i, algo.getRepeated().getValue());
			prepStmt.setInt(++i, algo.getRefId());
			prepStmt.setInt(++i, algo.getDuration());
			prepStmt.setInt(++i, algo.getPriceDuration());
			prepStmt.setInt(++i, algo.getStatus().getValue());
			prepStmt.setString(++i, algo.getDescr());
			prepStmt.setInt(++i, algo.getUserId());

			prepStmt.executeUpdate();
			
			result = true;
	    } catch (Exception e) {
	    		logger.error("Error here: ", e);
	    } finally{
	    		ConnectionFactory.closeConnPrep(prepStmt, conn);
	    }
		return result;
	}
	
	public static boolean updateAlgo(Algo algo) {
		// TODO last_update eklenebilir, cancel_date eklenebilir
		Boolean result = false;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String query = 
				" update algo set "
				+"   	name = ?,				"
				+"   	last_create = ?,			"
				+"   	expire_date = ?,			"
				+"		max_amount = ?,			"
				+"		buy_price = ?,			"
				+"		trig_price = ?,			"
				+"		sell_price = ?,			"
				+"		avg_price = ?,			"
				+"		repeated = ?,			"
				+"		ref_id = ?,			    "
				+"		duration = ?,			"
				+"		price_duration = ?,	    "
				+"		buy_id = ?,				"
				+"		sell_id = ?,				"
				+"		profit = ?,				"
				+"		status = ?,				"
				+"		descr = ?				"
				+"  where id = ?					";
		
		try{
			conn = ConnectionFactory.getConnection();
			prepStmt = conn.prepareStatement(query);
			
			prepStmt.setString(1, algo.getName());
			prepStmt.setTimestamp(2, DateUtils.getSqlTimestamp(algo.getLastCreate()));
			prepStmt.setTimestamp(3, DateUtils.getSqlTimestamp(algo.getExpireDate()));
			prepStmt.setBigDecimal(4, algo.getMaxAmount());
			prepStmt.setBigDecimal(5, algo.getBuyPrice());
			prepStmt.setBigDecimal(6, algo.getTrigPrice());
			prepStmt.setBigDecimal(7, algo.getSellPrice());
			prepStmt.setBigDecimal(8, algo.getAvgPrice());
			prepStmt.setInt(9, algo.getRepeated().getValue());
			prepStmt.setInt(10, algo.getRefId());
			prepStmt.setInt(11, algo.getDuration());
			prepStmt.setInt(12, algo.getPriceDuration());
			prepStmt.setInt(13, algo.getBuyId());
			prepStmt.setInt(14, algo.getSellId());
			prepStmt.setBigDecimal(15, algo.getProfit());
			prepStmt.setInt(16, algo.getStatus().getValue());
			prepStmt.setString(17, algo.getDescr());
			prepStmt.setInt(18, algo.getId());

			prepStmt.executeUpdate();
			
			result = true;
	    } catch (Exception e) {
	    		logger.error("Error here: ", e);
	    } finally{
	    		ConnectionFactory.closeConnPrep(prepStmt, conn);
	    }
		return result;
	}

	public static boolean updateAlgoStatus(Algo algo) {
		Boolean result = false;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String query = 
				" update algo set   "
				+ "		status = ?,	"
				+ "		 descr = ?,	"
				+ "		profit = ?	"
				+ "   where id = ?	";
		
		try{
			conn = ConnectionFactory.getConnection();
			prepStmt = conn.prepareStatement(query);
			prepStmt.setInt(1, algo.getStatus().getValue());
			prepStmt.setString(2, algo.getDescr());
			prepStmt.setBigDecimal(3, algo.getProfit());
			prepStmt.setInt(4, algo.getId());
			prepStmt.executeUpdate();
			
			result = true;
	    } catch (Exception e) {
	    		logger.error("Error here: ", e);
	    } finally{
	    		ConnectionFactory.closeConnPrep(prepStmt, conn);
	    }
		return result;
	}
	
	public static boolean updateBuyId(Algo algo) {
		Boolean result = false;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String query = 
				" update algo set   "
				+ "		buy_id = ?,	"
				+ "		status = ?,	"
				+ "		 descr = ?	"
				+ "   where id = ?	";
		
		try{
			conn = ConnectionFactory.getConnection();
			prepStmt = conn.prepareStatement(query);
			prepStmt.setInt(1, algo.getBuyId());
			prepStmt.setInt(2, algo.getStatus().getValue());
			prepStmt.setString(3, algo.getDescr());
			prepStmt.setInt(4, algo.getId());
			prepStmt.executeUpdate();
			
			result = true;
	    } catch (Exception e) {
	    		logger.error("Error here: ", e);
	    } finally{
	    		ConnectionFactory.closeConnPrep(prepStmt, conn);
	    }
		return result;
	}
	
	public static boolean updateSellId(Algo algo) {
		Boolean result = false;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String query = 
				" update algo set   "
				+ "	   sell_id = ?,	"
				+ "		status = ?,	"
				+ "		 descr = ?	"
				+ "   where id = ?	";
		
		try{
			conn = ConnectionFactory.getConnection();
			prepStmt = conn.prepareStatement(query);
			prepStmt.setInt(1, algo.getSellId());
			prepStmt.setInt(2, algo.getStatus().getValue());
			prepStmt.setString(3, algo.getDescr());
			prepStmt.setInt(4, algo.getId());
			prepStmt.executeUpdate();
			
			result = true;
	    } catch (Exception e) {
	    		logger.error("Error here: ", e);
	    } finally{
	    		ConnectionFactory.closeConnPrep(prepStmt, conn);
	    }
		return result;
	}		

	public static List<Algo> getActiveAlgoList(Tick t) {
		List<Algo> algoList = null;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String query = null;

		try{
			conn = ConnectionFactory.getConnection();
			query = "select a.*, b.rlz_date as buy_rlz, s.rlz_date as sell_rlz, b.amount as buy_amt from algo a\n" + 
					" left join orders b on (b.id = a.buy_id)\n" + 
					" left join orders s on (s.id = a.sell_id)" +
					" where a.pair_symbol = ? and a.status in (1,2,3,4,5)" +
					" order by a.id";
			prepStmt = conn.prepareStatement(query);
			prepStmt.setString(1, t.getPair());
			ResultSet rs = prepStmt.executeQuery();

			algoList = new ArrayList<Algo>();
			while (rs.next()) {
				algoList.add(rowToAlgo(rs));
			}
	    } catch (Exception e) {
    			logger.error("Error here: ", e);
	    } finally{
	    		ConnectionFactory.closeConnPrep(prepStmt, conn);
	    }
		
		return algoList;	
	}
		
	public static List<Algo> getAlgoList(User user, AlgoStatus status) {
		return getAlgoList(user, status, false);
	}
	
	public static List<Algo> getOpenAlgoList(User user) {
		List<Algo> algoList = null;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String query = null;

		try{
			conn = ConnectionFactory.getConnection();
			query = "select a.*, b.rlz_date as buy_rlz, s.rlz_date as sell_rlz, b.amount as buy_amt from algo a\n" + 
					" left join orders b on (b.id = a.buy_id)\n" + 
					" left join orders s on (s.id = a.sell_id)\n" + 
					" where a.user_id = ? and a.status not in (10,30) order by a.id desc";
			prepStmt = conn.prepareStatement(query);
			prepStmt.setInt(1, user.getId());

			ResultSet rs = prepStmt.executeQuery();

			algoList = new ArrayList<Algo>();
			while (rs.next()) {
				algoList.add(rowToAlgo(rs));
			}
	    } catch (Exception e) {
    			logger.error("Error here: ", e);
	    } finally{
	    		ConnectionFactory.closeConnPrep(prepStmt, conn);
	    }
		
		return algoList;	
	}	
	
	public static List<Algo> getLatestAlgo(User user, int count) {
		List<Algo> algoList = null;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String query = null;
		int i = 0;

		try{
			conn = ConnectionFactory.getConnection();
			query = "select a.*, b.rlz_date as buy_rlz, s.rlz_date as sell_rlz, b.amount as buy_amt from algo a" + 
					" left join orders b on (b.id = a.buy_id)" + 
					" left join orders s on (s.id = a.sell_id)" + 
					" where a.user_id = ? " +
					" order by create_date desc LIMIT ?";
			prepStmt = conn.prepareStatement(query);
			prepStmt.setInt(++i, user.getId());
			prepStmt.setInt(++i, count);

			ResultSet rs = prepStmt.executeQuery();

			algoList = new ArrayList<Algo>();
			while (rs.next()) {
				algoList.add(rowToAlgo(rs));
			}
	    } catch (Exception e) {
    			logger.error("Error here: ", e);
	    } finally{
	    		ConnectionFactory.closeConnPrep(prepStmt, conn);
	    }
		
		return algoList;	
	}		
	
	public static List<Algo> getLatestDoneAlgo(User user, int count) {
		List<Algo> algoList = null;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String query = null;
		int i = 0;

		try{
			conn = ConnectionFactory.getConnection();
			query = "select a.*, b.rlz_date as buy_rlz, s.rlz_date as sell_rlz, b.amount as buy_amt from algo a" + 
					" left join orders b on (b.id = a.buy_id)" + 
					" left join orders s on (s.id = a.sell_id)" + 
					" where a.user_id = ? and a.status = 10" +
					" order by s.rlz_date desc LIMIT ?";
			prepStmt = conn.prepareStatement(query);
			prepStmt.setInt(++i, user.getId());
			prepStmt.setInt(++i, count);

			ResultSet rs = prepStmt.executeQuery();

			algoList = new ArrayList<Algo>();
			while (rs.next()) {
				algoList.add(rowToAlgo(rs));
			}
	    } catch (Exception e) {
    			logger.error("Error here: ", e);
	    } finally{
	    		ConnectionFactory.closeConnPrep(prepStmt, conn);
	    }
		
		return algoList;	
	}			
	
	public static List<Algo> getAlgoList(User user, AlgoStatus status, boolean exclude) {
		List<Algo> algoList = null;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String query = null;

		try{
			conn = ConnectionFactory.getConnection();
			if (status.equals(AlgoStatus.ALL)) {
				query = "select a.*, b.rlz_date as buy_rlz, s.rlz_date as sell_rlz, b.amount as buy_amt from algo a\n" + 
						" left join orders b on (b.id = a.buy_id)\n" + 
						" left join orders s on (s.id = a.sell_id)\n" +
						" where a.user_id = ?" +
						" order by order by a.id desc";
				prepStmt = conn.prepareStatement(query);
				prepStmt.setInt(1, user.getId());

			} else if (exclude) {
				query = "select a.*, b.rlz_date as buy_rlz, s.rlz_date as sell_rlz, b.amount as buy_amt from algo a\n" + 
						" left join orders b on (b.id = a.buy_id)\n" + 
						" left join orders s on (s.id = a.sell_id)\n" + 
						" where a.status != ? and a.user_id = ? order by a.id desc";
				prepStmt = conn.prepareStatement(query);
				prepStmt.setInt(1, status.getValue());
				prepStmt.setInt(2, user.getId());
			} else {
				query = "select a.*, b.rlz_date as buy_rlz, s.rlz_date as sell_rlz, b.amount as buy_amt from algo a\n" + 
						" left join orders b on (b.id = a.buy_id)\n" + 
						" left join orders s on (s.id = a.sell_id)\n" + 
						" where a.status = ? and a.user_id = ? order by a.id desc";
				prepStmt = conn.prepareStatement(query);
				prepStmt.setInt(1, status.getValue());
				prepStmt.setInt(2, user.getId());
			} 
			ResultSet rs = prepStmt.executeQuery();

			algoList = new ArrayList<Algo>();
			while (rs.next()) {
				algoList.add(rowToAlgo(rs));
			}
	    } catch (Exception e) {
    			logger.error("Error here: ", e);
	    } finally{
	    		ConnectionFactory.closeConnPrep(prepStmt, conn);
	    }
		
		return algoList;	
	}	
	
	public static Algo getAlgo(int algoId) {
		Algo algo = null;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String query = null;

		try{
			conn = ConnectionFactory.getConnection();
			query = "select a.*, b.rlz_date as buy_rlz, s.rlz_date as sell_rlz, b.amount as buy_amt from algo a\n" + 
					" left join orders b on (b.id = a.buy_id)\n" + 
					" left join orders s on (s.id = a.sell_id)" +
					" where a.id = ?";
			prepStmt = conn.prepareStatement(query);
			prepStmt.setInt(1, algoId);
			ResultSet rs = prepStmt.executeQuery();

			while (rs.next()) {
				algo = rowToAlgo(rs);
			}
	    } catch (Exception e) {
    			logger.error("Error here: ", e);
	    } finally{
	    		ConnectionFactory.closeConnPrep(prepStmt, conn);
	    }
		
		return algo;	
	}		
	
	private static Algo rowToAlgo(ResultSet rs) throws SQLException {
		Algo algo = new Algo();
		
		algo.setId(rs.getInt("id"));
		algo.setName(rs.getString("name"));
		algo.setPairSynbol(PairSymbol.setValue(rs.getString("pair_symbol")));
		algo.setCreateDate(rs.getTimestamp("create_date"));
		algo.setLastCreate(rs.getTimestamp("last_create"));
		algo.setExpireDate(rs.getTimestamp("expire_date"));
		algo.setMaxAmount(rs.getBigDecimal("max_amount"));
		algo.setBuyRate(rs.getBigDecimal("buy_rate"));
		algo.setBuyPrice(rs.getBigDecimal("buy_price"));
		algo.setTrigRate(rs.getBigDecimal("trig_rate"));
		algo.setTrigPrice(rs.getBigDecimal("trig_price"));
		algo.setAvgPrice(rs.getBigDecimal("avg_price"));
		algo.setSellRate(rs.getBigDecimal("sell_rate"));
		algo.setSellPrice(rs.getBigDecimal("sell_price"));
		algo.setRepeated(TrueFalse.setValue(rs.getInt("repeated")));
		algo.setRefId(rs.getInt("ref_id"));
		algo.setDuration(rs.getInt("duration"));
		algo.setPriceDuration(rs.getInt("price_duration"));
		algo.setStatus(AlgoStatus.setValue(rs.getInt("status")));	
		algo.setDescr(rs.getString("descr"));
		algo.setBuyId(rs.getInt("buy_id"));
		algo.setSellId(rs.getInt("sell_id"));
		algo.setProfit(rs.getBigDecimal("profit"));

		algo.setBuyRlzDate(rs.getTimestamp("buy_rlz"));
		algo.setSellRlzDate(rs.getTimestamp("sell_rlz"));
		
		algo.setUserId(rs.getInt("user_id"));
		algo.setBuyAmt(rs.getBigDecimal("buy_amt"));

		return algo;
	}
	
	public static LinkedHashMap<String, BigDecimal> getPriceDropdown(PairSymbol pair){
		LinkedHashMap<String, BigDecimal> list = null;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String query = null;
		int i = 0;

		try{
			conn = ConnectionFactory.getConnection();
			query = " select (open+high+low+close)/4 as average, close " + 
					" from tick_hourly t where pair = ? order by tick_date desc limit 24"; 
			prepStmt = conn.prepareStatement(query);
			prepStmt.setString(++i, pair.getValue());
			
			ResultSet rs = prepStmt.executeQuery();
			list = new LinkedHashMap<String, BigDecimal>();
			int j = 1;
					
			while (rs.next()) {
				if (j == 1 || j == 2 || j == 4 || j == 12 || j == 24) {
					list.put(j + " saatlik ortalama", rs.getBigDecimal("average"));
					//list.put(k+5, rs.getBigDecimal("close")); close ları şimdilik alma.
				}
				j++;
			}
	    } catch (Exception e) {
    			logger.error("Error here: ", e);
	    } finally{
	    		ConnectionFactory.closeConnPrep(prepStmt, conn);
	    }		
		
		
		return list;
	}
}
