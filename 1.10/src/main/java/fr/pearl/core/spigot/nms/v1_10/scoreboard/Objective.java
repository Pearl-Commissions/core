package fr.pearl.core.spigot.nms.v1_10.scoreboard;

import fr.pearl.api.spigot.nms.scoreboard.NmsObjective;
import net.minecraft.server.v1_10_R1.*;

public class Objective extends ScoreboardObjective implements NmsObjective<ScoreboardObjective> {

    private String displayName;

    public Objective(String name, ScoreboardServer server, IScoreboardCriteria criteria) {
        super(server, name, criteria);
        this.displayName = name;
    }

    @Override
    public String getCurrentDisplayName() {
        return getDisplayName();
    }

    @Override
    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public ScoreboardObjective getServerObjective() {
        return this;
    }
}