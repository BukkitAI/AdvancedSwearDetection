package org.bukkitai.advancedsweardetection.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkitai.advancedsweardetection.ASD;

/**
 * @author TheEnderCrafter9
 */
public class CMDUtils {
    public static void sendHelp(CommandSender sender) {

        try {
            String[] commands = new String[]{
                    ChatColor.getByChar(ASD.getInstance().getConfig().getString("color1").charAt(0))
                            + "AdvancedSwearDetection by BukkitAI Team",

                    ChatColor.getByChar(ASD.getInstance().getConfig().getString("color1").charAt(0)) + "Usage: ",

                    ChatColor.getByChar(ASD.getInstance().getConfig().getString("color1").charAt(0)) + "/asd - "
                            + ChatColor.getByChar(ASD.getInstance().getConfig().getString("color2").charAt(0))
                            + "Prints this page",

                    ChatColor.getByChar(ASD.getInstance().getConfig().getString("color1").charAt(0))
                            + "/asd ver|version - "
                            + ChatColor.getByChar(ASD.getInstance().getConfig().getString("color2").charAt(0))
                            + "Prints the version",

                    ChatColor.getByChar(ASD.getInstance().getConfig().getString("color1").charAt(0))
                            + "/asd test Wo Rd S - "
                            + ChatColor.getByChar(ASD.getInstance().getConfig().getString("color2").charAt(0))
                            + "Tests a string against the database",

                    ChatColor.getByChar(ASD.getInstance().getConfig().getString("color1").charAt(0))
                            + "/asd get|getCount|data - "
                            + ChatColor.getByChar(ASD.getInstance().getConfig().getString("color2").charAt(0))
                            + "Gets a player's curse count.",

                    ChatColor.getByChar(ASD.getInstance().getConfig().getString("color1").charAt(0))
                            + "/asd permissions|perm - "
                            + ChatColor.getByChar(ASD.getInstance().getConfig().getString("color2").charAt(0))
                            + "Prints all permissions.",

                    ChatColor.getByChar(ASD.getInstance().getConfig().getString("color1").charAt(0))
                            + "/asd color|scheme - "
                            + ChatColor.getByChar(ASD.getInstance().getConfig().getString("color2").charAt(0))
                            + "Changes the color scheme from a pallette selected by the devs.",

                    ChatColor.getByChar(ASD.getInstance().getConfig().getString("color1").charAt(0))
                            + "/asd reload|load -"
                            + ChatColor.getByChar(ASD.getInstance().getConfig().getString("color2").charAt(0))
                            + "Reloads all configs."

            };
            if (sender instanceof ConsoleCommandSender) {
                sender.sendMessage(commands);
                return;
            }
            if (sender.isOp()) {
                sender.sendMessage(commands);
                return;
            }
            String[] perms = new String[]{"ASD.test", "ASD.data", "ASD.perm", "ASD.color", "ASD.load"};
            sender.sendMessage(commands[0]);
            sender.sendMessage(commands[1]);
            sender.sendMessage(commands[2]);
            sender.sendMessage(commands[3]);
            /*
             * TODO check if works
			 */
            int i = 4;
            int j = 0;
            while(true) {
                if (i < commands.length || j < perms.length) {
                    if (sender.hasPermission(perms[j])) {
                        sender.sendMessage(commands[i]);
                    } else {
                        ASD.debug(sender.getName() + " did not have perm " + perms[j]);
                    }
                    i++;
                    j++;
                } else {
                    break;
                }
            }

        } catch (NullPointerException ignored) {
            sender.sendMessage(new String[]{ChatColor.DARK_AQUA + "AdvancedSwearDetection by BukkitAI Team",
                    ChatColor.DARK_AQUA + "Usage: ",
                    ChatColor.DARK_AQUA + "/asd - " + ChatColor.AQUA + "Prints this page",
                    ChatColor.DARK_AQUA + "/asd ver|version - " + ChatColor.AQUA + "Prints the version",
                    ChatColor.DARK_AQUA + "/asd test [Wo Rd S] - " + ChatColor.AQUA
                            + "Tests a string against the database",
                    ChatColor.DARK_AQUA + "/asd get|getCount|data [player] - " + ChatColor.AQUA
                            + "Gets a player's curse count.",
                    ChatColor.DARK_AQUA + "/asd permissions|perm - " + ChatColor.AQUA + "Prints all permissions.",
                    ChatColor.DARK_AQUA + "/asd color|scheme [palette] -" + ChatColor.AQUA
                            + "Changes the color scheme from a pallette selected by the devs.",
                    ChatColor.DARK_AQUA + "/asd reload|load -" + ChatColor.AQUA + "Reloads all configs."});
            ASD.getInstance().getConfig().createSection("color1");
            ASD.getInstance().getConfig().createSection("color2");
            ASD.getInstance().getConfig().set("color1", "3");
            ASD.getInstance().getConfig().set("color2", "a");
            ASD.getInstance().saveConfig();
            ASD.getInstance().reloadConfig();
        }

    }

