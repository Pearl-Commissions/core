package fr.pearl.core.spigot;

import fr.pearl.api.common.PearlAPI;
import fr.pearl.api.common.configuration.ConfigurationType;
import fr.pearl.api.common.configuration.PearlConfiguration;
import fr.pearl.api.common.configuration.dynamic.ConfigurationPath;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestConf {

    @ConfigurationPath(value = "list", comments = "")
    public List<String> list = new ArrayList<>(Arrays.asList(
            "èèèèèèèèèèèèèè",
            "ddddddddddddd",
            "*********************"
    ));

    public TestConf() {
        PearlConfiguration configuration = PearlAPI.getInstance().getConfigurationManager().loadConfiguration(this, new File(CoreSpigot.getInstance().getDataFolder(), "config.yml"), ConfigurationType.DYNAMIC);
        configuration.load();
        configuration.save();

        System.out.println(this.list.toString());
    }
}
