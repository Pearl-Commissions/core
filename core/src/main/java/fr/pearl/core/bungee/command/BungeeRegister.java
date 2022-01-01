package fr.pearl.core.bungee.command;

import fr.pearl.api.common.command.PearlCommand;
import fr.pearl.core.bungee.CoreBungee;
import net.md_5.bungee.BungeeCord;

public class BungeeRegister {

    public static void registerCommands(PearlCommand... commands) {
        for (PearlCommand command : commands) {
            BungeeCord.getInstance().getPluginManager().registerCommand(CoreBungee.getInstance(), new BungeeCommand(command));
        }
    }
}
