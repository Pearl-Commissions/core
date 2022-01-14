package fr.pearl.core.spigot.tag;

import fr.pearl.api.spigot.PearlSpigot;
import fr.pearl.api.spigot.nms.scoreboard.NmsTeam;
import fr.pearl.api.spigot.packet.registry.ServerRegistry;
import fr.pearl.api.spigot.packet.registry.enums.PlayerInfoType;
import fr.pearl.api.spigot.packet.registry.outbound.NmsPacketServerPlayerInfo;
import fr.pearl.api.spigot.packet.registry.outbound.NmsPacketServerScoreboardTeam;
import fr.pearl.api.spigot.tag.PearlTag;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Tag implements PearlTag {

    private String name;
    private Player player;
    private String listName;

    private final NmsTeam<?> team;

    public Tag(String name, Player player) {
        this.name = name;
        this.player = player;

        this.team = PearlSpigot.getInstance().getTagManager().getMainScoreboard().createNewTeam(name);
        this.team.setPlayerName(player.getName());
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public void setPlayer(Player player) {
        this.player = player;
        this.team.setPlayerName(this.name);
    }

    @Override
    public void setName(String name) {
        this.name = name;
        this.team.setName(name);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setNamePrefix(String namePrefix) {
        this.team.setTeamPrefix(namePrefix);
    }

    @Override
    public void setNameSuffix(String nameSuffix) {
        this.team.setTeamSuffix(nameSuffix);
    }

    @Override
    public String getNamePrefix() {
        return this.team.getTeamPrefix();
    }

    @Override
    public String getNameSuffix() {
        return this.team.getTeamSuffix();
    }

    @Override
    public void setListName(String listName) {
        Validate.notNull(this.player, "Player tag cannot be null");
        this.listName = listName;
        PearlSpigot.getInstance().getNmsManager().getNms().setListName(this.player, this.listName);
    }

    @Override
    public String getListName() {
        return this.listName;
    }

    @Override
    public void sendUpdate(Player player) {
        NmsPacketServerScoreboardTeam team = ServerRegistry.SCOREBOARD_TEAM.getPacket();
        team.setTeam(this.team);
        team.send(player);
        NmsPacketServerPlayerInfo info = ServerRegistry.PLAYER_INFO.getPacket();
        info.setPlayerInfoType(PlayerInfoType.UPDATE_DISPLAY_NAME);
        info.setPlayers(this.player);
        info.send(player);
    }

    @Override
    public void destroy(Player player) {
        NmsPacketServerScoreboardTeam team = ServerRegistry.SCOREBOARD_TEAM.getPacket();
        team.setTeam(this.team);
        team.setType(1);
        team.send(player);
    }

    @Override
    public void broadcastDestroy() {
        NmsPacketServerScoreboardTeam team = ServerRegistry.SCOREBOARD_TEAM.getPacket();
        team.setTeam(this.team);
        team.setType(1);
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            team.send(onlinePlayer);
        }
    }

    @Override
    public void broadcastUpdate() {
        NmsPacketServerScoreboardTeam team = ServerRegistry.SCOREBOARD_TEAM.getPacket();
        team.setTeam(this.team);
        NmsPacketServerPlayerInfo info = ServerRegistry.PLAYER_INFO.getPacket();
        info.setPlayerInfoType(PlayerInfoType.UPDATE_DISPLAY_NAME);
        info.setPlayers(this.player);
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            team.send(onlinePlayer);
            info.send(onlinePlayer);
        }
    }
}
