package fr.pearl.core.spigot.nms.v1_12.scoreboard;

import fr.pearl.api.spigot.nms.scoreboard.NmsScore;
import net.minecraft.server.v1_12_R1.*;

public class Score extends ScoreboardScore implements NmsScore<ScoreboardScore> {
    
    private String name;
    private int score;
    
    public Score(ScoreboardServer scoreboard, ScoreboardObjective objective, String name, int score) {
        super(scoreboard, objective, name);
        
        this.score = score;
    }

    @Override
    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public int getScore() {
        return this.score;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getPlayerName() {
        return this.name;
    }

    @Override
    public ScoreboardScore getServerScore() {
        return this;
    }
}
