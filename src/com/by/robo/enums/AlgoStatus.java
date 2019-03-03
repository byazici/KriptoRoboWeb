package com.by.robo.enums;

public enum AlgoStatus {
	NEW(1), 
	BUY_OPEN(2),
	TRIG_WAIT(3),
	SELL_WAIT(4),
	SELL_OPEN(5),

	DONE(10),
	ERROR(20),
	CANCEL(30),

	ALL(99);

    private final int status;

    AlgoStatus(int status) {
    		this.status = status; 
    }
    		
    public int getValue() {
    		return status; 
    }
    
    
    public static AlgoStatus setValue(int i) {
		switch (i) {
			case 1:
				return NEW;
			case 2:
				return BUY_OPEN;
			case 3:
				return TRIG_WAIT;
			case 4:
				return SELL_WAIT;
			case 5:
				return SELL_OPEN;
			case 10:
				return DONE;
			case 20:
				return ERROR;
			case 30:
				return CANCEL;
			default:
				return ALL;
		}
    }  
}
