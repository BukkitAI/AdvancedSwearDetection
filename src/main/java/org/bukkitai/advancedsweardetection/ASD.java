package org.bukkitai.advancedsweardetection;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkitai.advancedsweardetection.ai.AIThread;
import org.bukkitai.advancedsweardetection.commands.Main;
import org.bukkitai.advancedsweardetection.commands.TabExecutors;
import org.bukkitai.advancedsweardetection.listeners.ChatListener;

import java.io.File;

public class ASD extends JavaPlugin {
	public static File DICTONARY_FILE;
	public static File BAD_WORD_FILE;
	public static File EXAMPLE_CONFIG_FILE;
	public static File DATA_FILE;
	
	private AIThread aiThread;
	private static ASD instance;

	public void onEnable() {
		instance = this;

		DICTONARY_FILE = new File(getDataFolder(), "dictonary.txt");
		BAD_WORD_FILE = new File(getDataFolder(), "bad_words.txt");
		EXAMPLE_CONFIG_FILE = new File(getDataFolder(), "example.yml");
		DATA_FILE = new File(getDataFolder(), "data.yml");

		if (!DICTONARY_FILE.exists())
			saveResource("dictonary.txt", false);

		if (!BAD_WORD_FILE.exists())
			saveResource("bad_words.txt", false);

		if (!EXAMPLE_CONFIG_FILE.exists())
			saveResource("example.yml", false);
		if (!DATA_FILE.exists())
			saveResource("data.yml", false);

		saveDefaultConfig();
		// @formatter:off
		getConfig().options()
				.header("#             _____ "
						+ "      /\\   |_   _|"
						+ "     /  \\    | |  "
						+ "    / /\\ \\   | |  "
						+ "   / ____ \\ _| |_ "
						+ "  /_/    \\_\\_____|"
						+ "                  "
						+ "Made by the BukkitAI team!");
		// @formatter:on
		aiThread = new AIThread();
		aiThread.start();

		ChatListener chatListener = new ChatListener();
		getServer().getPluginManager().registerEvents(chatListener, getInstance());
		this.getCommand("advancedsweardetection").setExecutor(new Main());
		this.getCommand("advancedsweardetection").setTabCompleter(new TabExecutors());

	}

	public void onDisable() {
		instance = null;
		aiThread.interrupt();
	}

	public AIThread getAIThread() {
		return aiThread;
	}

	public static ASD getInstance() {
		return instance;
	}

	public static void debug(String msg) {
		if (getInstance().getConfig().getBoolean("debug", true)) {
			getInstance().getLogger().info("[DEBUG] " + msg);
		}
	}
}
