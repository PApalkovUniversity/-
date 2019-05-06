package ru.adk.configurator.first.testing.spec;

import lombok.Getter;
import ru.rti.application.http.server.model.HttpService;
import ru.rti.application.http.server.spec.HttpServiceSpecification;
import ru.rti.application.service.exception.UnknownServiceMethodException;

import static ru.adk.configurator.first.testing.constants.Constants.ServiceConstants.*;
import static ru.adk.configurator.first.testing.mapper.ConfigMessagesMapping.configMessageMapper;
import static ru.adk.configurator.first.testing.service.ConfigurationFirstTestingModuleService.showConfigMessages;
import static ru.rti.application.core.caster.Caster.cast;
import static ru.rti.application.http.constants.HttpMimeTypes.APPLICATION_JSON_UTF8;
import static ru.rti.application.http.server.model.HttpService.httpService;
import static ru.rti.application.http.server.module.HttpServerModule.httpServerModule;

@Getter
public class ConfigurationFirstTestingModuleServiceSpec implements HttpServiceSpecification {
    private final String serviceId = CONFIGURATOR_FIRST_TESTING_SERVICE_ID;

    private final HttpService httpService = httpService()
            .post(SHOW_CONFIG_MESSAGES)
            .produces(APPLICATION_JSON_UTF8)
            .withResp(configMessageMapper.getFromModel())
            .listen(SHOW_CONFIG_MESSAGES_PATH)
            .serve(httpServerModule().getPath());

    @Override
    public <P, R> R executeMethod(String methodId, P req) {
        switch (methodId) {
            case SHOW_CONFIG_MESSAGES:
                return cast(showConfigMessages());
            default:
                throw new UnknownServiceMethodException(serviceId, methodId);
        }
    }
}
