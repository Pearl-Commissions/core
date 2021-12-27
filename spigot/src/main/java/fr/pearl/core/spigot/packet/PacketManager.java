package fr.pearl.core.spigot.packet;

import fr.pearl.api.common.util.Reflection;
import fr.pearl.api.spigot.PearlSpigot;
import fr.pearl.api.spigot.nms.PearlNms;
import fr.pearl.api.spigot.packet.PacketHandler;
import fr.pearl.api.spigot.packet.PacketServer;
import fr.pearl.api.spigot.packet.PearlPacketManager;
import fr.pearl.api.spigot.packet.registry.ServerRegistry;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PacketManager implements PearlPacketManager {

    private final List<PacketHandler> packetHandlers = new ArrayList<>();
    private final Map<Class<? extends PacketServer>, PacketServer> serverPackets = new HashMap<>();
    private final Map<Class<?>, PacketServer> objectToPacketMap = new HashMap<>();

    public PacketManager() {
        String version = PearlSpigot.getInstance().getNmsManager().getVersion().name().toLowerCase();
        for (ServerRegistry registry : ServerRegistry.values()) {
            Class<? extends PacketServer> packetClass = registry.getPacketClass();
            PacketServer server = Reflection.newInstance(
                    Reflection.forName("fr.pearl.core.spigot.nms." + version + ".packet.outbound." + packetClass.getSimpleName().replace("NmsPacket", ""))
                            .asSubclass(PacketServer.class)
            );
            serverPackets.put(packetClass, server);
            objectToPacketMap.put(server.packetClass(), server);
        }

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

    @Override
    @SuppressWarnings("unchecked")
    public <T extends PacketServer> T getPacket(Class<? extends PacketServer> packetClass) {
        return (T) this.serverPackets.get(packetClass);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends PacketServer> T convertPacket(Object object) {
        return (T) this.objectToPacketMap.get(object.getClass());
    }

    @Override
    public <T extends PacketServer> T convertPacket(Object o, boolean b) {
        return null;
    }

    public void addPlayer(Player player, boolean remove) {
        PearlNms<?> nms = PearlSpigot.getInstance().getNmsManager().getNms();
        if (remove) nms.removeChannel(player, "pearl-handler");
        nms.registerChannel(player, "packet_handler", "pearl-handler", new PacketInjector(player));
    }
}
