package com.by.robo.enums;

public enum TrueFalse {
	TRUE(1),
	FALSE(2)	;

    private final int truefalse;

    TrueFalse(int truefalse) {
    		this.truefalse = truefalse; 
    }
    		
    public int getValue() {
    		return truefalse; 
    }
    
    public static TrueFalse setValue(int i) {
    		switch (i) {
			case 1:
				return TRUE;
			default:
				return FALSE;
		}
    }
}
