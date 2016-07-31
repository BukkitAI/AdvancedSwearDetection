package org.bukkitai.advancedsweardetection.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkitai.advancedsweardetection.Main;

public class ChatListener implements Listener {

	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		Main.getInstance().getAIThread().addString(event.getMessage());
		if (Main.getInstance().getAIThread().hasBlacklistedWord(event.getMessage())) {
			event.setCancelled(true);
			event.getPlayer().sendMessage("Do not swear!");
		}
	}
}
