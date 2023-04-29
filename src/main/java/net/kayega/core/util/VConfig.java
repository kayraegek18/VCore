package net.kayega.core.util;

import net.kayega.core.VCore;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public abstract class VConfig {
    private JavaPlugin plugin;
    private FileConfiguration dataConfig = null;
    private File configFile = null;
    private String configFileName = null;

    public VConfig(JavaPlugin plugin) {
        VConfigArguments arguments = this.getClass().getAnnotation(VConfigArguments.class);
        if (arguments.configFileName() == null) {
            VCore.sendMessage("§cFailed to load config file!");
            return;
        }

        this.configFileName = arguments.configFileName() + ".yml";

        this.plugin = plugin;
        this.saveDefaultConfig();
    }

    public void reloadConfig() {
        if (this.configFile == null)
            this.configFile = new File(this.plugin.getDataFolder(), this.configFileName);

        this.dataConfig = YamlConfiguration.loadConfiguration(this.configFile);

        InputStream defaultStream = this.plugin.getResource(this.configFileName);
        if (defaultStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            this.dataConfig.setDefaults(defaultConfig);
        }
    }

    public FileConfiguration getConfig() {
        if (this.dataConfig == null)
            this.reloadConfig();

        return this.dataConfig;
    }

    public void saveConfig() {
        if (this.dataConfig == null || this.configFile == null)
            return;

        try {
            this.getConfig().save(this.configFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save config to " + this.configFile, e);
        }
    }

    public void saveDefaultConfig() {
        if (this.configFile == null)
            this.configFile = new File(this.plugin.getDataFolder(), this.configFileName);

        this.getConfig().options().copyDefaults(true);
        if (!this.configFile.exists()) {
            this.plugin.saveResource(this.configFileName, false);
        }
    }
}
