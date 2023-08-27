package net.kayega.core.command;

import net.kayega.core.VCore;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class VSubCommand {
    private VCommand parentCommand;
    private String commandName;
    private String commandDescription;
    private String commandPermission = "";
    private final List<String> commandAliases = new ArrayList<>();
    private final List<String> commandHidedAliases = new ArrayList<>();

    public VSubCommand() {
        VSubCommandArguments args = this.getClass().getAnnotation(VSubCommandArguments.class);
        if (args.commandName() == null) {
            VCore.sendMessage("§cFailed to load command!");
            return;
        }

        setCommandName(args.commandName());
        setCommandDescription(args.commandDescription());
        setCommandPermission(args.commandPermission());
    }

    public void run(Player player, String[] args) { }
    public List<String> tabComplete(CommandSender sender, String[] args) { return null; }

    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    public void setCommandDescription(String commandDescription) {
        this.commandDescription = commandDescription;
    }

    public void setParentCommand(VCommand parentCommand) {
        this.parentCommand = parentCommand;
    }

    public void setCommandPermission(String commandPermission) {
        this.commandPermission = commandPermission;
    }

    public void registerCommandAlias(String alias) {
        this.commandAliases.add(alias);
    }
    public void registerCommandAlias(String alias, boolean hide) {
        if (hide) {
            this.commandHidedAliases.add(alias);
        } else {
            this.commandAliases.add(alias);
        }
    }

    public String getCommandName() {
        return commandName;
    }

    public String getCommandDescription() {
        return commandDescription;
    }

    public VCommand getParentCommand() {
        return parentCommand;
    }

    public String getCommandPermission() {
        return commandPermission;
    }

    public List<String> getCommandAliases() {
        return commandAliases;
    }

    public List<String> getCommandHidedAliases() {
        return commandHidedAliases;
    }

    public List<String> findArgsInArgs(List<String> arg0, String[] arg1, int argId) {
        List<String> args = new ArrayList<>();
        for (String arg : arg0) {
            if (arg.startsWith(arg1[argId]))
                args.add(arg);
        }
        return args;
    }
}
