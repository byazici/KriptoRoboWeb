package com.by.robo.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class NumberUtils {
	
	public static String formatDec2(BigDecimal d) {
		return d == null ? "" :  String.format("%,2.2f",  d);
	}

	public static String formatDec4(BigDecimal d) {
		return d == null ? "" :  String.format("%,2.4f",  d);
	}

	public static String formatDec6(BigDecimal d) {
		return d == null ? "" : String.format("%,2.6f",  d);
	}

	public static String formatDec8(BigDecimal d) {
		return d == null ? "" :  String.format("%,2.8f",  d);
	}
	
	public static boolean isNumeric(String s) {
		boolean r = false;
		DecimalFormat df;
		
		try {
			DecimalFormatSymbols symbols = new DecimalFormatSymbols();
			symbols.setGroupingSeparator(',');
			symbols.setDecimalSeparator('.');
			String pattern = "#,##0.0#";
			df = new DecimalFormat(pattern, symbols);
			df.setParseBigDecimal(true);
			df.parse(s);
			r = true;
		} catch (Exception e) {
			r = false;
		}
		
		return r;
	}
	
	public static BigDecimal nullToZero(BigDecimal b) {
		return b == null ? BigDecimal.ZERO : b;
		
	}
}

