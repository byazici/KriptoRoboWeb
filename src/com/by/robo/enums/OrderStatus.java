package com.by.robo.enums;

public enum OrderStatus {
	OPEN(1),
	RLZ(2),
	CANCEL(3),

	ALL(99);

    private final int status;

    OrderStatus(int status) {
    		this.status = status; 
    }

    public int getValue() {
    		return status; 
    }

    public static OrderStatus setValue(int i) {
		switch (i) {
			case 1:
				return OPEN;
			case 2:
				return RLZ;
			case 3:
				return CANCEL;
			default:
				return ALL;
		}
	}
}
