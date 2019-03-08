package ru.rti.application.configurator.dao;

import lombok.NoArgsConstructor;
import ru.rti.application.entity.Entity;
import ru.rti.application.entity.Entity.EntityBuilder;
import ru.rti.application.entity.Value;
import ru.rti.application.rocks.db.dao.RocksDbPrimitiveDao;

import java.util.Set;

import static java.lang.String.join;
import static lombok.AccessLevel.PRIVATE;
import static ru.rti.application.configurator.constants.ConfiguratorDbConstants.*;
import static ru.rti.application.core.constants.StringConstants.COLON;
import static ru.rti.application.entity.constants.ValueType.ENTITY;
import static ru.rti.application.rocks.db.dao.RocksDbValueDao.putAsProtobuf;

@NoArgsConstructor(access = PRIVATE)
class ConfigurationSaver {
    static void saveApplicationConfig(Entity applicationConfig) {
        if (applicationConfig.isEmpty()) {
            return;
        }
        putAsProtobuf(APPLICATION, applicationConfig);
    }

    static void saveProfileConfig(String profileId, Entity profileConfigEntity) {
        RocksDbPrimitiveDao.add(PROFILE_KEYS, profileId);
        EntityBuilder profileConfigBuilder = Entity.entityBuilder();
        Set<String> profileFields = profileConfigEntity.getFieldNames();
        for (String profileField : profileFields) {
            Value profileFieldConfig = profileConfigEntity.getValue(profileField);
            if (profileFieldConfig.getType().equals(ENTITY)) {
                String moduleKey = join(COLON, profileId, profileField);
                RocksDbPrimitiveDao.add(MODULE_KEYS, moduleKey);
                saveModuleConfig(moduleKey, profileConfigEntity, profileField);
                continue;
            }
            profileConfigBuilder = profileConfigBuilder.valueField(profileField, profileFieldConfig);
        }

        Entity profileConfig = profileConfigBuilder.build();
        if (profileConfig.isEmpty()) {
            return;
        }

        putAsProtobuf(profileId, profileConfig);
    }

    private static void saveModuleConfig(String moduleKey, Entity applicationInstanceFieldEntity, String applicationInstanceField) {
        Entity moduleConfig = applicationInstanceFieldEntity.getEntity(applicationInstanceField);
        if (moduleConfig.isEmpty()) {
            return;
        }
        putAsProtobuf(moduleKey, moduleConfig);
    }
}
