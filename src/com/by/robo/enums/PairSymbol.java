package com.by.robo.enums;

public enum PairSymbol {
	BTCTRY("BTCTRY"), 
	ETHTRY("ETHTRY")	;

    private final String symbol;

    PairSymbol(String symbol) {
    		this.symbol = symbol; 
    }
    		
    public String getValue() {
    		return symbol; 
    }
    
    public static PairSymbol setValue(String s) {
		switch (s) {
			case "ETHTRY":
				return ETHTRY;
			default:
				return BTCTRY;
		}
	}    
}
