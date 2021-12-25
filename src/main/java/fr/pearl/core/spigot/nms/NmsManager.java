package fr.pearl.core.spigot.nms;

import fr.pearl.api.spigot.nms.NmsVersion;
import fr.pearl.api.spigot.nms.PearlNms;
import fr.pearl.api.spigot.nms.PearlNmsManager;
import org.bukkit.Bukkit;

public class NmsManager implements PearlNmsManager {

    private final NmsVersion nmsVersion;
    private final PearlNms nms;

    public NmsManager() {
        String packageVersion = Bukkit.getServer().getClass().getName().split("\\.")[3];
        NmsVersion version = null;
        for (NmsVersion ver : NmsVersion.values()) {
            if (packageVersion.startsWith(ver.name().toLowerCase())) {
                version = ver;
                break;
            }
        }

        if (version == null) throw new IllegalArgumentException("Cannot get server version");

        PearlNms nms;
        try {
            Class<? extends PearlNms> nmsClass = Class.forName("fr.pearl.core.spigot.nms." + version.name().toLowerCase() + ".Nms").asSubclass(PearlNms.class);
            nms = nmsClass.newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            throw new IllegalArgumentException("Cannot get instance of nms class", e);
        }

        this.nmsVersion = version;
        this.nms = nms;
    }

    @Override
    public NmsVersion getVersion() {
        return this.nmsVersion;
    }

    @Override
    public PearlNms getNms() {
        return this.nms;
    }
}
