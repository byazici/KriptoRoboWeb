package com.by.robo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.by.robo.enums.TokenStatus;
import com.by.robo.model.UserToken;
import com.by.robo.utils.ConnectionFactory;

public class UserTokenDao {
	final static Logger logger = LoggerFactory.getLogger(UserTokenDao.class);
	
	// TODO: token süresi eklenmeli.

	public boolean insertUpdateToken(UserToken ut) {
		Boolean result = false;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String query = "insert into user_token (user_id, token, status) values (?,?,?)";
		
		try{
			deleteToken(ut.getUserId());
			
			conn = ConnectionFactory.getConnection();
			prepStmt = conn.prepareStatement(query);
			int i = 0;
			
			prepStmt.setInt(++i, ut.getUserId());
			prepStmt.setString(++i, ut.getToken());
			prepStmt.setInt(++i, ut.getStatus().getValue());
			prepStmt.executeUpdate();
			
			result = true;
	    } catch (Exception e) {
	    		logger.error("Error here: ", e);
	    } finally{
	    		ConnectionFactory.closeConnPrep(prepStmt, conn);
	    }
		return result;
	}		

	
	public static boolean deleteToken(int userId) {
		Boolean result = false;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String query = "delete from user_token where user_id = ?";
		
		try{
			conn = ConnectionFactory.getConnection();
			prepStmt = conn.prepareStatement(query);
			int i = 0;
			
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
	
	public static boolean updateStatus(int id, TokenStatus status) {
		Boolean result = false;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String query = "update user_token set status = ? where id = ?";
		
		try{
			conn = ConnectionFactory.getConnection();
			prepStmt = conn.prepareStatement(query);
			int i = 0;
			
			prepStmt.setInt(++i, status.getValue());
			prepStmt.setInt(++i, id);
			prepStmt.executeUpdate();
			
			result = true;
	    } catch (Exception e) {
	    		logger.error("Error here: ", e);
	    } finally{
	    		ConnectionFactory.closeConnPrep(prepStmt, conn);
	    }
		return result;
	}	
	
	public static UserToken getActiveToken(String token){
		UserToken userToken = null;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String query = null;

		try{
			query = "select * from user_token where token = ? and status in (1, 2)";

			conn = ConnectionFactory.getConnection();
			prepStmt= conn.prepareStatement(query);
			prepStmt.setString(1, token);

			ResultSet rs = prepStmt.executeQuery();
			if (rs.next()) {
				userToken = new UserToken();
				userToken.setId(rs.getInt("id"));
				userToken.setUserId(rs.getInt("user_id"));
				userToken.setToken(rs.getString("token"));
				userToken.setStatus(TokenStatus.setValue(rs.getInt("status")));
			}
	    } catch (Exception e) {
    			logger.error("Error here: ", e);
	    } finally{
	    		ConnectionFactory.closeConnPrep(prepStmt, conn);
	    }
		
		return userToken;
	}	
}
