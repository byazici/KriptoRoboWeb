package com.by.robo.utils;

import java.rmi.RemoteException;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.gov.nvi.tckimlik.WS.KPSPublicSoapProxy;

public class TcknUtils {
	private static final Logger logger = LoggerFactory.getLogger(TcknUtils.class);
	private TcknUtils() {}

	public static boolean TcKimlikNoSorgula(Long tckn, String name, String surname, int year) {
		boolean result = false;
		
		try {
			Locale tr = new Locale("tr","TR");
			result = new KPSPublicSoapProxy().TCKimlikNoDogrula(tckn, name.toUpperCase(tr), surname.toUpperCase(tr), year);
			if (!result) {
				logger.warn("false tckn: " + tckn + "," + name + "," + surname + "," + year);
			}
		} catch (RemoteException e) {
			logger.error("error here", e);
			result = false;
		}				
		return result;
	}
	
}
