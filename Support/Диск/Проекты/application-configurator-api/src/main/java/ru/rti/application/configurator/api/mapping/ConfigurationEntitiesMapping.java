package ru.rti.application.configurator.api.mapping;

import ru.rti.application.configurator.api.entity.ApplicationConfiguration;
import ru.rti.application.entity.Entity;
import ru.rti.application.entity.mapper.ValueFromModelMapper.EntityFromModelMapper;
import ru.rti.application.entity.mapper.ValueMapper;
import ru.rti.application.entity.mapper.ValueToModelMapper.EntityToModelMapper;

import static ru.rti.application.entity.mapper.ValueMapper.mapper;

public interface ConfigurationEntitiesMapping {
    interface ApplicationConfigurationMapping {
        EntityToModelMapper<ApplicationConfiguration> toModel = entity -> ApplicationConfiguration.builder()
                .configuration(entity)
                .build();

        EntityFromModelMapper<ApplicationConfiguration> fromModel = ApplicationConfiguration::getConfiguration;

        ValueMapper<ApplicationConfiguration, Entity> applicationConfigurationMapper = mapper(fromModel, toModel);
    }
}
