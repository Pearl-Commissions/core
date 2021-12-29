package fr.pearl.core.spigot.nms.v1_15.packet.outbound;

import fr.pearl.api.spigot.nms.scoreboard.NmsScore;
import fr.pearl.api.spigot.packet.PacketServer;
import fr.pearl.api.spigot.packet.registry.enums.ScoreAction;
import fr.pearl.api.spigot.packet.registry.outbound.NmsPacketServerScoreboardScore;
import net.minecraft.server.v1_15_R1.*;
import org.apache.commons.lang.Validate;

import java.util.EnumMap;
import java.util.function.Supplier;

public class ServerScoreboardScore implements NmsPacketServerScoreboardScore {

    private static final EnumMap<ScoreAction, ScoreboardServer.Action> actionMap = new EnumMap<>(ScoreAction.class);

    static {
        actionMap.put(ScoreAction.CHANGE, ScoreboardServer.Action.CHANGE);
        actionMap.put(ScoreAction.REMOVE, ScoreboardServer.Action.REMOVE);
    }

    private NmsScore<?> score;
    private ScoreAction action;
    
    @Override
    public Object getPacket() {
        ScoreboardScore score = (ScoreboardScore) this.score.getServerScore();
        ScoreboardObjective objective = score.getObjective();
        Validate.notNull(objective, "Objective cannot be null");
        return new PacketPlayOutScoreboardScore(actionMap.get(this.action), this.score.getName(), objective.getName(), this.score.getScore());
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