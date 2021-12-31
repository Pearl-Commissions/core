package fr.pearl.core.spigot;

import fr.pearl.api.spigot.PearlSpigot;
import fr.pearl.api.spigot.nms.PearlNmsManager;
import fr.pearl.api.spigot.packet.PearlPacketManager;
import fr.pearl.api.spigot.sidebar.PearlSidebarManager;
import fr.pearl.core.common.CoreAPI;
import fr.pearl.core.spigot.listener.ConnectionListener;
import fr.pearl.core.spigot.nms.NmsManager;
import fr.pearl.core.spigot.packet.PacketManager;
import fr.pearl.core.spigot.sidebar.SidebarManager;

public class CoreSpigot extends PearlSpigot {

    private NmsManager nmsManager;
    private PacketManager packetManager;
    private SidebarManager sidebarManager;

    @Override
    public void onEnable() {
        CoreAPI.enable(false);
        PearlSpigot.setInstance(this);

        // Managers
        this.nmsManager = new NmsManager();
        this.packetManager = new PacketManager();
        this.sidebarManager = new SidebarManager();

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

    @Override
    public PearlSidebarManager getSidebarManager() {
        return this.sidebarManager;
    }
}
