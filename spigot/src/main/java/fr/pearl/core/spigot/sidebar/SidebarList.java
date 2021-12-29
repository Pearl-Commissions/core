package fr.pearl.core.spigot.sidebar;

import fr.pearl.api.spigot.sidebar.PearlSidebarList;
import fr.pearl.api.spigot.sidebar.PearlSidebarSlot;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class SidebarList implements PearlSidebarList {

    private final List<PearlSidebarSlot> list = new ArrayList<>();
    private final SidebarManager manager;

    public SidebarList(SidebarManager manager) {
        this.manager = manager;
    }

    @Override
    public PearlSidebarSlot addSlot(String text) {
        return this.add(new SidebarSlot(this.manager, text, null, 0L));
    }

    @Override
    public PearlSidebarSlot addSlot(String text, BiFunction<Player, String, String> updater) {
        return this.add(new SidebarSlot(this.manager, text, updater, 0L));
    }

    @Override
    public PearlSidebarSlot addSlot(String text, BiFunction<Player, String, String> updater, long updateTime) {
        return this.add(new SidebarSlot(this.manager, text, null, updateTime));
    }

    @Override
    public List<PearlSidebarSlot> getList() {
        return this.list;
    }

    private PearlSidebarSlot add(PearlSidebarSlot slot) {
        this.list.add(slot);
        return slot;
    }
}
