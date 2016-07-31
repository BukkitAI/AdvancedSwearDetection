package org.bukkitai.advancedsweardetection.ai;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkitai.advancedsweardetection.Main;
import org.bukkitai.advancedsweardetection.utils.Pair;

public class AIThread extends Thread {

	private static final List<String> TO_STRIP = new ArrayList<>();
	private static final long DB_LOOKUP_RATE = 1800000;
	public static final List<String> DICTONARY = new ArrayList<>();
	private static final Pattern NUMBER_ONLY = Pattern.compile("[0-9]([0-9])*");

	private boolean doLookup = true;
	private Queue<Pair<String, String>> processingQueue = new ConcurrentLinkedQueue<>();
	private static final List<String> BLACKLIST = Collections.synchronizedList(new ArrayList<String>());
	private long lastDBLookup = 0;

	static {
		TO_STRIP.addAll(Arrays.asList("'", "\"", "_", "-", "+", "*", "[", "]", "{", "}", "\\", "|", ",", ".", "<", ">",
				"/", "?", ";", "#", "^", "%"));
	}

	@Override
	public void run() {
		try {
			for (String word : Files.readAllLines(Main.BAD_WORD_FILE.toPath())) {
				if (!word.startsWith("#") && !BLACKLIST.contains(word.toLowerCase()) && !word.equals(""))
					AIThread.BLACKLIST.add(word.toLowerCase());
			}

			for (String word : Files.readAllLines(Main.DICTONARY_FILE.toPath())) {
				if (!word.startsWith("#") && !DICTONARY.contains(word.toLowerCase()) && !word.equals(""))
					AIThread.DICTONARY.add(word.toLowerCase());
			}
		} catch (IOException e) {
			Main.getInstance().getLogger().log(Level.SEVERE, "Could not load the dictonary or bad word file!", e);
		}
		while (!isInterrupted()) {
			tick();
		}
	}

	private void tick() {
		if (doLookup() && (System.currentTimeMillis() - lastDBLookup >= DB_LOOKUP_RATE)) {
			// TODO Do a global database lookup
		}
		// TODO Complete this

		if (!processingQueue.isEmpty()) {
			Pair<String, String> poll = processingQueue.poll();
			String spaces = poll.getElementOne();
			String[] words = spaces.split(Pattern.quote(" "));
			for (int i = 0; i < words.length; i++) {
				if (!DICTONARY.contains(words[i])) {
					String finalWord = "";
					String finalWordWithSpaces = "";
					for (int j = i; j < words.length; j++) {
						finalWord += words[j];
						finalWordWithSpaces += words[j] + " ";
						if (NUMBER_ONLY.matcher(words[j]).matches())
							break;
						if (DICTONARY.contains(words[i]))
							break;
						else if (BLACKLIST.contains(finalWord.trim().toLowerCase())) {
							BLACKLIST.add(finalWordWithSpaces);
							try {
								Files.write(Main.BAD_WORD_FILE.toPath(), Arrays.asList(finalWordWithSpaces),
										StandardOpenOption.APPEND);
							} catch (IOException e) {
								Main.getInstance().getLogger().log(Level.SEVERE,
										"Could not save '" + finalWordWithSpaces + "' to BAD_WORD_FILE!", e);
							}
							break;
						}
					}
					for (String word: Files.readAllLines(Main.BAD_WORD_FILE.toPath())) {
					if (StringUtils.getJaroWinklerDistance(message, word) < 0.8) {
					//Mark as okay
					}
					if (StringUtils.getJaroWinklerDistance(message, word) > 0.8) {
					AIThread.BLACKLIST.add(message.toLowerCase());
					
					//Marked as blacklisted
					}
					
					}
					
					// TODO Check if a word is above 80%, or more, or less 		| CHECK
					// similar to any blocked word and add its original stage to	| CHECK
					// the blacklist.						| CHECK
					// Then do the same for every substring
				}
			}
		}

	}

	/**
	 * Queues a string for processing
	 * 
	 * @param toProcess
	 */
	public void addString(String toProcess) {
		String lowerCase = ChatColor.stripColor(strip(toProcess)).toLowerCase();
		processingQueue.add(new Pair<String, String>(lowerCase, lowerCase));
	}

	private String strip(String lowerCase) {
		for (String s : TO_STRIP) {
			lowerCase = lowerCase.replace(s, "");
		}
		while (lowerCase.contains("  ")) {
			lowerCase.replace("  ", " ");
		}
		return lowerCase;
	}

	/**
	 * Check is a word blacklisted
	 * 
	 * @param message
	 *            The word
	 * @return Is it blacklisted
	 */
	public boolean hasBlacklistedWord(String message) {
		for (String word : BLACKLIST) {
			if (ChatColor.stripColor(message).toLowerCase().contains(word.toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks should a lookup be done
	 * 
	 * @return True if the lookup needs to be done
	 */
	public boolean doLookup() {
		return doLookup;
	}

	/**
	 * Sets the doLookup property
	 * 
	 * @param doLookup
	 *            The new value
	 */
	public void doLookup(boolean doLookup) {
		this.doLookup = doLookup;
	}
}
