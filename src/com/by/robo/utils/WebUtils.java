package com.by.robo.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebUtils {
	private static final Logger logger = LoggerFactory.getLogger(WebUtils.class);
	private static final String EMAIL_PATTERN =
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";	
	
	private WebUtils() {}

	public static boolean hasAnyEmpty(String... strings) {
		boolean result = false;
		
		for(String s : strings){
			result = result | (s == null || s.length() == 0);
	    }		
		return result;
	}
	
	public static boolean isValidMail(String hex) {
		Pattern pattern;
		Matcher matcher;
		
		pattern = Pattern.compile(EMAIL_PATTERN);
		matcher = pattern.matcher(hex);
		
		return matcher.matches();
	}	
}
