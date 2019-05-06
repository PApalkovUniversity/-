package ru.rti.application.configurator.spec;

import lombok.Getter;
import ru.rti.application.http.server.model.HttpService;
import ru.rti.application.http.server.spec.HttpServiceSpecification;
import ru.rti.application.service.exception.UnknownServiceMethodException;
import static ru.rti.application.configurator.api.constants.ConfiguratorServiceConstants.Methods.CHECK_TOKEN;
import static ru.rti.application.configurator.api.constants.ConfiguratorServiceConstants.Methods.LOGIN;
import static ru.rti.application.configurator.api.constants.ConfiguratorServiceConstants.USER_SERVICE_ID;
import static ru.rti.application.configurator.api.mapping.UserMapping.userRequestToModelMapper;
import static ru.rti.application.configurator.api.mapping.UserMapping.userResponseFromModelMapper;
import static ru.rti.application.configurator.constants.ConfiguratorModuleConstants.*;
import static ru.rti.application.configurator.service.UserService.checkToken;
import static ru.rti.application.configurator.service.UserService.login;
import static ru.rti.application.core.caster.Caster.cast;
import static ru.rti.application.entity.PrimitiveMapping.boolMapper;
import static ru.rti.application.entity.PrimitiveMapping.stringMapper;
import static ru.rti.application.http.constants.HttpMimeTypes.APPLICATION_JSON_UTF8;
import static ru.rti.application.http.server.model.HttpService.httpService;

@Getter
public class UserServiceSpec implements HttpServiceSpecification {
    private final String serviceId = USER_SERVICE_ID;

    private final HttpService httpService = httpService()

            .post(LOGIN)
            .consumes(APPLICATION_JSON_UTF8)
            .fromBody()
            .withReq(userRequestToModelMapper)
            .produces(APPLICATION_JSON_UTF8)
            .withResp(userResponseFromModelMapper)
            .listen(LOGIN_URL)

            .post(CHECK_TOKEN)
            .consumes(APPLICATION_JSON_UTF8)
            .fromBody()
            .withReq(stringMapper.getToModel())
            .produces(APPLICATION_JSON_UTF8)
            .withResp(boolMapper.getFromModel())
            .listen(CHECK_TOKEN_URL)

            .serve(HTTP_PATH);
    @Override
    public <P, R> R executeMethod(String methodId, P req) {
        switch (methodId) {
            case LOGIN:
                return cast(login(cast(req)));
            case CHECK_TOKEN:
                return cast(checkToken(cast(req)));
            default:
                throw new UnknownServiceMethodException(serviceId, methodId);
        }
    }
}