    public static void sendColorText(CommandSender sender, String text) {
        try {
            sender.sendMessage(ChatColor.getByChar(ASD.getInstance().getConfig().getString("color2").charAt(0)) + text);

        } catch (NullPointerException ignored) {
            sendDefaultColorAndSet(new String[] { text }, sender);
        }

    }

    public static void sendColorText(CommandSender sender, String[] text) {
        if (text.length == 0) {
            return;
        }
        try {
            for (String s : text) {
                sender.sendMessage(ChatColor.getByChar(ASD.getInstance().getConfig().getString("color1")) + s);
            }

        } catch (NullPointerException ignored) {
            sendDefaultColorAndSet(text, sender);
        }

    }

    public static void sendColorText(CommandSender sender, String[] text, Colors color) {
        if (color.equals(Colors.MAIN)) {
            try {
                for (String s : text) {
                    sender.sendMessage(ChatColor.getByChar(ASD.getInstance().getConfig().getString("color1")) + s);
                }
            } catch (NullPointerException ignored) {
                sendDefaultColorAndSet(text, sender);
            }
        }
        if (color.equals(Colors.SUB)) {
            try {
                for (String s : text) {
                    sender.sendMessage(ChatColor.getByChar(ASD.getInstance().getConfig().getString("color2")) + s);
                }
            } catch (NullPointerException ignored) {
                sendDefaultColorAndSet(text, sender);
            }

        }
    }

    public static void sendDefaultColorAndSet(String[] text, CommandSender sender) {
        for (String s : text) {
            sender.sendMessage(ChatColor.DARK_AQUA + s);
        }
        saveDefaultColor();
    }

    public static void saveDefaultColor() {
        ASD.getInstance().getConfig().set("color1", "3");
        ASD.getInstance().getConfig().set("color2", "a");
        ASD.getInstance().saveConfig();
    }

    public static String sendColorMessage(String s, Colors Colors) {
        if (Colors.equals(org.bukkitai.advancedsweardetection.commands.Colors.MAIN)) {
            try {
                return ChatColor.getByChar(ASD.getInstance().getConfig().getString("color1")) + s;
            } catch (NullPointerException ignored) {
                saveDefaultColor();
                return ChatColor.DARK_AQUA + s;
            }

        }
        if (Colors.equals(org.bukkitai.advancedsweardetection.commands.Colors.SUB)) {
            try {
                return ChatColor.getByChar(ASD.getInstance().getConfig().getString("color2")) + s;
            } catch (NullPointerException ignored) {
                saveDefaultColor();
                return ChatColor.AQUA + s;
            }
        }
        return s;
    }

    public static boolean checkPerm(CommandSender sender, String perm) {
        if (sender.hasPermission(perm)) {
            return true;
        } else {
            if (!sender.hasPermission(perm) && !sender.isOp()) {
                sender.sendMessage(ChatColor.DARK_RED + "Sorry, you did not have the valid permission.");
                return false;
            }
        }
        return false;
    }
}
