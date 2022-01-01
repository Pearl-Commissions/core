package fr.pearl.core.common.configuration;

import com.google.common.base.Charsets;
import fr.pearl.api.common.configuration.PearlConfiguration;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class SimpleConfiguration extends Configuration {

    public SimpleConfiguration(File file) {
        super(file, SimpleConfiguration.class);
    }

    public SimpleConfiguration(Configuration configuration, Map<String, Object> entries) {
        super(configuration, entries);
    }

    @Override
    public void load() {
        try (FileInputStream input = new FileInputStream(this.file)) {
            Map<String, Object> entries = this.yaml.load(input);
            if (entries == null) return;
            this.loadEntries(entries);
        } catch (IOException e) {
            this.loadException(e);
        }
    }

    @Override
    public void save() {
        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(this.file), Charsets.UTF_8)) {
            this.yaml.dump(this.entries, writer);
        } catch (IOException e) {
            this.saveException(e);
        }
    }

    @Override
    public PearlConfiguration getSection(String path, Map<String, Object> defaultEntries) {
        Object object = this.entries.get(path);
        if (!(object instanceof SimpleConfiguration)) {
            PearlConfiguration configuration = new SimpleConfiguration(this, defaultEntries == null ? new LinkedHashMap<>() : defaultEntries);
            this.entries.put(path, configuration);
            return configuration;
        }

        return (PearlConfiguration) object;
    }

    @SuppressWarnings("unchecked")
    private void loadEntries(Map<String, Object> entries) {
        for (Map.Entry<String, Object> entry : entries.entrySet()) {
            String path = entry.getKey();
            Object object = entry.getValue();
            if (object instanceof Map) {
                SimpleConfiguration configuration = new SimpleConfiguration(this, new LinkedHashMap<>());
                configuration.loadEntries((Map<String, Object>) object);
                this.entries.put(path, configuration);
            } else {
                this.entries.put(path, object);
            }
        }
    }
}
