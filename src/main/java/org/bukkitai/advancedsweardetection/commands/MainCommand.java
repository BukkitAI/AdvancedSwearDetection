packagepackage org.bukkitai.advancedsweardetection.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkitai.advancedsweardetection.AIConfig;
import org.bukkitai.advancedsweardetection.ASD;

public class MainCommand implements CommandExecutor {

	public final AIConfig data = new AIConfig("data.yml", ASD.getInstance());
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		try {
			args[0].toLowerCase();
		} catch (ArrayIndexOutOfBoundsException e) {
			sendHelp(sender);
			return true;
		}
			if (args[0].equalsIgnoreCase("version") || args[0].equalsIgnoreCase("ver")) {
				sender.sendMessage(ChatColor.getByChar(ASD.getInstance().getConfig().getString("color1").charAt(0))
						+ "Running version: "
						+ ChatColor.getByChar(ASD.getInstance().getConfig().getString("color2").charAt(0))
						+ ASD.getInstance().getDescription().getVersion());
				return true;
			}
			if (args[0].equalsIgnoreCase("getCount") || args[0].equalsIgnoreCase("get")
					|| args[0].equalsIgnoreCase("data")) {
				try {
					args[1].toLowerCase();
				} catch (ArrayIndexOutOfBoundsException e) {
					sender.sendMessage(ChatColor.RED + "Syntax: /asd get {player}");
					return true;
				} 
				checkPerm(sender, "ASD.data");
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
				sender.sendMessage(
						ChatColor.getByChar(ASD.getInstance().getConfig().get("color1").toString().charAt(0))
								 + args[1] + ": "
								+ ChatColor.getByChar(ASD.getInstance().getConfig().getString("color2").charAt(0))
								+ String.valueOf(data.getYaml().getInt(path)));
				return true;
			}
			if (args[0].equalsIgnoreCase("permissions") || args[0].equalsIgnoreCase("perm")) {
				checkPerm(sender, "ASD.perm");
				try {
					sender.sendMessage(new String[] {
							ChatColor.getByChar(ASD.getInstance().getConfig().getString("color1").charAt(0)) + "AdvancedSwearDetection by BukkitAI Team",
							ChatColor.getByChar(ASD.getInstance().getConfig().getString("color1").charAt(0)) + "Permissions: ",
							ChatColor.getByChar(ASD.getInstance().getConfig().getString("color1").charAt(0)) + "/asd - " + ChatColor.getByChar(ASD.getInstance().getConfig().getString("color2").charAt(0)) + "ASD.*",
							ChatColor.getByChar(ASD.getInstance().getConfig().getString("color1").charAt(0)) + "/asd ver|version - " + ChatColor.getByChar(ASD.getInstance().getConfig().getString("color2").charAt(0)) + "No permission",
							ChatColor.getByChar(ASD.getInstance().getConfig().getString("color1").charAt(0)) + "/asd test Wo Rd S - " + ChatColor.getByChar(ASD.getInstance().getConfig().getString("color2").charAt(0)) + "ASD.test",
							ChatColor.getByChar(ASD.getInstance().getConfig().getString("color1").charAt(0)) + "/asd get|getCount|data - " + ChatColor.getByChar(ASD.getInstance().getConfig().getString("color2").charAt(0)) + "ASD.data",
							ChatColor.getByChar(ASD.getInstance().getConfig().getString("color1").charAt(0)) + "/asd permissions | perm - " + ChatColor.getByChar(ASD.getInstance().getConfig().getString("color2").charAt(0)) + "ASD.perm",
							ChatColor.getByChar(ASD.getInstance().getConfig().getString("color1").charAt(0)) + "/asd color|scheme - " + ChatColor.getByChar(ASD.getInstance().getConfig().getString("color2").charAt(0)) + "ASD.color",
							ChatColor.getByChar(ASD.getInstance().getConfig().getString("color1").charAt(0)) + "/asd reload | load - z" + ChatColor.getByChar(ASD.getInstance().getConfig().getString("color2").charAt(0)) + "ASD.load"});
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
				return true;
			} 
			if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("load")) {
				checkPerm(sender, "ASD.reload");
				try {
					sender.sendMessage(
							ChatColor.getByChar(ASD.getInstance().getConfig().getString("color1").charAt(0)) + "Reloading...");
					ASD.getInstance().reloadConfig();
					data.reloadYaml();
				} catch (NullPointerException e) {
					sender.sendMessage(ChatColor.DARK_AQUA + "Reloading...");
					ASD.getInstance().reloadConfig();
					data.reloadYaml();
					sender.sendMessage(ChatColor.DARK_AQUA + "Reloaded.");

				}
				return true;
			} 
			if (args[0].equalsIgnoreCase("color") || args[0].equalsIgnoreCase("scheme")) {
				try {
					args[1].toLowerCase();
				} catch (ArrayIndexOutOfBoundsException e) {
					sender.sendMessage(ChatColor.RED + "Syntax: /asd color {colors}");
					return true;
				} 
				checkPerm(sender, "ASD.color");
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
			} 
			if (args[0].equalsIgnoreCase("test")) {
				checkPerm(sender, "ASD.test");
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
			sendHelp(sender);
			return true;
	}

