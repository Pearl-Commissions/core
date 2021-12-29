package fr.pearl.core.spigot.nms.v1_17.scoreboard;

import fr.pearl.api.spigot.nms.scoreboard.NmsObjective;
import fr.pearl.api.spigot.nms.scoreboard.NmsScore;
import fr.pearl.api.spigot.nms.scoreboard.NmsScoreboard;
import fr.pearl.api.spigot.nms.scoreboard.NmsTeam;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ScoreboardServer;
import net.minecraft.world.scores.ScoreboardObjective;
import net.minecraft.world.scores.ScoreboardScore;
import net.minecraft.world.scores.ScoreboardTeam;
import net.minecraft.world.scores.criteria.IScoreboardCriteria;

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
        return new Objective(name, this, IScoreboardCriteria.o.get(criteria));
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
