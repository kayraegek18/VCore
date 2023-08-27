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
    private String permission;
    private boolean isPlayerCommand;
    private boolean isHaveSubCommand;
    private List<VSubCommand> subCommands;
    private JavaPlugin plugin;

    public VCommand() {
        VCommandArguments args = this.getClass().getAnnotation(VCommandArguments.class);
        if (args.commandName() == null) {
            VCore.sendMessage("§cFailed to load command!");
            return;
        }
        subCommands = new ArrayList<>();

        setCommandName(args.commandName());
        setPermission(args.permission());
        setIsPlayerCommand(args.isPlayerCommand());
        setIsHaveSubCommand(args.isHaveSubCommand());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (this.isPlayerCommand && !(sender instanceof Player)) {
            sender.sendMessage("This command usable only player!");
            return true;
        }
        if (!command.getName().equalsIgnoreCase(this.commandName)) {
            return true;
        }
        if (permission.equalsIgnoreCase("")) {
            return SubCommandRun(sender, args);
        }
        if (!((Player) sender).hasPermission(permission)) {
            sender.sendMessage("You don't have permission to use this command!");
            return true;
        }
        if (isHaveSubCommand()) {
            return SubCommandRun(sender, args);
        }
        run((Player) sender, args);
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (isHaveSubCommand()) {
            if (args.length > 1) {
                for (VSubCommand subCommand : subCommands) {
                    if (subCommand.getCommandName() == null) {
                        VCore.sendMessage("§cSub Command name can't empty!");
                        continue;
                    }

                    if (args[0].equalsIgnoreCase(subCommand.getCommandName())) {
                        return subCommand.tabComplete(sender, args);
                    } else {
                        boolean found = false;
                        for (String alias : subCommand.getCommandAliases()) {
                            if (args[0].equalsIgnoreCase(alias)) {
                                found = true;
                                break;
                            }
                        }
                        if (found)
                            return subCommand.tabComplete(sender, args);
                    }
                }
            }

            List<String> commandNames = new ArrayList<>();
            for (VSubCommand subCommand : subCommands) {
                if (subCommand.getCommandName() == null) {
                    VCore.sendMessage("§cSub Command name can't empty!");
                    continue;
                }

                if (!Objects.equals(subCommand.getCommandPermission(), "") &&
                        ((Player) sender).hasPermission(subCommand.getCommandPermission())) {
                    commandNames.add(subCommand.getCommandName());
                    commandNames.addAll(subCommand.getCommandAliases());
                }
            }
            if (args[0].length() > 0) {
                List<String> commands = new ArrayList<>();
                for (String cmd : commandNames) {
                    if (cmd.startsWith(args[0]))
                        commands.add(cmd);
                }
                return commands;
            }
            return commandNames;
        }
        return tabComplete(sender, args);
    }

    private boolean SubCommandRun(CommandSender sender, String[] args) {
        if (args.length == 0) {
            run((Player) sender, args);
            return true;
        }
        for (VSubCommand subCommand : subCommands) {
            if (subCommand.getCommandName() == null) {
                VCore.sendMessage("§cSub Command name can't empty!");
                continue;
            }
            if (!args[0].equalsIgnoreCase(subCommand.getCommandName())) {
                boolean found = false;
                for (String alias : subCommand.getCommandAliases()) {
                    if (args[0].equalsIgnoreCase(alias)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    for (String alias : subCommand.getCommandHidedAliases()) {
                        if (args[0].equalsIgnoreCase(alias)) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        continue;
                    }
                }
            }
            if (!Objects.equals(subCommand.getCommandPermission(), "") &&
                    !((Player) sender).hasPermission(subCommand.getCommandPermission())) {
                VCore.sendMessage(((Player) sender), "§cBu komut için yetkin yok!");
                return true;
            }

            subCommand.run((Player) sender, args);
            return true;
        }
        VCore.sendMessage(((Player) sender), "§cBu komut bulunamadı!");
        return true;
    }

    public String getCommandName() { return this.commandName; }
    public String getPermission() { return this.permission; }
    public boolean isPlayerCommand() { return this.isPlayerCommand; }
    public boolean isHaveSubCommand() { return isHaveSubCommand; }
    public JavaPlugin getPlugin() { return this.plugin; }
    public List<VSubCommand> getSubCommands() { return this.subCommands; }
    private void setCommandName(String commandName) { this.commandName = commandName; }
    private void setPermission(String permission) { this.permission = permission; }
    private void setIsPlayerCommand(boolean isPlayerCommand) { this.isPlayerCommand = isPlayerCommand; }
    private void setPlugin(JavaPlugin plugin) { this.plugin = plugin; }
    private void setIsHaveSubCommand(boolean haveSubCommand) { isHaveSubCommand = haveSubCommand; }
    protected void registerSubCommand(VSubCommand subCommand) { this.subCommands.add(subCommand); }

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
    public String[] getFinalArgsAsList(String[] args, int start) {
        String[] finalArgs = new String[args.length];
        for (int i = start; i < args.length; i++) {
            if (i != start)
                continue;
            if (args[i] == null)
                continue;
            finalArgs[i] = args[i];
        }
        return finalArgs;
    }
    public List<String> findArgsInArgs(List<String> arg0, String[] arg1, int argId) {
        List<String> args = new ArrayList<>();
        for (String arg : arg0) {
            if (arg.startsWith(arg1[argId]))
                args.add(arg);
        }
        return args;
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
        command.setPlugin(plugin);
        Objects.requireNonNull(plugin.getCommand(command.getCommandName())).setExecutor(command);
    }
    public static void registerCommand(JavaPlugin plugin, VCommand command, boolean withTabCompleter) {
        registerCommand(plugin, command);
        if (withTabCompleter) {
            Objects.requireNonNull(plugin.getCommand(command.getCommandName())).setTabCompleter(command);
        }
    }
}
