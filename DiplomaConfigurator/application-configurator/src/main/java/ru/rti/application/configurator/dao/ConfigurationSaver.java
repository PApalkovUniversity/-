package ru.rti.application.configurator.dao;

import lombok.NoArgsConstructor;
import ru.rti.application.entity.Entity;
import ru.rti.application.entity.Entity.EntityBuilder;
import ru.rti.application.entity.Value;
import ru.rti.application.rocks.db.dao.RocksDbPrimitiveDao;
import static java.lang.String.join;
import static lombok.AccessLevel.PRIVATE;
import static ru.rti.application.configurator.constants.ConfiguratorDbConstants.MODULE_KEYS;
import static ru.rti.application.configurator.constants.ConfiguratorDbConstants.PROFILE_KEYS;
import static ru.rti.application.core.constants.StringConstants.COLON;
import static ru.rti.application.entity.constants.ValueType.ENTITY;
import static ru.rti.application.rocks.db.dao.RocksDbValueDao.putAsProtobuf;
import java.util.Set;

@NoArgsConstructor(access = PRIVATE)
class ConfigurationSaver {
    static void saveProfileModulesConfiguration(String profileId, Entity profileConfigEntity) {
        RocksDbPrimitiveDao.add(PROFILE_KEYS, profileId);
        EntityBuilder profileConfigBuilder = Entity.entityBuilder();
        Set<String> profileFields = profileConfigEntity.getFieldNames();
        for (String profileField : profileFields) {
            Value profileFieldConfig = profileConfigEntity.getValue(profileField);
            if (profileFieldConfig.getType().equals(ENTITY)) {
                String moduleKey = join(COLON, profileId, profileField);
                RocksDbPrimitiveDao.add(MODULE_KEYS, moduleKey);
                saveModuleConfiguration(moduleKey, profileConfigEntity, profileField);
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

    private static void saveModuleConfiguration(String moduleKey, Entity profileConfigEntity, String profileId) {
        Entity moduleConfig = profileConfigEntity.getEntity(profileId);
        if (moduleConfig.isEmpty()) {
            return;
        }
        putAsProtobuf(moduleKey, moduleConfig);
    }
}
