package com.by.robo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.by.robo.enums.UserStatus;
import com.by.robo.model.User;
import com.by.robo.model.UserAlgoLimits;
import com.by.robo.model.UserStats;
import com.by.robo.utils.AESUtil;
import com.by.robo.utils.ConnectionFactory;
import com.by.robo.utils.DateUtils;
import com.by.robo.utils.NumberUtils;
import com.by.robo.utils.PasswordUtil;

public class UserDao {
	final static Logger logger = LoggerFactory.getLogger(UserDao.class);

	public static List<User> getActiveUsers(){
		List<User> list = null;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String query = null;

		try{
			conn = ConnectionFactory.getConnection();
			query = "select * from user where status = ? order by id";
			prepStmt = conn.prepareStatement(query);
			prepStmt.setInt(1, UserStatus.ACTIVE.getValue());

			ResultSet rs = prepStmt.executeQuery();
			list = new ArrayList<User>();
			while (rs.next()) {
				list.add(rowToOrder(rs));
			}
	    } catch (Exception e) {
    			logger.error("Error here: ", e);
	    } finally{
	    		ConnectionFactory.closeConnPrep(prepStmt, conn);
	    }
		
		return list;		
	}	
	
	public static HashMap<Integer, User> getUserMap(){
		HashMap<Integer, User> map = null;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String query = null;

		try{
			conn = ConnectionFactory.getConnection();
			query = "select * from user where status = ? order by id";
			prepStmt = conn.prepareStatement(query);
			prepStmt.setInt(1, UserStatus.ACTIVE.getValue());

			ResultSet rs = prepStmt.executeQuery();
			map = new HashMap<Integer, User>();
			while (rs.next()) {
				map.put(rs.getInt("id"), rowToOrder(rs));
			}
	    } catch (Exception e) {
    			logger.error("Error here: ", e);
	    } finally{
	    		ConnectionFactory.closeConnPrep(prepStmt, conn);
	    }
		
		return map;		
	}		
	
	public static User getUser(int id){
		User user = null;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String query = null;

		try{
			query = "select * from user where id = ?";

			conn = ConnectionFactory.getConnection();
			prepStmt= conn.prepareStatement(query);
			prepStmt.setInt(1, id);

			ResultSet rs = prepStmt.executeQuery();
			if (rs.next()) {
				user = rowToOrder(rs);
			}
	    } catch (Exception e) {
    			logger.error("Error here: ", e);
	    } finally{
	    		ConnectionFactory.closeConnPrep(prepStmt, conn);
	    }
		
		return user;
	}
	
	public static User getUserByMail(String mail){
		User user = null;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String query = null;

		try{
			query = "select * from user where user_mail = ?";

			conn = ConnectionFactory.getConnection();
			prepStmt= conn.prepareStatement(query);
			prepStmt.setString(1, mail);

			ResultSet rs = prepStmt.executeQuery();
			if (rs.next()) {
				user = new User();
				user.setId(rs.getInt("id"));
				user.setUserMail(rs.getString("user_mail"));
				user.setUserPass(rs.getString("user_pass"));
				user.setRoles(rs.getInt("roles"));
				user.setWrongLogin(rs.getInt("wrong_login"));
				user.setStatus(UserStatus.setValue(rs.getInt("status")));				
			}
	    } catch (Exception e) {
    			logger.error("Error here: ", e);
	    } finally{
	    		ConnectionFactory.closeConnPrep(prepStmt, conn);
	    }
		
		return user;
	}	
	
	public static User getUserByToken(String token){
		User user = null;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String query = null;

		try{
			query = "select * from user where token = ?";

			conn = ConnectionFactory.getConnection();
			prepStmt= conn.prepareStatement(query);
			prepStmt.setString(1, token);

			ResultSet rs = prepStmt.executeQuery();
			if (rs.next()) {
				user = rowToOrderInfo(rs);
			}
	    } catch (Exception e) {
    			logger.error("Error here: ", e);
	    } finally{
	    		ConnectionFactory.closeConnPrep(prepStmt, conn);
	    }
		
		return user;
	}	
	
