package ru.rti.application.configurator.api.mapping;


import ru.rti.application.configurator.api.entity.ModuleKey;
import ru.rti.application.entity.Entity;
import ru.rti.application.entity.mapper.ValueFromModelMapper.EntityFromModelMapper;
import ru.rti.application.entity.mapper.ValueMapper;
import ru.rti.application.entity.mapper.ValueToModelMapper.EntityToModelMapper;

import static ru.rti.application.configurator.api.mapping.ModuleKeyMapping.Fields.MODULE_ID;
import static ru.rti.application.configurator.api.mapping.ModuleKeyMapping.Fields.PROFILE_ID;
import static ru.rti.application.entity.Entity.entityBuilder;
import static ru.rti.application.entity.mapper.ValueMapper.mapper;

public interface ModuleKeyMapping {
    EntityToModelMapper<ModuleKey> toModel = entity -> ModuleKey.builder()
            .profileId(entity.getString(PROFILE_ID))
            .moduleId(entity.getString(MODULE_ID))
            .build();

    EntityFromModelMapper<ModuleKey> fromModel = entity -> entityBuilder()
            .stringField(PROFILE_ID, entity.getProfileId())
            .stringField(MODULE_ID, entity.getModuleId())
            .build();

    ValueMapper<ModuleKey, Entity> moduleKeyMapper = mapper(fromModel, toModel);

    interface Fields {
        String PROFILE_ID = "profileId";
        String MODULE_ID = "moduleId";
    }
}
