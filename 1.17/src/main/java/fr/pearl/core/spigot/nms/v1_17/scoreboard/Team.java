package fr.pearl.core.spigot.nms.v1_17.scoreboard;

import fr.pearl.api.spigot.nms.scoreboard.NmsTeam;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.server.ScoreboardServer;
import net.minecraft.world.scores.ScoreboardTeam;
import org.bukkit.craftbukkit.v1_17_R1.util.CraftChatMessage;
import org.bukkit.entity.Player;

import java.util.*;

public class Team extends ScoreboardTeam implements NmsTeam<ScoreboardTeam> {

    private Set<String> playerNames;
    private String name;

    public Team(ScoreboardServer scoreboard, String teamName) {
        super(scoreboard, teamName);

        this.name = teamName;
    }

    @Override
    public IChatBaseComponent getDisplayName() {
        return CraftChatMessage.fromString(name)[0];
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getTeamPrefix() {
        return CraftChatMessage.fromComponent(super.getPrefix());
    }

    @Override
    public String getTeamSuffix() {
        return CraftChatMessage.fromComponent(super.getSuffix());
    }

    @Override
    public void setTeamPrefix(String prefix) {
        super.setPrefix(CraftChatMessage.fromString(prefix)[0]);
    }

    @Override
    public void setTeamSuffix(String suffix) {
        super.setSuffix(CraftChatMessage.fromString(suffix)[0]);
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setPlayer(Player player) {
        this.playerNames = Collections.singleton(player.getName());
    }

    @Override
    public Set<String> getPlayerNameSet() {
        if (this.playerNames == null) {
            this.playerNames = new HashSet<>();
        }
        return this.playerNames;
    }

    @Override
    public void addPlayer(Player player) {
        this.getPlayerNameSet().add(player.getName());
    }

    @Override
    public void removePlayer(Player player) {
        this.getPlayerNameSet().remove(player.getName());
    }

    @Override
    public ScoreboardTeam getServerTeam() {
        return this;
    }
}