	public static boolean userHasAlgo(int userId, int algoId){
		boolean result = false;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String query = null;

		try{
			query = "select * from algo where id = ? and user_id = ?";

			conn = ConnectionFactory.getConnection();
			prepStmt= conn.prepareStatement(query);

			int i = 0;
			prepStmt.setInt(++i, algoId);
			prepStmt.setInt(++i, userId);

			ResultSet rs = prepStmt.executeQuery();
			if (rs.next()) {
				result = true;
			}
	    } catch (Exception e) {
    			logger.error("Error here: ", e);
	    } finally{
	    		ConnectionFactory.closeConnPrep(prepStmt, conn);
	    }
		
		return result;
	}	

	private static User rowToOrder(ResultSet rs) throws SQLException {
		User user = new User();
		user.setId(rs.getInt("id"));
		user.setUserMail(rs.getString("user_mail"));
		user.setUserPass(rs.getString("user_pass"));
		if (rs.getString("public_key") != null) {
			user.setPublicKey(AESUtil.decrypt(rs.getString("public_key")));
		}

		if (rs.getString("private_key") != null) {
			user.setPrivateKey(AESUtil.decrypt(rs.getString("private_key")));
		}

		user.setRoles(rs.getInt("roles"));
		user.setStatus(UserStatus.setValue(rs.getInt("status")));
		user.setWrongLogin(rs.getInt("wrong_login"));
		user.setLastLogin(rs.getTime("last_login"));
		user.setMaxAlgo(rs.getInt("max_algo"));
		user.setMaxAmt(rs.getBigDecimal("max_amt"));
		
		return user;
	}	

	private static User rowToOrderInfo(ResultSet rs) throws SQLException {
		User user = new User();
		user.setId(rs.getInt("id"));
		user.setUserMail(rs.getString("user_mail"));
		if (rs.getString("public_key") != null) {
			user.setPublicKey(AESUtil.decrypt(rs.getString("public_key")));
		}		
		user.setRoles(rs.getInt("roles"));	// TODO User rolü db den al. sil
		user.setStatus(UserStatus.setValue(rs.getInt("status")));
		user.setWrongLogin(rs.getInt("wrong_login"));
		user.setLastLogin(rs.getTime("last_login"));
		user.setFirstName(rs.getString("first_name"));
		user.setLastName(rs.getString("last_name"));
		user.setMaxAlgo(rs.getInt("max_algo"));
		user.setMaxAmt(rs.getBigDecimal("max_amt"));

		return user;
	}	
	
	public static boolean updateKeys(int userId, String publicKey, String privateKey) throws SQLException{
		Boolean result = false;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String query = "update user set public_key = ?, private_key = ? where id = ?";
		
		try{
			conn = ConnectionFactory.getConnection();
			prepStmt = conn.prepareStatement(query);
			int i = 0;
			
			prepStmt.setString (++i, AESUtil.encrypt(publicKey));
			prepStmt.setString(++i,  AESUtil.encrypt(privateKey));
			prepStmt.setInt(++i, userId);
			prepStmt.executeUpdate();
			
			result = true;
	    } catch (Exception e) {
	    		logger.error("Error here: ", e);
	    } finally{
	    		ConnectionFactory.closeConnPrep(prepStmt, conn);
	    }
		return result;
	}
	
	public static boolean updateStatus(int userId, UserStatus status) {
		Boolean result = false;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String query = "update user set status = ? where id = ?";
		
		try{
			conn = ConnectionFactory.getConnection();
			prepStmt = conn.prepareStatement(query);
			int i = 0;
			
			prepStmt.setInt(++i, status.getValue());
			prepStmt.setInt(++i, userId);
			prepStmt.executeUpdate();
			
			result = true;
	    } catch (Exception e) {
	    		logger.error("Error here: ", e);
	    } finally{
	    		ConnectionFactory.closeConnPrep(prepStmt, conn);
	    }
		return result;
	}	
	
