package com.by.robo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.by.robo.model.Feedback;
import com.by.robo.utils.ConnectionFactory;
import com.by.robo.utils.DateUtils;

public class FeedbackDao {
	private final static Logger logger = LoggerFactory.getLogger(FeedbackDao.class);

	public static boolean insertFeedback(Feedback feedback) {
		Boolean result = false;
		Connection conn = null;
		PreparedStatement prepStmt = null;
		String query = "insert into feedback"
				+ " (user_id, name, mail, subject, message, insert_date, status)"
				+ "	values (?, ?, ?, ?, ?, ?, ?)";

		try{
			conn = ConnectionFactory.getConnection();
			prepStmt = conn.prepareStatement(query);
		
			int i = 0;
			prepStmt.setInt(++i, feedback.getUserId());
			prepStmt.setString(++i, feedback.getName());
			prepStmt.setString(++i, feedback.getMail());
			prepStmt.setString(++i, feedback.getSubject());
			prepStmt.setString(++i, feedback.getMessage());
			prepStmt.setTimestamp(++i, DateUtils.getSqlTimestamp(feedback.getInsertDate()));
			prepStmt.setInt(++i, feedback.getStatus());
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
