package fr.pearl.core.spigot.listener;

import fr.pearl.api.spigot.PearlSpigot;
import fr.pearl.api.spigot.nms.scoreboard.NmsObjective;
import fr.pearl.api.spigot.packet.registry.ServerRegistry;
import fr.pearl.api.spigot.packet.registry.outbound.NmsPacketServerScoreboardObjective;
import fr.pearl.core.spigot.packet.PacketManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectionListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        ((PacketManager) PearlSpigot.getInstance().getPacketManager()).addPlayer(player, false);

        NmsObjective<?> objective = PearlSpigot.getInstance().getNmsManager().getNms().createScoreboard().createObjective("Bramsou", "dummy");

        Bukkit.getScheduler().runTaskLater(PearlSpigot.getInstance(), () -> {
            NmsPacketServerScoreboardObjective scoreboardObjective = ServerRegistry.SCOREBOARD_OBJECTIVE.getPacket();
            scoreboardObjective.setObjective(objective);
            scoreboardObjective.setType(0);
            scoreboardObjective.send(player);
        }, 1L);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        PearlSpigot.getInstance().getNmsManager().getNms().removeChannel(event.getPlayer(), "pearl-handler");
    }
}
