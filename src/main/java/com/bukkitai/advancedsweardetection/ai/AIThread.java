package com.bukkitai.advancedsweardetection.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class AIThread extends Thread {

	private Queue<String> processingQueue = new ConcurrentLinkedQueue<>();
	private List<String> blacklist = Collections.synchronizedList(new ArrayList<String>());
	private long lastDBLookup = 0;
	private static final long DB_LOOKUP_RATE = 1800000;
	private boolean doLookup = true;
	
	@Override
	public void run() {
		while(!isInterrupted()) {
			tick();
		}
	}

	private void tick() {
		if(doLookup() && System.currentTimeMillis() - lastDBLookup >= DB_LOOKUP_RATE ) {
			// TODO Do a global database lookup
		}
		// TODO Include the AI processing
		
	}
	
	public void addString(String toProcess) {
		processingQueue.add(toProcess.toLowerCase());
	}
	
	public boolean isBlacklisted(String word) {
		return blacklist.contains(word.toLowerCase());
	}

	public boolean doLookup() {
		return doLookup;
	}

	public void doLookup(boolean doLookup) {
		this.doLookup = doLookup;
	}
}
