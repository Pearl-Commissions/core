package fr.pearl.core.common.configuration;

import fr.pearl.api.common.configuration.ConfigurationException;
import fr.pearl.api.common.configuration.PearlConfiguration;
import fr.pearl.api.common.configuration.PearlSection;
import fr.pearl.api.common.util.io.IOUtils;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

import java.io.*;
import java.util.Map;

public abstract class Configuration extends ConfigurationSection implements PearlConfiguration {

    protected final File file;
    protected final Yaml yaml;

    public Configuration(File file) {
        this.file = file;
        Representer representer = new Representer() {{
            this.representers.put(ConfigurationSection.class, data -> represent(((PearlSection) data).getEntries()));
        }};
        representer.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        DumperOptions options = new DumperOptions();
        options.setIndent(2);
        options.setAllowUnicode(true);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        this.yaml = new Yaml(new Constructor(), representer, options);
        IOUtils.getOrCreate(this.file);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void load() {
        try (BufferedInputStream input = new BufferedInputStream(new FileInputStream(this.file))) {
            Map<String, Object> map = this.yaml.load(input);
            if (map == null) return;
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
        } catch (IOException e) {
            throw new ConfigurationException("Cannot load configuration file (" + this.file.getPath() + ")", e);
        }
    }

    @Override
    public File getFile() {
        return this.file;
    }

}
