package com.by.robo.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
public class AESUtil {
	final static Logger logger = LoggerFactory.getLogger(AESUtil.class);

    private static SecretKeySpec secretKey;
    private static byte[] key;
    private static final String secret = "15uqK1(%+TZ<]J`jKThLk!1C=hk6BR";
    
    public static void main(String[] args)
    {
         
        String originalString = "onetwotreeforo";
        String encryptedString = AESUtil.encrypt(originalString, secret) ;
        String decryptedString = AESUtil.decrypt(encryptedString, secret) ;
         
        System.out.println(originalString);
        System.out.println(encryptedString);
        System.out.println(decryptedString);
    }
 
    public static void setKey(String myKey)
    {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        }
        catch (NoSuchAlgorithmException e) {
        		logger.error("error here", e);
        }
        catch (UnsupportedEncodingException e) {
        		logger.error("error here", e);
        }
    }
 
    public static String encrypt(String strToEncrypt) {
    		return encrypt(strToEncrypt, secret);
    }
    
    public static String decrypt(String strToDecrypt) {
    		return decrypt(strToDecrypt, secret);
    }

    private static String encrypt(String strToEncrypt, String secret)
    {
        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        }
        catch (Exception e)
        {
        		logger.error("Error while encrypting", e);
        }
        return null;
    }
 
    private static String decrypt(String strToDecrypt, String secret)
    {
        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        }
        catch (Exception e)
        {
    		logger.error("Error while decrypting", e);
        }
        return null;
    }
}