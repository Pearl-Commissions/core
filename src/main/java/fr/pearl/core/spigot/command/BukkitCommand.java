package fr.pearl.core.spigot.command;

import fr.pearl.api.common.command.PearlCommand;
import fr.pearl.core.common.command.CommandExecutor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class BukkitCommand extends Command {
    
    private final PearlCommand commandHandler;
    
    public BukkitCommand(PearlCommand commandHandler) {
        super(commandHandler.getName());
        
        this.commandHandler = commandHandler;
        if (commandHandler.getAliases() != null) {
            this.setAliases(Arrays.asList(commandHandler.getAliases()));
        }
        this.setPermission(commandHandler.getPermission() == null ? "" : commandHandler.getPermission());
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, String[] args) {
        CommandExecutor.execute(this.commandHandler, new BukkitSender(sender), label, args);
        return false;
    }

    @Override
    @NotNull
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, String[] args) throws IllegalArgumentException {
        List<String> completers = CommandExecutor.tabComplete(this.commandHandler, new BukkitSender(sender), alias, args);
        if (completers == null) return super.tabComplete(sender, alias, args);
        return completers;
    }
}
