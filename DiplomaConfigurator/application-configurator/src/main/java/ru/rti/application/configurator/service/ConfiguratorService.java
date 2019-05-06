package ru.rti.application.configurator.service;

import ru.rti.application.configurator.api.entity.Configuration;
import ru.rti.application.configurator.api.entity.ModuleConfiguration;
import ru.rti.application.configurator.api.entity.ModuleKey;
import ru.rti.application.configurator.api.entity.ProfileConfiguration;
import ru.rti.application.core.extension.JavaExtensions;
import ru.rti.application.entity.Value;
import static ru.rti.application.config.remote.api.constants.RemoteConfigServiceConstants.Methods.APPLY_CONFIGURATION_METHOD_ID;
import static ru.rti.application.configurator.api.entity.Configuration.ConfigurationBuilder;
import static ru.rti.application.configurator.api.entity.Configuration.builder;
import static ru.rti.application.configurator.constants.ConfiguratorDbConstants.APPLICATION;
import static ru.rti.application.configurator.dao.ConfiguratorDao.*;
import static ru.rti.application.configurator.factory.RemoteConfigProxyServiceSpecFactory.createRemoteConfigProxySpecs;
import static ru.rti.application.configurator.provider.ApplicationModulesParametersProvider.getApplicationModuleParameters;
import static ru.rti.application.entity.Entity.entityBuilder;
import java.util.Set;


public interface ConfiguratorService {
    static void uploadConfiguration(Configuration configuration) {
        saveConfig(configuration);
    }

    static void uploadApplicationConfiguration(Configuration configuration) {
        saveApplicationConfiguration(configuration.getConfiguration());
    }

    static void uploadProfileConfiguration(ProfileConfiguration configuration) {
        saveProfileConfiguration(configuration.getProfileId(), configuration.getConfiguration());
    }

    static void uploadModuleConfiguration(ModuleConfiguration configuration) {
        saveModuleConfiguration(configuration.getModuleKey(), configuration.getConfiguration());
    }


    static Configuration getConfiguration(ModuleKey moduleKey) {
        ConfigurationBuilder applicationConfigurationBuilder = builder().configuration(entityBuilder().build());
        getConfig(moduleKey.formatKey())
                .map(Value::asEntity)
                .ifPresent(applicationConfigurationBuilder::configuration);
        return applicationConfigurationBuilder.build();
    }

    static Set<ModuleKey> getProfiles() {
        return getProfileKeys();
    }

    static Set<ModuleKey> getModules() {
        return getModuleKeys();
    }

    static Configuration getApplicationConfiguration() {
        return builder()
                .configuration(getConfig(APPLICATION).map(Value::asEntity).orElse(entityBuilder().build()))
                .build();
    }


    static void applyModuleConfiguration(ModuleKey moduleKey) {
        getApplicationModuleParameters(moduleKey)
                .filter(JavaExtensions::isNotEmpty)
                .map(parameters -> createRemoteConfigProxySpecs(parameters, moduleKey))
                .ifPresent(specs -> specs.forEach(spec -> spec.executeMethod(APPLY_CONFIGURATION_METHOD_ID, null)));
    }
}