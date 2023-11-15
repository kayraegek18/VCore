package net.kayega.core;

import net.kayega.core.armor.ArmorListener;
import net.kayega.core.inventory.VInvManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class VCore extends JavaPlugin {

    private static final String name = "§6VCore";
    private static final String version = "1.0.0";
    private static final PluginVersion versionType = PluginVersion.STABLE;

    public static VCore instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        VInvManager.register(this);

        Bukkit.getPluginManager().registerEvents(new ArmorListener(new ArrayList<>()), this);
        Bukkit.getPluginManager().registerEvents(new EventListener(this), this);

        // Startup message
        sendConsoleMessage(name, versionType, version, "§aPlugin Enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        // Disable message
        sendConsoleMessage(name, versionType, version, "§cPlugin Disabled!");
    }

    public static void sendConsoleMessage(@NotNull String name, @NotNull PluginVersion mode, @NotNull String version, @NotNull String desc) {
        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage("     " + name);
        Bukkit.getConsoleSender().sendMessage("     §8Version " + PluginVersion.getVersionString(mode) + " §8" + version);
        Bukkit.getConsoleSender().sendMessage("     " + desc);
        Bukkit.getConsoleSender().sendMessage("     §eDeveloped by §9Kayega");
        Bukkit.getConsoleSender().sendMessage("");
    }

    public static String getPluginName() {
        return name;
    }
    public static PluginVersion getVersionType() {
        return versionType;
    }
    public static String getVersion() {
        return version;
    }

    public static void sendMessage(String arg0) {
        Bukkit.getConsoleSender().sendMessage(name + " §f" + arg0);
    }

    public static void sendMessage(Player arg0, String arg1) {
        arg0.sendMessage(name + " §f" + arg1);
    }
    public static void sendMessage(Player arg0, String arg1, String arg2) {
        arg0.sendMessage("§6"+ arg2 + " §f" + arg1);
    }
    public static void sendMessage(String arg0, String arg1) {
        Bukkit.getConsoleSender().sendMessage("§6"+ arg1 + " §f" + arg0);
    }
}
