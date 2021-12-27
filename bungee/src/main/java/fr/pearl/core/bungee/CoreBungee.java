package fr.pearl.core.bungee;

import fr.pearl.api.bungee.PearlBungee;

public class CoreBungee extends PearlBungee {

    @Override
    public void onEnable() {
        PearlBungee.setInstance(this);
    }
}
