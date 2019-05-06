package ru.rti.application.configurator.constants;

import static ru.rti.application.core.factory.CollectionsFactory.setOf;
import java.util.Set;

public interface ConfiguratorModuleConstants {
    String CONFIGURATOR_MODULE_ID = "CONFIGURATOR_MODULE";
    String HTTP_SERVER_BOOTSTRAP_THREAD = "http-server-bootstrap-thread";
    String GRPC_INSTANCES = ".grpc.instances";

    String APPLICATION_BALANCER_HOST_CONFIG_KEY = "balancer.host";
    String APPLICATION_BALANCER_PORT_CONFIG_KEY = "balancer.port";
    String HTTP_PATH = "/configurator";
    String APPLICATION_MODULE_PROTOBUF_SERVER_PATH_CONFIG_KEY = "protobuf.server.path";
    String TOKEN_COOKIE = "TOKEN";
    String LOGIN_URL = "/login";
    String CHECK_TOKEN_URL = "/checkToken";
    Set<String> AUTHORIZATION_CHECKING_URLS = setOf("/upload",
            "/get",
            "/getApplicationConfiguration",
            "/apply",
            "/profiles",
            "/modules",
            "/uploadApplication",
            "/uploadProfile",
            "/uploadModule");

    interface ConfiguratorLocalConfigKeys {
        String CONFIGURATOR_SECTION_ID = "configurator";
        String CONFIGURATOR_HTTP_PORT_PROPERTY = "http.port";
        String CONFIGURATOR_PROTOBUF_PORT_PROPERTY = "protobuf.port";
        String CONFIGURATOR_WEB_URL_PROPERTY = "http.url";
        String CONFIGURATOR_USER = "user";
        String CONFIGURATOR_PASSWORD = "password";
        String CONFIGURATOR_ROCKS_DB_PATH = "rocks.db.path";
    }

    String BROADCAST_URL_POSTFIX = "_all";
}
