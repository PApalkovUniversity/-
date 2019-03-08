package ru.rti.application.configurator.api.constants;

public interface ConfiguratorServiceConstants {
    String CONFIGURATOR_SERVLET_PATH = "ru.rti.application.configurator.ConfiguratorGrpcServlet";
    String CONFIGURATOR_SERVICE_ID = "CONFIGURATOR_SERVICE";

    interface Methods {
        String UPLOAD_CONFIG = "UPLOAD_CONFIG";
        String GET_PROTOBUF_CONFIG = "GET_PROTOBUF_CONFIG";
        String GET_JSON_CONFIG = "GET_JSON_CONFIG";
        String APPLY_MODULE_CONFIG = "APPLY_MODULE_CONFIG";
        String GET_ALL_PROFILES = "GET_ALL_PROFILES";
        String GET_ALL_MODULES = "GET_ALL_MODULES";
        String UPLOAD_APPLICATION_CONFIG = "UPLOAD_APPLICATION_CONFIG";
        String GET_APPLICATION_CONFIG = "GET_APPLICATION_CONFIG";
    }
}
