package fr.pearl.core.spigot.sidebar;

import fr.pearl.api.spigot.sidebar.PearlSidebarSlot;
import org.bukkit.scheduler.BukkitRunnable;

public class SidebarSlotTask extends BukkitRunnable {

    private final Sidebar sidebar;
    private final PearlSidebarSlot slot;

    public SidebarSlotTask(Sidebar sidebar, PearlSidebarSlot slot) {
        this.sidebar = sidebar;
        this.slot = slot;
    }

    @Override
    public void run() {
        this.sidebar.updateSlot(this.slot);
    }
}
