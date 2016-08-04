packagepackage org.bukkitai.advancedsweardetection.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkitai.advancedsweardetection.AIConfig;
import org.bukkitai.advancedsweardetection.Main;

public class MainCommand implements CommandExecutor {
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		switch (args.length) {
		case 1:
			if (args[0].equalsIgnoreCase("version") || args[0].equalsIgnoreCase("ver")) {
				sender.sendMessage(ChatColor.DARK_AQUA + "Running version: " + ChatColor.AQUA
						+ Main.getInstance().getDescription().getVersion());
			} else
				sendHelp(sender);
			break;
		case 2:
			if (args[0].equalsIgnoreCase("getCount") || args[0].equalsIgnoreCase("get")
					|| args[0].equalsIgnoreCase("data")) {
				if (args.length < 1) {
					sender.sendMessage(ChatColor.DARK_RED + "ERROR:" + ChatColor.RED
							+ "Syntax: /get [PLAYER] || /getCount [PLAYER]");
					return true;
				}
				AIConfig data = new AIConfig("data.yml", Main.getInstance());
				String path = String.valueOf(args[1]);
				if (!sender.hasPermission("ASD.data"))
					break;
				if (!Bukkit.getOfflinePlayer(args[1]).hasPlayedBefore()) {
					sender.sendMessage(
							ChatColor.DARK_RED + "ERROR:" + ChatColor.RED + "That player has never played before!");
					break;
				}
				try {
					data.getYaml().getInt(path);
					data.reloadYaml();
				} catch (NullPointerException e) {
					data.getYaml().createSection(path);
					data.reloadYaml();
				}
				sender.sendMessage(ChatColor.getByChar(Main.getInstance().getConfig().get("color1").toString().charAt(0)) + "Player: "
						+ args[1] + ":" + ChatColor.getByChar(Main.getInstance().getConfig().get("color2").toString())
						+ String.valueOf(data.getYaml().getInt(path)));
			} else
				sendHelp(sender);
			break;
		case 3:
			if (args[0].equalsIgnoreCase("permissions") || args[0].equalsIgnoreCase("perm")) {
				if (!(sender.hasPermission("ASD.perm"))) break;
				try {
				sender.sendMessage(new String[] {
						org.bukkit.ChatColor.getByChar(Main.getInstance().getConfig().getString("color1")) + "AdvancedSwearDetection by BukkitAI Team",
						org.bukkit.ChatColor.getByChar(Main.getInstance().getConfig().getString("color1")) + "Permissions: ",
						org.bukkit.ChatColor.getByChar(Main.getInstance().getConfig().getString("color1")) + "/asd - " + org.bukkit.ChatColor.getByChar(Main.getInstance().getConfig().getString("color2")) + "ASD.*",
						org.bukkit.ChatColor.getByChar(Main.getInstance().getConfig().getString("color1")) + "/asd ver|version - " + org.bukkit.ChatColor.getByChar(Main.getInstance().getConfig().getString("color2")) + "No permission",
						org.bukkit.ChatColor.getByChar(Main.getInstance().getConfig().getString("color1")) + "/asd test Wo Rd S - " + org.bukkit.ChatColor.getByChar(Main.getInstance().getConfig().getString("color2")) + "ASD.test",
						org.bukkit.ChatColor.getByChar(Main.getInstance().getConfig().getString("color1")) + "/asd get|getCount|data - "+ org.bukkit.ChatColor.getByChar(Main.getInstance().getConfig().getString("color2")) + "ASD.data",
						org.bukkit.ChatColor.getByChar(Main.getInstance().getConfig().getString("color1")) + "/asd permissions | perm" + org.bukkit.ChatColor.getByChar(Main.getInstance().getConfig().getString("color2")) + "ASD.perm"});
} catch (NullPointerException e) {
					sender.sendMessage(new String[] {
							org.bukkit.ChatColor.DARK_AQUA + "AdvancedSwearDetection by BukkitAI Team",
							org.bukkit.ChatColor.DARK_AQUA + "Usage: ",
							org.bukkit.ChatColor.DARK_AQUA + "/asd - " + org.bukkit.ChatColor.AQUA + "Prints this page",
							org.bukkit.ChatColor.DARK_AQUA + "/asd ver|version - " + org.bukkit.ChatColor.AQUA + "Prints the version",
							org.bukkit.ChatColor.DARK_AQUA + "/asd test Wo Rd S - " + org.bukkit.ChatColor.AQUA + "Tests a string against the database",
							org.bukkit.ChatColor.DARK_AQUA + "/asd get|getCount|data - " + org.bukkit.ChatColor.AQUA + "Gets a player's curse count." });
							Main.getInstance().getConfig().createSection("color1");
							Main.getInstance().getConfig().set("color1", "3");
							Main.getInstance().getConfig().set("color2", "a");
						}

			}
		case 0:
			sendHelp(sender);
			break;
		default:
			if (args[0].equalsIgnoreCase("test")) {
				if (!(sender.hasPermission("ASD.test"))) break;
				StringBuilder string = new StringBuilder();
				for (int i = 1; i < args.length; i++) {
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
			} else
				sendHelp(sender);
			break;
		}
		return true;
	}

