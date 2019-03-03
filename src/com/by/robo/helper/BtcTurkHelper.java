package com.by.robo.helper;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.by.robo.cache.UserCache;
import com.by.robo.enums.PairSymbol;
import com.by.robo.enums.TrueFalse;
import com.by.robo.model.Account;
import com.by.robo.model.OpenOrdersResult;
import com.by.robo.model.Order;
import com.by.robo.model.OrderResult;
import com.by.robo.model.Tick;
import com.by.robo.model.User;
import com.by.robo.model.UserTransactionsResult;
import com.by.robo.server.AlgoServer;
import com.by.robo.utils.DateUtils;
import com.by.robo.utils.StringUtils;
import com.by.robo.utils.SysUtils;
import com.by.robo.utils.UrlConnectionFactory;

public class BtcTurkHelper {
	private final static Logger logger = LoggerFactory.getLogger(BtcTurkHelper.class);
	private static final String API_ROOT = SysUtils.readEncryptedProperty("API_ROOT");
	private static final int API_TRY_COUNT = 3;
	
	/**
	 * Bazen bağlantı hatası oluyor. Bu durumda 1 sn bekleyip tekrar deniyoruz. 
	 * @param conn
	 * @return
	 */
	private InputStream getInputStream(HttpsURLConnection conn) {
		InputStream stream = null;
		int i = API_TRY_COUNT;
		
		while (i-- > 0) {
			try {
				stream = conn.getInputStream();
				break;
			} catch (IOException e) {
				logger.error("conn io problem, trying again :" + i, e);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					logger.error("error here!", e1);
				}
			}
		}
		
