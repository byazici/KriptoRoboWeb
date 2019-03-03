package com.by.robo.test;

import java.sql.SQLException;
import java.util.Set;

import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jwa.AlgorithmConstraints.ConstraintType;
import org.jose4j.jwe.ContentEncryptionAlgorithmIdentifiers;
import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.jwe.KeyManagementAlgorithmIdentifiers;
import org.jose4j.keys.AesKey;
import org.jose4j.lang.ByteUtil;
import org.jose4j.lang.JoseException;

import com.by.robo.dao.FeedbackDao;
import com.by.robo.dao.TickDao;
import com.by.robo.enums.PairSymbol;
import com.by.robo.helper.BtcTurkHelper;
import com.by.robo.model.Account;
import com.by.robo.model.Feedback;
import com.by.robo.model.OpenOrdersResult;
import com.by.robo.model.Order;
import com.by.robo.model.UserTransactionsResult;
import com.by.robo.utils.DateUtils;
import com.by.robo.utils.PasswordUtil;

@SuppressWarnings("unused")
public class Test {
	
	public static void main(String[] args) {
//		System.out.println(DateUtils.formatJsonDate("2017-12-24T09:26:02"));
//		new BalanceHelper().insertDailyBalance(DateUtils.formatJsonDate("2017-12-27T23:23:23"));
//		cancelOrder("1282398");
//		openOrders();
//		getUserTxn();
//		getBalance();
//		new BtcTurkHelper().getTick(PairSymbol.BTCTRY);
//		insertTickHourly();
//		tokenTest();
//		passUtil();
		
//		new BtcTurkHelper().getTick(PairSymbol.BTCTRY);
//		
//		System.out.println(  new RandomString(64).nextString());
//		
//		testFeedback();	
	}
	
	public static void testFeedback() {
		Feedback f = new Feedback();
		f.setUserId(2);
		f.setMail("burcin@yazici.com");
		f.setName("Burçin Yazıcı");
		f.setSubject("Derdim büyük");
		f.setMessage("Çok büyük küçülmüyor. Öyle bir derdim var...");
		f.setInsertDate(DateUtils.getCurrentDate());
		f.setStatus(1);
		
		System.out.println(FeedbackDao.insertFeedback(f));
	}

	public static void passUtil() {
		String hash;
		try {
			hash = PasswordUtil.createHash("test123!!");
			System.out.println(hash);
			System.out.println(PasswordUtil.verifyPassword("test123!!", hash));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void tokenTest() throws JoseException {
		try {
			 AesKey key = new AesKey(ByteUtil.randomBytes(16));
			 JsonWebEncryption jwe = new JsonWebEncryption();
			 jwe.setPayload("Hello World!");
			 jwe.setAlgorithmHeaderValue(KeyManagementAlgorithmIdentifiers.A128KW);
			 jwe.setEncryptionMethodHeaderParameter(ContentEncryptionAlgorithmIdentifiers.AES_128_CBC_HMAC_SHA_256);
			 jwe.setKey(key);
			 String serializedJwe = jwe.getCompactSerialization();
			 System.out.println("Serialized Encrypted JWE: " + serializedJwe);
			 jwe = new JsonWebEncryption();
			 jwe.setAlgorithmConstraints(new AlgorithmConstraints(ConstraintType.WHITELIST, 
			        KeyManagementAlgorithmIdentifiers.A128KW));
			 jwe.setContentEncryptionAlgorithmConstraints(new AlgorithmConstraints(ConstraintType.WHITELIST, 
			        ContentEncryptionAlgorithmIdentifiers.AES_128_CBC_HMAC_SHA_256));
			 jwe.setKey(key);
			 jwe.setCompactSerialization(serializedJwe);
			 System.out.println("Payload: " + jwe.getPayload());	
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}


	private static void getBalance() {
		Account a = new BtcTurkHelper().	AccountBalance(1);
		System.out.println(a.toString());
	}


	private static void getUserTxn() {
		Set<UserTransactionsResult> x = new BtcTurkHelper().userTransactions(1);
		for (UserTransactionsResult a : x) {
			System.out.println(a.toString());
		}
	}


	private static void insertTickHourly() {
		try {
			//System.out.println(TickDao.insertTickDaily(DateUtils.getTodayHourStart(), PairSymbol.BTCTRY));
			
			System.out.println(TickDao.insertTickHourly(DateUtils.getTodayHourStart(), PairSymbol.BTCTRY));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

	private static void openOrders() {
		 Set<OpenOrdersResult> o = new BtcTurkHelper().openOrders(1, PairSymbol.BTCTRY);
		 for (OpenOrdersResult x : o) {
			 System.out.println(x.toString());
		 }		
	}
	
	private static void cancelOrder(String s) {
		Order order = new Order();
		order.setTradeRef(s);
		System.out.println(new BtcTurkHelper().cancelOrder(order));
	}
}
