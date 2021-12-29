package fr.pearl.core.spigot.nms.v1_13.scoreboard;

import fr.pearl.api.spigot.nms.scoreboard.NmsObjective;
import fr.pearl.api.spigot.nms.scoreboard.NmsScore;
import fr.pearl.api.spigot.nms.scoreboard.NmsScoreboard;
import fr.pearl.api.spigot.nms.scoreboard.NmsTeam;
import net.minecraft.server.v1_13_R2.*;

public class Scoreboard extends ScoreboardServer implements NmsScoreboard<ScoreboardServer> {

    public Scoreboard() {
        super(MinecraftServer.getServer());
    }

    @Override
    public ScoreboardServer getScoreboardServer() {
        return this;
    }

    @Override
    public NmsObjective<ScoreboardObjective> createObjective(String name, String criteria) {
        return new Objective(name, this, IScoreboardCriteria.criteria.get(criteria));
    }

    @Override
    public NmsTeam<ScoreboardTeam> createNewTeam(String teamName) {
        return new Team(this, teamName);
    }

    @Override
    public NmsScore<ScoreboardScore> createScore(NmsScoreboard<?> scoreboard, NmsObjective<?> objective, String name, int initialScore) {
        return new Score(this, (ScoreboardObjective) objective.getServerObjective(), name, initialScore);
    }
}
