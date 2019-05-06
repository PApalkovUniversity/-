package ru.rti.application.configurator.dao;

import static java.util.UUID.randomUUID;
import static ru.rti.application.configurator.constants.ConfiguratorDbConstants.TOKEN_KEY;
import static ru.rti.application.core.extension.JavaExtensions.isEmpty;
import static ru.rti.application.rocks.db.dao.RocksDbPrimitiveDao.getString;
import static ru.rti.application.rocks.db.dao.RocksDbPrimitiveDao.put;

public interface UserDao {
    static void saveUser(String username, String password) {
        put(username, password);
        put(TOKEN_KEY, randomUUID().toString());
    }

    static boolean userExists(String username, String password) {
        String userPassword = getString(username);
        if (isEmpty(userPassword) || isEmpty(password)) return false;
        return password.equalsIgnoreCase(userPassword);
    }

    static String getToken() {
        return getString(TOKEN_KEY);
    }
}
