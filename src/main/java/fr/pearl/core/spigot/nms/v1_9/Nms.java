package fr.pearl.core.spigot.nms.v1_9;

import fr.pearl.api.spigot.nms.PearlNms;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import net.minecraft.server.v1_9_R2.EntityPlayer;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Nms implements PearlNms<EntityPlayer> {

    @Override
    public void registerChannel(Player player, String baseName, String name, ChannelHandler handler) {
        this.getEntityPlayer(player).playerConnection.networkManager.channel.pipeline().addBefore(baseName, name, handler);
    }

    @Override
    public void removeChannel(Player player, String name) {
        Channel channel = this.getEntityPlayer(player).playerConnection.networkManager.channel;
        ChannelHandler handler = channel.pipeline().get("pearl-handler");
        if (handler != null) channel.pipeline().remove(handler);
    }

    @Override
    public EntityPlayer getEntityPlayer(Player player) {
        return ((CraftPlayer) player).getHandle();
    }
}
