package ru.rti.application.configurator.factory;

import lombok.SneakyThrows;
import org.apache.http.client.utils.URIBuilder;
import ru.rti.application.config.remote.api.spec.RemoteConfigProxyServiceSpec;
import ru.rti.application.configurator.api.entity.ModuleKey;
import ru.rti.application.configurator.provider.ApplicationModulesParametersProvider.ApplicationModuleParameters;
import ru.rti.application.http.client.proxy.HttpClientProxyBuilder;
import static java.lang.Integer.valueOf;
import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toSet;
import static ru.rti.application.configurator.constants.ConfiguratorModuleConstants.GRPC_INSTANCES;
import static ru.rti.application.core.constants.StringConstants.*;
import static ru.rti.application.entity.Value.asCollection;
import static ru.rti.application.http.client.constants.MimeToContentTypeMapper.applicationJsonUtf8;
import static ru.rti.application.http.constants.HttpCommonConstants.HTTP_SCHEME;
import java.util.Set;

public interface RemoteConfigProxyServiceSpecFactory {
    @SneakyThrows
    static Set<RemoteConfigProxyServiceSpec> createRemoteConfigProxySpecs(ApplicationModuleParameters parameters, ModuleKey moduleKey) {
        URIBuilder uriBuilder = new URIBuilder();
        String url = uriBuilder
                .setScheme(HTTP_SCHEME)
                .setHost(parameters.getBalancerHost())
                .setPort(parameters.getBalancerPort())
                .setPath(moduleKey.getModuleId().replaceAll(DASH, UNDERSCORE) + DOT + moduleKey.getProfileId().replaceAll(DASH, UNDERSCORE) + GRPC_INSTANCES)
                .build()
                .toString();
        return HttpClientProxyBuilder.<String, Set<String>>httpClientProxy(url)
                .responseMapper(hosts -> asCollection(hosts).getStringSet())
                .consumes(applicationJsonUtf8())
                .get()
                .prepare()
                .execute()
                .orElse(emptySet())
                .stream()
                .map(host -> new RemoteConfigProxyServiceSpec(host.split(COLON)[0], valueOf(host.split(COLON)[1]), parameters.getPath()))
                .collect(toSet());

    }
}
