package fr.pearl.core.common.configuration;

import fr.pearl.api.common.configuration.ConfigurationException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class SimpleConfiguration extends Configuration {

    public SimpleConfiguration(File file) {
        super(file);
    }

    @Override
    public void save() {
        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(this.file), StandardCharsets.UTF_8)) {
            writer.write(this.yaml.dump(this.entries));
        } catch (IOException e) {
            throw new ConfigurationException("Cannot save configuration file (" + this.file.getPath() + ")", e);
        }
    }
}