	public static boolean resetWrongLogin(int userId) {
		Boolean result = false;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String query = "update user set wrong_login = ? where id = ?";
		
		try{
			conn = ConnectionFactory.getConnection();
			prepStmt = conn.prepareStatement(query);
			int i = 0;
			
			prepStmt.setInt(++i, 0);
			prepStmt.setInt(++i, userId);
			prepStmt.executeUpdate();
			
			result = true;
	    } catch (Exception e) {
	    		logger.error("Error here: ", e);
	    } finally{
	    		ConnectionFactory.closeConnPrep(prepStmt, conn);
	    }
		return result;
	}		

	public static boolean updateLoginAttemp(User u, String token, boolean success) {
		// TODO code success
		Boolean result = false;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String query = "update user set last_login = ?, wrong_login = ?, token = ? where id = ?";
		
		try{
			conn = ConnectionFactory.getConnection();
			prepStmt = conn.prepareStatement(query);
			int i = 0;
			
			prepStmt.setTimestamp(++i, DateUtils.getSqlTimestamp(u.getLastLogin()));
			prepStmt.setInt(++i, u.getWrongLogin());
			prepStmt.setString(++i, token);
			prepStmt.setInt(++i, u.getId());
			prepStmt.executeUpdate();
			
			result = true;
	    } catch (Exception e) {
	    		logger.error("Error here: ", e);
	    } finally{
	    		ConnectionFactory.closeConnPrep(prepStmt, conn);
	    }
		return result;
	}	
	
	public static boolean updateLogout(User u) {
		// TODO code success
		Boolean result = false;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String query = "update user set token = null where id = ?";
		
		try{
			conn = ConnectionFactory.getConnection();
			prepStmt = conn.prepareStatement(query);
			int i = 0;
			
			prepStmt.setInt(++i, u.getId());
			prepStmt.executeUpdate();
			
			result = true;
	    } catch (Exception e) {
	    		logger.error("Error here: ", e);
	    } finally{
	    		ConnectionFactory.closeConnPrep(prepStmt, conn);
	    }
		return result;
	}

	public static boolean updatePassword(int userId, String newPass) {
		Boolean result = false;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String query = "update user set user_pass = ? where id = ?";
		
		try{
			conn = ConnectionFactory.getConnection();
			prepStmt = conn.prepareStatement(query);
			int i = 0;
			
			prepStmt.setString(++i, PasswordUtil.createHash(newPass));
			prepStmt.setInt(++i, userId);
			prepStmt.executeUpdate();
			
			result = true;
	    } catch (Exception e) {
	    		logger.error("Error here: ", e);
	    } finally{
	    		ConnectionFactory.closeConnPrep(prepStmt, conn);
	    }
		return result;
	}

	public static int insertUser(User user) {
		int userId = 0;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String query = 
				"insert into user (user_mail, first_name, last_name, tckn, birth_year, roles, max_algo, max_amt, status)" +
				"values (?,?,?,?,?,?,?,?,?)";
		
		try{
			conn = ConnectionFactory.getConnection();
			prepStmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			int i = 0;
			
			prepStmt.setString(++i, user.getUserMail());
			prepStmt.setString(++i, user.getFirstName());
			prepStmt.setString(++i, user.getLastName());
			prepStmt.setString(++i, user.getTckn());
			prepStmt.setString(++i, user.getBirthYear());
			prepStmt.setInt(++i, user.getRoles());
			prepStmt.setInt(++i, user.getMaxAlgo());
			prepStmt.setBigDecimal(++i, user.getMaxAmt());
			prepStmt.setInt(++i, user.getStatus().getValue());

			prepStmt.executeUpdate();
			ResultSet generatedKeysResultSet = prepStmt.getGeneratedKeys();
			generatedKeysResultSet.next(); 
			userId = generatedKeysResultSet.getInt(1); 			
			
	    } catch (Exception e) {
	    		logger.error("Error here: ", e);
	    } finally{
	    		ConnectionFactory.closeConnPrep(prepStmt, conn);
	    }
		
		return userId;
	}

