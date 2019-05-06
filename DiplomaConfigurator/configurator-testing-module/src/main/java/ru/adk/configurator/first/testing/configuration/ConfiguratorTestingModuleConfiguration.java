package ru.adk.configurator.first.testing.configuration;

import lombok.Getter;
import ru.rti.application.core.module.ModuleConfiguration;

import static ru.adk.configurator.first.testing.constants.Constants.ConfigKeys.*;
import static ru.rti.application.config.extensions.ConfigExtentions.configString;

@Getter
public class ConfiguratorTestingModuleConfiguration implements ModuleConfiguration {

    private String firstMessage;
    private String secondMessage;

    public ConfiguratorTestingModuleConfiguration() {
        refresh();
    }

    @Override
    public void refresh() {
        firstMessage = configString(MESSAGES_SECTION, FIRST_MESSAGE);
        secondMessage = configString(MESSAGES_SECTION, SECOND_MESSAGE);
    }
}