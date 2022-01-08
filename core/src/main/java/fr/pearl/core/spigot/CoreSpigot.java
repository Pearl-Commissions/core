package fr.pearl.core.spigot;

import fr.pearl.api.spigot.PearlSpigot;
import fr.pearl.api.spigot.menu.PearlMenuManager;
import fr.pearl.api.spigot.nms.PearlNmsManager;
import fr.pearl.api.spigot.packet.PearlPacketManager;
import fr.pearl.api.spigot.sidebar.PearlSidebarManager;
import fr.pearl.core.common.CoreAPI;
import fr.pearl.core.spigot.listener.ConnectionListener;
import fr.pearl.core.spigot.listener.MenuListener;
import fr.pearl.core.spigot.menu.MenuManager;
import fr.pearl.core.spigot.nms.NmsManager;
import fr.pearl.core.spigot.packet.PacketManager;
import fr.pearl.core.spigot.sidebar.SidebarManager;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class CoreSpigot extends PearlSpigot {

    private NmsManager nmsManager;
    private PacketManager packetManager;
    private SidebarManager sidebarManager;
    private MenuManager menuManager;

    @Override
    public void onEnable() {
        new CoreAPI(false);
        PearlSpigot.setInstance(this);

        // Managers
        this.nmsManager = new NmsManager();
        this.packetManager = new PacketManager();
        this.sidebarManager = new SidebarManager();
        this.menuManager = new MenuManager();

        // Listeners
        for (Listener listener : Arrays.asList(new MenuListener(), new ConnectionListener())) {
            this.getServer().getPluginManager().registerEvents(listener, this);
        }
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

    @Override
    public PearlMenuManager getMenuManager() {
        return this.menuManager;
    }
}
