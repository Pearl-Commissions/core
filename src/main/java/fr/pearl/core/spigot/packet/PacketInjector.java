package fr.pearl.core.spigot.packet;

import fr.pearl.api.spigot.PearlSpigot;
import fr.pearl.api.spigot.packet.PacketHandler;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import org.bukkit.entity.Player;

public class PacketInjector extends ChannelDuplexHandler {

    private final Player player;

    public PacketInjector(Player player) {
        this.player = player;
    }

    @Override
    public void channelRead(ChannelHandlerContext context, Object object) throws Exception {
        for (PacketHandler packetHandler : PearlSpigot.getInstance().getPacketManager().getPacketHandlers()) {
            packetHandler.inboundPacket(this.player, object);
        }

        super.channelRead(context, object);
    }

    @Override
    public void write(ChannelHandlerContext context, Object object, ChannelPromise promise) throws Exception {
        for (PacketHandler packetHandler : PearlSpigot.getInstance().getPacketManager().getPacketHandlers()) {
            packetHandler.outboundPacket(this.player, object);
        }

        super.write(context, object, promise);
    }
}
