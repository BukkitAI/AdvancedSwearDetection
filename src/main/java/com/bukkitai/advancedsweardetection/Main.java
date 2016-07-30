package com.bukkitai.advancedsweardetection;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.bukkitai.advancedsweardetection.ai.AIThread;
import com.bukkitai.advancedsweardetection.listeners.ChatListener;

public class Main extends JavaPlugin {
	private AIThread aiThread;
	private static Main instance;
	private ChatListener chatListener;
	
	public void onEnable() {
		instance = this;
		
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
