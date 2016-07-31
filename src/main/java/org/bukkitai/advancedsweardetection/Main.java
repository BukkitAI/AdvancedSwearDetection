package org.bukkitai.advancedsweardetection;

import java.io.File;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkitai.advancedsweardetection.ai.AIThread;
import org.bukkitai.advancedsweardetection.listeners.ChatListener;

public class Main extends JavaPlugin {
	public static File DICTONARY_FILE;
	public static File BAD_WORD_FILE;

	private AIThread aiThread;
	private static Main instance;
	private ChatListener chatListener;

	public void onEnable() {
		instance = this;

		DICTONARY_FILE = new File(getDataFolder(), "dictonary.txt");
		BAD_WORD_FILE = new File(getDataFolder(), "bad_words.txt");

		if (!DICTONARY_FILE.exists())
			saveResource("dictonary.txt", false);

		if (!BAD_WORD_FILE.exists())
			saveResource("bad_words.txt", false);

		aiThread = new AIThread();
		aiThread.start();

		chatListener = new ChatListener();
		Bukkit.getPluginManager().registerEvents(chatListener, getInstance());
	}

	public void onDisable() {
		instance = null;
		aiThread.interrupt();
	}

	public AIThread getAIThread() {
		return aiThread;
	}

	public static Main getInstance() {
		return instance;
	}
}
