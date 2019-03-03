package com.by.robo.enums;

public enum TokenStatus {
	NEW(1),
	READ(2),
	DONE(3);

    private final int status;

    TokenStatus(int status) {
    		this.status = status; 
    }
    		
    public int getValue() {
    		return status; 
    }
    
    public static TokenStatus setValue(int i) {
    	TokenStatus s = null;
    	
    		switch (i) {
			case 1:
				s = NEW;
			case 2:
				s = READ;
			case 3:
				s = DONE;
		}
    		
    		return s;
    }
}
