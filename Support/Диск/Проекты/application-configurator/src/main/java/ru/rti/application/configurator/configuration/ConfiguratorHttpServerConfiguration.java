package ru.rti.application.configurator.configuration;

import lombok.Getter;
import ru.rti.application.http.mapper.HttpContentMapper;
import ru.rti.application.http.mime.MimeType;
import ru.rti.application.http.server.HttpServerModuleConfiguration.HttpServerModuleDefaultConfiguration;

import java.util.Map;

import static ru.rti.application.config.ConfigProvider.config;
import static ru.rti.application.config.constants.ConfigType.YAML;
import static ru.rti.application.configurator.constants.ConfiguratorModuleConstants.ConfiguratorLocalConfigKeys.CONFIGURATOR_HTTP_PORT_PROPERTY;
import static ru.rti.application.configurator.constants.ConfiguratorModuleConstants.ConfiguratorLocalConfigKeys.CONFIGURATOR_SECTION_ID;
import static ru.rti.application.configurator.http.content.mapping.ConfiguratorHttpContentMapping.configureContentMappers;

@Getter
public class ConfiguratorHttpServerConfiguration extends HttpServerModuleDefaultConfiguration {
    private final Map<MimeType, HttpContentMapper> contentMappers = configureContentMappers(super.getContentMappers());
    private final int port = config(CONFIGURATOR_SECTION_ID, YAML).asYamlConfig().getInt(CONFIGURATOR_HTTP_PORT_PROPERTY);
}
