package ru.rti.application.configurator.configuration;

import lombok.Getter;
import org.zalando.logbook.Logbook;
import ru.rti.application.configurator.dao.UserDao;
import ru.rti.application.http.mapper.HttpContentMapper;
import ru.rti.application.http.mime.MimeType;
import ru.rti.application.http.server.HttpServerModuleConfiguration.HttpServerModuleDefaultConfiguration;
import ru.rti.application.http.server.interceptor.CookieInterceptor;
import ru.rti.application.http.server.interceptor.HttpServerInterceptor;
import static ru.rti.application.config.ConfigProvider.config;
import static ru.rti.application.config.constants.ConfigType.YAML;
import static ru.rti.application.configurator.constants.ConfiguratorModuleConstants.*;
import static ru.rti.application.configurator.constants.ConfiguratorModuleConstants.ConfiguratorLocalConfigKeys.*;
import static ru.rti.application.configurator.http.content.mapping.ConfiguratorHttpContentMapping.configureContentMappers;
import static ru.rti.application.core.factory.CollectionsFactory.dynamicArrayOf;
import static ru.rti.application.http.constants.HttpStatus.UNAUTHORIZED;
import static ru.rti.application.http.server.HttpServerModuleConfiguration.initializeWebServerInterceptors;
import static ru.rti.application.http.server.HttpServerModuleConfiguration.logbookWithoutWebLogs;
import static ru.rti.application.http.server.configurator.HttpWebContentMappersConfigurator.configureWebContentMappers;
import static ru.rti.application.http.server.constants.HttpServerModuleConstants.HttpWebUiServiceConstants.*;
import static ru.rti.application.http.server.interceptor.HttpServerInterceptor.intercept;
import static ru.rti.application.http.server.service.HttpWebResourceService.getStringResource;
import static ru.rti.application.metrics.http.filter.MetricsHttpLogFilter.logbookWithoutMetricsLogs;
import java.util.List;
import java.util.Map;

@Getter
public class ConfiguratorHttpServerConfiguration extends HttpServerModuleDefaultConfiguration {
    private final Map<MimeType, HttpContentMapper> contentMappers = configureWebContentMappers(configureContentMappers(super.getContentMappers()));
    private final int port = config(CONFIGURATOR_SECTION_ID, YAML).asYamlConfig().getInt(CONFIGURATOR_HTTP_PORT_PROPERTY);
    @Getter(lazy = true)
    private final List<HttpServerInterceptor> requestInterceptors = initializeRequestInterceptors(super.getRequestInterceptors());
    @Getter(lazy = true)
    private final HttpWebConfiguration webConfiguration = HttpWebConfiguration.builder()
            .resourceBufferSize(DEFAULT_BUFFER_SIZE)
            .htmlTemplateVariable(URL_TEMPLATE_VARIABLE, (url) -> config(CONFIGURATOR_SECTION_ID).asYamlConfig().getString(CONFIGURATOR_WEB_URL_PROPERTY))
            .build();
    @Getter(lazy = true)
    private final Logbook logbook = logbookWithoutMetricsLogs(logbookWithoutWebLogs()).build();

    private static List<HttpServerInterceptor> initializeRequestInterceptors(List<HttpServerInterceptor> superInterceptors) {
        List<HttpServerInterceptor> httpServerInterceptors = dynamicArrayOf(initializeWebServerInterceptors(superInterceptors));
        httpServerInterceptors.add(intercept(CookieInterceptor.builder()
                .checkingUrls(AUTHORIZATION_CHECKING_URLS)
                .cookieValue(TOKEN_COOKIE, UserDao::getToken)
                .errorStatus(UNAUTHORIZED.getCode())
                .errorContent(getStringResource(INDEX_HTML))
                .build()));
        return httpServerInterceptors;
    }
}