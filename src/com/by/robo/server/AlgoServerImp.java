
package com.by.robo.server;

public class AlgoServerImp {
	private static AlgoServer algoServer = null;	
	
	private AlgoServerImp() {
		// never being initialized!
	}
	
	public static AlgoServer getInstance() {
		if (algoServer == null) {
			algoServer = new AlgoServer();
		}
		return algoServer;
	}
	
	public static void startServer() {
		AlgoServerImp.getInstance().startThread();
	}

	public static void stopServer() {
		AlgoServerImp.getInstance().stopThread();
	}
	
	public static boolean isRunning() {
		return (!AlgoServerImp.getInstance().isStopped());
	}
}
