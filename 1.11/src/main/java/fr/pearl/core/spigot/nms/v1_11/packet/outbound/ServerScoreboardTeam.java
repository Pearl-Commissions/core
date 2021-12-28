package fr.pearl.core.spigot.nms.v1_11.packet.outbound;

import fr.pearl.api.common.util.Reflection;
import fr.pearl.api.spigot.nms.scoreboard.NmsTeam;
import fr.pearl.api.spigot.packet.registry.outbound.NmsPacketServerScoreboardTeam;
import net.minecraft.server.v1_11_R1.*;

import java.lang.reflect.Field;
import java.util.function.Supplier;

public class ServerScoreboardTeam implements NmsPacketServerScoreboardTeam {
    
    private static final Field typeField = Reflection.access(PacketPlayOutScoreboardTeam.class, "i");
    
    private int type;
    private NmsTeam<ScoreboardTeam> team;
    
    @Override
    public Object getPacket() {
        return new PacketPlayOutScoreboardTeam(this.team.getServerTeam(), type);
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