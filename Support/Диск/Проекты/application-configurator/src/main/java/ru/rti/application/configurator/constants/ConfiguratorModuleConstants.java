package ru.rti.application.configurator.constants;

public interface ConfiguratorModuleConstants {
    String CONFIGURATOR_MODULE_ID = "CONFIGURATOR_MODULE";
    String HTTP_SERVER_BOOTSTRAP_THREAD = "http-server-bootstrap-thread";

    String APPLICATION_BALANCER_HOST_CONFIG_KEY = "balancer.host";
    String APPLICATION_BALANCER_PORT_CONFIG_KEY = "balancer.port";
    String HTTP_PATH = "/configurator";
    String APPLICATION_MODULE_PROTOBUF_SERVLET_PATH_CONFIG_KEY = "protobuf.servlet.path";

    interface ConfiguratorLocalConfigKeys {
        String CONFIGURATOR_SECTION_ID = "configurator";
        String CONFIGURATOR_HTTP_PORT_PROPERTY = "http.port";
        String CONFIGURATOR_PROTOBUF_PORT_PROPERTY = "protobuf.port";
    }
}
