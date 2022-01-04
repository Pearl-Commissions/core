package fr.pearl.core.common.configuration;

import fr.pearl.api.common.configuration.dynamic.*;
import fr.pearl.api.common.util.Reflection;
import org.apache.commons.lang3.StringUtils;
import org.simpleyaml.configuration.ConfigurationSection;

import java.io.File;
import java.lang.reflect.Field;
import java.util.function.Consumer;

public class DynamicConfiguration extends Configuration {

    private final Object instance;

    public DynamicConfiguration(Object instance, File file) {
        super(file);

        this.instance = instance;
        ConfigurationHeader header = instance.getClass().getDeclaredAnnotation(ConfigurationHeader.class);
        if (header != null) {
             this.yamlFile.options().copyHeader(true).header(StringUtils.join(header.value(), "\n"));
             this.yamlFile.options().copyDefaults(false);
        }
    }

    @Override
    public void load() {
        super.load();
        this.loadFields(null, this.instance);
    }

    @Override
    public void save() {
        this.saveFields(null, this.instance);
        super.save();
    }

    @SuppressWarnings("unchecked")
    private void loadFields(String parent, Object instance) {
        for (Field field : instance.getClass().getDeclaredFields()) {
            Reflection.setAccessible(field);
            ConfigurationKeys keys = field.getAnnotation(ConfigurationKeys.class);
            if (keys != null) {
                Object value = Reflection.get(field, instance);
                if (value instanceof ConfigurationList list) {
                    String[] keyComments = keys.keyComments();
                    Consumer<String> setComments = keyComments.length != 0 ? s -> this.yamlFile.setComment(s, StringUtils.join(keyComments, "\n")) : s -> {};
                    String path = (parent == null ? "" : parent + ".") + keys.value();
                    this.yamlFile.setComment(path, StringUtils.join(keys.comments(), "\n"), keys.commentType());
                    ConfigurationSection section = this.yamlFile.getConfigurationSection(path);
                    if (section == null) {
                        String[] defaultKeys = keys.defaultKeys();
                        for (int i = 0; i < list.size(); i++) {
                            if (i >= defaultKeys.length) break;
                            ConfigurationKey part = (ConfigurationKey) list.get(i);
                            String keyName = defaultKeys[i];
                            String keyPath = path + "." + keyName;
                            setComments.accept(keyPath);
                            this.loadFields(keyPath, part);
                            part.setName(keyName);
                            part.loaded();
                        }

                        continue;
                    }
                    list.clear();
                    for (String key : section.getKeys(false)) {
                        ConfigurationKey part = list.create(key);
                        String keyPath = path + "." + key;
                        setComments.accept(keyPath);
                        this.loadFields(keyPath, part);
                        part.setName(key);
                        part.loaded();
                        list.add(part);
                    }

                }
                continue;
            }
            ConfigurationPath configurationPath = field.getAnnotation(ConfigurationPath.class);
            if (configurationPath == null) continue;
            String path = (parent == null ? "" : parent + ".") + configurationPath.value();
            Object value = Reflection.get(field, instance);
            this.yamlFile.setComment(path, StringUtils.join(configurationPath.comments(), "\n"), configurationPath.commentType());
            if (value instanceof ConfigurationPart part) {
                this.loadFields(path, part);
                part.loaded();
                continue;
            }
            Object configValue = this.yamlFile.get(path, value);
            if (configValue != null && configValue.getClass() == value.getClass()) {
                value = configValue;
            }

            if (value instanceof String string) {
                value = string.replaceAll("&", "§");
            }

            Reflection.set(field, instance, value);
        }
    }

    private void saveFields(String parent, Object instance) {
        for (Field field : instance.getClass().getDeclaredFields()) {
            Reflection.setAccessible(field);
            ConfigurationKeys keys = field.getAnnotation(ConfigurationKeys.class);
            if (keys != null) {
                String path = (parent == null ? "" : parent + ".") + keys.value();
                Object value = Reflection.get(field, instance);
                if (value instanceof ConfigurationList list) {
                    for (Object object : list) {
                        ConfigurationKey key = (ConfigurationKey) object;
                        this.saveFields(path + "." + key.getName(), key);
                    }
                }
                continue;
            }
            ConfigurationPath configurationPath = field.getAnnotation(ConfigurationPath.class);
            if (configurationPath == null) continue;
            String path = (parent == null ? "" : parent + ".") + configurationPath.value();
            Object value = Reflection.get(field, instance);
            if (value instanceof ConfigurationPart part) {
                this.saveFields(path, part);
                continue;
            }
            if (value instanceof String string) {
                value = string.replaceAll("§", "&");
            }
            this.yamlFile.set(path, value);
        }
    }
}
