package fr.pearl.core.spigot.nms.v1_16.scoreboard;

import fr.pearl.api.spigot.nms.scoreboard.NmsObjective;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftChatMessage;

public class Objective extends ScoreboardObjective implements NmsObjective<ScoreboardObjective> {

    private IChatBaseComponent displayName;

    public Objective(String name, ScoreboardServer server, IScoreboardCriteria criteria) {
        super(server, name, criteria, CraftChatMessage.fromString(name)[0], criteria.e());
        this.displayName = this.getDisplayName();
    }

    @Override
    public String getCurrentDisplayName() {
        return CraftChatMessage.fromComponent(this.displayName);
    }

    @Override
    public IChatBaseComponent getDisplayName() {
        if (this.displayName == null) return super.getDisplayName();
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