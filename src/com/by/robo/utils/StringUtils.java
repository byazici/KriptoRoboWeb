package com.by.robo.utils;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class StringUtils {
	final static Logger logger = LoggerFactory.getLogger(StringUtils.class);

	private StringUtils() {}
	
	public static String getPrecisionString(BigDecimal decimal){
		String result = null;
		
		if (decimal.scale() == 0){		// ex: 7000	> 00
			result = "00";			
		} else {						// ex: 7000.123 > 123
			result = decimal.remainder(BigDecimal.ONE).toPlainString().substring(2); 	
		}
		return result;
	}
	
	public static String getLeftPart(String s, int i) {
		if (s!= null && s.length() > i) {
			return s.substring(0, i);
		} else {
			return s;
		} 
	}
}
