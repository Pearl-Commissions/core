package fr.pearl.core.common.configuration;

import fr.pearl.api.common.configuration.ConfigurationHeader;
import fr.pearl.api.common.configuration.ConfigurationPart;
import fr.pearl.api.common.configuration.ConfigurationPath;
import fr.pearl.api.common.util.Reflection;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.lang.reflect.Field;

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

    private void loadFields(String parent, Object instance) {
        for (Field field : instance.getClass().getDeclaredFields()) {
            ConfigurationPath configurationPath = field.getAnnotation(ConfigurationPath.class);
            if (configurationPath == null) continue;
            Reflection.setAccessible(field);
            String path = (parent == null ? "" : parent + ".") + configurationPath.value();
            Object value = Reflection.get(field, instance);

            this.yamlFile.setComment(path, StringUtils.join(configurationPath.comments(), "\n"), configurationPath.commentType());
            if (value instanceof ConfigurationPart part) {
                this.loadFields(path, part);
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
            ConfigurationPath configurationPath = field.getAnnotation(ConfigurationPath.class);
            if (configurationPath == null) continue;
            Reflection.setAccessible(field);
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
