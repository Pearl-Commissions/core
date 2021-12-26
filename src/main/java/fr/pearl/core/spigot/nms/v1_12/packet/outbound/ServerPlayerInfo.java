package fr.pearl.core.spigot.nms.v1_12.packet.outbound;

import fr.pearl.api.spigot.packet.registry.outbound.NmsPacketServerPlayerInfo;
import fr.pearl.api.spigot.packet.registry.enums.PlayerInfoType;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerInfo;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.EnumMap;

public class ServerPlayerInfo implements NmsPacketServerPlayerInfo {

    private static final EnumMap<PlayerInfoType, PacketPlayOutPlayerInfo.EnumPlayerInfoAction> infoMap = new EnumMap<>(PlayerInfoType.class);

    static {
        PlayerInfoType[] infoTypes = PlayerInfoType.values();
        PacketPlayOutPlayerInfo.EnumPlayerInfoAction[] infoActions = PacketPlayOutPlayerInfo.EnumPlayerInfoAction.values();
        for (int i = 0; i < infoActions.length; i++) {
            infoMap.put(infoTypes[i], infoActions[i]);
        }
    }

    private PacketPlayOutPlayerInfo.EnumPlayerInfoAction infoAction = null;
    private EntityPlayer[] entityPlayers = null;

    @Override
    public void setPlayerInfoType(PlayerInfoType infoType) {
        this.infoAction = infoMap.get(infoType);
    }

    @Override
    public void setPlayers(Player... players) {
        EntityPlayer[] entityPlayers = new EntityPlayer[players.length];
        for (int i = 0; i < players.length; i++) {
            entityPlayers[i] = ((CraftPlayer) players[0]).getHandle();
        }

        this.entityPlayers = entityPlayers;
    }

    @Override
    public void setPlayers(Collection<? extends Player> players) {
        EntityPlayer[] entityPlayers = new EntityPlayer[players.size()];
        int index = 0;
        for (Player player : players) {
            entityPlayers[index] = ((CraftPlayer) player).getHandle();
            index++;
        }

        this.entityPlayers = entityPlayers;
    }

    @Override
    public Object getPacket() {
        PacketPlayOutPlayerInfo info = new PacketPlayOutPlayerInfo(this.infoAction, this.entityPlayers);
        this.infoAction = null;
        this.entityPlayers = null;
        return info;
    }

    @Override
    public Class<?> packetClass() {
        return PacketPlayOutPlayerInfo.class;
    }
}
