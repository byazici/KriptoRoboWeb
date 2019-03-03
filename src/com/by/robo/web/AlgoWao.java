package com.by.robo.web;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.by.robo.dao.AlgoDao;
import com.by.robo.dao.UserDao;
import com.by.robo.enums.AlgoStatus;
import com.by.robo.enums.PairSymbol;
import com.by.robo.enums.TrueFalse;
import com.by.robo.enums.UserRole;
import com.by.robo.helper.AlgoHelper;
import com.by.robo.helper.UserHelper;
import com.by.robo.model.Algo;
import com.by.robo.model.Result;
import com.by.robo.model.User;
import com.by.robo.utils.NumberUtils;

public class AlgoWao {
	final static Logger logger = LoggerFactory.getLogger(AlgoWao.class);
	
	public static List<Algo> getAlgoList(User user, AlgoStatus status) {
		List<Algo> list = null;
		if (status == null) {
			list = AlgoDao.getOpenAlgoList(user);
		} else {
			list = AlgoDao.getAlgoList(user, status, false);
		}
		return list;
	}
	
	public static boolean retryAlgo(User user, int algoId) {
		boolean result = false;
		UserHelper helper = new UserHelper();

		if (helper.userHasRole(user, UserRole.CUSTOMER)) {
			if (checkUserHasAlgo(user.getId(), algoId)) {
				result = new AlgoHelper().retryAlgo(algoId);
			}
		}

		return result;
	}
	
	public static Result cancelAlgo(User user, int algoId) {
		Result result = null;
		UserHelper helper = new UserHelper();

		if (helper.userHasRole(user, UserRole.CUSTOMER)) {
			if (checkUserHasAlgo(user.getId(), algoId)) {
				result = new AlgoHelper().cancelAlgo(algoId);
			}
		}
		
		return result;
	}	
	
	public static Result createAlgo(
			User user,
			String name, PairSymbol pairSymbol, 
			BigDecimal maxAmount, BigDecimal buyRate,
			BigDecimal sellRate, BigDecimal trigRate,
			TrueFalse repeated, int duration, int refId,
			int priceDuration,
			BigDecimal basePrice) throws Exception{	
		
		Result result = null;
		UserHelper helper = new UserHelper();
		
		if (helper.userHasRole(user, UserRole.CUSTOMER)) {
			result = new AlgoHelper().createAlgo(user, name, pairSymbol,
					maxAmount, buyRate, sellRate, trigRate, 
					repeated, duration, refId, priceDuration, basePrice);
		}

		return result;
	}
	
	private static boolean checkUserHasAlgo(int userId, int algoId) {
		return UserDao.userHasAlgo(userId, algoId);		
	}
	
	public static String getAlgoBadge(Algo algo) {
		String badge = "primary";
		
		switch (algo.getStatus()){
		case NEW:
			badge = "dark";
			break;
		case BUY_OPEN:
			badge = "primary";
			break;
		case TRIG_WAIT:
			badge = "warning";
			break;
		case SELL_WAIT:
			badge = "info";
			break;
		case SELL_OPEN:
			badge = "info";
			break;
		case DONE:
			badge = "success";
			break;
		case ERROR:
			badge = "danger";
			break;
		case CANCEL:
			badge = "muted";
			break;
		case ALL:
			badge = "muted";
			break;				
		}		
		
		return badge;
	}
	
	public static String getProfit(Algo algo) {
		String profitOut = "&nbsp;";

		if (algo.getProfit() != null && algo.getProfit().compareTo(BigDecimal.ZERO) != 0){
			profitOut = NumberUtils.formatDec2(algo.getProfit());
		} else if (algo.getBuyPrice() != null && algo.getSellPrice() != null && algo.getBuyAmt() != null) {
			BigDecimal profit = algo.getSellPrice().subtract(algo.getBuyPrice()).multiply(algo.getBuyAmt());
			profitOut = "~" + NumberUtils.formatDec2(profit);	// tahmini kar
		}

		return profitOut;
	}	
}
