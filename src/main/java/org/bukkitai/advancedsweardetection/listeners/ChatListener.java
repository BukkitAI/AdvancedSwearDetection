package org.bukkitai.advancedsweardetection.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkitai.advancedsweardetection.Main;
import org.bukkitai.advancedsweardetection.AIConfig;

public class ChatListener implements Listener {
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		AIConfig data = new AIConfig("data.yml", Main.getInstance());
		String path = String.valueOf(event.getPlayer().getUniqueId());
		
			for (String players: Main.getInstance().getConfig().getStringList("whitelist")) {
				if (players.equalsIgnoreCase(event.getPlayer().getName())) return;
			}

		if (Main.getInstance().getAIThread().hasBlacklistedWord(event.getMessage())) {
			event.setCancelled(true);
			try {
				int playerCount = data.getYaml().getInt(path);
				data.getYaml().set(path, playerCount++);
				data.saveYaml();
				data.reloadYaml();
			} catch (NullPointerException e) {
				int playerCount = data.getYaml().getInt(path);
				data.getYaml().createSection(path);
				data.getYaml().set(path, playerCount++);
				data.saveYaml();
				data.reloadYaml();
			}
			event.getPlayer().sendMessage(ChatColor.RED + "Do not swear!!!");
		} else Main.getInstance().getAIThread().addString(event.getMessage());
	}
}
