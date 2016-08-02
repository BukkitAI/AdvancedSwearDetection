package org.bukkitai.advancedsweardetection.ai;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkitai.advancedsweardetection.Main;

import info.debatty.java.stringsimilarity.JaroWinkler;

public class AIThread extends Thread {
	private static final Collection<String> TO_STRIP = new HashSet<>();
	private static final long DB_LOOKUP_RATE = 1800000L;
	private static final Collection<String> DICTONARY = new HashSet<>();
	private static final Pattern NUMBER_ONLY = Pattern.compile("[0-9]([0-9])*");
	private static final Collection<String> BLACKLIST = Collections.synchronizedSet(new HashSet<String>());
	private boolean doLookup = true;
	private Queue<String> processingQueue = new ConcurrentLinkedQueue<>();
	private long lastDBLookup = 0L;
	public static final JaroWinkler jaroWinkler = new JaroWinkler();

	static {
		TO_STRIP.addAll(Arrays.asList(new String[] { "'", "\"", "_", "-", "+", "*", "[", "]", "{", "}", "\\", "|", ",",
				".", "<", ">", "/", "?", ";", "#", "^", "%" }));
	}

	public void run() {
		try {
			for (String word : Files.readAllLines(Main.BAD_WORD_FILE.toPath())) {
				if ((!word.startsWith("#")) && (!BLACKLIST.contains(word.toLowerCase())) && (!word.equals(""))) {
					BLACKLIST.add(word.toLowerCase());
				}
			}
			for (String word : Files.readAllLines(Main.DICTONARY_FILE.toPath())) {
				if ((!word.startsWith("#")) && (!DICTONARY.contains(word.toLowerCase())) && (!word.equals(""))) {
					DICTONARY.add(word.toLowerCase());
				}
			}
		} catch (IOException e) {
			Main.getInstance().getLogger().log(Level.SEVERE, "Could not load the dictonary or bad word file!", e);
		}
		Main.debug("Tick started!");
		while (!isInterrupted()) {
			tick();
		}
	}

	private void tick() {
		if (System.currentTimeMillis() - lastDBLookup >= DB_LOOKUP_RATE) {
			lastDBLookup = System.currentTimeMillis();
		}
		if (!processingQueue.isEmpty()) {
			Main.debug("Queue!");
			String poll = (String) processingQueue.poll();
			String[] words = poll.split(Pattern.quote(" "));
			for (int i = 0; i < words.length; i++) {
				String finalWord = "";
				String finalWordWithSpaces = "";
				for (int j = i; j < words.length; j++) {
					finalWord = finalWord + words[j];
					finalWordWithSpaces = finalWordWithSpaces + words[j] + " ";
					Main.debug("Word: " + finalWord);
					Main.debug("Word + spaces: " + finalWordWithSpaces);
					if (NUMBER_ONLY.matcher(words[j].trim()).matches()) {
						finalWord = "";
						finalWordWithSpaces = "";
						i = j;
						continue;
					}
					if (contains(DICTONARY, finalWord) || contains(DICTONARY, finalWordWithSpaces)) {
						finalWord = "";
						finalWordWithSpaces = "";
						i = j;
						continue;
					}
					for (String bad : BLACKLIST) {
						if (bad.equalsIgnoreCase(finalWord.trim())) {
							registerWord(finalWordWithSpaces.trim());
							finalWord = "";
							finalWordWithSpaces = "";
							i = j;
							continue;
						}
						double simlarity = 100 - (jaroWinkler.distance(finalWord, bad) * 100);
						Main.debug("Matched: " + simlarity + " for word " + bad);
						if (simlarity >= Main.getInstance().getConfig().getDouble("similarity", 75)) {
							Main.debug("Match");
							registerWord(finalWordWithSpaces);
							i = j;
							finalWord = "";
							finalWordWithSpaces = "";
							return;
						}
					}
				}
			}
		}
	}

	private boolean contains(Collection<String> list, String words) {
		for (String s : list) {
			if (s.equalsIgnoreCase(words.trim())) {
				return true;
			}
		}
		return false;
	}

	private void registerWord(String word) {
		BLACKLIST.add(word);
		try {
			Files.write(Main.BAD_WORD_FILE.toPath(), Arrays.asList(new String[] { word }),
					new OpenOption[] { StandardOpenOption.APPEND });
		} catch (IOException e) {
			Main.getInstance().getLogger().log(Level.SEVERE, "Could not save '" + word + "' to BAD_WORD_FILE!", e);
		}
	}

	public void addString(String toProcess) {
		String lowerCase = strip(ChatColor.stripColor(toProcess).toLowerCase());
		processingQueue.add(lowerCase);
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

	public boolean hasBlacklistedWord(String message) {
		String lowerCase = ChatColor.stripColor(message).toLowerCase() + " ";
		for (String word : BLACKLIST) {
			if (lowerCase.contains(word.toLowerCase() + " ")) {
				return true;
			}
		}
		return false;
	}

	public boolean doLookup() {
		return doLookup;
	}

	public void doLookup(boolean doLookup) {
		this.doLookup = doLookup;
	}
}
