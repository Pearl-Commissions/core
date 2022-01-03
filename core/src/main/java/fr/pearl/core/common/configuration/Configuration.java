package fr.pearl.core.common.configuration;

import fr.pearl.api.common.configuration.ConfigurationException;
import fr.pearl.api.common.configuration.PearlConfiguration;
import org.simpleyaml.configuration.file.YamlFile;
import org.simpleyaml.exceptions.InvalidConfigurationException;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

public abstract class Configuration implements PearlConfiguration {

    protected final File file;
    protected final YamlFile yamlFile;

    public Configuration(File file) {
        this.file = file;
        this.yamlFile = new YamlFile(file);
        this.yamlFile.options().copyDefaults(true);
    }

    @Override
    public final Set<String> getKeys() {
        return this.yamlFile.getKeys(false);
    }

    @Override
    public void load() {
        try {
            this.yamlFile.createOrLoadWithComments();
        } catch (InvalidConfigurationException | IOException e) {
            throw new ConfigurationException("Cannot load configuration file (" + this.file.getPath() + ")", e);
        }
    }

    @Override
    public void save() {
        try {
            System.out.println(this.yamlFile.saveToStringWithComments());
            this.yamlFile.saveWithComments();
        } catch (IOException e) {
            throw new ConfigurationException("Cannot load configuration file (" + this.file.getPath() + ")", e);
        }
    }

    @Override
    public File getFile() {
        return this.file;
    }

    @Override
    public <T> void set(String path, T value) {
        this.yamlFile.set(path, value);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(String path, T defaultValue) {
        this.yamlFile.addDefault(path, defaultValue);
        return (T) this.yamlFile.get(path, defaultValue);
    }

    @Override
    public PearlConfiguration getSection(String path, Map<String, Object> defaultEntries) {
        return null;
    }
}
