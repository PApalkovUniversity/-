package ru.rti.application.configurator.exceptions;

public class ConfiguratorModuleException extends RuntimeException {
    public ConfiguratorModuleException(String message) {
        super(message);
    }

    public ConfiguratorModuleException(String message, Exception e) {
        super(message, e);
    }
}
