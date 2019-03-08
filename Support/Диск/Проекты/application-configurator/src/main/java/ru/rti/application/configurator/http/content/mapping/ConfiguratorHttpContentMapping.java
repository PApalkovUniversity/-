package ru.rti.application.configurator.http.content.mapping;

import ru.rti.application.http.json.HttpJsonMapper;
import ru.rti.application.http.mapper.HttpContentMapper;
import ru.rti.application.http.mapper.HttpTextPlainMapper;
import ru.rti.application.http.mime.MimeType;

import java.util.Map;

import static ru.rti.application.core.factory.CollectionsFactory.tableOf;
import static ru.rti.application.http.constants.HttpMimeTypes.APPLICATION_JSON;
import static ru.rti.application.http.constants.HttpMimeTypes.APPLICATION_JSON_UTF8;
import static ru.rti.application.metrics.http.constants.MetricsModuleHttpConstants.METRICS_CONTENT_TYPE;

public interface ConfiguratorHttpContentMapping {
    static Map<MimeType, HttpContentMapper> configureContentMappers(Map<MimeType, HttpContentMapper> defaultMappers) {
        HttpJsonMapper jsonMapper = new HttpJsonMapper();
        HttpContentMapper httpJsonMapper = new HttpContentMapper(jsonMapper, jsonMapper);
        return tableOf(defaultMappers)
                .add(APPLICATION_JSON, httpJsonMapper)
                .add(APPLICATION_JSON_UTF8, httpJsonMapper)
                .add(METRICS_CONTENT_TYPE, new HttpContentMapper(new HttpTextPlainMapper(), new HttpTextPlainMapper()));
    }
}
