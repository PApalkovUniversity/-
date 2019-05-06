package ru.adk.configurator.first.testing.service;

import ru.adk.configurator.first.testing.entity.ConfigMessages;

import static ru.adk.configurator.first.testing.app.ConfiguratorTestingModule.configuratorTestingModule;

public interface ConfigurationFirstTestingModuleService {
    static ConfigMessages showConfigMessages() {
        return ConfigMessages.builder()
                .firstMessage(configuratorTestingModule().getFirstMessage())
                .secondMessage(configuratorTestingModule().getSecondMessage())
                .build();
    }
}