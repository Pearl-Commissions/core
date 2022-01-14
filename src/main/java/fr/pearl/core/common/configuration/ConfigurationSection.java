package fr.pearl.core.common.configuration;

import fr.pearl.api.common.configuration.PearlConfiguration;
import fr.pearl.api.common.configuration.PearlSection;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

public class ConfigurationSection implements PearlSection {

    protected final Map<String, Object> entries = new LinkedHashMap<>();

    @SuppressWarnings("unchecked")
    protected void loadFromMap(Map<String, Object> map) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String path = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof Map) {
                ConfigurationSection section = new ConfigurationSection();
                section.loadFromMap((Map<String, Object>) value);
                this.entries.put(path, section);
                continue;
            }

            this.entries.put(path, value);
        }
    }

    @Override
    public <T> void set(String path, T value) {
        this.findSection(path, (entries, lastPath) -> entries.put(lastPath, value));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(String path, T defaultValue) {
        return (T) this.findSection(path, (entries, lastPath) -> {
            Object value = this.getValue(entries.get(lastPath), defaultValue);
            if (value == defaultValue) entries.put(lastPath, value);
            return value;
        });
    }

    @Override
    public PearlSection getSection(String path) {
        return (PearlSection) this.findSection(path, (entries, lastPath) -> {
            Object value = entries.get(lastPath);
            if (!(value instanceof ConfigurationSection)) {
                value = new ConfigurationSection();
                entries.put(lastPath, value);
            }

            return value;
        });
    }

    @Override
    public Set<String> getKeys() {
        return this.entries.keySet();
    }

    @Override
    public Map<String, Object> getEntries() {
        return this.entries;
    }

    protected  <T> T findSection(String path, BiFunction<Map<String, Object>, String, T> function) {
        String[] paths = path.split("\\.");
        if (paths.length == 0) {
            return function.apply(this.entries, path);
        } else {
            int size = paths.length - 1;
            PearlSection section = this;
            for (int i = 0; i < size; i++) {
                String sectionPath = paths[i];
                Object value = section.getEntries().get(sectionPath);
                if (value instanceof PearlSection) {
                    section = (PearlSection) value;
                } else {
                    ConfigurationSection newSection = new ConfigurationSection();
                    section.getEntries().put(sectionPath, newSection);
                    section = newSection;
                }
            }

            return function.apply(section.getEntries(), paths[size]);
        }
    }

    protected Object getValue(Object value, Object defaultValue) {
        Class<?> excepted = defaultValue.getClass();
        boolean isFloat = false;
        if (excepted == Float.class) {
            excepted = Double.class;
            isFloat = true;
        }

        if (value == null || value.getClass() != excepted) {
            value = defaultValue;
        } else if (isFloat) {
            double doubleValue = (double) value;
            value = (float) doubleValue;
        }

        return value;
    }
}
