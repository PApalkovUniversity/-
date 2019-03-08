package ru.rti.application.configurator.api.spec;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.rti.application.configurator.api.entity.ApplicationConfiguration;
import ru.rti.application.configurator.api.entity.ModuleKey;
import ru.rti.application.entity.Entity;
import ru.rti.application.protobuf.client.builder.ProtobufClientProxyBuilder;
import ru.rti.application.protobuf.client.builder.ProtobufClientProxyBuilder.ProtobufClientProxyPreparedBuilder;
import ru.rti.application.protobuf.client.proxy.spec.ProtobufProxyServiceSpecification;
import ru.rti.application.service.exception.UnknownServiceMethodException;

import static java.lang.System.getProperty;
import static ru.rti.application.configurator.api.constants.ConfiguratorProxyServiceConstants.CONFIGURATOR_PROXY_SERVICE_ID;
import static ru.rti.application.configurator.api.constants.ConfiguratorServiceConstants.CONFIGURATOR_SERVICE_ID;
import static ru.rti.application.configurator.api.constants.ConfiguratorServiceConstants.CONFIGURATOR_SERVLET_PATH;
import static ru.rti.application.configurator.api.constants.ConfiguratorServiceConstants.Methods.GET_PROTOBUF_CONFIG;
import static ru.rti.application.configurator.api.mapping.ConfigurationEntitiesMapping.ApplicationConfigurationMapping.applicationConfigurationMapper;
import static ru.rti.application.configurator.api.mapping.ModuleKeyMapping.moduleKeyMapper;
import static ru.rti.application.core.caster.Caster.cast;
import static ru.rti.application.core.constants.ContextConstants.LOCAL_PROFILE;
import static ru.rti.application.core.constants.SystemPropeties.PROFILE_PROPERTY;
import static ru.rti.application.core.context.Context.contextConfiguration;
import static ru.rti.application.core.extension.JavaExtensions.isEmpty;

@Getter
@AllArgsConstructor
public class ConfiguratorProxyServiceSpec implements ProtobufProxyServiceSpecification {
    private final String host;
    private final int port;
    private final String clientServletPath = CONFIGURATOR_SERVLET_PATH;
    private final String serviceId = CONFIGURATOR_PROXY_SERVICE_ID;

    private final String profileId = getProperty(PROFILE_PROPERTY);
    private final ModuleKey moduleKey = new ModuleKey(isEmpty(profileId) ? LOCAL_PROFILE : profileId, contextConfiguration().getApplicationModuleId());

    @Getter(lazy = true)
    private final ProtobufClientProxyPreparedBuilder<ModuleKey, ApplicationConfiguration> getProtobufConfigProxy = ProtobufClientProxyBuilder.<ModuleKey, ApplicationConfiguration>protobufClientProxy()
            .syncClient(syncClient())
            .serviceId(CONFIGURATOR_SERVICE_ID)
            .methodId(GET_PROTOBUF_CONFIG)
            .requestMapper(moduleKeyMapper.getFromModel())
            .responseMapper(applicationConfigurationMapper.getToModel())
            .prepare();


    private ApplicationConfiguration getConfiguration() {
        try {
            return getGetProtobufConfigProxy().execute(moduleKey).orElse(ApplicationConfiguration.builder().build());
        } catch (Exception e) {
            return ApplicationConfiguration.builder()
                    .configuration(Entity.entityBuilder().build())
                    .build();
        }
    }

    @Override
    public <P, R> R executeMethod(String methodId, P request) {
        switch (methodId) {
            case GET_PROTOBUF_CONFIG:
                return cast(getConfiguration());
        }
        throw new UnknownServiceMethodException(getServiceId(), methodId);
    }
}

