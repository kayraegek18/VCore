package net.kayega.core.command;

import net.kayega.core.VCore;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class VCommand implements CommandExecutor, TabExecutor {

    private String commandName;
    private boolean isPlayerCommand;
    private JavaPlugin plugin;

    public VCommand(JavaPlugin plugin) {
        VCommandArguments args = this.getClass().getAnnotation(VCommandArguments.class);
        if (args.commandName() == null) {
            VCore.sendMessage("§cFailed to load command!");
            return;
        }

        setCommandName(args.commandName());
        setIsPlayerCommand(args.isPlayerCommand());
        setPlugin(plugin);

        if (args.registerOnStart()) {
            registerCommand(plugin, this, true);
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (this.isPlayerCommand) {
            if (sender instanceof Player) {
                if (command.getName().equalsIgnoreCase(this.commandName)) {
                    run((Player) sender, args);
                }
            } else {
                sender.sendMessage("This command usable only player!");
            }
        } else {
            run((Player) sender, args);
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        return tabComplete(sender, args);
    }

    public String getCommandName() { return this.commandName; }
    public boolean isPlayerCommand() { return this.isPlayerCommand; }
    public JavaPlugin getPlugin() { return this.plugin; }
    private void setCommandName(String commandName) { this.commandName = commandName; }
    private void setIsPlayerCommand(boolean isPlayerCommand) { this.isPlayerCommand = isPlayerCommand; }
    private void setPlugin(JavaPlugin plugin) { this.plugin = plugin; }

    public void run(Player player, String[] args) { }
    public List<String> tabComplete(CommandSender player, String[] args) { return null; }

    public String getFinalArgs(String[] args, int start) {
        StringBuilder bldr = new StringBuilder();
        for (int i = start; i < args.length; i++) {
            if (i != start)
                bldr.append(" ");
            bldr.append(args[i]);
        }
        return bldr.toString();
    }
    public List<String> getPlayerNicknames() {
        List<String> players = new ArrayList<>();
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            players.add(player.getName());
        }
        return players;
    }
    public List<String> getPlayerNicknames(String startsWith) {
        List<String> players = new ArrayList<>();
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            if (player.getName().startsWith(startsWith)) {
                players.add(player.getName());
            }
        }
        return players;
    }

    public static void registerCommand(JavaPlugin plugin, VCommand command) {
        Objects.requireNonNull(plugin.getCommand(command.getCommandName())).setExecutor(command);
    }
    public static void registerCommand(JavaPlugin plugin, VCommand command, boolean withTabCompleter) {
        registerCommand(plugin, command);
        if (withTabCompleter) {
            Objects.requireNonNull(plugin.getCommand(command.getCommandName())).setTabCompleter(command);
        }
    }
}
