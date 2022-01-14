package fr.pearl.core.common;

import fr.pearl.api.common.PearlAPI;
import fr.pearl.api.common.command.PearlCommandManager;
import fr.pearl.api.common.configuration.PearlConfigurationManager;
import fr.pearl.core.common.command.CommandManager;
import fr.pearl.core.common.configuration.ConfigurationManager;

public class CoreAPI extends PearlAPI {

    private final boolean bungeeCord;

    private final ConfigurationManager configurationManager;
    private final CommandManager commandManager;

    public CoreAPI(boolean bungeeCord) {
        setInstance(this);

        this.bungeeCord = bungeeCord;

        this.configurationManager = new ConfigurationManager();
        this.commandManager = new CommandManager();
    }

    @Override
    public boolean isBungeeCord() {
        return this.bungeeCord;
    }

    @Override
    public PearlConfigurationManager getConfigurationManager() {
        return this.configurationManager;
    }

    @Override
    public PearlCommandManager getCommandManager() {
        return this.commandManager;
    }
}
