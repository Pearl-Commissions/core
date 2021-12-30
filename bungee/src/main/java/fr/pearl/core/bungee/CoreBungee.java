package fr.pearl.core.bungee;

import fr.pearl.api.bungee.PearlBungee;
import fr.pearl.core.common.CoreAPI;

public class CoreBungee extends PearlBungee {

    @Override
    public void onEnable() {
        CoreAPI.enable(true);
        PearlBungee.setInstance(this);
    }
}
