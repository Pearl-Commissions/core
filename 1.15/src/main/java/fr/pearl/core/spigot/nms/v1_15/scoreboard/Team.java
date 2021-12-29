package fr.pearl.core.spigot.nms.v1_15.scoreboard;

import fr.pearl.api.spigot.nms.scoreboard.NmsTeam;
import net.minecraft.server.v1_15_R1.*;
import org.bukkit.craftbukkit.v1_15_R1.util.CraftChatMessage;
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
    public void setPlayerName(String playerName) {
        this.playerNames = Collections.singleton(playerName);
    }

    @Override
    public Set<String> getPlayerNameSet() {
        if (this.playerNames == null) {
            this.playerNames = new HashSet<>();
        }
        return this.playerNames;
    }

    @Override
    public void addName(String playerName) {
        this.getPlayerNameSet().add(playerName);
    }

    @Override
    public void removeName(String playerName) {
        this.getPlayerNameSet().remove(playerName);
    }

    @Override
    public ScoreboardTeam getServerTeam() {
        return this;
    }
}
