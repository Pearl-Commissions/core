package fr.pearl.core.common.configuration;

import com.google.common.base.Charsets;

import java.io.*;
import java.util.Map;

public class SimpleConfiguration extends Configuration {

    public SimpleConfiguration(File file) {
        super(file);
    }

    public SimpleConfiguration(Configuration configuration, Map<String, Object> entries) {
        super(configuration, entries);
    }

    @Override
    public void load() {
        try (FileInputStream input = new FileInputStream(this.file)) {
            Map<String, Object> entries = this.yaml.load(input);
            if (entries == null) return;
            for (Map.Entry<String, Object> entry : entries.entrySet()) {
                String path = entry.getKey();
                Object object = entry.getValue();
                if (object instanceof Map) {
                    this.entries.put(path, new SimpleConfiguration(this, entries));
                } else {
                    this.entries.put(path, object);
                }
            }
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
}
