package com.by.robo.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.properties.EncryptableProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SysUtils {
	private static final Logger logger = LoggerFactory.getLogger(SysUtils.class);
	private SysUtils() {}

	public static void sleepAndRest(int interval) {
		try {
			Thread.sleep(interval);
		} catch (InterruptedException e) {
			logger.error("Exception", e);
		}
	}
	
	public static String readProperty(String key) {
		InputStream inputStream = null;
		String value = null;
		
		try {
			Properties prop = new Properties();
			//inputStream = new FileInputStream("resources/config.properties");
			//inputStream = new Systils().getClass().getResourceAsStream("resources/config.properties");
			
			ClassLoader loader = Thread.currentThread().getContextClassLoader();     
			inputStream = loader.getResourceAsStream("config.properties");

			if (inputStream != null) {
				prop.load(inputStream);
				value = prop.getProperty(key);
			}
			
			if (value == null) {
				throw new NullPointerException("config parameter not found! " + key);
			}
 				
		} catch (IOException io) {
			logger.error("error here", io);
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}				

		return value;
	}
	
	public static String readEncryptedProperty(String key) {
		InputStream inputStream = null;
		String value = null;
		
		try {
			StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
			encryptor.setPassword("5tvmscFG6efhjt2"); 
			Properties props = new EncryptableProperties(encryptor);

			ClassLoader loader = Thread.currentThread().getContextClassLoader();     
			inputStream = loader.getResourceAsStream("config.properties");
				
			if (inputStream != null) {
				props.load(inputStream);
				value = props.getProperty("datasource.username");
				props.load(inputStream);
				value = props.getProperty(key);
			}			 

		} catch (IOException io) {
			logger.error("error here", io);
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}				

		if (value == null) {
			throw new NullPointerException("config parameter not found! " + key);
		}
		return value;
	}	
	

	public static StringBuffer readLogFile(){
		StringBuffer sf = null;
		FileInputStream fstream = null;
		BufferedReader br = null;
		try{
			sf = new StringBuffer();
			fstream = new FileInputStream("kriptoRoboWeb-app.log");
			br = new BufferedReader(new InputStreamReader(fstream));

			String strLine;
			while ((strLine = br.readLine()) != null){
				sf.append(strLine).append("\n");
			}
			   
		} catch (Exception e) {
		     System.err.println("Error: " + e.getMessage());
		} finally {
			try {
				fstream .close();
				br.close();
			} catch (IOException e) {
				//
			}
		}
		
		return sf;
	}
	
	public static boolean isDevMode() {
		Object o = System.getProperties().get("devEnv");
		return (o != null && ((String) o).equals("true"));
	}
}
