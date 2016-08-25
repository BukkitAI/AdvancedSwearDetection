package org.bukkitai.advancedsweardetection.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkitai.advancedsweardetection.ASD;
import org.bukkitai.advancedsweardetection.AIConfig;

public class ChatListener implements Listener {

    private AIConfig data = new AIConfig("data.yml", ASD.getInstance());

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        for (String players : ASD.getInstance().getConfig().getStringList("whitelist")) {
            if (players.equalsIgnoreCase(event.getPlayer().getName())) return;
        }

        String path = String.valueOf(event.getPlayer().getUniqueId());
        if (ASD.getInstance().getAIThread().hasBlacklistedWord(event.getMessage())) {
            event.setCancelled(true);
            int playerCount = data.getYaml().getInt(path);
            ASD.debug(event.getPlayer().getName() + " now has a score of " + ++playerCount);
            data.getYaml().set(path, playerCount);
            data.saveYaml();
            event.getPlayer().sendMessage(ChatColor.RED + "Do not swear!!!");
        } else ASD.getInstance().getAIThread().addString(event.getMessage());
    }
}
