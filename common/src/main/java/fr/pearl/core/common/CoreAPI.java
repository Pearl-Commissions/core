package fr.pearl.core.common;

import fr.pearl.api.common.PearlAPI;

public class CoreAPI extends PearlAPI {

    private final boolean bungeecord;

    public CoreAPI(boolean bungeecord) {
        this.bungeecord = bungeecord;
    }

    public static void enable(boolean bungeecord) {
        CoreAPI api = new CoreAPI(bungeecord);
        setInstance(api);
    }

    @Override
    public boolean isBungeeCord() {
        return this.bungeecord;
    }
}
