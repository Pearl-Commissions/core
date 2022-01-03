package fr.pearl.core.common.configuration.util;

import java.util.Map;

public class PathEntry {

    private final Map<String, Object> entries;
    private final String newPath;

    public PathEntry(Map<String, Object> entries, String newPath) {
        this.entries = entries;
        this.newPath = newPath;
    }

    public Map<String, Object> getEntries() {
        return entries;
    }

    public String getNewPath() {
        return newPath;
    }
}
