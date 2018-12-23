package ru.rti.application.configurator.service;

import ru.rti.application.config.remote.api.spec.RemoteConfigProxyServiceSpec;
import ru.rti.application.configurator.api.entity.ApplicationConfiguration;
import ru.rti.application.configurator.api.entity.ApplicationConfiguration.ApplicationConfigurationBuilder;
import ru.rti.application.configurator.api.entity.ModuleKey;
import ru.rti.application.configurator.provider.ApplicationModulesParametersProvider;
import ru.rti.application.configurator.provider.ApplicationModulesParametersProvider.ApplicationModuleParameters;
import ru.rti.application.entity.Value;

import java.util.Optional;
import java.util.Set;

import static ru.rti.application.config.remote.api.constants.RemoteConfigServiceConstants.Methods.APPLY_CONFIGURATION_METHOD_ID;
import static ru.rti.application.configurator.api.entity.ApplicationConfiguration.builder;
import static ru.rti.application.configurator.constants.ConfiguratorDbConstants.APPLICATION;
import static ru.rti.application.configurator.dao.ConfiguratorDao.*;
import static ru.rti.application.configurator.provider.ApplicationModulesParametersProvider.getApplicationModuleParameters;
import static ru.rti.application.core.extension.JavaExtensions.isNotEmpty;
import static ru.rti.application.protobuf.client.proxy.spec.ProtobufProxyServiceSpecification.protobufProxyServiceId;
import static ru.rti.application.service.ServiceController.executeServiceMethod;


public interface ConfiguratorService {
    static void uploadConfiguration(ApplicationConfiguration configuration) {
        saveConfig(configuration);
    }

    static ApplicationConfiguration getConfiguration(ModuleKey moduleKey) {
        ApplicationConfigurationBuilder applicationConfigurationBuilder = builder();
        getConfig(moduleKey.formatKey())
                .map(Value::asEntity)
                .ifPresent(applicationConfigurationBuilder::configuration);
        return applicationConfigurationBuilder.build();
    }

    static void applyModuleConfiguration(ModuleKey moduleKey) {
        ApplicationModuleParameters parameters = getApplicationModuleParameters(moduleKey).orElse(null);
        if (isNotEmpty(parameters)) {
            new RemoteConfigProxyServiceSpec(parameters.getBalancerHost(),
                    parameters.getBalancerPort(), parameters.getServletPath()).
                    getApplyConfigurationProxy().executeAsync();
        }
    }

    static Set<ModuleKey> getAllProfiles() {
        return getProfileKeys();
    }

    static Set<ModuleKey> getAllModules() {
        return getModuleKeys();
    }

    static void uploadApplicationConfiguration(ApplicationConfiguration configuration) {
        saveApplicationConfig(configuration);
    }

    static ApplicationConfiguration getApplicationConfiguration() {
        return ApplicationConfiguration.builder()
                .configuration(getConfig(APPLICATION).map(Value::asEntity).orElse(null))
                .build();
    }
}