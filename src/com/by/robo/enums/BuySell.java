package com.by.robo.enums;

public enum BuySell {
	BUY(1), 
	SELL(2);

    private final int status;

    BuySell(int status) {
    		this.status = status; 
    }
    		
    public int getValue() {
    		return status; 
    }
    
    public static BuySell setValue(int i) {
		switch (i) {
			case 1:
				return BUY;
			case 2:
				return SELL;
			default:
				return BUY;
		}
	}    
}
