package fr.pearl.core.nms.v1_14.packet.outbound;

import fr.pearl.api.common.util.Reflection;
import fr.pearl.api.spigot.packet.registry.outbound.NmsPacketServerPlayerInfo;
import fr.pearl.api.spigot.packet.registry.enums.PlayerInfoType;
import net.minecraft.server.v1_14_R1.EntityPlayer;
import net.minecraft.server.v1_14_R1.PacketPlayOutPlayerInfo;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.EnumMap;

public class ServerPlayerInfo implements NmsPacketServerPlayerInfo {

    private static final EnumMap<PlayerInfoType, PacketPlayOutPlayerInfo.EnumPlayerInfoAction> toServerInfoMap = new EnumMap<>(PlayerInfoType.class);
    private static final EnumMap<PacketPlayOutPlayerInfo.EnumPlayerInfoAction, PlayerInfoType> toNmsInfoMap = new EnumMap<>(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.class);
    private static final Field actionField = Reflection.access(PacketPlayOutPlayerInfo.class, "a");

    static {
        PlayerInfoType[] infoTypes = PlayerInfoType.values();
        PacketPlayOutPlayerInfo.EnumPlayerInfoAction[] infoActions = PacketPlayOutPlayerInfo.EnumPlayerInfoAction.values();
        for (int i = 0; i < infoActions.length; i++) {
            PlayerInfoType infoType = infoTypes[i];
            PacketPlayOutPlayerInfo.EnumPlayerInfoAction infoAction = infoActions[i];
            toServerInfoMap.put(infoType, infoAction);
            toNmsInfoMap.put(infoAction, infoType);
        }
    }

    private PacketPlayOutPlayerInfo.EnumPlayerInfoAction infoAction = null;
    private EntityPlayer[] entityPlayers = null;

    @Override
    public void setPlayerInfoType(PlayerInfoType infoType) {
        this.infoAction = toServerInfoMap.get(infoType);
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
    public PlayerInfoType getPlayerInfoType() {
        return toNmsInfoMap.get(this.infoAction);
    }

    @Override
    public Object getPacket() {
        return new PacketPlayOutPlayerInfo(this.infoAction, this.entityPlayers);
    }

    @Override
    public Class<?> packetClass() {
        return PacketPlayOutPlayerInfo.class;
    }

    @Override
    public void setValues(Object packet) {
        this.infoAction = Reflection.get(actionField, packet);
    }
}