package com.by.robo.helper;

import java.math.BigDecimal;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.by.robo.model.BackTestResult;
import com.by.robo.model.User;

public class BackTestHelper {
	final static Logger logger = LoggerFactory.getLogger(BackTestHelper.class);

	public BackTestResult getBackTest(User user, BigDecimal amount, BigDecimal buyRate, BigDecimal sellRate, Date startDate, Date endDate ) {
		BackTestResult result = null;
		
		// TODO: yap
		
		// Fiyatları al
		
		// fiyat döngüsü
		

		result = new BackTestResult(); 
		result.setBuyOrderCount(10);
		result.setSellOrderCount(11);
		result.setSumProfit(new BigDecimal(1000));
		result.setSumCommAmt(new BigDecimal(5));
		result.setSumProfitNet(new BigDecimal(950));
		
		return result;
	}
}
