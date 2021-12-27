package fr.pearl.core.nms.v1_13.scoreboard;

import fr.pearl.api.spigot.nms.scoreboard.NmsObjective;
import fr.pearl.api.spigot.nms.scoreboard.NmsScoreboard;
import fr.pearl.api.spigot.nms.scoreboard.NmsTeam;
import net.minecraft.server.v1_13_R2.*;

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
        return new Objective(name, this.scoreboardServer, IScoreboardCriteria.criteria.get(criteria));
    }

    @Override
    public NmsTeam<ScoreboardTeam> createTeam(String s) {
        return null;
    }
}
