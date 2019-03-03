package com.by.robo.enums;

public enum UserRole {
	NONE(0),
	CUSTOMER(1),
	ADMIN(1024);
	
    private final int role;

    UserRole(int role) {
    		this.role = role; 
    }
    		
    public int getValue() {
    		return role; 
    }
    
    
    public static UserRole setValue(int i) {
		switch (i) {
			case 1:
				return CUSTOMER;
			case 2:
				return ADMIN;
			default:
				return NONE;
		}
    }
}
