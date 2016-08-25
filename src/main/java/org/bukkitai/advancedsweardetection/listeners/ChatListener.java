package org.bukkitai.advancedsweardetection.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkitai.advancedsweardetection.ASD;
import org.bukkitai.advancedsweardetection.AIConfig;

public class ChatListener implements Listener {
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		AIConfig data = new AIConfig("data.yml", ASD.getInstance());
		String path = String.valueOf(event.getPlayer().getName());
		
			for (String players: ASD.getInstance().getConfig().getStringList("whitelist")) {
				if (players.equalsIgnoreCase(event.getPlayer().getName())) return;
			}

		if (ASD.getInstance().getAIThread().hasBlacklistedWord(event.getMessage())) {
			event.setCancelled(true);
			try {
				int playerCount = data.getYaml().getInt(path);
				ASD.debug(String.valueOf(playerCount));
				data.getYaml().set(path, playerCount + 1);
				data.saveYaml();
				data.reloadYaml();
			} catch (NullPointerException ignored) {
				int playerCount = data.getYaml().getInt(path);
				ASD.debug(event.getPlayer().getName() + " " + String.valueOf(playerCount));
				data.getYaml().createSection(path);
				data.getYaml().set(path, playerCount + 1);
				data.saveYaml();
				data.reloadYaml();
			}
			event.getPlayer().sendMessage(ChatColor.RED + "Do not swear!!!");
		} else ASD.getInstance().getAIThread().addString(event.getMessage());
	}
}
