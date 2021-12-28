package fr.pearl.core.spigot.nms.v1_9.scoreboard;

import fr.pearl.api.spigot.nms.scoreboard.NmsTeam;
import net.minecraft.server.v1_9_R2.ScoreboardServer;
import net.minecraft.server.v1_9_R2.ScoreboardTeam;
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
    public String getDisplayName() {
        return this.name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getTeamPrefix() {
        return super.getPrefix();
    }

    @Override
    public String getTeamSuffix() {
        return super.getSuffix();
    }

    @Override
    public void setTeamPrefix(String prefix) {
        super.setPrefix(prefix);
    }

    @Override
    public void setTeamSuffix(String suffix) {
        super.setSuffix(suffix);
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
