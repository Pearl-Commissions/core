package fr.pearl.core.common.configuration;

import fr.pearl.api.common.configuration.ConfigurationType;
import fr.pearl.api.common.configuration.PearlConfiguration;
import fr.pearl.api.common.configuration.PearlConfigurationManager;
import org.apache.commons.lang.Validate;

import java.io.File;

public class ConfigurationManager implements PearlConfigurationManager {

    @Override
    public PearlConfiguration loadConfiguration(Object instance, File file, ConfigurationType type) {
        Validate.notNull(instance, "Instance cannot be null");
        Validate.notNull(file, "File cannot be null");
        Validate.notNull(type, "Configuration type cannot be null");
        if (type == ConfigurationType.SIMPLE) {
            return new SimpleConfiguration(file);
        } else {
            return new DynamicConfiguration(instance, file);
        }
    }
}
