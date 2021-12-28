package fr.pearl.core.spigot.nms.v1_17.scoreboard;

import fr.pearl.api.spigot.nms.scoreboard.NmsObjective;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.server.ScoreboardServer;
import net.minecraft.world.scores.ScoreboardObjective;
import net.minecraft.world.scores.criteria.IScoreboardCriteria;
import org.bukkit.craftbukkit.v1_17_R1.util.CraftChatMessage;

public class Objective extends ScoreboardObjective implements NmsObjective<ScoreboardObjective> {

    private IChatBaseComponent displayName;

    public Objective(String name, ScoreboardServer server, IScoreboardCriteria criteria) {
        super(server, name, criteria, null, criteria.f());
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