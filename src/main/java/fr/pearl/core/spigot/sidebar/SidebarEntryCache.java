package fr.pearl.core.spigot.sidebar;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class SidebarEntryCache {

    private static final List<String> entries = new ArrayList<>();

    public static String getEntry(int slot) {
        if (slot >= entries.size()) {
            ChatColor[] colors = ChatColor.values();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < 6; i++) {
                builder.append(colors[ThreadLocalRandom.current().nextInt(colors.length)]);
            }
            builder.append(ChatColor.RESET);
            String entry = builder.toString();
            entries.add(entry);
            return entry;
        }

        return entries.get(slot);
    }
}
