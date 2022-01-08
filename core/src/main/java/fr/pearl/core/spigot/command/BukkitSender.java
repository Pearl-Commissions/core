package fr.pearl.core.spigot.command;

import fr.pearl.api.common.command.PearlSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BukkitSender implements PearlSender {

    private final CommandSender sender;

    public BukkitSender(CommandSender sender) {
        this.sender = sender;
    }

    @Override
    public String getName() {
        return this.sender.getName();
    }

    @Override
    public UUID getUniqueId() {
        Player player = this.getBukkitPlayer();
        return player == null ? null : player.getUniqueId();
    }

    @Override
    public void sendMessage(String message) {
        this.sender.sendMessage(message);
    }

    @Override
    public boolean hasPermission(String permission) {
        return this.sender.hasPermission(permission);
    }

    @Override
    public Player getBukkitPlayer() {
        if (this.sender instanceof Player) {
            return (Player) this.sender;
        }
        return null;
    }

    @Override
    public net.md_5.bungee.api.connection.ProxiedPlayer getProxyPlayer() {
        return null;
    }

    @Override
    public CommandSender getBukkitSender() {
        return this.sender;
    }

    @Override
    public net.md_5.bungee.api.CommandSender getProxySender() {
        return null;
    }
}
