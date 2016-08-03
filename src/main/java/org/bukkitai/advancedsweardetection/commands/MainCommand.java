package org.bukkitai.advancedsweardetection.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkitai.advancedsweardetection.Main;
import org.bukkitai.advancedsweardetection.AIConfig;

public class MainCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		switch(args.length) {
		case 1:
			if(args[0].equalsIgnoreCase("version") || args[0].equalsIgnoreCase("ver")) {
				sender.sendMessage(ChatColor.DARK_AQUA + "Running version: " + ChatColor.AQUA + Main.getInstance().getDescription().getVersion());
			} else sendHelp(sender);
			break;
		case 2:
			if (args[0].equalIgnoreCase("getCount") || args[0].equalsIgnoreCase("get") || args[0].equalsIgnoreCase("data")) {
				if (!args.length == 1) {
					sender.sendMessage(ChatColor.DARK_RED + "ERROR:" + ChatColor.RED + "Syntax: /get [PLAYER] || /getCount [PLAYER]");
					break;
				} else {
				AIConfig data = new AICONFIG("data.yml", Main.getInstance());
				String path = String.valueOf(args[1]);
				if (!sender.hasPermission("AI.checkPlayers")) break;
				if (!Bukkit.getOfflinePlayer(args[1]).hasPlayedBefore()) {
					sender.sendMessage(ChatColor.DARK_RED + "ERROR:" + ChatColor.RED + "That player has never played before!");
					break;
				}
				try {
					int playerCount = data.getYaml().getInt(path);
				} catch (NullPointerException e) {
					int playerCount = data.getYaml().getInt(path);
					data.getYaml().getConfig().createSection(path);
				}
				sender.sendMessage(ChatColor.DARK_AQUA + "Player: " +  args[1] + ":" + ChatColor.AQUA + String.valueOf(data.getYaml().getInt(path)));
				}
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
		sender.sendMessage(new String[] { ChatColor.DARK_AQUA + "AdvancedSwearDetection by BukkitAI Team",
				ChatColor.DARK_AQUA + "Usage: ", 
				ChatColor.DARKAQUA + "/asd - " + ChatColor.AQUA + "Prints this page",
				ChatColor.DARK_AQUA + "/asd ver|version - " + ChatColor.AQUA + "Prints the version",
				ChatColor.DARK_AQUA + "/asd test Wo Rd S - " + ChatColor.AQUA + "Tests a string against the database",
				ChatColor.DARK_AQUA + "/asg getCount|get|data - " + ChatColor.AQUA + "Gets the amount of times a player sweared."});
	}
}
