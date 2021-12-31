package fr.pearl.core.common;

import fr.pearl.api.common.PearlAPI;
import fr.pearl.api.common.configuration.PearlConfigurationManager;
import fr.pearl.core.common.configuration.ConfigurationManager;

public class CoreAPI extends PearlAPI {

    private final boolean bungeecord;

    private final ConfigurationManager configurationManager;

    public CoreAPI(boolean bungeecord) {
        this.bungeecord = bungeecord;

        this.configurationManager = new ConfigurationManager();
    }

    public static void enable(boolean bungeecord) {
        CoreAPI api = new CoreAPI(bungeecord);
        setInstance(api);
    }

    @Override
    public boolean isBungeeCord() {
        return this.bungeecord;
    }

    @Override
    public PearlConfigurationManager getConfigurationManager() {
        return this.configurationManager;
    }
}
