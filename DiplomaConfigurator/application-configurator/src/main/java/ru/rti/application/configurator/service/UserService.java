package ru.rti.application.configurator.service;

import ru.rti.application.configurator.api.entity.UserRequest;
import ru.rti.application.configurator.api.entity.UserResponse;
import static ru.rti.application.configurator.dao.UserDao.*;
import static ru.rti.application.core.extension.JavaExtensions.isEmpty;

public interface UserService {
    static UserResponse login(UserRequest userRequest) {
        if (!userExists(userRequest.getName(), userRequest.getPassword())) return new UserResponse(false);
        return new UserResponse(true, getToken());
    }

    static void register(String userName, String password) {
        saveUser(userName, password);
    }

    static boolean checkToken(String requestToken) {
        String token = getToken();
        if (isEmpty(token)) return false;
        return token.equals(requestToken);
    }
}
