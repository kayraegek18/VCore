package net.kayega.core;

import net.kayega.core.util.VConfig;
import net.kayega.core.util.VConfigArguments;
import org.bukkit.plugin.java.JavaPlugin;

@VConfigArguments(configFileName = "config")
public class VCoreConfig extends VConfig {
    public VCoreConfig(JavaPlugin plugin) {
        super(plugin);

        getConfig().addDefault("useWorldBorderApi", false);
        getConfig().addDefault("useItemsAdder", true);
        saveConfig();
    }
}
