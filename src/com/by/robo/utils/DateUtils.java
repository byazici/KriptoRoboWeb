package com.by.robo.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DateUtils {
	final static Logger logger = LoggerFactory.getLogger(DateUtils.class);
	static final long ONE_MINUTE_IN_MILLIS = 60000;
	public static final TimeZone defaultTimeZone = TimeZone.getTimeZone("GMT+3");

	static final int SECOND = 1000;        // no. of ms in a second
	static final int MINUTE = SECOND * 60; // no. of ms in a minute
	static final int HOUR = MINUTE * 60;   // no. of ms in an hour
	static final int DAY = HOUR * 24;      // no. of ms in a day
	static final int WEEK = DAY * 7;       // no. of ms in a week
	
	private DateUtils() {}

//	public static Date getToday(){
//		Calendar c = Calendar.getInstance();
//	    c.set(Calendar.HOUR_OF_DAY, 0);
//	    c.set(Calendar.MINUTE, 0);
//	    c.set(Calendar.SECOND, 0);
//	    c.set(Calendar.MILLISECOND, 0);
//
//	    return c.getTime();
//	}

//	public static String getTodayString(){
//		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(getToday());
//	}
	
	public static String formatAsDate(Date d){
		if (d == null) {
			return "-";
		} else {
			return new SimpleDateFormat("yyyy-MM-dd").format(d);
		}
	}
	
	public static String formatAsDateTime(Date d){ 
		if (d == null) {
			return "-";
		} else {
			return new SimpleDateFormat("dd.MM.yyyy HH:mm").format(d);
		}
	}		
	
	public static String formatAsTime(Date d){ 
		if (d == null) {
			return "-";
		} else {
			return new SimpleDateFormat("HH:mm").format(d);
		}
	}
	
//	public static String getTodayDateString(){
//		return new SimpleDateFormat("yyyy-MM-dd").format(getToday());
//	}	
	
//	public static Date getTodayTime(){
//		Calendar c = Calendar.getInstance();
//	    return c.getTime();
//	}
	
	
	public static Calendar getCalendar() {
		TimeZone.setDefault(defaultTimeZone);
		Calendar c = Calendar.getInstance();
		return c;
	}
	
	public static Date getTodayHourStart(){	
		Calendar c = getCalendar();
		c.set(Calendar.SECOND , 0);
		c.set(Calendar.MILLISECOND , 0);
	    return c.getTime();
	}	
	
	public static Date formatJsonShortDate(String d){
		Date result = null;
		try {
			result = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(d);
		} catch (ParseException e) {
			logger.error("Exception", e);
		}
		
		return result;
	}

	public static Date formatJsonDate(String d){
		Date result = null;
		try {
			if (d.length() == 19) {
				result = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(d);
			} else {
				result = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse(d);
			}
		} catch (ParseException e) {
			logger.error("Exception", e);
		}
		
		return result;
	}
	
//	public static java.sql.Timestamp getCurrentDateTime() {
//		Calendar cal = Calendar.getInstance();
//	    java.util.Date today = cal.getTime();		// jvm ayarlamasıyla düzgün çalışıyor!
//	    return new java.sql.Timestamp(today.getTime());
//	}	
	
//	public static java.sql.Date getCurrentSqlDate() {	
//		Calendar cal = Calendar.getInstance();		// jvm ayarlamasıyla düzgün çalışıyor!
//	    java.util.Date today = cal.getTime();
//	    return new java.sql.Date(today.getTime());
//	}
	

	public static Date getCurrentDate() {
		Calendar cal =  getCalendar();
	    java.util.Date today = cal.getTime();
	    return today;
	}	
	
	public static java.sql.Date getSqlDate(Date d) {
		return new java.sql.Date(d.getTime());
	}
	
	public static Date addMinutes(Date date, int minutes) {
		long t = date.getTime();
		Date newDate = new Date(t + (minutes * ONE_MINUTE_IN_MILLIS));
		
		return newDate;
	}
	
	public static java.sql.Time getSqlTime(Date date){
		return new java.sql.Time(date.getTime());
	}
	
	public static java.sql.Timestamp getSqlTimestamp(Date date){
		return new java.sql.Timestamp(date.getTime());
	}

	public static Date getPrevPeriod(Date date, int period){
		Calendar cal = getCalendar();
		cal.setTime(getPeriodStart(date, period));
		cal.add(Calendar.HOUR_OF_DAY, -1*period);

		return cal.getTime();
	}

	public static Date getNextPeriod(Date date, int period){
		Calendar cal = getCalendar();
		cal.setTime(getPeriodStart(date, period));
		cal.add(Calendar.HOUR_OF_DAY, period);
		cal.add(Calendar.MILLISECOND, -1);

		return cal.getTime();
	}	
	
	public static Date getPeriodStart(Date date, int period){
		Calendar cal = getCalendar();
		cal.setTime(date);
		
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		cal.set(Calendar.HOUR_OF_DAY, (hour/period) * period);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		return cal.getTime();
	}
	
	public static long nextMinuteDelay(Date d){
		Calendar c = getCalendar();
		c.setTime(d);
		c.add(Calendar.MINUTE, 1);
		
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		
		return (c.getTimeInMillis() - d.getTime());
	}
	
	public static int getHour(Date d) {
		Calendar c = getCalendar();
		c.setTime(d);
		
		return c.get(Calendar.HOUR_OF_DAY);
	}
	
	public static int getDay(Date d) {
		Calendar c = getCalendar();
		c.setTime(d);
		
		return c.get(Calendar.DAY_OF_MONTH);
	}
	
	public static String getRemaining(Date d) {
		long time = d.getTime() - getCurrentDate().getTime();
		String s = "";
		
		int weeks   = (int)( time / WEEK);
		int days    = (int)((time % WEEK) / DAY);
		int hours   = (int)((time % DAY) / HOUR);
		int minutes = (int)((time % HOUR) / MINUTE);
		//int seconds = (int)((time % MINUTE) / SECOND);
		
		if (weeks > 0) s += weeks + "w";
		if (days > 0) s += days + "d";
		if (hours > 0) s += hours + "''";
		if (minutes > 0) s += minutes + "'";
		if (minutes == 0) s = "1m";
		
		return s.trim();
	}
	
	public static String getDiff(Date d) {
		long time = getCurrentDate().getTime() - d.getTime();
		String s = "";
		
		int weeks   = (int)( time / WEEK);
		int days    = (int)((time % WEEK) / DAY);
		int hours   = (int)((time % DAY) / HOUR);
		int minutes = (int)((time % HOUR) / MINUTE);
		//int seconds = (int)((time % MINUTE) / SECOND);
		
		if (weeks > 0) s += weeks + "w";
		if (days > 0) s += days + "d";
		if (hours > 0) s += hours + "''";
		if (minutes > 0) s += minutes + "'";
		if (minutes == 0) s = "1m";
		
		return s.trim();
	}	

	public static String getCurrentYear() {
		Calendar c = getCalendar();
		return String.valueOf(c.get(Calendar.YEAR));
		
	}

	public static int getYearDiff(int year) {
		Calendar c = getCalendar();
		return c.get(Calendar.YEAR) - year;
	}
}
