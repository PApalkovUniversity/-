package ru.rti.application.configurator.app;

import io.advantageous.config.Config;
import lombok.Getter;
import ru.rti.application.config.extensions.rocks.RocksDbAgileConfiguration;
import ru.rti.application.config.module.ConfigModule;
import ru.rti.application.configurator.configuration.*;
import ru.rti.application.configurator.spec.ConfiguratorServiceSpec;
import ru.rti.application.configurator.spec.UserServiceSpec;
import ru.rti.application.core.module.Module;
import ru.rti.application.core.module.ModuleState;
import ru.rti.application.http.client.module.HttpClientModule;
import ru.rti.application.http.server.module.HttpServerModule;
import ru.rti.application.http.server.spec.HttpWebUiServiceSpecification;
import ru.rti.application.json.module.JsonModule;
import ru.rti.application.logging.LoggingModule;
import ru.rti.application.metrics.http.spec.MetricServiceSpecification;
import ru.rti.application.metrics.module.MetricsModule;
import ru.rti.application.protobuf.client.module.ProtobufClientModule;
import ru.rti.application.protobuf.server.module.ProtobufServerModule;
import ru.rti.application.rocks.db.module.RocksDbModule;
import ru.rti.application.service.ServiceModule;
import static lombok.AccessLevel.PRIVATE;
import static ru.rti.application.config.ConfigProvider.config;
import static ru.rti.application.config.constants.ConfigType.YAML;
import static ru.rti.application.configurator.constants.ConfiguratorModuleConstants.CONFIGURATOR_MODULE_ID;
import static ru.rti.application.configurator.constants.ConfiguratorModuleConstants.ConfiguratorLocalConfigKeys.*;
import static ru.rti.application.configurator.constants.ConfiguratorModuleConstants.HTTP_SERVER_BOOTSTRAP_THREAD;
import static ru.rti.application.configurator.service.UserService.register;
import static ru.rti.application.core.configuration.ContextInitialConfiguration.ApplicationContextConfiguration;
import static ru.rti.application.core.context.Context.context;
import static ru.rti.application.core.context.Context.initContext;
import static ru.rti.application.core.extension.ThreadExtensions.thread;
import static ru.rti.application.http.server.HttpServer.httpServer;
import static ru.rti.application.protobuf.server.ProtobufServer.protobufServer;
import static ru.rti.application.service.ServiceModule.serviceModule;

@Getter
public class ConfiguratorModule implements Module<ConfiguratorModuleConfiguration, ModuleState> {
    @Getter(lazy = true, value = PRIVATE)
    private static final ConfiguratorModuleConfiguration configuratorModule = context().getModule(CONFIGURATOR_MODULE_ID);

    private final ConfiguratorModuleConfiguration defaultConfiguration = new ConfiguratorModuleConfiguration();
    private final String id = CONFIGURATOR_MODULE_ID;

    public static void startConfigurator() {
        initContext(new ApplicationContextConfiguration(CONFIGURATOR_MODULE_ID))
                .loadModule(new ConfigModule())
                .loadModule(new JsonModule())
                .loadModule(new LoggingModule())
                .loadModule(new ServiceModule())
                .loadModule(new RocksDbModule(), new RocksDbAgileConfiguration())
                .loadModule(new HttpServerModule(), new ConfiguratorHttpServerConfiguration())
                .loadModule(new ProtobufServerModule(), new ConfiguratorProtobufServerConfiguration())
                .loadModule(new MetricsModule(), new ConfiguratorMetricsConfiguration())
                .loadModule(new ConfiguratorModule())
                .loadModule(new ProtobufClientModule())
                .loadModule(new HttpClientModule(), new ConfiguratorHttpClientConfiguration());
        serviceModule()
                .getServiceRegistry()
                .registerService(new ConfiguratorServiceSpec())
                .registerService(new HttpWebUiServiceSpecification())
                .registerService(new UserServiceSpec())
                .registerService(new MetricServiceSpecification());
        thread(HTTP_SERVER_BOOTSTRAP_THREAD, () -> httpServer().await());
        protobufServer().await();
    }

    public static ConfiguratorModuleConfiguration configuratorModule() {
        return getConfiguratorModule();
    }

    public static void main(String[] args) {
        startConfigurator();
    }

    @Override
    public void onLoad() {
        Config config;
        register((config = config(CONFIGURATOR_SECTION_ID, YAML).asYamlConfig()).getString(CONFIGURATOR_USER), config.getString(CONFIGURATOR_PASSWORD));
    }
}
