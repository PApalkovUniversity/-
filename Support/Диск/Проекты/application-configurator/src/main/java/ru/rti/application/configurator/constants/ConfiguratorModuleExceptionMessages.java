package ru.rti.application.configurator.constants;

public interface ConfiguratorModuleExceptionMessages {
    String CONFIGURATION_PROTOBUF_PARSING_EXCEPTION = "Failed to parse bytes into protobuf";
    String CONFIGURATION_NOT_FOUND = "Configuration for key: ''{0}'' was not found";
    String APPLICATION_BALANCER_HOST_VALUE_NOT_EXISTS = "Application ''{0}'' hasn't balancer host value";
    String APPLICATION_BALANCER_PORT_VALUE_NOT_EXISTS = "Application ''{0}'' hasn't balancer port value";
    String APPLICATION_MODULE_SERVLET_PATH_VALUE_NOT_EXISTS = "Application module ''{0}'' hasn't servlet path value";
}
