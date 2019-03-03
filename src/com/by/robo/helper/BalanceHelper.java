package com.by.robo.helper;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.by.robo.dao.BalanceDao;
import com.by.robo.dao.UserDao;
import com.by.robo.enums.PairSymbol;
import com.by.robo.model.Account;
import com.by.robo.model.User;
import com.by.robo.server.AlgoServer;

public class BalanceHelper {
	
	private final static Logger logger = LoggerFactory.getLogger(BalanceHelper.class);

	public void insertDailyBalance(Date lastExec) {
		BigDecimal btcLastPrice = AlgoServer.getLastTick(PairSymbol.BTCTRY).getLast();
		BigDecimal ethLastPrice = AlgoServer.getLastTick(PairSymbol.ETHTRY).getLast();
		
		if (btcLastPrice == null || ethLastPrice == null) {
			logger.error("price is null!! btcLastPrice:" + btcLastPrice + ", ethLastPrice:" + ethLastPrice);
		} else {
			List<User> users = UserDao.getActiveUsers();
			for (User user : users) {
				Account acc = new BtcTurkHelper().AccountBalance(user.getId());
				BalanceDao.insertDailyBalance(user.getId(), lastExec, acc, btcLastPrice, ethLastPrice);			
			}  
		}

	}
}
