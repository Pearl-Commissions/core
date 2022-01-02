package fr.pearl.core.common.configuration;

import com.google.common.base.Charsets;
import fr.pearl.api.common.configuration.ConfigurationHeader;
import fr.pearl.api.common.configuration.ConfigurationPart;
import fr.pearl.api.common.configuration.ConfigurationPath;
import fr.pearl.api.common.configuration.PearlConfiguration;
import fr.pearl.api.common.util.Reflection;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DynamicConfiguration extends Configuration {

    private final Object instance;
    private final String[] header;

    public DynamicConfiguration(Object instance, File file) {
        super(file, DynamicConfiguration.class);

        this.instance = instance;
        ConfigurationHeader configurationHeader = instance.getClass().getDeclaredAnnotation(ConfigurationHeader.class);
        if (configurationHeader == null) {
            this.header = new String[]{};
        } else {
            this.header = configurationHeader.value();
        }

        this.loadFields();
    }

    public DynamicConfiguration(Object instance, DynamicConfiguration configuration, Map<String, Object> entries) {
        super(configuration, entries);

        this.header = configuration.header;
        this.instance = instance;
    }

    private void loadFields() {
        for (Field field : instance.getClass().getDeclaredFields()) {
            ConfigurationPath configPath = field.getAnnotation(ConfigurationPath.class);
            if (configPath == null) continue;
            String path = configPath.value();
            String[] comments = configPath.comments();
            Object value = Reflection.get(field, instance);
            if (value instanceof ConfigurationPart part) {
                DynamicConfiguration configuration = new DynamicConfiguration(value, this, new LinkedHashMap<>());
                configuration.loadFields();
                part.setSection(configuration);
                this.entries.put(path, new ConfigurationValue(null, configuration, comments));
                part.loaded();
                continue;
            }
            ConfigurationValue configValue = new ConfigurationValue(field, value, comments);
            this.entries.put(path, configValue);
        }
    }

    @Override
    public void load() {
        try (FileInputStream input = new FileInputStream(this.file)) {
            Map<String, Object> map = this.yaml.load(input);
            if (map == null) return;
            this.loadMap(map);
        } catch (IOException e) {
            this.loadException(e);
        }
    }

    @Override
    public void save() {
        List<String> lines = new ArrayList<>();
        for (String header : this.header) {
            lines.add("# " + header);
        }
        if (!lines.isEmpty()) {
            lines.add("");
        }
        this.saveEntries(lines, 0);

        if (lines.isEmpty()) return;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(this.file, Charsets.UTF_8))) {
            final int size = lines.size();
            for (int i = 0; i < size; i++) {
                if (i != 0) writer.newLine();
                writer.write(lines.get(i));
                writer.flush();
            }
        } catch (IOException e) {
            this.saveException(e);
        }
    }

    @Override
    public <T> void set(String path, T value) {
        ConfigurationValue configurationValue = (ConfigurationValue) this.entries.get(path);
        if (configurationValue == null) {
            configurationValue = new ConfigurationValue(null, value);
            this.entries.put(path, configurationValue);
            return;
        }

        configurationValue.setValue(value);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(String path, T defaultValue) {
        ConfigurationValue configurationValue = (ConfigurationValue) this.entries.get(path);
        if (configurationValue == null) {
            configurationValue = new ConfigurationValue(null, defaultValue);
            this.entries.put(path, configurationValue);
            return defaultValue;
        } else if (configurationValue.getValue().getClass() != defaultValue.getClass()) {
            configurationValue.setValue(defaultValue);
            return defaultValue;
        }
        return (T) configurationValue.getValue();
    }

    @Override
    public PearlConfiguration getSection(String path, Map<String, Object> defaultEntries) {
        Object object = this.entries.get(path);
        if (!(object instanceof DynamicConfiguration)) {
            return null;
        }

        return (PearlConfiguration) object;
    }

    @SuppressWarnings("unchecked")
    private void loadMap(Map<String, Object> map) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String path = entry.getKey();
            Object inputValue = entry.getValue();
            ConfigurationValue configurationValue = (ConfigurationValue) this.entries.get(path);
            if (configurationValue == null) continue;
            Object value = configurationValue.getValue();
            if (value instanceof DynamicConfiguration dynamicConfig) {
                Map<String, Object> sectionMap = inputValue instanceof Map ? (Map<String, Object>) inputValue : new LinkedHashMap<>();
                dynamicConfig.loadMap(sectionMap);
            } else {
                if (inputValue.getClass() != value.getClass()) {
                    continue;
                }

                configurationValue.setValue(inputValue);
                configurationValue.getField().ifPresent(field -> Reflection.set(field, this.instance, inputValue));
            }
        }
    }

    private void saveEntries(List<String> lines, int indent) {
        String prefix = " ".repeat(Math.max(0, indent));
        int parts = 0;
        for (Map.Entry<String, Object> entry : this.entries.entrySet()) {
            String path = entry.getKey();
            ConfigurationValue configurationValue = (ConfigurationValue) entry.getValue();
            Object value = configurationValue.getValue();
            if (value instanceof DynamicConfiguration dynamicConfig) {
                if (parts > 0) {
                    lines.add("");
                }
                for (String comment : configurationValue.getComments()) {
                    lines.add(prefix + "# " + comment);
                }
                lines.add(path + ":" + (dynamicConfig.entries.isEmpty() ? " {}" : ""));
                dynamicConfig.saveEntries(lines, indent + 2);
                parts++;
            } else {
                for (String comment : configurationValue.getComments()) {
                    lines.add(prefix + "# " + comment);
                }
                String line = prefix + path + ": ";
                if (value instanceof List<?> list) {
                    if (list.isEmpty()) {
                        lines.add(line + "[]");
                    } else {
                        lines.add(line);
                        for (Object o : list) {
                            lines.add(prefix + "- " + toString(o));
                        }
                    }
                } else {
                    lines.add(line + toString(value));
                }
                parts++;
            }
        }
    }

    private static String toString(Object object) {
        if (object instanceof String) {
            return "\"" + object + "\"";
        }

        return object.toString();
    }
}
