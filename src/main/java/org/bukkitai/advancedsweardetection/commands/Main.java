package org.bukkitai.advancedsweardetection.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkitai.advancedsweardetection.AIConfig;
import org.bukkitai.advancedsweardetection.ASD;

public class Main implements CommandExecutor {
	public final AIConfig data = new AIConfig("data.yml", ASD.getInstance());
			@SuppressWarnings("deprecation")
			@Override
			public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
				try {
					args[0].toLowerCase();
				} catch (ArrayIndexOutOfBoundsException e) {
					CMDUtils.sendHelp(sender);
					return true;
				}
				/**
				 * @author TheEnderCrafter9
				 * DATA COMMAND
				 */
				if (args[0].equalsIgnoreCase("getCount") || args[0].equalsIgnoreCase("get")
						|| args[0].equalsIgnoreCase("data")) {
					try {
						args[1].toLowerCase();
					} catch (ArrayIndexOutOfBoundsException e) {
						sender.sendMessage(ChatColor.RED + "Syntax: /asd get {player}");
						return true;
					}
					if (!CMDUtils.checkPerm(sender, "ASD.data")) return true;
					String path = String.valueOf(args[1]);
					if (!Bukkit.getOfflinePlayer(args[1]).hasPlayedBefore()) {
						sender.sendMessage(
								ChatColor.DARK_RED + "ERROR: " + ChatColor.RED + "That player has never played before!");
						return true;
					}
					try {
						data.getYaml().getInt(path);
						data.saveYaml();
						data.reloadYaml();
					} catch (NullPointerException e) {
						data.getYaml().createSection(path);
						data.saveYaml();
						data.reloadYaml();
					}
					sender.sendMessage(CMDUtils.sendColorMessage(args[1] + " : ", Colors.MAIN)
									 + CMDUtils.sendColorMessage(String.valueOf(data.getYaml().getInt(path)), Colors.SUB));
					return true;
				}
				
				/**
				 * @author TheEnderCrafter9
				 * PERM COMMAND
				 */
				
				if (args[0].equalsIgnoreCase("permissions") || args[0].equalsIgnoreCase("perm")) {
					if (!CMDUtils.checkPerm(sender, "ASD.perm")) return true;
					try {
						sender.sendMessage(new String[] {
								CMDUtils.sendColorMessage("AdvancedSwearDetection by BukkitAI Team", Colors.MAIN),
								CMDUtils.sendColorMessage("Permissions: ", Colors.MAIN),
								CMDUtils.sendColorMessage("/asd - " , Colors.MAIN) + CMDUtils.sendColorMessage("ASD.*", Colors.SUB),
								CMDUtils.sendColorMessage("/asd ver|version - ", Colors.MAIN) + CMDUtils.sendColorMessage("No permission", Colors.SUB),
								CMDUtils.sendColorMessage("/asd test Wo Rd S - ", Colors.MAIN) +  CMDUtils.sendColorMessage("ASD.test", Colors.SUB),
								CMDUtils.sendColorMessage("/asd get|getCount|data - ", Colors.MAIN) + CMDUtils.sendColorMessage("ASD.data", Colors.SUB),
								CMDUtils.sendColorMessage("/asd permissions | perm - ", Colors.MAIN) + CMDUtils.sendColorMessage("ASD.perm", Colors.SUB),
								CMDUtils.sendColorMessage("/asd color|scheme - ", Colors.MAIN) + CMDUtils.sendColorMessage("ASD.color", Colors.SUB),
								CMDUtils.sendColorMessage("/asd reload | load - ", Colors.MAIN) + CMDUtils.sendColorMessage("ASD.load", Colors.SUB),
						});
					} catch (NullPointerException e) {
						sender.sendMessage(
								new String[] { ChatColor.DARK_AQUA + "AdvancedSwearDetection by BukkitAI Team",
										ChatColor.DARK_AQUA + "Usage: ",
										ChatColor.DARK_AQUA + "/asd - " + ChatColor.AQUA + "ASD.*",
										ChatColor.DARK_AQUA + "/asd ver|version - " + ChatColor.AQUA + "No permission",
										ChatColor.DARK_AQUA + "/asd test Wo Rd S - " + ChatColor.AQUA + "ASD.test",
										ChatColor.DARK_AQUA + "/asd get|getCount|data - " + ChatColor.AQUA + "ASD.perm",
										ChatColor.DARK_AQUA + "/asd color|scheme - " + ChatColor.DARK_AQUA + "ASD.color",
										ChatColor.DARK_AQUA + "/asd reload | load - " + ChatColor.DARK_AQUA + "ASD.load"
								
								});
						ASD.getInstance().getConfig().createSection("color1");
						ASD.getInstance().getConfig().createSection("color2");
						ASD.getInstance().getConfig().set("color1", "3");
						ASD.getInstance().getConfig().set("color2", "b");
						ASD.getInstance().saveConfig();
						ASD.getInstance().reloadConfig();
						return true;
					}
				}
				/**
				 * @author ArsenArsen
				 * TEST COMMAND
				 */
				if (args[0].equalsIgnoreCase("test")) {
					if (!CMDUtils.checkPerm(sender, "ASD.test")) return true;
					StringBuilder string = new StringBuilder();
					for (int i = 1; i < args.length; i++) {
						string.append(args[i]);
						string.append(' ');
					}
					String trim = string.toString().trim();
					if (ASD.getInstance().getAIThread().hasBlacklistedWord(trim)) {
						sender.sendMessage(ChatColor.RED + "Found a swear word!");
						return true;
					} else {
						ASD.getInstance().getAIThread().addString(trim);
						sender.sendMessage(ChatColor.getByChar(ASD.getInstance().getConfig().getString("color1").charAt(0)) + "Nothing found.");
						return true;
					}
				}				
				
