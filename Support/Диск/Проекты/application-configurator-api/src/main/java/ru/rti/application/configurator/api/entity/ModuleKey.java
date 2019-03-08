package ru.rti.application.configurator.api.entity;

import lombok.*;
import ru.rti.application.configurator.api.exception.ModuleKeyParsingException;

import static java.lang.String.join;
import static ru.rti.application.core.constants.StringConstants.*;
import static ru.rti.application.core.extension.JavaExtensions.isEmpty;
import static ru.rti.application.core.extension.JavaExtensions.isNotEmpty;

@Getter
@Builder
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class ModuleKey {
    String profileId;
    String moduleId;

    public static ModuleKey parseKey(String key) {
        String[] keyParts = key.split(COLON);
        if (isEmpty(keyParts)) throw new ModuleKeyParsingException(key);
        ModuleKeyBuilder builder = ModuleKey.builder();
        builder.profileId = keyParts[0];
        if (keyParts.length > 1) {
            builder.moduleId = keyParts[1];
        }
        return builder.build();
    }

    public String formatKey() {
        if (isNotEmpty(profileId) && isNotEmpty(moduleId)) {
            return join(COLON, profileId, moduleId);
        }

        if (isNotEmpty(profileId)) {
            return profileId;
        }

        return EMPTY_STRING;
    }

//    public String formatKey() {
//        if (isNotEmpty(profileId) && isNotEmpty(moduleId)) {
//            return join(UNDERSCORE, profileId.toLowerCase(), moduleId.replaceAll(UNDERSCORE, DASH).toLowerCase());
//        }
//
//        if (isNotEmpty(profileId)) {
//            return profileId.toLowerCase();
//        }
//
//        return EMPTY_STRING;
//    }
}