	private void sendHelp(CommandSender sender) {
		try {
			sender.sendMessage(new String[] {
					ChatColor.getByChar(ASD.getInstance().getConfig().getString("color1").charAt(0)) + "AdvancedSwearDetection by BukkitAI Team",
					ChatColor.getByChar(ASD.getInstance().getConfig().getString("color1").charAt(0)) + "Usage: ",
					ChatColor.getByChar(ASD.getInstance().getConfig().getString("color1").charAt(0)) + "/asd - " + ChatColor.getByChar(ASD.getInstance().getConfig().getString("color2").charAt(0)) + "Prints this page",
					ChatColor.getByChar(ASD.getInstance().getConfig().getString("color1").charAt(0)) + "/asd ver|version - " + ChatColor.getByChar(ASD.getInstance().getConfig().getString("color2").charAt(0)) + "Prints the version",
					ChatColor.getByChar(ASD.getInstance().getConfig().getString("color1").charAt(0)) + "/asd test Wo Rd S - " + ChatColor.getByChar(ASD.getInstance().getConfig().getString("color2").charAt(0)) + "Tests a string against the database",
					ChatColor.getByChar(ASD.getInstance().getConfig().getString("color1").charAt(0)) + "/asd get|getCount|data - " + ChatColor.getByChar(ASD.getInstance().getConfig().getString("color2").charAt(0)) + "Gets a player's curse count.",
					ChatColor.getByChar(ASD.getInstance().getConfig().getString("color1").charAt(0)) + "/asd permissions|perm - " + ChatColor.getByChar(ASD.getInstance().getConfig().getString("color2").charAt(0)) + "Prints all permissions.",
					ChatColor.getByChar(ASD.getInstance().getConfig().getString("color1").charAt(0)) + "/asd color|scheme - " + ChatColor.getByChar(ASD.getInstance().getConfig().getString("color2").charAt(0))  + "Changes the color scheme from a pallette selected by the devs.",
					ChatColor.getByChar(ASD.getInstance().getConfig().getString("color1").charAt(0)) + "/asd reload|load -" + ChatColor.getByChar(ASD.getInstance().getConfig().getString("color2").charAt(0)) + "Reloads all configs."
					
			});

		} catch (NullPointerException e) {
			sender.sendMessage(
					new String[] { 
					ChatColor.DARK_AQUA + "AdvancedSwearDetection by BukkitAI Team",
							ChatColor.DARK_AQUA + "Usage: ",
							ChatColor.DARK_AQUA + "/asd - " + ChatColor.AQUA + "Prints this page",
							ChatColor.DARK_AQUA + "/asd ver|version - " + ChatColor.AQUA + "Prints the version",
							ChatColor.DARK_AQUA + "/asd test [Wo Rd S] - " + ChatColor.AQUA + "Tests a string against the database",
							ChatColor.DARK_AQUA + "/asd get|getCount|data [player] - " + ChatColor.AQUA + "Gets a player's curse count.",
							ChatColor.DARK_AQUA + "/asd permissions|perm - " + ChatColor.AQUA + "Prints all permissions.",
							ChatColor.DARK_AQUA + "/asd color|scheme [palette] -" + ChatColor.AQUA + "Changes the color scheme from a pallette selected by the devs.",
							ChatColor.DARK_AQUA + "/asd reload|load -" + ChatColor.AQUA + "Reloads all configs."							
					});
			ASD.getInstance().getConfig().createSection("color1");
			ASD.getInstance().getConfig().createSection("color2");
			ASD.getInstance().getConfig().set("color1", "3");
			ASD.getInstance().getConfig().set("color2", "a");
			ASD.getInstance().saveConfig();
			ASD.getInstance().reloadConfig();
		}

	}

	private boolean hasPerm(CommandSender sender, String perm) {
		if (sender.isOp())
			return true;
		if (!sender.hasPermission(perm))
			return false;
		if (sender.hasPermission(perm))
			return true;
		else {
			return false;
		}
	}

	private void checkPerm(CommandSender sender, String perm) {
		if (!hasPerm(sender, perm)) {
			sender.sendMessage(
					ChatColor.DARK_RED + "ERROR:" + ChatColor.RED + "You do not have valid permissions to run this!");
			return;
		}

	}
}
