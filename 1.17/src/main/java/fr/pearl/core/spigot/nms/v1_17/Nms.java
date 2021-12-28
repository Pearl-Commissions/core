package fr.pearl.core.spigot.nms.v1_17;

import fr.pearl.api.spigot.nms.PearlNms;
import fr.pearl.api.spigot.nms.scoreboard.NmsScoreboard;
import fr.pearl.core.spigot.nms.v1_17.scoreboard.Scoreboard;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.ScoreboardServer;
import net.minecraft.server.level.EntityPlayer;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_17_R1.util.CraftChatMessage;
import org.bukkit.entity.Player;

public class Nms implements PearlNms<EntityPlayer> {

    @Override
    public void registerChannel(Player player, String baseName, String name, ChannelHandler handler) {
        this.getEntityPlayer(player).b.a.k.pipeline().addBefore(baseName, name, handler);
    }

    @Override
    public void removeChannel(Player player, String name) {
        Channel channel = this.getEntityPlayer(player).b.a.k;
        ChannelHandler handler = channel.pipeline().get("pearl-handler");
        if (handler != null) channel.pipeline().remove(handler);
    }

    @Override
    public void sendPacket(Player player, Object object) {
        getEntityPlayer(player).b.sendPacket((Packet<?>) object);
    }

    @Override
    public void setListName(Player player, String listName) {
        getEntityPlayer(player).listName = CraftChatMessage.fromString(listName)[0];
    }

    @Override
    public NmsScoreboard<ScoreboardServer> createScoreboard() {
        return new Scoreboard();
    }

    @Override
    public EntityPlayer getEntityPlayer(Player player) {
        return ((CraftPlayer) player).getHandle();
    }
}
