package com.by.robo.enums;

public enum UserStatus {
	APPROVE(1),
	ACTIVE(2),
	CLOSED(10),
	ALL(99);
	
	private final int status;

	UserStatus(int status) {
    		this.status = status; 
	}

	public int getValue() {
			return status; 
	}

    public static UserStatus setValue(int i) {
		switch (i) {
			case 1:
				return APPROVE;
			case 2:
				return ACTIVE;
			case 10:
				return CLOSED;
			default:
				return ALL;
		}
	}	
}
