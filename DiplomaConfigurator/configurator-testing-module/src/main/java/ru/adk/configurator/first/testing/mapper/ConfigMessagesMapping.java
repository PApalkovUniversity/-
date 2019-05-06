package ru.adk.configurator.first.testing.mapper;

import ru.adk.configurator.first.testing.entity.ConfigMessages;
import ru.rti.application.entity.Entity;
import ru.rti.application.entity.mapper.ValueFromModelMapper.EntityFromModelMapper;
import ru.rti.application.entity.mapper.ValueMapper;
import ru.rti.application.entity.mapper.ValueToModelMapper.EntityToModelMapper;

import static ru.adk.configurator.first.testing.mapper.ConfigMessagesMapping.Fields.FIRST_MESSAGE;
import static ru.adk.configurator.first.testing.mapper.ConfigMessagesMapping.Fields.SECOND_MESSAGE;
import static ru.rti.application.entity.Entity.entityBuilder;
import static ru.rti.application.entity.mapper.ValueMapper.mapper;

public interface ConfigMessagesMapping {

    EntityToModelMapper<ConfigMessages> toModel = entity -> ConfigMessages.builder()
            .firstMessage(entity.getString(FIRST_MESSAGE))
            .secondMessage(entity.getString(SECOND_MESSAGE))
            .build();

    EntityFromModelMapper<ConfigMessages> fromModel = messages -> entityBuilder()
            .stringField(FIRST_MESSAGE, messages.getFirstMessage())
            .stringField(SECOND_MESSAGE, messages.getSecondMessage())
            .build();

    ValueMapper<ConfigMessages, Entity> configMessageMapper = mapper(fromModel, toModel);

    interface Fields {
        String FIRST_MESSAGE = "firstMessage";
        String SECOND_MESSAGE = "secondMessage";
    }
}
