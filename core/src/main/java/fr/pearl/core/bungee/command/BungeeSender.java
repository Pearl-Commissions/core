package fr.pearl.core.bungee.command;

import fr.pearl.api.bungee.util.BungeeUtils;
import fr.pearl.api.common.command.PearlSender;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.command.ConsoleCommandSender;

import java.util.UUID;

public class BungeeSender implements PearlSender {

    private final CommandSender sender;

    public BungeeSender(CommandSender sender) {
        this.sender = sender;
    }

    @Override
    public String getName() {
        return this.sender.getName();
    }

    @Override
    public UUID getUniqueId() {
        ProxiedPlayer player = this.getProxyPlayer();
        return player == null ? null : player.getUniqueId();
    }

    @Override
    public void sendMessage(String message) {
        BungeeUtils.sendMessage(this.sender, message);
    }

    @Override
    public boolean hasPermission(String permission) {
        return this.sender.hasPermission(permission);
    }

    @Override
    public org.bukkit.entity.Player getBukkitPlayer() {
        return null;
    }

    @Override
    public ProxiedPlayer getProxyPlayer() {
        if (this.sender instanceof ProxiedPlayer) {
            return (ProxiedPlayer) this.sender;
        }

        return null;
    }

    @Override
    public org.bukkit.command.ConsoleCommandSender getBukkitConsole() {
        return null;
    }

    @Override
    public ConsoleCommandSender getProxyConsole() {
        if (this.sender instanceof ConsoleCommandSender) {
            return (ConsoleCommandSender) this.sender;
        }

        return null;
    }
}
