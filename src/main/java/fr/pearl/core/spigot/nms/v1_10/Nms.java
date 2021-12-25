package fr.pearl.core.spigot.nms.v1_10;

import fr.pearl.api.spigot.nms.PearlNms;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import net.minecraft.server.v1_10_R1.*;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_10_R1.util.CraftChatMessage;
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
    public void sendPacket(Player player, Object object) {
        getEntityPlayer(player).playerConnection.sendPacket((Packet<?>) object);
    }

    @Override
    public void setListName(Player player, String listName) {
        getEntityPlayer(player).listName = CraftChatMessage.fromString(listName)[0];
    }

    @Override
    public EntityPlayer getEntityPlayer(Player player) {
        return ((CraftPlayer) player).getHandle();
    }
}
