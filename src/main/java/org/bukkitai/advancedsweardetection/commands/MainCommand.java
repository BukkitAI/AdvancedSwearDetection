package org.bukkitai.advancedsweardetection.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkitai.advancedsweardetection.Main;

public class MainCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		switch(args.length) {
		case 1:
			if(args[0].equalsIgnoreCase("version") || args[0].equalsIgnoreCase("ver")) {
				sender.sendMessage(ChatColor.GOLD + "Running version: " + Main.getInstance().getDescription().getVersion());
			} else sendHelp(sender);
			break;
		case 0:
			sendHelp(sender);
			break;
		default:
			if(args[0].equalsIgnoreCase("test")) {
				StringBuilder string = new StringBuilder();
				for(int i = 1; i < args.length; i++) {
					string.append(args[i]);
					string.append(' ');
				}
				String trim = string.toString().trim();
				if (Main.getInstance().getAIThread().hasBlacklistedWord(trim)) {
					sender.sendMessage(ChatColor.RED + "Found a swear word!");
				} else {
					Main.getInstance().getAIThread().addString(trim);
					sender.sendMessage(ChatColor.RED + "Nothing found.");
				}
			} else sendHelp(sender);
			break;
		}
		return true;
	}

	private void sendHelp(CommandSender sender) {
		sender.sendMessage(new String[] {
				ChatColor.GOLD + "AdvancedSwearDetection by BukkitAI Team",
				ChatColor.GOLD + "Usage: ",
				ChatColor.GOLD + "/asd - Prints this page",
				ChatColor.GOLD + "/asd ver|version - Prints the version",
				ChatColor.GOLD + "/asd test Wo Rd S - Tests a string against the database"
		});
	}
}
