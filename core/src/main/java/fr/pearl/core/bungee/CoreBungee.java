package fr.pearl.core.bungee;

import fr.pearl.api.bungee.PearlBungee;
import fr.pearl.core.common.CoreAPI;
import net.md_5.bungee.config.YamlConfiguration;

public class CoreBungee extends PearlBungee {

    @Override
    public void onEnable() {
        new CoreAPI(true);
        PearlBungee.setInstance(this);
    }
}
