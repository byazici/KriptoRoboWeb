package com.by.robo.server;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.by.robo.dao.TickDao;
import com.by.robo.enums.PairSymbol;
import com.by.robo.helper.AlgoHelper;
import com.by.robo.helper.BalanceHelper;
import com.by.robo.helper.OrderHelper;
import com.by.robo.helper.TickHelper;
import com.by.robo.model.Tick;
import com.by.robo.utils.DateUtils;
import com.by.robo.utils.SysUtils;

public class AlgoServer extends Thread {
	private final Logger logger = LoggerFactory.getLogger(AlgoServer.class);
	public static String appVersion = "v0.9.2/16.09.2018";

	ScheduledExecutorService ses = null;
	private static Tick lastTick = new Tick();
	private static Tick lastTick24h = new Tick();
	private static Tick lastTickETH = new Tick();
	private static Tick lastTick24hETH = new Tick();
	private static Date startTime = DateUtils.getCurrentDate();

	private static Date lastExec = null;
	private static Date algoDate = null;
	ScheduledFuture<?> scheduledFuture = null;
	private static boolean isTesting = false;
	
	private static long successCount = 0; 
	private static long failCount = 0; 
	
	public AlgoServer() {		
		logger.info("AlgoServer created.");
	}
	
	public void startThread(){
		if (ses == null && scheduledFuture == null) {
			setAlgoDate();
			ses = Executors.newSingleThreadScheduledExecutor();
			doTheJobs();
			//UserCache.initUserCache();

			logger.info("AlgoServer started.");
		} else {
			logger.info("AlgoServer already started.");
		}
	}

	private void doTheJobs() {
		try {
			// run for init values.
			updateTicks();

			ses = Executors.newSingleThreadScheduledExecutor();	
			scheduledFuture = ses.scheduleAtFixedRate(new Runnable() {
				
				@Override
			    public void run() {
					//if (isStopped()) throw new RuntimeException("doJobs ran but ses was stopped!");
					
					AlgoHelper algoHelper = new AlgoHelper();
					OrderHelper orderHelper = new OrderHelper();
					TickHelper tickHelper = new TickHelper();	
					BalanceHelper balanceHelper = new BalanceHelper();

					try {
						// init
						setAlgoDate();

						if (!SysUtils.isDevMode()) {
							// 1. tick jobs.
							updateTicks();

			    				// 2. insert hourly tick
							if (DateUtils.getHour(lastExec) != DateUtils.getHour(algoDate)) {
								tickHelper.insertTickHourly(lastExec, PairSymbol.BTCTRY);
								tickHelper.insertTickHourly(lastExec, PairSymbol.ETHTRY);
							}

			    				// 3. insert daily  tick
							if (DateUtils.getDay(lastExec) != DateUtils.getDay(algoDate)) {
								tickHelper.insertTickDaily(lastExec, PairSymbol.BTCTRY);
								tickHelper.insertTickDaily(lastExec, PairSymbol.ETHTRY);
								balanceHelper.insertDailyBalance(lastExec);
							}

							// 4. check realized orders
							orderHelper.updateRlzOrders(algoDate, PairSymbol.BTCTRY);
							orderHelper.updateRlzOrders(algoDate, PairSymbol.ETHTRY);

			    				// 5. run algo
			    				if (lastTick != null) {
			    					algoHelper.executeAllAlgo(lastTick);
			    				} else {
			    					logger.error("lastTick is empty!");		
			    				}

			    				if (lastTickETH != null) {
				    				algoHelper.executeAllAlgo(lastTickETH);							
			    				} else {
			    					logger.error("lastTickETH is empty!");		
			    				}
						}

						successCount ++;
					} catch (Exception e) {
						logger.error("Exception", e);
						failCount ++;
					} finally {
						lastExec = algoDate;
					}
				}

			}, DateUtils.nextMinuteDelay(DateUtils.getCurrentDate()), 60000, TimeUnit.MILLISECONDS);					
		} catch (Exception e) {
			logger.error("stopped with exception.");
			ses.shutdownNow();
		}
	}

	public void setAlgoDate() {
		algoDate = DateUtils.getTodayHourStart();
		if (lastExec == null) lastExec = algoDate;
	}

	public void updateTicks() {
		TickHelper tickHelper = new TickHelper();		
		Tick tmp = null;
		Tick tmpETH = null;
		
		// insert tick
		// BTC
		tmp = tickHelper.insertTick(algoDate, PairSymbol.BTCTRY);
		if (tmp != null) lastTick = tmp;	// eğer null gelirse var olan son değer ezilmesin.
		if (getAlgoDate() != null) {
			lastTick24h = TickDao.getLastMinus24hours(PairSymbol.BTCTRY);	
		} else {
			logger.error("getAlgoDate is empty!");
		}
		

		// ETH
		tmpETH = tickHelper.insertTick(algoDate, PairSymbol.ETHTRY);
		if (tmpETH != null) lastTickETH = tmpETH; // eğer null gelirse var olan son değer ezilmesin.
		if (getAlgoDate() != null) {
			lastTick24hETH = TickDao.getLastMinus24hours(PairSymbol.ETHTRY); 
		} else {
			logger.error("getAlgoDate is empty!");
		}
	}	

	public void stopThread() {
		logger.info("AlgoServer got stop signal.");
		
		try {
			if (ses != null && scheduledFuture != null) {
				scheduledFuture.cancel(true);
				ses.shutdown();
				try {
					if (ses.awaitTermination(10000, TimeUnit.MILLISECONDS)) {
						logger.info("AlgoServer stopped succesfully. IsStopped: " + ses.isShutdown() + "," + scheduledFuture.isCancelled());
						ses = null;
						scheduledFuture = null;
					} else {
						logger.info("AlgoServer stop error! isStopped: " + ses.isShutdown() + "," + scheduledFuture.isCancelled());
					};
					;;
				} catch (Exception e) {
					logger.error("error while waiting to shutdown.", e);
				}			
			} else {
				logger.info("AlgoServer is already stopped.");
			}			
		} catch (Exception e) {
			logger.error("error here", e);
		}
	}
	
	public synchronized boolean isStopped() {
		return (ses == null);
	}
	
	public static Tick getLastTick(PairSymbol p) {
		if (p.equals(PairSymbol.BTCTRY)) {
			return lastTick;
		} else {
			return lastTickETH;
		}
	}


	public static Date getAlgoDate() {
		return algoDate;
	}

	public static boolean isTesting() {
		return isTesting;
	}

	public static void setTesting(boolean isTesting) {
		AlgoServer.isTesting = isTesting;
	}

	public static Tick getLastTick24h(PairSymbol p) {
		if (p.equals(PairSymbol.BTCTRY)) {
			return lastTick24h;
		} else {
			return lastTick24hETH;
		}
	}
	
	public static long getSuccessCount() {
		return successCount;
	}


	public static long getFailCount() {
		return failCount;
	}

	public static Date getStartTime() {
		return startTime;
	}

}
