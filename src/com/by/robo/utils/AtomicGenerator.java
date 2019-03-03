package com.by.robo.utils;

import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class AtomicGenerator {
	final static Logger logger = LoggerFactory.getLogger(AtomicGenerator.class);
	static AtomicInteger nextId = new AtomicInteger();
	
	private AtomicGenerator() {}

    public static int generateUniqeID() {
        return nextId.incrementAndGet();
    }
}
