package fr.pearl.core.nms.v1_13.scoreboard;

import fr.pearl.api.spigot.nms.scoreboard.NmsObjective;
import net.minecraft.server.v1_13_R2.*;
import org.bukkit.craftbukkit.v1_13_R2.util.CraftChatMessage;

public class Objective extends ScoreboardObjective implements NmsObjective<ScoreboardObjective> {

    private IChatBaseComponent displayName;

    public Objective(String name, ScoreboardServer server, IScoreboardCriteria criteria) {
        super(server, name, criteria, null, criteria.e());
        this.displayName = CraftChatMessage.fromString(name)[0];
    }

    @Override
    public String getCurrentDisplayName() {
        return CraftChatMessage.fromComponent(this.displayName);
    }

    @Override
    public IChatBaseComponent getDisplayName() {
        return this.displayName;
    }

    @Override
    public void setDisplayName(String displayName) {
        this.displayName = CraftChatMessage.fromString(displayName)[0];
    }

    @Override
    public ScoreboardObjective getServerObjective() {
        return this;
    }
}