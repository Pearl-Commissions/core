package fr.pearl.core.spigot.nms;

import fr.pearl.api.common.util.Reflection;
import fr.pearl.api.spigot.nms.NmsVersion;
import fr.pearl.api.spigot.nms.PearlNms;
import fr.pearl.api.spigot.nms.PearlNmsManager;
import org.bukkit.Bukkit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NmsManager implements PearlNmsManager {

    private final NmsVersion nmsVersion;
    private final PearlNms<?> nms;

    public NmsManager() {
        String serverVersion = Bukkit.getServer().getVersion();
        Pattern versionPattern = Pattern.compile("^.+ \\(MC: (1.\\d{1,2})\\.\\d\\)$");
        Matcher matcher = versionPattern.matcher(serverVersion);
        String versionName;
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Cannot match server version from: " + serverVersion);
        } else {
            versionName = "V" + matcher.toMatchResult().group(1).replaceAll("\\.", "_");
        }

        NmsVersion version;
        try {
            version = NmsVersion.valueOf(versionName);
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot find server version: " + versionName);
        }

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
