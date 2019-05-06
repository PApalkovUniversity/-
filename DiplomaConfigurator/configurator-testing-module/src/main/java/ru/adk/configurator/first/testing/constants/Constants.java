package ru.adk.configurator.first.testing.constants;

public interface Constants {

    String CONFIGURATOR_TESTING_MODULE_ID = "CONFIGURATOR_TESTING_MODULE";

    String HTTP_SERVER_BOOTSTRAP_THREAD = "http-server-bootstrap-thread";

    interface ServiceConstants {
        String CONFIGURATOR_FIRST_TESTING_SERVICE_ID = "CONFIGURATOR_FIRST_TESTING_SERVICE_ID";
        String SHOW_CONFIG_MESSAGES = "SHOW_CONFIG_MESSAGES";
        String SHOW_CONFIG_MESSAGES_PATH = "/configMessages";
    }

    interface ConfigKeys {
        String MESSAGES_SECTION = "messages";
        String FIRST_MESSAGE = "firstMessage";
        String SECOND_MESSAGE = "secondMessage";
    }
}
