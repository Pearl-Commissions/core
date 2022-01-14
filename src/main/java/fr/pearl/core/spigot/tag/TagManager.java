package fr.pearl.core.spigot.tag;

import fr.pearl.api.spigot.PearlSpigot;
import fr.pearl.api.spigot.nms.scoreboard.NmsObjective;
import fr.pearl.api.spigot.nms.scoreboard.NmsScoreboard;
import fr.pearl.api.spigot.tag.PearlTag;
import fr.pearl.api.spigot.tag.PearlTagManager;
import org.bukkit.entity.Player;

public class TagManager implements PearlTagManager {

    private final NmsScoreboard<?> scoreboard;
    private final NmsObjective<?> objective;

    public TagManager() {
        this.scoreboard = PearlSpigot.getInstance().getNmsManager().getNms().createScoreboard();
        this.objective = this.scoreboard.createObjective("tags", "dummy");
    }

    @Override
    public PearlTag createTag(String name, Player player) {
        return new Tag(name, player);
    }

    @Override
    public NmsScoreboard<?> getMainScoreboard() {
        return this.scoreboard;
    }

    @Override
    public NmsObjective<?> getMainObjective() {
        return this.objective;
    }
}
