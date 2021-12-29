package fr.pearl.core.spigot.sidebar;

import fr.pearl.api.spigot.nms.scoreboard.NmsScore;
import fr.pearl.api.spigot.nms.scoreboard.NmsTeam;
import fr.pearl.api.spigot.sidebar.PearlSidebarSlot;
import org.bukkit.entity.Player;

import java.util.function.BiFunction;

public class SidebarSlot implements PearlSidebarSlot {

    private final NmsScore<?> score;
    private final NmsTeam<?> team;

    private String text;
    private final BiFunction<Player, String, String> updater;
    private final long updateTime;

    public SidebarSlot(SidebarManager manager, String text, BiFunction<Player, String, String> updater, long updateTime) {
        this.text = text;
        this.updater = updater;
        this.updateTime = updateTime;
        this.score = manager.getScoreboard().createScore(manager.getScoreboard(), manager.getObjective(), "", 0);
        this.team = manager.getScoreboard().createNewTeam("");
    }

    @Override
    public String getText() {
        return this.text;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public BiFunction<Player, String, String> getUpdater() {
        return this.updater;
    }

    @Override
    public long updateTime() {
        return this.updateTime;
    }

    @Override
    public NmsScore<?> getScore() {
        return score;
    }

    @Override
    public NmsTeam<?> getTeam() {
        return this.team;
    }
}
