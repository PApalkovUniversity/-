package ru.rti.application.configurator.dao;


import ru.rti.application.configurator.api.entity.ApplicationConfiguration;
import ru.rti.application.configurator.api.entity.ModuleKey;
import ru.rti.application.entity.Entity;
import ru.rti.application.entity.Value;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Optional.of;
import static java.util.Optional.ofNullable;
import static ru.rti.application.configurator.constants.ConfiguratorDbConstants.*;
import static ru.rti.application.configurator.dao.ConfigurationSaver.saveApplicationConfig;
import static ru.rti.application.configurator.dao.ConfigurationSaver.saveProfileConfig;
import static ru.rti.application.rocks.db.dao.RocksDbCollectionDao.getStringList;
import static ru.rti.application.rocks.db.dao.RocksDbPrimitiveDao.getString;
import static ru.rti.application.rocks.db.dao.RocksDbPrimitiveDao.put;
import static ru.rti.application.rocks.db.dao.RocksDbValueDao.getAsProtobuf;
import static ru.rti.application.rocks.db.dao.RocksDbValueDao.putAsProtobuf;

public interface ConfiguratorDao {
    static void saveConfig(ApplicationConfiguration inputConfig) {
        Entity fullConfig = inputConfig.getConfiguration();
        Set<String> profiles = fullConfig.getFieldNames();

        for (String profileId : profiles) {
            saveProfileConfig(profileId, fullConfig.getEntity(profileId));
        }
    }

    static void saveApplicationConfig(ApplicationConfiguration inputConfig) {
        Entity applicationConfiguration = inputConfig.getConfiguration();
        if (applicationConfiguration.isEmpty()) {
            return;
        }
        putAsProtobuf(APPLICATION, applicationConfiguration);
    }

    static Optional<Value> getConfig(String moduleKey) {
        return ofNullable(getAsProtobuf(moduleKey));
    }

    static Set<ModuleKey> getModuleKeys() {
        return getStringList((MODULE_KEYS)).stream().map(ModuleKey::parseKey).collect(Collectors.toSet());
    }

    static Set<ModuleKey> getProfileKeys() {
        return getStringList((PROFILE_KEYS)).stream().map(ModuleKey::parseKey).collect(Collectors.toSet());
    }
}
