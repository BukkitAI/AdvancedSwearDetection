package org.bukkitai.advancedsweardetection.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class TabExecutors implements TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if (command.getName().equalsIgnoreCase("asd")) {
			List<String> asd = new ArrayList<>();
			String[] commands = { "ver", "version", "test", "getCount", "get", "data" };
			for (String stringCommand : commands) {
				asd.add(stringCommand);
			}
			return asd;
		}
		return null;
	}

}