	private void sendHelp(CommandSender sender) {
		try {
		sender.sendMessage(new String[] {
				org.bukkit.ChatColor.getByChar(Main.getInstance().getConfig().getString("color1")) + "AdvancedSwearDetection by BukkitAI Team",
				org.bukkit.ChatColor.getByChar(Main.getInstance().getConfig().getString("color1")) + "Usage: ",
				org.bukkit.ChatColor.getByChar(Main.getInstance().getConfig().getString("color1")) + "/asd - " + org.bukkit.ChatColor.getByChar(Main.getInstance().getConfig().getString("color2")) + "Prints this page",
				org.bukkit.ChatColor.getByChar(Main.getInstance().getConfig().getString("color1")) + "/asd ver|version - " + org.bukkit.ChatColor.getByChar(Main.getInstance().getConfig().getString("color2")) + "Prints the version",
				org.bukkit.ChatColor.getByChar(Main.getInstance().getConfig().getString("color1")) + "/asd test Wo Rd S - " + org.bukkit.ChatColor.getByChar(Main.getInstance().getConfig().getString("color2")) + "Tests a string against the database",
				org.bukkit.ChatColor.getByChar(Main.getInstance().getConfig().getString("color1")) + "/asd get|getCount|data - " + org.bukkit.ChatColor.getByChar(Main.getInstance().getConfig().getString("color2")) + "Gets a player's curse count.",
				org.bukkit.ChatColor.getByChar(Main.getInstance().getConfig().getString("color")) + "/asd permissions|perm - " + org.bukkit.ChatColor.getByChar(Main.getInstance().getConfig().getString("color2")) + "Prints all permissions."
		});

		
			} catch (NullPointerException e) {
				sender.sendMessage(new String[] {
						org.bukkit.ChatColor.DARK_AQUA + "AdvancedSwearDetection by BukkitAI Team",
						org.bukkit.ChatColor.DARK_AQUA + "Usage: ",
						org.bukkit.ChatColor.DARK_AQUA + "/asd - " + org.bukkit.ChatColor.AQUA + "Prints this page",
						org.bukkit.ChatColor.DARK_AQUA + "/asd ver|version - " + org.bukkit.ChatColor.AQUA + "Prints the version",
						org.bukkit.ChatColor.DARK_AQUA + "/asd test Wo Rd S - " + org.bukkit.ChatColor.AQUA + "Tests a string against the database",
						org.bukkit.ChatColor.DARK_AQUA + "/asd get|getCount|data - " + org.bukkit.ChatColor.AQUA + "Gets a player's curse count.",
						org.bukkit.ChatColor.DARK_AQUA + "/asd permissions|perm - " + org.bukkit.ChatColor.AQUA + "Prints all permissions."
				});
				Main.getInstance().getConfig().createSection("color1");
				Main.getInstance().getConfig().createSection("color2");
				Main.getInstance().getConfig().set("color1", "3");
				Main.getInstance().getConfig().set("color2", "a");
			}
			

	}	
}

