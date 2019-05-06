package ru.adk.configurator.first.testing.app;

import lombok.Getter;
import ru.adk.configurator.first.testing.configuration.ConfiguratorTestingModuleConfiguration;
import ru.adk.configurator.first.testing.spec.ConfigurationFirstTestingModuleServiceSpec;
import ru.rti.application.config.extensions.http.HttpServerAgileConfiguration;
import ru.rti.application.config.module.ConfigModule;
import ru.rti.application.core.configuration.ContextInitialConfiguration.ApplicationContextConfiguration;
import ru.rti.application.core.module.Module;
import ru.rti.application.core.module.ModuleState;
import ru.rti.application.http.server.module.HttpServerModule;
import ru.rti.application.json.module.JsonModule;
import ru.rti.application.logging.LoggingModule;
import ru.rti.application.service.ServiceModule;

import static lombok.AccessLevel.PRIVATE;
import static ru.adk.configurator.first.testing.constants.Constants.CONFIGURATOR_TESTING_MODULE_ID;
import static ru.adk.configurator.first.testing.constants.Constants.HTTP_SERVER_BOOTSTRAP_THREAD;
import static ru.rti.application.config.remote.initializer.RemoteConfigInitializer.withRemoteConfig;
import static ru.rti.application.core.context.Context.context;
import static ru.rti.application.core.context.Context.initContext;
import static ru.rti.application.http.server.HttpServer.httpServer;
import static ru.rti.application.service.ServiceModule.serviceModule;

@Getter
public class ConfiguratorTestingModule implements Module<ConfiguratorTestingModuleConfiguration, ModuleState> {
    @Getter(lazy = true, value = PRIVATE)
    private static final ConfiguratorTestingModuleConfiguration configuratorTestingModule = context().getModule(CONFIGURATOR_TESTING_MODULE_ID);

    private final ConfiguratorTestingModuleConfiguration defaultConfiguration = new ConfiguratorTestingModuleConfiguration();
    private String id = CONFIGURATOR_TESTING_MODULE_ID;

    public static ConfiguratorTestingModuleConfiguration configuratorTestingModule() {
        return getConfiguratorTestingModule();
    }

    public static void startFirstTestingModule() {
        withRemoteConfig(initContext(new ApplicationContextConfiguration(CONFIGURATOR_TESTING_MODULE_ID))
                .loadModule(new JsonModule())
                .loadModule(new ConfigModule())
                .loadModule(new LoggingModule())
                .loadModule(new ServiceModule())
                .loadModule(new HttpServerModule(), new HttpServerAgileConfiguration()))
                .loadModule(new ConfiguratorTestingModule(), new ConfiguratorTestingModuleConfiguration());

        serviceModule()
                .getServiceRegistry()
                .registerService(new ConfigurationFirstTestingModuleServiceSpec());

        new Thread(() -> httpServer().await(), HTTP_SERVER_BOOTSTRAP_THREAD).start();
    }

    public static void main(String[] args) {
        startFirstTestingModule();
    }
}
