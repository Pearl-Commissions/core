package fr.pearl.core.spigot;

import fr.pearl.api.spigot.PearlSpigot;
import fr.pearl.api.spigot.nms.PearlNmsManager;
import fr.pearl.api.spigot.packet.PearlPacketManager;
import fr.pearl.core.spigot.listener.ConnectionListener;
import fr.pearl.core.spigot.nms.NmsManager;
import fr.pearl.core.spigot.packet.PacketManager;

public class CoreSpigot extends PearlSpigot {

    private NmsManager nmsManager;
    private PacketManager packetManager;

    @Override
    public void onEnable() {
        setInstance(this);

        // Managers
        this.nmsManager = new NmsManager();
        this.packetManager = new PacketManager();

        // Listeners
        this.getServer().getPluginManager().registerEvents(new ConnectionListener(), this);
    }

    @Override
    public PearlNmsManager getNmsManager() {
        return this.nmsManager;
    }

    @Override
    public PearlPacketManager getPacketManager() {
        return this.packetManager;
    }
}
