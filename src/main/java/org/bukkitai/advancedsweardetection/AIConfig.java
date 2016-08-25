package org.bukkitai.advancedsweardetection;

import org.apache.commons.lang.Validate;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

public class AIConfig {

	private final String fileName;
	private final Plugin plugin;
	private File configFile;
	private FileConfiguration fileConfiguration;

	public AIConfig(String fileName, Plugin plugin) {
        Validate.notNull(plugin, "Plugin must not be null!");
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
            if (defConfigStream != null) {
                defConfigStream.close();
            }
        } catch (IOException ignored) {
		}
	}

	public FileConfiguration getYaml() {
		if (fileConfiguration == null) {
			this.reloadYaml();
		}
		return fileConfiguration;
	}

	public void saveYaml() {
		if (fileConfiguration != null && configFile != null) {
			try {
				getYaml().save(configFile);
			} catch (IOException ex) {
				plugin.getLogger().log(Level.SEVERE, "Could not save config to " + configFile, ex);
			}
		} else throw new IllegalStateException("fileConfiguration and configFile must not be null!");
	}

	public void saveDefaultYaml() {
		if (!configFile.exists()) {
			this.plugin.saveResource(fileName, false);
		}
	}

	public void modifyYaml() {
		saveYaml();
	}
}
