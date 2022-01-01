package fr.pearl.core.spigot.command;

import fr.pearl.api.common.command.PearlCommand;
import fr.pearl.api.common.util.Reflection;
import org.bukkit.Bukkit;
import org.bukkit.command.SimpleCommandMap;

public class BukkitRegister {

    private static final SimpleCommandMap commandMap = Reflection.get(Reflection.access(Bukkit.getServer().getClass(), "commandMap"), Bukkit.getServer());

    public static void registerCommands(String fallbackPrefix, PearlCommand... commands) {
        for (PearlCommand command : commands) {
            commandMap.register(fallbackPrefix, new BukkitCommand(command));
        }
    }
}
