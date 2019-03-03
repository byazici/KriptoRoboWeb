package com.by.robo.cache;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.by.robo.dao.UserDao;
import com.by.robo.model.User;

public class UserCache {
	final static Logger logger = LoggerFactory.getLogger(UserCache.class);
	private static HashMap<Integer, User> userCache = null;

	public static void initUserCache() {
		userCache = UserDao.getUserMap();
		logger.debug("User cache init");
	}
	
	public static void updateUser(int userId) {
		if (userCache == null) {
			initUserCache();
		} else {
			User user = UserDao.getUser(userId);
			userCache.put(userId, user);
			logger.debug("User cache updated with " + userId);
			
		}
	}
	
	public static User getUser(int id) {
		if (userCache == null) {
			initUserCache();
		}
		return userCache.get(Integer.valueOf(id));
	}
	
	// TODO admin refresh
}
