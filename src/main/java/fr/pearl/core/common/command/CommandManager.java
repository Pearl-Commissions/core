package fr.pearl.core.common.command;

import fr.pearl.api.common.PearlAPI;
import fr.pearl.api.common.command.PearlCommand;
import fr.pearl.api.common.command.PearlCommandManager;
import fr.pearl.core.bungee.command.BungeeRegister;
import fr.pearl.core.spigot.command.BukkitRegister;

public class CommandManager implements PearlCommandManager {

    @Override
    public void registerCommands(String fallbackPrefix, PearlCommand... commands) {
        if (PearlAPI.getInstance().isBungeeCord()) {
            BungeeRegister.registerCommands(commands);
        } else {
            BukkitRegister.registerCommands(fallbackPrefix, commands);
        }
    }
}
