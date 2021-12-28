package fr.pearl.core.spigot.nms.v1_17.scoreboard;

import fr.pearl.api.spigot.nms.scoreboard.NmsObjective;
import fr.pearl.api.spigot.nms.scoreboard.NmsScoreboard;
import fr.pearl.api.spigot.nms.scoreboard.NmsTeam;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ScoreboardServer;
import net.minecraft.world.scores.ScoreboardObjective;
import net.minecraft.world.scores.ScoreboardTeam;
import net.minecraft.world.scores.criteria.IScoreboardCriteria;

public class Scoreboard implements NmsScoreboard<ScoreboardServer> {

    private final ScoreboardServer scoreboardServer;

    public Scoreboard() {
        this.scoreboardServer = new ScoreboardServer(MinecraftServer.getServer());
    }

    @Override
    public ScoreboardServer getScoreboardServer() {
        return this.scoreboardServer;
    }

    @Override
    public NmsObjective<ScoreboardObjective> createObjective(String name, String criteria) {
        return new Objective(name, this.scoreboardServer, IScoreboardCriteria.o.get(criteria));
    }

    @Override
    public NmsTeam<ScoreboardTeam> createTeam(String s) {
        return null;
    }
}
