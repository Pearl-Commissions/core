package fr.pearl.core.spigot.nms;

import fr.pearl.api.common.util.Reflection;
import fr.pearl.api.spigot.nms.NmsVersion;
import fr.pearl.api.spigot.nms.PearlNms;
import fr.pearl.api.spigot.nms.PearlNmsManager;
import org.bukkit.Bukkit;

public class NmsManager implements PearlNmsManager {

    private final NmsVersion nmsVersion;
    private final PearlNms<?> nms;

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

        PearlNms<?> nms = Reflection.newInstance(Reflection.forName("fr.pearl.core.spigot.nms." + version.name().toLowerCase() + ".Nms").asSubclass(PearlNms.class));
        this.nmsVersion = version;
        this.nms = nms;
    }

    @Override
    public NmsVersion getVersion() {
        return this.nmsVersion;
    }

    @Override
    public PearlNms<?> getNms() {
        return this.nms;
    }
}
