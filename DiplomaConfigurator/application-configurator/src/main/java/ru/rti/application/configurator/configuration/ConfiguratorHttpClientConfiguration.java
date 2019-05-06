package ru.rti.application.configurator.configuration;

import lombok.Getter;
import ru.rti.application.http.client.configuration.HttpClientModuleConfiguration.HttpClientModuleDefaultConfiguration;
import ru.rti.application.http.mapper.HttpContentMapper;
import ru.rti.application.http.mime.MimeType;
import static ru.rti.application.configurator.http.content.mapping.ConfiguratorHttpContentMapping.configureContentMappers;
import static ru.rti.application.http.server.configurator.HttpWebContentMappersConfigurator.configureWebContentMappers;
import java.util.Map;

@Getter
public class ConfiguratorHttpClientConfiguration extends HttpClientModuleDefaultConfiguration {
    private final Map<MimeType, HttpContentMapper> contentMappers = configureWebContentMappers(configureContentMappers(super.getContentMappers()));
}