			/**
			 * @author TheEnderCrafter9
			 * LOAD COMMAND
			 */
			if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("load")) {
				if (!CMDUtils.checkPerm(sender, "ASD.load")) return true;
				try {
					sender.sendMessage(CMDUtils.sendColorMessage("Reloading...", Colors.MAIN));
					ASD.getInstance().reloadConfig();
					data.reloadYaml();
					sender.sendMessage(CMDUtils.sendColorMessage("Reloaded.", Colors.SUB));
				} catch (NullPointerException e) {
					sender.sendMessage(ChatColor.DARK_AQUA + "Reloading...");
					ASD.getInstance().reloadConfig();
					data.reloadYaml();
					sender.sendMessage(ChatColor.DARK_AQUA + "Reloaded.");

				}
			}
			/**
			 * @author TheEnderCrafter9
			 * SCHEME COMMAND
			 */
			if (args[0].equalsIgnoreCase("color") || args[0].equalsIgnoreCase("scheme")) {
				try {
					args[0].toLowerCase();
					args[1].toLowerCase();
				} catch (ArrayIndexOutOfBoundsException e) {
					sender.sendMessage(ChatColor.RED + "Syntax: /asd color {colors}");
					return true;
				} 
				if (!CMDUtils.checkPerm(sender, "ASD.color")) return true;
				String[] colors = { "blue", "green", "aqua", "red", "pink", "yellow", "gray" };
				String path1 = "color1";
				String path2 = "color2";
				if (args[1].equalsIgnoreCase(colors[0])) {
					try {
						ASD.getInstance().getConfig().set(path1, "1");
						ASD.getInstance().getConfig().set(path2, "9");
						ASD.getInstance().saveConfig();
						ASD.getInstance().reloadConfig();
						sender.sendMessage(ChatColor.getByChar(ASD.getInstance().getConfig().getString("color1").charAt(0)) +"Set pallete to " + args[1]);
					} catch (NullPointerException e) {
						ASD.getInstance().getConfig().createSection("color1");
						ASD.getInstance().getConfig().createSection("color2");
						ASD.getInstance().getConfig().set("color1", "1");
						ASD.getInstance().getConfig().set("color2", "9");
						ASD.getInstance().saveConfig();
						ASD.getInstance().reloadConfig();
						sender.sendMessage(ChatColor.getByChar(ASD.getInstance().getConfig().getString("color1").charAt(0)) +"Set pallete to " + args[1]);

					}
					return true;
				}
				
				if (args[1].equalsIgnoreCase(colors[1])) {
					try {
						ASD.getInstance().getConfig().set(path1, "2");
						ASD.getInstance().getConfig().set(path2, "a");
						ASD.getInstance().saveConfig();
						ASD.getInstance().reloadConfig();
						sender.sendMessage(ChatColor.getByChar(ASD.getInstance().getConfig().getString("color1").charAt(0)) +"Set pallete to " + args[1]);
					} catch (NullPointerException e) {
						ASD.getInstance().getConfig().createSection("color1");
						ASD.getInstance().getConfig().createSection("color2");
						ASD.getInstance().getConfig().set("color1", "2");
						ASD.getInstance().getConfig().set("color2", "a");
						ASD.getInstance().saveConfig();
						ASD.getInstance().reloadConfig();
						sender.sendMessage(ChatColor.getByChar(ASD.getInstance().getConfig().getString("color1").charAt(0)) +"Set pallete to " + args[1]);
						}
					return true;
				}
				if (args[1].equalsIgnoreCase(colors[2])) {
					try {
						ASD.getInstance().getConfig().set(path1, "3");
						ASD.getInstance().getConfig().set(path2, "b");
						ASD.getInstance().saveConfig();
						ASD.getInstance().reloadConfig();
						sender.sendMessage(ChatColor.getByChar(ASD.getInstance().getConfig().getString("color1").charAt(0)) +"Set pallete to " + args[1]);
					} catch (NullPointerException e) {
						ASD.getInstance().getConfig().createSection("color1");
						ASD.getInstance().getConfig().createSection("color2");
						ASD.getInstance().getConfig().set(path1, "3");
						ASD.getInstance().getConfig().set(path2, "b");
						ASD.getInstance().saveConfig();
						ASD.getInstance().reloadConfig();
						sender.sendMessage(ChatColor.getByChar(ASD.getInstance().getConfig().getString("color1").charAt(0)) +"Set pallete to " + args[1]);
					}					
					return true;

				}
				if (args[1].equalsIgnoreCase(colors[3])) {
					try {
						ASD.getInstance().getConfig().set(path1, "4");
						ASD.getInstance().getConfig().set(path2, "c");
						ASD.getInstance().saveConfig();
						ASD.getInstance().reloadConfig();
						sender.sendMessage(ChatColor.getByChar(ASD.getInstance().getConfig().getString("color1").charAt(0)) +"Set pallete to " + args[1]);
					} catch (NullPointerException e) {
						ASD.getInstance().getConfig().createSection("color1");
						ASD.getInstance().getConfig().createSection("color2");
						ASD.getInstance().getConfig().set("color1", "4");
						ASD.getInstance().getConfig().set("color2", "c");
						ASD.getInstance().saveConfig();
						ASD.getInstance().reloadConfig();
						sender.sendMessage(ChatColor.getByChar(ASD.getInstance().getConfig().getString("color1").charAt(0)) +"Set pallete to " + args[1]);
						}
					return true;

				}
				if (args[1].equalsIgnoreCase(colors[4])) {
					try {
						ASD.getInstance().getConfig().set(path1, "5");
						ASD.getInstance().getConfig().set(path2, "d");
						ASD.getInstance().saveConfig();
						ASD.getInstance().reloadConfig();
						sender.sendMessage(ChatColor.getByChar(ASD.getInstance().getConfig().getString("color1").charAt(0)) +"Set pallete to " + args[1]);
					} catch (NullPointerException e) {
						ASD.getInstance().getConfig().createSection("color1");
						ASD.getInstance().getConfig().createSection("color2");
						ASD.getInstance().getConfig().set("color1", "5");
						ASD.getInstance().getConfig().set("color2", "d");
						ASD.getInstance().saveConfig();
						ASD.getInstance().reloadConfig();
						sender.sendMessage(ChatColor.getByChar(ASD.getInstance().getConfig().getString("color1").charAt(0)) +"Set pallete to " + args[1]);
						}
					return true;
				}
				if (args[1].equalsIgnoreCase(colors[5])) {
					try {
						ASD.getInstance().getConfig().set(path1, "6");
						ASD.getInstance().getConfig().set(path2, "e");
						ASD.getInstance().saveConfig();
						ASD.getInstance().reloadConfig();
						sender.sendMessage(ChatColor.getByChar(ASD.getInstance().getConfig().getString("color1").charAt(0)) +"Set pallete to " + args[1]);
					} catch (NullPointerException e) {
						ASD.getInstance().getConfig().createSection("color1");
						ASD.getInstance().getConfig().createSection("color2");
						ASD.getInstance().getConfig().set("color1", "6");
						ASD.getInstance().getConfig().set("color2", "e");
						ASD.getInstance().saveConfig();
						ASD.getInstance().reloadConfig();
						sender.sendMessage(ChatColor.getByChar(ASD.getInstance().getConfig().getString("color1").charAt(0)) +"Set pallete to " + args[1]);
						}
					return true;
				}
				if (args[1].equalsIgnoreCase(colors[6])) {
					try {
						ASD.getInstance().getConfig().set(path1, "7");
						ASD.getInstance().getConfig().set(path2, "8");
						ASD.getInstance().saveConfig();
						ASD.getInstance().reloadConfig();
						sender.sendMessage(ChatColor.getByChar(ASD.getInstance().getConfig().getString("color1").charAt(0)) +"Set pallete to " + args[1]);
					} catch (NullPointerException e) {
						ASD.getInstance().getConfig().createSection("color1");
						ASD.getInstance().getConfig().createSection("color2");
						ASD.getInstance().getConfig().set("color1", "7");
						ASD.getInstance().getConfig().set("color2", "8");
						ASD.getInstance().saveConfig();
						ASD.getInstance().reloadConfig();
						sender.sendMessage(ChatColor.getByChar(ASD.getInstance().getConfig().getString("color1").charAt(0)) +"Set pallete to " + args[1]);
						}
					return true;
				}
				CMDUtils.sendHelp(sender);
			}
			/**
			 * @author ARSENARSEN1
			 * VER COMMAND
			 */
			if (args[0].equalsIgnoreCase("version") || args[0].equalsIgnoreCase("ver")) {
			sender.sendMessage(CMDUtils.sendColorMessage("Running version: ", Colors.MAIN) + CMDUtils.sendColorMessage(ASD.getInstance().getDescription().getVersion(), Colors.SUB));
			return true;
			}
			return true;
		}
}
