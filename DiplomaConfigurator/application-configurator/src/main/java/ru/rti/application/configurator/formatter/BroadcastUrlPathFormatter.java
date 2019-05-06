package ru.rti.application.configurator.formatter;

import static ru.rti.application.configurator.constants.ConfiguratorModuleConstants.BROADCAST_URL_POSTFIX;
import static ru.rti.application.core.constants.StringConstants.DASH;
import static ru.rti.application.core.constants.StringConstants.UNDERSCORE;

public interface BroadcastUrlPathFormatter {
    static String formatBroadcastUrl(String path, String profile) {
        return path + UNDERSCORE + profile.replaceAll(DASH, UNDERSCORE) + BROADCAST_URL_POSTFIX;
    }
}
