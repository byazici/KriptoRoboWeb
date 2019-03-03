package com.by.robo.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class ConnectionFactory {
	final static Logger  logger = LoggerFactory.getLogger(ConnectionFactory.class);
	final static String myDriver = SysUtils.readEncryptedProperty("myDriver");
	final static String myUrl = SysUtils.readEncryptedProperty("myUrl") + SysUtils.readEncryptedProperty("myPass");

	private ConnectionFactory() {}

	public static Connection getConnection() throws Exception {
		Connection c = null;
		try {
			Class.forName(myDriver);
			c = DriverManager.getConnection(myUrl);
		} catch (Exception e) {
			logger.error("Error here: ", e);
			throw new Exception("Connection Problems");
		}
		return c;
	}
	
	public static void closeConnPrep (PreparedStatement p, Connection c) {
		closePrepState(p);	
		closeConnection(c);
	}
	
	public static void closeConnection(Connection c) {
		try {
			if (c != null) c.close();
		} catch (SQLException e) {
			logger.error("Error here: ", e);
		}
	}

	public static void closePrepState(PreparedStatement p) {
		try {
			if (p != null) p.close();
		} catch (SQLException e) {
			logger.error("Error here: ", e);
		}
	}
}
