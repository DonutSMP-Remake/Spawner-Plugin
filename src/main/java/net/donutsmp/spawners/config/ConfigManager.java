package net.donutsmp.spawners.config;

import net.donutsmp.spawners.DonutSpawners;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {
    private final DonutSpawners plugin;
    private FileConfiguration config;

    public ConfigManager(DonutSpawners plugin) {
        this.plugin = plugin;
        reloadConfig();
    }

    public void reloadConfig() {
        plugin.reloadConfig();
        config = plugin.getConfig();
    }

    public FileConfiguration getConfig() {
        return config;
    }
}
