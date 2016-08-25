package org.bukkitai.advancedsweardetection.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TabExecutors implements TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if (command.getName().equalsIgnoreCase("advancedsweardetection") && args.length == 1) {
			List<String> asd = new ArrayList<>();
			String[] commands = { "ver", "version", "test", "getCount", "get", "data","permissions","perm","color","scheme","reload","load"};
            Collections.addAll(asd, commands);
			return asd;
		}
		if (command.getName().equalsIgnoreCase("advancedsweardetection") && args[0].equalsIgnoreCase("color") || args[0].equalsIgnoreCase("scheme") && args.length == 2) {
			List<String> scheme = new ArrayList<>();
			String[] colors = {"aqua" , "blue" , "gray" , "green", "red" , "pink" , "yellow"};
            Collections.addAll(scheme, colors);
			return scheme;
		}
		if (command.getName().equalsIgnoreCase("advancedsweardetection") && args[0].equalsIgnoreCase("get") || args[0].equalsIgnoreCase("data") || args[0].equalsIgnoreCase("getCount") && args.length == 2) {
			List<String> data = new ArrayList<>();
			for (OfflinePlayer p : Bukkit.getOfflinePlayers()) {
				if (p.hasPlayedBefore()) {
					data.add(String.valueOf(p.getName()));
				}
			}
			return data;
		}
		return null;
	}

}
