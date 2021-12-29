package fr.pearl.core.spigot.sidebar;

import fr.pearl.api.spigot.PearlSpigot;
import fr.pearl.api.spigot.nms.scoreboard.NmsObjective;
import fr.pearl.api.spigot.nms.scoreboard.NmsScoreboard;
import fr.pearl.api.spigot.sidebar.PearlSidebar;
import fr.pearl.api.spigot.sidebar.PearlSidebarManager;
import fr.pearl.api.spigot.sidebar.SidebarHandler;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class SidebarManager implements PearlSidebarManager {

    private final Map<Player, PearlSidebar> sidebarMap = new HashMap<>();
    private final NmsScoreboard<?> scoreboard;
    private final NmsObjective<?> objective;

    public SidebarManager() {
        this.scoreboard = PearlSpigot.getInstance().getNmsManager().getNms().createScoreboard();
        this.objective = this.scoreboard.createObjective("PearlSidebar", "dummy");
    }

    @Override
    public PearlSidebar createSidebar(SidebarHandler handler) {
        return new Sidebar(this, handler);
    }

    @Override
    public void setSidebar(Player player, PearlSidebar sidebar) {
        sidebar.apply(player);
        this.sidebarMap.put(player, sidebar);
    }

    @Override
    public void removeSidebar(Player player) {
        PearlSidebar sidebar = this.sidebarMap.remove(player);
        if (sidebar == null) return;
        sidebar.remove(player);
    }

    @Override
    public PearlSidebar getSidebar(Player player) {
        return this.sidebarMap.get(player);
    }

    public NmsScoreboard<?> getScoreboard() {
        return scoreboard;
    }

    public NmsObjective<?> getObjective() {
        return objective;
    }
}
