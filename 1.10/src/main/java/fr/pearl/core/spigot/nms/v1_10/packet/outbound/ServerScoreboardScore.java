package fr.pearl.core.spigot.nms.v1_10.packet.outbound;

import fr.pearl.api.spigot.nms.scoreboard.NmsScore;
import fr.pearl.api.spigot.packet.PacketServer;
import fr.pearl.api.spigot.packet.registry.outbound.NmsPacketServerScoreboardScore;
import net.minecraft.server.v1_10_R1.*;

import java.util.function.Supplier;

public class ServerScoreboardScore implements NmsPacketServerScoreboardScore {
    
    private NmsScore<?> score;
    
    @Override
    public Object getPacket() {
        return new PacketPlayOutScoreboardScore((ScoreboardScore) this.score.getServerScore());
    }

    @Override
    public Class<?> packetClass() {
        return PacketPlayOutScoreboardScore.class;
    }

    @Override
    public void setScore(NmsScore<?> score) {
        this.score = score;
    }

    public static Supplier<PacketServer> getSupplier() {
        return ServerScoreboardScore::new;
    }
}
