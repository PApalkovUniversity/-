package ru.rti.application.configurator.spec;

import lombok.Getter;
import ru.rti.application.configurator.api.entity.ApplicationConfiguration;
import ru.rti.application.configurator.api.entity.ModuleKey;
import ru.rti.application.http.server.model.HttpServiceConfig;
import ru.rti.application.http.server.spec.HttpServiceSpecification;
import ru.rti.application.protobuf.server.model.ProtobufServiceConfig;
import ru.rti.application.protobuf.server.spec.ProtobufServiceSpecification;
import ru.rti.application.service.exception.UnknownServiceMethodException;

import java.util.List;

import static ru.rti.application.configurator.api.constants.ConfiguratorServiceConstants.CONFIGURATOR_SERVICE_ID;
import static ru.rti.application.configurator.api.constants.ConfiguratorServiceConstants.Methods.*;
import static ru.rti.application.configurator.api.mapping.ConfigurationEntitiesMapping.ApplicationConfigurationMapping.applicationConfigurationMapper;
import static ru.rti.application.configurator.api.mapping.ModuleKeyCollectionMapping.moduleKeyCollectionMapper;
import static ru.rti.application.configurator.api.mapping.ModuleKeyMapping.moduleKeyMapper;
import static ru.rti.application.configurator.constants.ConfiguratorModuleConstants.HTTP_PATH;
import static ru.rti.application.configurator.service.ConfiguratorService.*;
import static ru.rti.application.core.caster.Caster.cast;
import static ru.rti.application.core.factory.CollectionsFactory.fixedArrayOf;
import static ru.rti.application.http.constants.HttpMimeTypes.APPLICATION_JSON;
import static ru.rti.application.http.server.constants.HttpServerModuleConstants.HTTP_SERVICE_TYPE;
import static ru.rti.application.http.server.model.HttpServiceConfig.httpService;
import static ru.rti.application.protobuf.server.constants.ProtobufServerModuleConstants.PROTOBUF_SERVICE_TYPE;
import static ru.rti.application.protobuf.server.model.ProtobufServiceConfig.ProtobufServiceMethodConfig.protobufMethodConfig;
import static ru.rti.application.protobuf.server.model.ProtobufServiceConfig.protobufServiceConfig;
import static ru.rti.application.service.constants.RequestValidationPolicy.NOT_NULL;
import static ru.rti.application.service.constants.RequestValidationPolicy.VALIDATABLE;

@Getter
public class ConfiguratorServiceSpec implements HttpServiceSpecification, ProtobufServiceSpecification {
    private final String serviceId = CONFIGURATOR_SERVICE_ID;

    private final HttpServiceConfig httpServiceConfig = httpService()
            .post(UPLOAD_CONFIG)
            .consumes(APPLICATION_JSON)
            .fromBody()
            .withReq(applicationConfigurationMapper.getToModel(), VALIDATABLE)
            .produces(APPLICATION_JSON)
            .listen("/upload")

            .post(GET_JSON_CONFIG)
            .fromBody()
            .withReq(moduleKeyMapper.getToModel(), NOT_NULL)
            .produces(APPLICATION_JSON)
            .withResp(applicationConfigurationMapper.getFromModel())
            .listen("/get")

            .post(APPLY_MODULE_CONFIG)
            .consumes(APPLICATION_JSON)
            .fromBody()
            .withReq(moduleKeyMapper.getToModel(), NOT_NULL)
            .listen("/apply")

            .post(GET_ALL_PROFILES)
            .produces(APPLICATION_JSON)
            .withResp(moduleKeyCollectionMapper.getFromModel())
            .listen("/get-profiles")

            .post(GET_ALL_MODULES)
            .consumes(APPLICATION_JSON)
            .fromBody()
            .withReq(moduleKeyMapper.getToModel())
            .produces(APPLICATION_JSON)
            .withResp(moduleKeyCollectionMapper.getFromModel())
            .listen("/get-modules")

            .post(UPLOAD_APPLICATION_CONFIG)
            .consumes(APPLICATION_JSON)
            .fromBody()
            .withReq(applicationConfigurationMapper.getToModel(), VALIDATABLE)
            .produces(APPLICATION_JSON)
            .listen("/upload-application")

            .post(GET_APPLICATION_CONFIG)
            .produces(APPLICATION_JSON)
            .withResp(applicationConfigurationMapper.getFromModel())
            .listen("/get-application")

            .serve(HTTP_PATH);

    private final ProtobufServiceConfig protobufServiceConfig = protobufServiceConfig()
            .methodConfig(GET_PROTOBUF_CONFIG, protobufMethodConfig()
                    .requestMapper(moduleKeyMapper.getToModel())
                    .responseMapper(applicationConfigurationMapper.getFromModel())
                    .build())
            .build();

    private final List<String> serviceTypes = fixedArrayOf(PROTOBUF_SERVICE_TYPE, HTTP_SERVICE_TYPE);

    @Override
    public <P, R> R executeMethod(String methodId, P req) {
        switch (methodId) {
            case UPLOAD_CONFIG:
                uploadConfiguration((ApplicationConfiguration) req);
                return null;
            case GET_JSON_CONFIG:
            case GET_PROTOBUF_CONFIG:
                return cast(getConfiguration((ModuleKey) req));
            case APPLY_MODULE_CONFIG:
                applyModuleConfiguration((ModuleKey) req);
                return null;
            case GET_ALL_PROFILES:
                return cast(getAllProfiles());
            case GET_ALL_MODULES:
                return cast(getAllModules());
            case UPLOAD_APPLICATION_CONFIG:
                uploadApplicationConfiguration((ApplicationConfiguration) req);
                return null;
            case GET_APPLICATION_CONFIG:
                return cast(getApplicationConfiguration());
            default:
                throw new UnknownServiceMethodException(serviceId, methodId);
        }
    }
}
