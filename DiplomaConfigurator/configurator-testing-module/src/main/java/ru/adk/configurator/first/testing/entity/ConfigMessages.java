package ru.adk.configurator.first.testing.entity;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import ru.rti.application.entity.Value;
import ru.rti.application.service.validation.Validatable;
import ru.rti.application.service.validation.Validator;

import static ru.rti.application.service.validation.ValidationExpressions.notNull;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class ConfigMessages implements Validatable {
    private String firstMessage;
    private String secondMessage;

    @Override
    public void onValidating(Validator validator) {
        validator.validate("firstMessage", firstMessage, notNull());
        validator.validate("secondMessage", secondMessage, notNull());
    }
}

