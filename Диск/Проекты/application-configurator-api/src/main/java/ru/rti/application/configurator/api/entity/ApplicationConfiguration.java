package ru.rti.application.configurator.api.entity;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import ru.rti.application.entity.Entity;
import ru.rti.application.service.validation.Validatable;
import ru.rti.application.service.validation.Validator;

import static ru.rti.application.service.validation.ValidationExpressions.notNull;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class ApplicationConfiguration implements Validatable {
    Entity configuration;

    @Override
    public void onValidating(Validator validator) {
        validator.validate("configuration", configuration, notNull());
    }
}

