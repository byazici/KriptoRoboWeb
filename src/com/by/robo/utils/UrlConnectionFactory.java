package com.by.robo.utils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class UrlConnectionFactory {
	final static Logger logger = LoggerFactory.getLogger(UrlConnectionFactory.class);

	private UrlConnectionFactory() {}

	static {
	    HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
			@Override
			public boolean verify(String hostname, SSLSession session) {
				//
				return true;
			}
		});
	}
	
	public static HttpsURLConnection getConnection(String serviceUrl, String params) {
		return getConnection(serviceUrl, params, null, null);
	}

	public static HttpsURLConnection getConnection(String serviceUrl, String params, String publicKey, String secret) {
		URL url = null;
		HttpsURLConnection urlcon = null;
		DataOutputStream wr = null;
		
		try {
			url = new URL(serviceUrl);
			urlcon = (HttpsURLConnection) url.openConnection(); 
			urlcon.setUseCaches(false);	
			urlcon.setDoOutput(true);
			
			if (publicKey != null){
				long unixTime = System.currentTimeMillis() / 1000L;
			    String message = publicKey + unixTime;
			    	SecretKeySpec keySpec = new SecretKeySpec(javax.xml.bind.DatatypeConverter.parseBase64Binary(secret), "HmacSHA256");

			    	Mac mac = Mac.getInstance("HmacSHA256");
		        mac.init(keySpec);
		        mac.update(message.getBytes());
		        byte[] m = mac.doFinal();
		        
		        //String temp = new String(Base64.getEncoder().encode((m)));		// java 8
		        //byte[] messageBytes = "hello world".getBytes("UTF-8");
		        String temp = DatatypeConverter.printBase64Binary(m);
		
		        urlcon.setRequestProperty("X-PCK", publicKey);
				urlcon.setRequestProperty("X-Stamp", String.valueOf(unixTime));
				urlcon.setRequestProperty("X-Signature", temp);	
			}
			
			if (params != null){
				urlcon.setRequestMethod( "POST" );
				urlcon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				urlcon.setRequestProperty("Content-Length", "" + Integer.toString(params.getBytes("UTF-8").length));
			
				wr = new DataOutputStream(urlcon.getOutputStream());
				wr.writeBytes(params);
				wr.flush();
			} else {
				urlcon.setRequestMethod("GET");
			}			
		} catch (IOException e) {
			logger.error("Error here: ", e);
		} catch (NoSuchAlgorithmException e) {
			logger.error("Error here: ", e);
		} catch (InvalidKeyException e) {
			logger.error("Error here: ", e);
		} finally {
			try {
				if (wr != null) wr.close();
			} catch (Exception e) {
				logger.error("Error here: ", e);
			}
		}

		return urlcon;
	}
	
	public static void closeConnection(InputStream stream, HttpsURLConnection conn) {
		try {
			if (stream != null) stream.close();
			conn.disconnect();
		} catch (IOException e) {
			logger.error("Exception", e);
		}		
	}	
}