	public static UserStats getUserStats(User user) {
		UserStats u = null;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String query = null;
		int i = 0;

		try{
			query = "select \n" + 
					"	(select count(*) from algo where user_id = ? and status = 10) as rlz_algo,\n" + 
					"	(select count(*) from algo where user_id = ? and status not in (10, 30)) as open_algo,\n" + 
					"	(select ifnull(sum(profit), 0) from algo where user_id = ? and status = 10 group by user_id) as profit,\n" + 
					"	(select ifnull(sum(profit) / sum(max_amount), 0) from algo where user_id = ? and status = 10) as rate,\n" + 
					"	ifnull(max_algo, 0) as max_algo,\n" + 
					"	ifnull(max_amt, 0) as max_amt,\n" + 
					"	(select ifnull(sum(max_amount),0) from algo where user_id = ? and status not in (10, 30)) as open_amt\n" + 
					" from user\n" + 
					" where id = ?";

			conn = ConnectionFactory.getConnection();
			prepStmt= conn.prepareStatement(query);
			prepStmt.setInt(++i, user.getId());
			prepStmt.setInt(++i, user.getId());
			prepStmt.setInt(++i, user.getId());
			prepStmt.setInt(++i, user.getId());
			prepStmt.setInt(++i, user.getId());
			prepStmt.setInt(++i, user.getId());

			ResultSet rs = prepStmt.executeQuery();
			if (rs.next()) {
				u = new UserStats();
				u.setRlzAlgo(NumberUtils.nullToZero(rs.getBigDecimal("rlz_algo")));
				u.setProfit(NumberUtils.nullToZero(rs.getBigDecimal("profit")));
				u.setRate(NumberUtils.nullToZero(rs.getBigDecimal("rate")));

				u.setMaxAlgo(NumberUtils.nullToZero(rs.getBigDecimal("max_algo")));
				u.setOpenAlgo(NumberUtils.nullToZero(rs.getBigDecimal("open_algo")));
				u.setMaxAmt(NumberUtils.nullToZero(rs.getBigDecimal("max_amt")));
				u.setOpenAmt(NumberUtils.nullToZero(rs.getBigDecimal("open_amt")));
			}
	    } catch (Exception e) {
    			logger.error("Error here: ", e);
	    } finally{
	    		ConnectionFactory.closeConnPrep(prepStmt, conn);
	    }		

		
		return u;
		
	}
	
	public static UserAlgoLimits getUserAlgoLimits(User user) {
		UserAlgoLimits u = null;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String query = null;
		int i = 0;

		try{
			query = "select \n" + 
					"   max_algo, " + 
					"   (select count(*) from algo where user_id = ? and status not in (10, 30)) as open_algo, \n" + 
					"   max_amt, " +
					"   (select ifnull(sum(max_amount),0) from algo where user_id = ? and status not in (10, 30)) as open_amt \n" + 
					" from user where id = ?";

			conn = ConnectionFactory.getConnection();
			prepStmt= conn.prepareStatement(query);
			prepStmt.setInt(++i, user.getId());
			prepStmt.setInt(++i, user.getId());
			prepStmt.setInt(++i, user.getId());

			ResultSet rs = prepStmt.executeQuery();
			if (rs.next()) {
				u = new UserAlgoLimits();
				u.setMaxAlgo(rs.getBigDecimal("max_algo"));
				u.setOpenAlgo(rs.getBigDecimal("open_algo"));
				u.setMaxAmt(rs.getBigDecimal("max_amt"));
				u.setOpenAmt(rs.getBigDecimal("open_amt"));
			}
	    } catch (Exception e) {
    			logger.error("Error here: ", e);
	    } finally{
	    		ConnectionFactory.closeConnPrep(prepStmt, conn);
	    }		
		return u;	
	}	
}
