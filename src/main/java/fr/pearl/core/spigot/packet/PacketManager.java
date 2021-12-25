package fr.pearl.core.spigot.packet;

import fr.pearl.api.spigot.PearlSpigot;
import fr.pearl.api.spigot.nms.PearlNms;
import fr.pearl.api.spigot.packet.PacketHandler;
import fr.pearl.api.spigot.packet.PearlPacketManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PacketManager implements PearlPacketManager {

    private final List<PacketHandler> packetHandlers = new ArrayList<>();

    public PacketManager() {
        Bukkit.getOnlinePlayers().forEach(player -> this.addPlayer(player, true));
    }

    @Override
    public void registerHandler(PacketHandler packetHandler) {
        this.packetHandlers.add(packetHandler);
    }

    @Override
    public List<PacketHandler> getPacketHandlers() {
        return this.packetHandlers;
    }

    public void addPlayer(Player player, boolean remove) {
        PearlNms<?> nms = PearlSpigot.getInstance().getNmsManager().getNms();
        if (remove) nms.removeChannel(player, "pearl-handler");
        nms.registerChannel(player, "packet_handler", "pearl-handler", new PacketInjector(player));
    }
}
