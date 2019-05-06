package ru.rti.application.configurator.spec;

import lombok.Getter;
import ru.rti.application.configurator.api.entity.Configuration;
import ru.rti.application.configurator.api.entity.ModuleConfiguration;
import ru.rti.application.configurator.api.entity.ModuleKey;
import ru.rti.application.configurator.api.entity.ProfileConfiguration;
import ru.rti.application.http.server.model.HttpService;
import ru.rti.application.http.server.spec.HttpServiceSpecification;
import ru.rti.application.protobuf.server.model.ProtobufService;
import ru.rti.application.protobuf.server.spec.ProtobufServiceSpecification;
import ru.rti.application.service.exception.UnknownServiceMethodException;
import static ru.rti.application.configurator.api.constants.ConfiguratorServiceConstants.CONFIGURATOR_SERVICE_ID;
import static ru.rti.application.configurator.api.constants.ConfiguratorServiceConstants.Methods.*;
import static ru.rti.application.configurator.api.mapping.ConfigurationMapping.configurationMapper;
import static ru.rti.application.configurator.api.mapping.ModuleConfigurationMapping.moduleConfigurationMapper;
import static ru.rti.application.configurator.api.mapping.ModuleKeyCollectionMapping.moduleKeyCollectionMapper;
import static ru.rti.application.configurator.api.mapping.ModuleKeyMapping.moduleKeyMapper;
import static ru.rti.application.configurator.api.mapping.ProfileConfigurationMapping.profileConfigurationMapper;
import static ru.rti.application.configurator.constants.ConfiguratorModuleConstants.HTTP_PATH;
import static ru.rti.application.configurator.service.ConfiguratorService.*;
import static ru.rti.application.core.caster.Caster.cast;
import static ru.rti.application.core.factory.CollectionsFactory.fixedArrayOf;
import static ru.rti.application.http.constants.HttpMimeTypes.APPLICATION_JSON_UTF8;
import static ru.rti.application.http.server.constants.HttpServerModuleConstants.HTTP_SERVICE_TYPE;
import static ru.rti.application.http.server.model.HttpService.httpService;
import static ru.rti.application.protobuf.server.constants.ProtobufServerModuleConstants.PROTOBUF_SERVICE_TYPE;
import static ru.rti.application.protobuf.server.model.ProtobufService.ProtobufMethod.protobufMethod;
import static ru.rti.application.protobuf.server.model.ProtobufService.protobufService;
import static ru.rti.application.service.constants.RequestValidationPolicy.NOT_NULL;
import static ru.rti.application.service.constants.RequestValidationPolicy.VALIDATABLE;
import java.util.List;

@Getter
public class ConfiguratorServiceSpec implements HttpServiceSpecification, ProtobufServiceSpecification {
    private final String serviceId = CONFIGURATOR_SERVICE_ID;

    private final HttpService httpService = httpService()
            .post(UPLOAD_CONFIG)
            .consumes(APPLICATION_JSON_UTF8)
            .fromBody()
            .withReq(configurationMapper.getToModel(), VALIDATABLE)
            .produces(APPLICATION_JSON_UTF8)
            .listen("/upload")

            .post(GET_JSON_CONFIG)
            .fromBody()
            .withReq(moduleKeyMapper.getToModel(), NOT_NULL)
            .produces(APPLICATION_JSON_UTF8)
            .withResp(configurationMapper.getFromModel())
            .listen("/get")

            .post(GET_APPLICATION_CONFIG)
            .produces(APPLICATION_JSON_UTF8)
            .withResp(configurationMapper.getFromModel())
            .listen("/getApplicationConfiguration")

            .post(APPLY_MODULE_CONFIG)
            .consumes(APPLICATION_JSON_UTF8)
            .fromBody()
            .withReq(moduleKeyMapper.getToModel(), NOT_NULL)
            .listen("/apply")

            .post(GET_ALL_PROFILES)
            .produces(APPLICATION_JSON_UTF8)
            .withResp(moduleKeyCollectionMapper.getFromModel())
            .listen("/profiles")

            .post(GET_ALL_MODULES)
            .consumes(APPLICATION_JSON_UTF8)
            .fromBody()
            .withReq(moduleKeyMapper.getToModel())
            .produces(APPLICATION_JSON_UTF8)
            .withResp(moduleKeyCollectionMapper.getFromModel())
            .listen("/modules")

            .post(UPLOAD_APPLICATION_CONFIG)
            .consumes(APPLICATION_JSON_UTF8)
            .fromBody()
            .withReq(configurationMapper.getToModel(), VALIDATABLE)
            .produces(APPLICATION_JSON_UTF8)
            .listen("/uploadApplication")

            .post(UPLOAD_PROFILE_CONFIG)
            .consumes(APPLICATION_JSON_UTF8)
            .fromBody()
            .withReq(profileConfigurationMapper.getToModel())
            .produces(APPLICATION_JSON_UTF8)
            .listen("/uploadProfile")

            .post(UPLOAD_MODULE_CONFIG)
            .consumes(APPLICATION_JSON_UTF8)
            .fromBody()
            .withReq(moduleConfigurationMapper.getToModel())
            .produces(APPLICATION_JSON_UTF8)
            .listen("/uploadModule")

            .serve(HTTP_PATH);

    private final ProtobufService protobufService = protobufService()
            .method(GET_PROTOBUF_CONFIG, protobufMethod()
                    .requestMapper(moduleKeyMapper.getToModel())
                    .responseMapper(configurationMapper.getFromModel())
                    .build())
            .build();

    private final List<String> serviceTypes = fixedArrayOf(PROTOBUF_SERVICE_TYPE, HTTP_SERVICE_TYPE);

    @Override
    public <P, R> R executeMethod(String methodId, P req) {
        switch (methodId) {
            case UPLOAD_CONFIG:
                uploadConfiguration((Configuration) req);
                return null;
            case UPLOAD_APPLICATION_CONFIG:
                uploadApplicationConfiguration((Configuration) req);
                return null;
            case UPLOAD_PROFILE_CONFIG:
                uploadProfileConfiguration((ProfileConfiguration) req);
                return null;
            case UPLOAD_MODULE_CONFIG:
                uploadModuleConfiguration((ModuleConfiguration) req);
                return null;
            case GET_JSON_CONFIG:
            case GET_PROTOBUF_CONFIG:
                return cast(getConfiguration((ModuleKey) req));
            case APPLY_MODULE_CONFIG:
                applyModuleConfiguration((ModuleKey) req);
                return null;
            case GET_ALL_PROFILES:
                return cast(getProfiles());
            case GET_ALL_MODULES:
                return cast(getModules());
            case GET_APPLICATION_CONFIG:
                return cast(getApplicationConfiguration());
            default:
                throw new UnknownServiceMethodException(serviceId, methodId);
        }
    }
}
