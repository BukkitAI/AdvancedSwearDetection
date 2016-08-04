package org.bukkitai.advancedsweardetection;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class AIConfig {

	private final String fileName;
	private final JavaPlugin plugin;
	private File configFile;
	private FileConfiguration fileConfiguration;

	public AIConfig(String fileName, JavaPlugin plugin) {
		if (plugin == null) throw new IllegalArgumentException("Plugin cannot be null");
		this.plugin = plugin;
		this.fileName = fileName;
		File dataFolder = plugin.getDataFolder();
		if (dataFolder == null) throw new IllegalStateException();
		this.configFile = new File(plugin.getDataFolder(), fileName);
		
		saveDefaultYaml();
	}

	public void reloadYaml() {
		fileConfiguration = YamlConfiguration.loadConfiguration(configFile);

		InputStream defConfigStream = plugin.getResource(fileName);
		if (defConfigStream != null) {
			@SuppressWarnings("deprecation")
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			fileConfiguration.setDefaults(defConfig);
		}
		try {
			defConfigStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public FileConfiguration getYaml() {
		if (fileConfiguration == null) {
			this.reloadYaml();
		}
		return fileConfiguration;
	}

	public void saveYaml() {
		if (fileConfiguration == null || configFile == null) {
			return;
		} else {
			try {
				getYaml().save(configFile);
			} catch (IOException ex) {
				plugin.getLogger().log(Level.SEVERE, "Could not save config to " + configFile, ex);
			}
		}
	}

	public void saveDefaultYaml() {
		if (!configFile.exists()) {
			this.plugin.saveResource(fileName, false);
		}
	}

	public void modifyYaml() {
		saveYaml();
		reloadYaml();
	}
}