		return stream;
	}
	
	public Tick getTick(PairSymbol pair){
		Tick tick = null;
		HttpsURLConnection conn = null;
		InputStream stream = null;
		JSONTokener tokener = null;
		JSONArray jsonArray = null;
		JSONObject data = null;
		
		try {
			conn = UrlConnectionFactory.getConnection(API_ROOT + "/ticker", null);
			stream = getInputStream(conn); 
			tokener = new JSONTokener(stream);
			jsonArray = new JSONArray(tokener);
			
			for (int i=0; i< jsonArray.length(); i++) {
				data = jsonArray.getJSONObject(i);
				if (data.get("pair").equals(pair.getValue())) {
					
					tick = new Tick();
					tick.setDate(AlgoServer.getAlgoDate());
					tick.setTimestamp(data.getLong("timestamp"));
					tick.setLast(data.getBigDecimal("last"));

					tick.setHigh(data.getBigDecimal("high"));	
					tick.setBid(data.getBigDecimal("bid"));
					tick.setVolume(data.getBigDecimal("volume"));
					tick.setLow(data.getBigDecimal("low"));
					tick.setAsk(data.getBigDecimal("ask"));
					tick.setOpen(data.getBigDecimal("open"));
					tick.setAverage(data.getBigDecimal("average"));
					tick.setPair(data.getString("pair"));
					
					break; 
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			tick = null;
		} finally {
			UrlConnectionFactory.closeConnection(stream, conn);
		}
		return tick;
	}

	public OrderResult buyOrder(Order order){
		User user = null;
		OrderResult result = null;
		HttpsURLConnection conn = null;
		InputStream stream = null;
		JSONTokener tokener = null;
		JSONObject data = null;
		String url = API_ROOT + "/exchange";
		String params = "";
		
		try {
			//user = UserDao.getUser(order.getUserId());
			user = UserCache.getUser(order.getUserId());
			if (user == null) {
				throw new Exception("User not found: " + order.getUserId());
			}
					
			if (order.getMarketOrder().equals(TrueFalse.TRUE)){

				params += "OrderMethod=1";	// Market Order
				params += "&OrderType=0";	// Buy
				// price omitted
				params += "&PricePrecision=00";
				//amount omitted
				params += "&AmountPrecision=00";
				params += "&Total=" + order.getTotal().toBigInteger();
				params += "&TotalPrecision=" + StringUtils.getPrecisionString(order.getTotal());
				params += "&TriggerPrice=0";
				params += "&TriggerPricePrecision=00";
				params += "&PairSymbol=" + order.getPairSymbol().getValue();			
				params += "&DenominatorPrecision=2";			
				
			} else {

				params += "OrderMethod=0";	// Limit Order
				params += "&OrderType=0";	// Buy
				params += "&Price=" + order.getPrice().toBigInteger();	
				params += "&PricePrecision=" + StringUtils.getPrecisionString(order.getPrice());
				params += "&Amount=" + order.getAmount().toBigInteger();
				params += "&AmountPrecision=" + StringUtils.getPrecisionString(order.getAmount());
				// total omitted
				params += "&TotalPrecision=000";
				params += "&TriggerPrice=0";
				params += "&TriggerPricePrecision=00";
				params += "&PairSymbol=" + order.getPairSymbol().getValue();
				params += "&DenominatorPrecision=2";			
			}

			conn = UrlConnectionFactory.getConnection(url, params, user.getPublicKey(), user.getPrivateKey());
			stream = getInputStream(conn); 
			tokener = new JSONTokener(stream);
			data = new JSONObject(tokener);

			result = new OrderResult();
			if (data.has("error")){
				String error = data.getJSONObject("error").getInt("code") + "-" + data.getJSONObject("error").getString("message");
				logger.error("buyOrder() " + error);
				result.setSuccess(false);
				result.setErrMsg(error);
			} else {
				result.setOrderId(data.getString("id"));
				result.setDatetime(DateUtils.formatJsonDate(data.getString("datetime")));
				result.setType(data.getString("type"));
				result.setPrice(data.getBigDecimal("price"));
				result.setAmount(data.getBigDecimal("amount"));
				result.setSuccess(true);
				//logger.info("buyOrder() " + data.toString());			
			}
		} catch (Exception e) {
			logger.error(e.getMessage() + " params:" + params);
			result = null;
		} finally {
			UrlConnectionFactory.closeConnection(stream, conn);
		}
		return result;
	}

	public OrderResult sellOrder(Order order){
		User user = null;
		OrderResult result = null;
		HttpsURLConnection conn = null;
		InputStream stream = null;
		JSONTokener tokener = null;
		JSONObject data = null;
		String params = "";
		String url = API_ROOT + "/exchange";

		try {
			//user = UserDao.getUser(order.getUserId());
			user = UserCache.getUser(order.getUserId());
			if (user == null) {
				throw new Exception("User not found: " + order.getUserId());
			}
			
			if (order.getMarketOrder().equals(TrueFalse.TRUE)){
				params += "OrderMethod=1";	// Market Order
				params += "&OrderType=1";	// Sell Order
				// price
				params += "&PricePrecision=00";
				//amount
				params += "&AmountPrecision=00";
				params += "&Total=" + order.getAmount().toBigInteger();
				params += "&TotalPrecision=" + StringUtils.getPrecisionString(order.getAmount());;
				params += "&TriggerPrice=0";
				params += "&TriggerPricePrecision=00";
				params += "&PairSymbol=" + order.getPairSymbol().getValue();				
				params += "&DenominatorPrecision=2";			
			} else {
				params += "OrderMethod=0";	// Limit Order
				params += "&OrderType=1";	// Sell Order
				params += "&Price=" + order.getPrice().toBigInteger();
				params += "&PricePrecision=" + StringUtils.getPrecisionString(order.getPrice());;
				params += "&Amount=" + order.getAmount().toBigInteger();
				params += "&AmountPrecision=" + StringUtils.getPrecisionString(order.getAmount());;
				// total	
				params += "&TotalPrecision=00";
				params += "&TriggerPrice=0";
				params += "&TriggerPricePrecision=00";
				params += "&PairSymbol=" + order.getPairSymbol().getValue();
				params += "&DenominatorPrecision=2";			
			}

			conn = UrlConnectionFactory.getConnection(url, params, user.getPublicKey(), user.getPrivateKey());
			stream = getInputStream(conn); 
			tokener = new JSONTokener(stream);
			data = new JSONObject(tokener);

			result = new OrderResult();
			if (data.has("error")){
				String error = data.getJSONObject("error").getInt("code") 
						+ "-" + data.getJSONObject("error").getString("message");
				logger.error("sellOrder() " + error);
				result.setSuccess(false);
				result.setErrMsg(error);
			} else {
				result = new OrderResult();
				result.setOrderId(data.getString("id"));
				result.setDatetime(DateUtils.formatJsonDate(data.getString("datetime")));
				result.setType(data.getString("type"));
				result.setPrice(data.getBigDecimal("price"));
				result.setAmount(data.getBigDecimal("amount"));
				result.setSuccess(true);
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage() + " params:" + params);
			result = null;
		} finally {
			UrlConnectionFactory.closeConnection(stream, conn);
		}
		
		return result;
	}

	public boolean cancelOrder(Order order){
		User user = null;
		boolean result = false;
		HttpsURLConnection conn = null;
		InputStream stream = null;
		JSONTokener tokener = null;
		JSONObject data = null;
		String params = "";
		String url = API_ROOT + "/cancelOrder";

		try {
			//user = UserDao.getUser(order.getUserId());
			user = UserCache.getUser(order.getUserId());
			if (user == null) {
				throw new Exception("User not found: " + order.getUserId());
			}

			params += "id=" + order.getTradeRef();
			conn = UrlConnectionFactory.getConnection(url, params, user.getPublicKey(), user.getPrivateKey());
			stream = getInputStream(conn); 
			tokener = new JSONTokener(stream);
			data = new JSONObject(tokener);
			
			if (data.has("error")){
				String error = data.getJSONObject("error").getInt("code") 
						+ "-" + data.getJSONObject("error").getString("message");
				logger.error("cancelOrder() " + error);
			} else {
				result = data.getBoolean("result");
				logger.info("cancelOrder() id: " + order.getTradeRef() + ", result: " + result);			
			}
			
		} catch (Exception e) {
			result = false;
			logger.error(e.getMessage() + " params:" + params);
		} finally {
			UrlConnectionFactory.closeConnection(stream, conn);
		}
		return result;
	}
	
	public Set<UserTransactionsResult> userTransactions(int userId){
		User user = null;
		Set<UserTransactionsResult> orderSet = null;
		HttpsURLConnection conn = null;
		InputStream stream = null;
		JSONTokener tokener = null;
		JSONArray jsonArray = null;
		JSONObject data = null;
		String url = API_ROOT + "/userTransactions?limit=10000&sort=desc";

		try {
			//user = UserDao.getUser(userId);
			user = UserCache.getUser(userId);
			if (user == null) {
				throw new Exception("User not found: " + userId);
			}
			
			conn = UrlConnectionFactory.getConnection(url, null, user.getPublicKey(), user.getPrivateKey());
			stream = getInputStream(conn); 
		    tokener = new JSONTokener(stream);
			jsonArray = new JSONArray(tokener);

			orderSet = new LinkedHashSet<UserTransactionsResult>();
			for (int i=0; i< jsonArray.length(); i++) {
				data = jsonArray.getJSONObject(i);
				UserTransactionsResult order = new UserTransactionsResult();
				order.setId(data.getString("id"));
				order.setDate(DateUtils.formatJsonDate(data.getString("date")));
				order.setOperation(data.getString("operation"));
				order.setBtc(data.getBigDecimal("amount"));
				order.setCurrency(data.getString("currency"));
				order.setPrice(data.getBigDecimal("price"));
				orderSet.add(order);
			}

			logger.info("userTransactions() length: " + jsonArray.length());

		} catch (Exception e) {
			logger.error(e.getMessage() );
			orderSet = null;
		} finally {
			UrlConnectionFactory.closeConnection(stream, conn);
		}
		return orderSet;
	}
	
	public Set<OpenOrdersResult> openOrders(int userId, PairSymbol symbol){
		User user = null;
		Set<OpenOrdersResult> orderSet = null;
		HttpsURLConnection conn = null;
		InputStream stream = null;
		JSONTokener tokener = null;
		JSONArray jsonArray = null;
		JSONObject data = null;
		String url = API_ROOT + "/openOrders?pairSymbol=" + symbol;

		try {
			//user = UserDao.getUser(userId);
			user = UserCache.getUser(userId);

			if (user == null) {
				throw new Exception("User not found: " + userId);
			}
			
			conn = UrlConnectionFactory.getConnection(url, null, user.getPublicKey(), user.getPrivateKey());
			stream = getInputStream(conn); 
		    tokener = new JSONTokener(stream);
			jsonArray = new JSONArray(tokener);
			
			orderSet = new HashSet<OpenOrdersResult>();
			for (int i=0; i< jsonArray.length(); i++) {
				data = jsonArray.getJSONObject(i);
				
				OpenOrdersResult order = new OpenOrdersResult();
				order.setId(data.getString("id"));
				order.setDatetime(DateUtils.formatJsonDate(data.getString("datetime")));
				order.setType(data.getString("type"));
				order.setPrice(data.getBigDecimal("price"));
				order.setAmount(data.getBigDecimal("amount"));
				order.setPairSymbol(data.getString("pairsymbol"));
				orderSet.add(order);
			}
			
			//logger.info("openOrders() length:" + jsonArray.length());	

		} catch (Exception e) {
			logger.error(e.getMessage());
			orderSet = null;
		} finally {
			UrlConnectionFactory.closeConnection(stream, conn);
		}
		return orderSet;
	}
	
	public Account AccountBalance(int userId){
		User user = null;
		HttpsURLConnection conn = null;
		InputStream stream = null;
		JSONTokener tokener = null;
		//JSONArray jsonArray = null;
		JSONObject data = null;
		Account account = null;
		String url = API_ROOT + "/balance";
		
		try {
			//user = UserDao.getUser(userId);
			user = UserCache.getUser(userId);

			if (user == null) {
				throw new Exception("User not found: " + userId);
			}
			
			conn = UrlConnectionFactory.getConnection(url, null, user.getPublicKey(), user.getPrivateKey());
			stream = getInputStream(conn); 
		    tokener = new JSONTokener(stream);
			data = new JSONObject(tokener);
			
			if (data.has("error")){
				String error = data.getJSONObject("error").getInt("code") + "-" + data.getJSONObject("error").getString("message");
				logger.error("AccountBalance() " + error);
			} else {
				account = new Account();
				account.setTRYBalance(data.getBigDecimal("try_balance"));
				account.setTRYBlockage(data.getBigDecimal("try_reserved"));
				account.setTRYNetBalance(data.getBigDecimal("try_available"));

				account.setBTCBalance(data.getBigDecimal("btc_balance"));
				account.setBTCBlockage(data.getBigDecimal("btc_reserved"));
				account.setBTCNetBalance(data.getBigDecimal("btc_available"));

				account.setETHBalance(data.getBigDecimal("eth_balance"));
				account.setETHBlockage(data.getBigDecimal("eth_reserved"));
				account.setETHNetBalance(data.getBigDecimal("eth_available"));

				logger.info("AccountBalance() " + data.toString());						
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			account = null;
		} finally {
			try {
				if (stream != null) stream.close();
			} catch (IOException e) {
				logger.error("Exception", e);
				account = null;
			}
		}
		return account;
	}
	
	
	public boolean testKeys(int userId, String publicKey, String privateKey){
		HttpsURLConnection conn = null;
		InputStream stream = null;
		JSONTokener tokener = null;
		//JSONArray jsonArray = null;
		JSONObject data = null;
		String url = API_ROOT + "/balance";
		boolean result = false;
		
		try {
			conn = UrlConnectionFactory.getConnection(url, null, publicKey, privateKey);
			stream = getInputStream(conn); 
		    tokener = new JSONTokener(stream);
			data = new JSONObject(tokener);
			
			if (data.has("error")){
				String error = data.getJSONObject("error").getInt("code") + "-" + data.getJSONObject("error").getString("message");
				logger.error("testKeys() for user: " + userId + " "+ error);
			} else {
				result = true;
				logger.info("testKeys() is true");						
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			try {
				if (stream != null) stream.close();
			} catch (IOException e) {
				logger.error("Exception", e);
			}
		}
		return result;
	}		
}
