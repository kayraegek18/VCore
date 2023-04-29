package net.kayega.core;

import net.kayega.core.armorequip.ArmorListener;
//import net.kayega.core.vcloud.commands.VCloudCommand;
import net.kayega.core.vinv.VInvManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public final class VCore extends JavaPlugin {

    private static final String name = "§6VCore";
    private static final String version = "1.0";
    private static final PluginVersion versionType = PluginVersion.STABLE;

    //private static VCloud vCloud;
    private VCoreConfig coreConfig;

    @Override
    public void onEnable() {
        // Plugin startup logic
        VInvManager.register(this);

        Bukkit.getPluginManager().registerEvents(new ArmorListener(new ArrayList<>()), this);
        Bukkit.getPluginManager().registerEvents(new EventListener(this), this);

        //vCloud = new VCloud(this);
        //new VCloudCommand(this);

        coreConfig = new VCoreConfig(this);

        // Startup message
        sendConsoleMessage(name, versionType, version, "§aPlugin Enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        // Disable message
        sendConsoleMessage(name, versionType, version, "§cPlugin Disabled!");
    }

    private void sendConsoleMessage(@NotNull String name, @NotNull PluginVersion mode, @NotNull String version, @NotNull String desc) {
        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage("     " + name);
        Bukkit.getConsoleSender().sendMessage("     §8Version " + PluginVersion.getVersionString(mode) + " §8" + version);
        Bukkit.getConsoleSender().sendMessage("     " + desc);
        Bukkit.getConsoleSender().sendMessage("");
    }

    public static void sendMessage(String arg0) {
        Bukkit.getConsoleSender().sendMessage(name + " §f" + arg0);
    }

    public static PluginVersion getVersionType() {
        return versionType;
    }

    public static boolean isItemsAdderEnabled() {
        return Bukkit.getServer().getPluginManager().getPlugin("ItemsAdder") != null;
    }

    public static boolean isWorldBorderApiEnabled() {
        return Bukkit.getServer().getPluginManager().getPlugin("WorldBorderAPI") != null;
    }

    /*public static VCloud getVCloud() {
        return vCloud;
    }*/
}
