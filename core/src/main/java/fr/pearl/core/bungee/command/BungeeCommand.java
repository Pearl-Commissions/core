package fr.pearl.core.bungee.command;

import fr.pearl.api.common.command.PearlCommand;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class BungeeCommand extends Command {

    private final PearlCommand commandHandler;

    public BungeeCommand(PearlCommand commandHandler) {
        super(commandHandler.getName());

        this.commandHandler = commandHandler;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        this.commandHandler.execute(new BungeeSender(sender), this.commandHandler.getName(), args);
    }

    @Override
    public String[] getAliases() {
        return this.commandHandler.getAliases();
    }

    @Override
    public String getPermission() {
        return this.commandHandler.getPermission();
    }
}
