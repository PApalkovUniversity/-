package ru.rti.application.configurator.app;

import lombok.Getter;
import ru.rti.application.config.remote.api.spec.RemoteConfigProxyServiceSpec;
import ru.rti.application.configurator.configuration.ConfiguratorHttpServerConfiguration;
import ru.rti.application.configurator.configuration.ConfiguratorMetricsConfiguration;
import ru.rti.application.configurator.configuration.ConfiguratorModuleConfiguration;
import ru.rti.application.configurator.configuration.ConfiguratorProtobufServerConfiguration;
import ru.rti.application.configurator.provider.ApplicationModulesParametersProvider;
import ru.rti.application.configurator.spec.ConfiguratorServiceSpec;
import ru.rti.application.core.module.Module;
import ru.rti.application.core.module.ModuleState;
import ru.rti.application.http.server.module.HttpServerModule;
import ru.rti.application.json.module.JsonModule;
import ru.rti.application.logging.LoggingModule;
import ru.rti.application.metrics.http.spec.MetricServiceSpecification;
import ru.rti.application.metrics.module.MetricsModule;
import ru.rti.application.protobuf.client.module.ProtobufClientModule;
import ru.rti.application.protobuf.server.module.ProtobufServerModule;
import ru.rti.application.rocks.db.configuration.RocksDbModuleConfiguration.RocksDBModuleFileConfigurableConfiguration;
import ru.rti.application.rocks.db.module.RocksDbModule;
import ru.rti.application.service.ServiceModule;
import ru.rti.application.service.ServiceModuleConfiguration;

import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;
import static ru.rti.application.configurator.constants.ConfiguratorModuleConstants.CONFIGURATOR_MODULE_ID;
import static ru.rti.application.configurator.constants.ConfiguratorModuleConstants.HTTP_SERVER_BOOTSTRAP_THREAD;
import static ru.rti.application.configurator.dao.ConfiguratorDao.getModuleKeys;
import static ru.rti.application.core.configuration.ContextInitialConfiguration.ApplciationContextConfiguration;
import static ru.rti.application.core.context.Context.context;
import static ru.rti.application.core.context.Context.initContext;
import static ru.rti.application.http.server.HttpServer.httpServer;
import static ru.rti.application.protobuf.server.ProtobufServer.protobufServer;
import static ru.rti.application.service.ServiceModule.serviceModule;
import static ru.rti.application.task.deferred.executor.SchedulerModuleActions.thread;

@Getter
public class ConfiguratorModule implements Module<ConfiguratorModuleConfiguration, ModuleState> {
    @Getter(lazy = true, value = PRIVATE)
    private static final ConfiguratorModuleConfiguration configuratorModule = context().getModule(CONFIGURATOR_MODULE_ID);

    private final ConfiguratorModuleConfiguration defaultConfiguration = new ConfiguratorModuleConfiguration();
    private final String id = CONFIGURATOR_MODULE_ID;

    public static void startConfigurator() {
        initContext(new ApplciationContextConfiguration(CONFIGURATOR_MODULE_ID))
                .loadModule(new JsonModule())
                .loadModule(new LoggingModule())
                .loadModule(new ServiceModule())
                .loadModule(new RocksDbModule(), new RocksDBModuleFileConfigurableConfiguration())
                .loadModule(new HttpServerModule(), new ConfiguratorHttpServerConfiguration())
                .loadModule(new ProtobufServerModule(), new ConfiguratorProtobufServerConfiguration())
                .loadModule(new MetricsModule(), new ConfiguratorMetricsConfiguration())
                .loadModule(new ProtobufClientModule());
        ServiceModuleConfiguration.ServiceRegistry serviceRegistry = serviceModule()
                .getServiceRegistry()
                .registerService(new ConfiguratorServiceSpec())
                .registerService(new MetricServiceSpecification());
//        registerRemoteLoaderProxyServices(serviceRegistry);
        thread(HTTP_SERVER_BOOTSTRAP_THREAD, () -> httpServer().await());
        protobufServer().await();
    }

//    private static void registerRemoteLoaderProxyServices(ServiceModuleConfiguration.ServiceRegistry serviceRegistry) {
//        getModuleKeys()
//                .stream()
//                .map(ApplicationModulesParametersProvider::getApplicationModuleParameters)
//                .filter(Optional::isPresent)
//                .map(Optional::get)
//                .forEach(parameter -> serviceRegistry.
//                  registerService(new RemoteConfigProxyServiceSpec(parameter.getBalancerHost(),
//                                  parameter.getBalancerPort(), parameter.getServletPath())));
//    }

    public static ConfiguratorModuleConfiguration configuratorModule() {
        return getConfiguratorModule();
    }

    public static void main(String[] args) {
        startConfigurator();
    }
}
