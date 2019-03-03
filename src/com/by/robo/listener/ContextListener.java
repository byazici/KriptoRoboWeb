package com.by.robo.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.by.robo.cache.UserCache;
import com.by.robo.server.AlgoServer;
import com.by.robo.server.AlgoServerImp;
import com.by.robo.utils.MailUtils;
import com.by.robo.utils.SysUtils;

@WebListener
public class ContextListener implements ServletContextListener {
	private final Logger logger = LoggerFactory.getLogger(ContextListener.class);

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		UserCache.initUserCache();
		
		if (SysUtils.isDevMode()) {
			AlgoServer.setTesting(true);
			AlgoServer server = new AlgoServer();
			server.setAlgoDate();
			server.updateTicks();
			// test veya dev ise manuel başlatacağız.
			logger.info("contextInitialized on fire! DevMode is On !!!");
		} else {
			AlgoServer.setTesting(false);
			AlgoServerImp.startServer();	
			logger.info("contextInitialized on fire! DevMode is Off");
			MailUtils.sendEmailToAdmin("Achtung!", "KriptoroboServer başlatıldı! (dev?" + SysUtils.isDevMode() + ")");		
		}
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		logger.info("contextDestroyed on fire!");
	    AlgoServerImp.stopServer();	
	    
	    if (SysUtils.isDevMode()) {
	    		// 
	    } else {
			MailUtils.sendEmailToAdmin("Achtung!", "KriptoroboServer durduruldu! (dev?" + SysUtils.isDevMode() + ")");
	    }
	}	
}
