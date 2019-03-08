package ru.rti.application.configurator.api.mapping;


import ru.rti.application.configurator.api.entity.ModuleKey;
import ru.rti.application.entity.Entity;
import ru.rti.application.entity.mapper.ValueFromModelMapper.EntityFromModelMapper;
import ru.rti.application.entity.mapper.ValueMapper;
import ru.rti.application.entity.mapper.ValueToModelMapper.EntityToModelMapper;

import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static ru.rti.application.configurator.api.mapping.ModuleKeyCollectionMapping.Fields.KEYS;
import static ru.rti.application.configurator.api.mapping.ModuleKeyMapping.moduleKeyMapper;
import static ru.rti.application.entity.Entity.entityBuilder;
import static ru.rti.application.entity.mapper.ValueMapper.mapper;

public interface ModuleKeyCollectionMapping {
    EntityToModelMapper<Set<ModuleKey>> toModel = entity -> entity.getEntityList(KEYS)
            .stream()
            .map(moduleKeyMapper.getToModel()::map)
            .collect(toSet());

    EntityFromModelMapper<Set<ModuleKey>> fromModel = keyCollection -> entityBuilder()
            .entityCollectionField(KEYS, keyCollection.stream().map(moduleKeyMapper.getFromModel()::map).collect(toSet()))
            .build();

    ValueMapper<Set<ModuleKey>, Entity> moduleKeyCollectionMapper = mapper(fromModel, toModel);

    interface Fields {
        String KEYS = "keys";
    }
}
