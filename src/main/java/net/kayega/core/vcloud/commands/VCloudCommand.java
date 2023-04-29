/*package net.kayega.core.vcloud.commands;

import net.kayega.core.VCore;
import net.kayega.core.command.VCommand;
import net.kayega.core.command.VCommandArguments;
import net.kayega.core.vcloud.VCloud;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@VCommandArguments(commandName = "vcloud")
public class VCloudCommand extends VCommand {
    List<String> arguments = new ArrayList<>();

    public VCloudCommand(JavaPlugin plugin) {
        super(plugin);
        arguments.add("load");
        arguments.add("reload");
        arguments.add("unload");
    }

    @Override
    public void run(Player player, String[] args) {
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("load")) {
                if (args[1] == null) return;
                File file = new File(VCore.getVCloud().getDataPath(), args[1] + ".jar");
                if (file.isFile() && file.exists()) {
                    player.sendMessage(file.getName());
                } else {
                    player.sendMessage("Invalid file!");
                }
            }
        }
    }

    @Override
    public List<String> tabComplete(CommandSender player, String[] args) {
        List<String> strings = new ArrayList<>();
        if (args.length == 1) {
            for (String arg : arguments) {
                if (arg.startsWith(args[0])) {
                    strings.add(arg);
                }
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("load")) {
                List<String> plugins = new ArrayList<>();
                for (File file : (new File(VCore.getVCloud().getDataPath())).listFiles()) {
                    if (file.isFile() && file.getName().toLowerCase(Locale.ROOT).endsWith(".jar")) {
                        try (JarFile jar = new JarFile(file)) {
                            JarEntry pdf = jar.getJarEntry("plugin.yml");
                            if (pdf != null) {
                                plugins.add(file.getName());
                            }
                        } catch (IOException ex) { }
                    }
                }
                List<String> realPlugins = new ArrayList<>();
                for (String com : plugins) {
                    if (com.toLowerCase(Locale.ROOT).startsWith(args[1].toLowerCase(Locale.ROOT)))
                        realPlugins.add(com);
                }
                return (realPlugins.size() > 0) ? realPlugins : plugins;
            }
        }
        return strings;
    }
}*/
