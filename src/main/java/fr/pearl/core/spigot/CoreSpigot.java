package fr.pearl.core.spigot;

import fr.pearl.api.spigot.PearlSpigot;
import fr.pearl.api.spigot.nms.PearlNmsManager;
import fr.pearl.core.spigot.nms.NmsManager;

public class CoreSpigot extends PearlSpigot {

    private NmsManager nmsManager;

    @Override
    public void onEnable() {
        setInstance(this);

        this.nmsManager = new NmsManager();
    }

    @Override
    public PearlNmsManager getNmsManager() {
        return this.nmsManager;
    }
}
