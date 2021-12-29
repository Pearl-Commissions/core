package fr.pearl.core.spigot.nms.v1_12.packet.outbound;

import fr.pearl.api.spigot.nms.scoreboard.NmsScore;
import fr.pearl.api.spigot.packet.PacketServer;
import fr.pearl.api.spigot.packet.registry.enums.ScoreAction;
import fr.pearl.api.spigot.packet.registry.outbound.NmsPacketServerScoreboardScore;
import net.minecraft.server.v1_12_R1.*;

import java.util.function.Supplier;

public class ServerScoreboardScore implements NmsPacketServerScoreboardScore {

    private NmsScore<?> score;
    private ScoreAction action;

    @Override
    public Object getPacket() {
        ScoreboardScore score = (ScoreboardScore) this.score.getServerScore();
        return this.action == ScoreAction.CHANGE ? new PacketPlayOutScoreboardScore(score) : new PacketPlayOutScoreboardScore(score.getPlayerName());
    }

    @Override
    public Class<?> packetClass() {
        return PacketPlayOutScoreboardScore.class;
    }

    @Override
    public void setScore(NmsScore<?> score) {
        this.score = score;
    }

    @Override
    public void setAction(ScoreAction action) {
        this.action = action;
    }

    public static Supplier<PacketServer> getSupplier() {
        return ServerScoreboardScore::new;
    }
}