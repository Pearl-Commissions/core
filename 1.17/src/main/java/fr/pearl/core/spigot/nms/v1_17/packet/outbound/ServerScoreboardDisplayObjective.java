package fr.pearl.core.spigot.nms.v1_17.packet.outbound;

import fr.pearl.api.spigot.nms.scoreboard.NmsObjective;
import fr.pearl.api.spigot.packet.PacketServer;
import fr.pearl.api.spigot.packet.registry.outbound.NmsPacketServerScoreboardDisplayObjective;
import net.minecraft.network.protocol.game.PacketPlayOutScoreboardDisplayObjective;
import net.minecraft.world.scores.ScoreboardObjective;

import java.util.function.Supplier;

public class ServerScoreboardDisplayObjective implements NmsPacketServerScoreboardDisplayObjective {

    private NmsObjective<ScoreboardObjective> objective;
    private int type;

    @Override
    public Object getPacket() {
        return new PacketPlayOutScoreboardDisplayObjective(this.type, this.objective.getServerObjective());
    }

    @Override
    public Class<?> packetClass() {
        return PacketPlayOutScoreboardDisplayObjective.class;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setObjective(NmsObjective<?> objective) {
        this.objective = (NmsObjective<ScoreboardObjective>) objective;
    }

    @Override
    public void setType(int type) {
        this.type = type;
    }

    public static Supplier<PacketServer> getSupplier() {
        return ServerScoreboardDisplayObjective::new;
    }
}
