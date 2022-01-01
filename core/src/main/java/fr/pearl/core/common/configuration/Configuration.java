package fr.pearl.core.common.configuration;

import fr.pearl.api.common.configuration.ConfigurationException;
import fr.pearl.api.common.configuration.PearlConfiguration;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public abstract class Configuration implements PearlConfiguration {

    protected final Yaml yaml;
    protected final File file;
    protected final Map<String, Object> entries;

    public Configuration(File file, Class<?> clazz) {
        this.entries = new LinkedHashMap<>();
        Representer representer = new Representer() {
            {
                this.representers.put(clazz, config -> represent(((Configuration) config).entries));
            }
        };
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        this.yaml = new Yaml(new Constructor(), representer, options);
        this.file = file;
        if (!this.file.exists()) {
            if (this.file.getParentFile() != null && !this.file.getParentFile().exists()) {
                this.file.getParentFile().mkdirs();
            }

            try {
                this.file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Configuration(Configuration configuration, Map<String, Object> entries) {
        this.entries = entries;
        this.file = configuration.file;
        this.yaml = configuration.yaml;
    }

    @Override
    public final Set<String> getKeys() {
        return this.entries.keySet();
    }

    @Override
    public File getFile() {
        return this.file;
    }

    protected final void loadException(Exception e) {
        throw new ConfigurationException("Cannot load configuration file (" + this.file.getPath() + ")", e);
    }

    protected final void saveException(Exception e) {
        throw new ConfigurationException("Cannot load configuration file (" + this.file.getPath() + ")", e);
    }
}