package fr.pearl.core.spigot.nms.v1_17.packet.outbound;

import fr.pearl.api.common.util.Reflection;
import fr.pearl.api.spigot.nms.scoreboard.NmsTeam;
import fr.pearl.api.spigot.packet.registry.outbound.NmsPacketServerScoreboardTeam;
import net.minecraft.network.protocol.game.PacketPlayOutScoreboardTeam;
import net.minecraft.world.scores.ScoreboardTeam;

import java.lang.reflect.Field;
import java.util.function.Supplier;

public class ServerScoreboardTeam implements NmsPacketServerScoreboardTeam {
    
    private static final Field typeField = Reflection.access(PacketPlayOutScoreboardTeam.class, "h");
    
    private int type;
    private NmsTeam<ScoreboardTeam> team;
    
    @Override
    public Object getPacket() {
        if (this.type == 0 || this.type == 2) {
            return PacketPlayOutScoreboardTeam.a(this.team.getServerTeam(), this.type == 0);
        } else {
            return PacketPlayOutScoreboardTeam.a(this.team.getServerTeam());
        }
    }

    @Override
    public Class<?> packetClass() {
        return PacketPlayOutScoreboardTeam.class;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setTeam(NmsTeam<?> team) {
        this.team = (NmsTeam<ScoreboardTeam>) team;
    }

    @Override
    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int getType() {
        return this.type;
    }

    @Override
    public void setValues(Object packet) {
        this.type = Reflection.get(typeField, packet);
    }

    public static Supplier<ServerScoreboardTeam> getSupplier() {
        return ServerScoreboardTeam::new;
    }
}