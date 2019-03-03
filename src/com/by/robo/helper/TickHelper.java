package com.by.robo.helper;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.by.robo.dao.TickDao;
import com.by.robo.enums.PairSymbol;
import com.by.robo.model.Tick;

public class TickHelper {
	final static Logger logger = LoggerFactory.getLogger(TickHelper.class);
	
	public void insertTickHourly(Date d, PairSymbol p) {
        try {
			TickDao.insertTickHourly(d, p);
		} catch (SQLException e) {
			logger.error("Error here", e);
		}		
	}
	
	public void insertTickDaily(Date d, PairSymbol p) {
        try {
			TickDao.insertTickDaily(d, p);
		} catch (SQLException e) {
			logger.error("Error here", e);
		}					
	}
	
	public Tick insertTick(Date d, PairSymbol pair) {
		BtcTurkHelper btcTurkHelper = null;
		Tick t = null;

		try {
			btcTurkHelper = new BtcTurkHelper();
			t = btcTurkHelper.getTick(pair);
			if (t != null && t.getLast() != null && t.getLast().compareTo(BigDecimal.ZERO) > 0) {	
				TickDao.insertTick(d, t);
			} else {
				t = null; // bazen tick sıfır geliyor.
			}
		} catch (Exception e) {
			logger.error("Error here", e);
			t = null;
		}

		return t;
	}	
}
