package com.by.robo.helper;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.by.robo.dao.UserDao;
import com.by.robo.enums.UserRole;
import com.by.robo.enums.UserStatus;
import com.by.robo.model.User;
import com.by.robo.utils.DateUtils;
import com.by.robo.utils.MailUtils;
import com.by.robo.utils.PasswordUtil;

public class UserHelper {
	final static Logger logger = LoggerFactory.getLogger(UserHelper.class);
	final int MAX_LOGIN_ATTEMP = 3;
	
public final static String USER_TOKEN = "m0y0T000k0@0n0";
	
	public String loginUser(String mail, String pass) {
		String token = null;
		User user = UserDao.getUserByMail(mail);
		
		try {
			if (user != null 
				&& user.getStatus().equals(UserStatus.ACTIVE) 
				&& user.getRoles() > 0 
				&& user.getWrongLogin() < MAX_LOGIN_ATTEMP) {
				
				if (PasswordUtil.verifyPassword(pass, user.getUserPass()) ) {
					token = UUID.randomUUID().toString();
					user.setWrongLogin(0);
					user.setLastLogin(DateUtils.getCurrentDate());

					if (!UserDao.updateLoginAttemp(user, token, true)) {
						token = null;
					}
				} else {
					user.setWrongLogin(user.getWrongLogin() + 1);
					user.setLastLogin(DateUtils.getCurrentDate());
					
					if (!UserDao.updateLoginAttemp(user, token, true)) {
						logger.error("updateLoginAttemp error");
					}
				}
			} else {
				logger.info("Real user with weak credentials!" + user.toString());
			}
			
		} catch (Exception e) {
			logger.error("error here", e);
			token = null;
		}
	
		if (token == null) {
			logger.info("Wrong Login: " + mail);
			MailUtils.sendEmailToAdmin("Hatalı Şifre Denemesi",
					mail + " mail adresi için hatalı şifre denemesi yapıldı!");
			
		} else {
			logger.info("Successful Login: " + mail);
			MailUtils.sendEmailToUser(user.getId(), "Başarılı Giriş Uyarısı", "www.kriptorobo.com sitemize hesabınızdan başarılı bir giriş yapıldı. Bunu siz yaptıysanız bu uyarıyı silebilirsiniz."
					+ "<p> <b>Eğer siz yapmadıysanız<b/>, lütfen şifrenizi hemen değiştiriniz!");
		}

		return token;
	}
	
	public void logoutUser(User u) {
		// TODO: user object problem.
		try {
			if (u != null && UserDao.updateLogout(u)) {
				logger.info("Successful logout: " + u.toString());
			} else {
				logger.info("Unknown logout");
			}
		} catch (Exception e) {
			logger.error("error here : ", e);
		}
	}
	
	public boolean updateStatus(User u, UserStatus s, String reason) {
		// TODO updateStatus
		return false;
	}

	public boolean updatePassword(int userId, String newPass) {
		return UserDao.updatePassword(userId, newPass);
	}
	
	public User getUserByToken(String token) {
		return UserDao.getUserByToken(token);
	}
	
	public boolean userHasRole(User user, UserRole role) {
		boolean hasRole = false;
		// TODO User rolü db den al!!!!
		if (user != null && role != null) {
			int userRole = user.getRoles();
			
			hasRole = (userRole & role.getValue()) == role.getValue();
		}

		return hasRole;
	}
	

}
