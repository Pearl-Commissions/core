package fr.pearl.core.spigot.nms.v1_11.packet.outbound;

import fr.pearl.api.spigot.nms.scoreboard.NmsObjective;
import fr.pearl.api.spigot.packet.registry.outbound.NmsPacketServerScoreboardObjective;
import net.minecraft.server.v1_11_R1.PacketPlayOutScoreboardObjective;
import net.minecraft.server.v1_11_R1.ScoreboardObjective;

public class ServerScoreboardObjective implements NmsPacketServerScoreboardObjective {

    private ScoreboardObjective objective;
    private int type;

    @Override
    public void setObjective(NmsObjective<?> objective) {
        this.objective = (ScoreboardObjective) objective.getServerObjective();
    }

    @Override
    public void setType(int type) {
        this.type = type;
    }

    @Override
    public Object getPacket() {
        PacketPlayOutScoreboardObjective packet = new PacketPlayOutScoreboardObjective(this.objective, this.type);
        this.objective = null;
        this.type = 0;
        return packet;
    }

    @Override
    public Class<?> packetClass() {
        return PacketPlayOutScoreboardObjective.class;
    }
}
