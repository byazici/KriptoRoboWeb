package com.by.robo.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RecaptchaUtil {
	final static Logger logger = LoggerFactory.getLogger(RecaptchaUtil.class);

	public static final String url = "https://www.google.com/recaptcha/api/siteverify";
	public static final String secret = "6LdNjT8UAAAAAPgwDfLCV4YCWAyN2hLVa1vJ7iI-";
	private final static String USER_AGENT = "Mozilla/5.0";

	public static boolean verify(String gRecaptchaResponse)  {
		boolean result = false;
		
		if (gRecaptchaResponse == null || "".equals(gRecaptchaResponse)) {
			result = false;
		} else {
			
			BufferedReader in = null;
			try{
				URL obj = new URL(url);
				HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
		
				// add reuqest header
				con.setRequestMethod("POST");
				con.setRequestProperty("User-Agent", USER_AGENT);
				con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
				String postParams = "secret=" + secret + "&response=" + gRecaptchaResponse;
		
				// Send post request
				con.setDoOutput(true);
				DataOutputStream wr = new DataOutputStream(con.getOutputStream());
				wr.writeBytes(postParams);
				wr.flush();
				wr.close();
		
//				int responseCode = con.getResponseCode();
		
				in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}

				JSONTokener token = new JSONTokener(response.toString());
				JSONObject data = new JSONObject(token);
				
				result = data.getBoolean("success");
			}catch(Exception e){
				logger.error("error here", e);
				result = false;
			} finally {
				if (in != null)
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
			}			
		}
		
		return result;
	}
}