package com.bukkitai.advancedsweardetection.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.bukkitai.advancedsweardetection.Main;

public class ChatListener implements Listener {

	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		Main.getInstance().getAIThread().addString(event.getMessage());
		for(String word : event.getMessage().split("\\s")) {
			if(Main.getInstance().getAIThread().isBlacklisted(word)) {
				event.setCancelled(true);
				event.getPlayer().sendMessage( ChatColor.DARK_RED + "Do not swear!");
				return;
			}
		}
	}
}
