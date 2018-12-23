package ru.rti.application.configurator.configuration;

import lombok.Getter;
import ru.rti.application.protobuf.server.configuration.ProtobufServerModuleConfiguration.ProtobufServerModuleDefaultConfiguration;

import static ru.rti.application.config.ConfigProvider.config;
import static ru.rti.application.config.constants.ConfigType.YAML;
import static ru.rti.application.configurator.api.constants.ConfiguratorServiceConstants.CONFIGURATOR_SERVLET_PATH;
import static ru.rti.application.configurator.constants.ConfiguratorModuleConstants.ConfiguratorLocalConfigKeys.CONFIGURATOR_PROTOBUF_PORT_PROPERTY;
import static ru.rti.application.configurator.constants.ConfiguratorModuleConstants.ConfiguratorLocalConfigKeys.CONFIGURATOR_SECTION_ID;

@Getter
public class ConfiguratorProtobufServerConfiguration extends ProtobufServerModuleDefaultConfiguration {
    private final String servletPath = CONFIGURATOR_SERVLET_PATH;
    private final int port = config(CONFIGURATOR_SECTION_ID, YAML).asYamlConfig().getInt(CONFIGURATOR_PROTOBUF_PORT_PROPERTY);
}
