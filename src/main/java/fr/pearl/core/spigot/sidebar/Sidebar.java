package fr.pearl.core.spigot.sidebar;

import fr.pearl.api.spigot.PearlSpigot;
import fr.pearl.api.spigot.nms.scoreboard.NmsObjective;
import fr.pearl.api.spigot.nms.scoreboard.NmsScore;
import fr.pearl.api.spigot.nms.scoreboard.NmsTeam;
import fr.pearl.api.spigot.packet.registry.ServerRegistry;
import fr.pearl.api.spigot.packet.registry.enums.ScoreAction;
import fr.pearl.api.spigot.packet.registry.outbound.NmsPacketServerScoreboardDisplayObjective;
import fr.pearl.api.spigot.packet.registry.outbound.NmsPacketServerScoreboardObjective;
import fr.pearl.api.spigot.packet.registry.outbound.NmsPacketServerScoreboardScore;
import fr.pearl.api.spigot.packet.registry.outbound.NmsPacketServerScoreboardTeam;
import fr.pearl.api.spigot.sidebar.PearlSidebar;
import fr.pearl.api.spigot.sidebar.PearlSidebarSlot;
import fr.pearl.api.spigot.sidebar.SidebarHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;

public class Sidebar implements PearlSidebar {

    private final SidebarManager manager;
    private final SidebarHandler handler;
    private final Set<Player> viewers = new HashSet<>();
    private final SidebarList sidebarList;

    public Sidebar(SidebarManager manager, SidebarHandler handler) {
        this.manager = manager;
        this.handler = handler;
        this.sidebarList = new SidebarList(manager);
        this.setSlots();
    }

    private void setSlots() {
        this.handler.setSlots(this.sidebarList);
        List<PearlSidebarSlot> list = this.sidebarList.getList();
        int size = list.size();
        for (PearlSidebarSlot slot : list) {
            String entry = SidebarEntryCache.getEntry(size);
            NmsScore<?> score = slot.getScore();
            NmsTeam<?> team = slot.getTeam();
            score.setScore(size);
            score.setName(entry);
            team.setName("SideSlot-" + size);
            team.setPlayerName(entry);
            size--;

            long time = slot.updateTime();
            if (time != 0) {
                new SidebarSlotTask(this, slot).runTaskTimerAsynchronously(PearlSpigot.getInstance(), time, time);
            }
        }

        this.manager.getObjective().setDisplayName(this.handler.getDisplayName());
    }

    public void apply(Player player) {
        this.viewers.add(player);
        NmsObjective<?> objective = this.manager.getObjective();
        NmsPacketServerScoreboardObjective packetObjective = ServerRegistry.SCOREBOARD_OBJECTIVE.getPacket();
        packetObjective.setObjective(objective);
        packetObjective.setType(0);
        packetObjective.send(player);
        NmsPacketServerScoreboardDisplayObjective packetDisplayObjective = ServerRegistry.SCOREBOARD_DISPLAY_OBJECTIVE.getPacket();
        packetDisplayObjective.setObjective(objective);
        packetDisplayObjective.setType(1); // 1 = Sidebar
        packetDisplayObjective.send(player);
        for (PearlSidebarSlot slot : this.sidebarList.getList()) {
            NmsScore<?> score = slot.getScore();
            NmsTeam<?> team = slot.getTeam();
            this.setTeamText(slot, team, player);
            NmsPacketServerScoreboardScore packetScore = ServerRegistry.SCOREBOARD_SCORE.getPacket();
            packetScore.setScore(score);
            packetScore.setAction(ScoreAction.CHANGE);
            packetScore.send(player);
            NmsPacketServerScoreboardTeam packetTeam = ServerRegistry.SCOREBOARD_TEAM.getPacket();
            packetTeam.setTeam(team);
            packetTeam.setType(0);
            packetTeam.send(player);
        }
    }

    private void setTeamText(PearlSidebarSlot slot, NmsTeam<?> team, Player player) {
        String text = slot.getText();
        if (slot.getUpdater() != null && player != null) {
            text = slot.getUpdater().apply(player, text);
        }
        String prefix = text.substring(0, Math.min(text.length(), 16));
        String suffix = "";
        if (text.length() > 16) {
            String colors = ChatColor.getLastColors(prefix);
            suffix = colors + text.substring(16, Math.min(text.length(), 32 - colors.length()));
        }
        team.setTeamPrefix(prefix);
        team.setTeamSuffix(suffix);
    }

    @Override
    public void remove(Player player) {
        this.viewers.remove(player);
    }

    @Override
    public void updateSlot(int slot) {
        this.updateSlot(slot, false);
    }

    @Override
    public void updateSlot(int slot, boolean global) {
        List<PearlSidebarSlot> list = this.sidebarList.getList();
        if (slot >= list.size()) return;
        this.updateSlot(list.get(slot), global);
    }

    @Override
    public void updateSlot(PearlSidebarSlot slot) {
        this.updateSlot(slot, false);
    }

    @Override
    public void updateSlot(PearlSidebarSlot slot, boolean global) {
        NmsTeam<?> team = slot.getTeam();
        boolean hasUpdater = slot.getUpdater() != null;
        if (!hasUpdater || global) {
            this.setTeamText(slot, team, null);
        }

        for (Player viewer : this.getViewers()) {
            if (hasUpdater && !global) {
                this.setTeamText(slot, team, viewer);
            }
            NmsPacketServerScoreboardTeam packetTeam = ServerRegistry.SCOREBOARD_TEAM.getPacket();
            packetTeam.setTeam(team);
            packetTeam.setType(2);
            packetTeam.send(viewer);
        }
    }

    @Override
    public Collection<? extends Player> getViewers() {
        return viewers;
    }

    @Override
    public boolean hasViewers() {
        return !this.viewers.isEmpty();
    }

    @Override
    public SidebarHandler getHandler() {
        return handler;
    }
}
