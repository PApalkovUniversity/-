package ru.rti.application.configurator.configuration;

import io.micrometer.prometheus.PrometheusMeterRegistry;
import lombok.Getter;
import ru.rti.application.metrics.configuration.MetricModuleConfiguration.MetricModuleDefaultConfiguration;
import static ru.rti.application.configurator.constants.ConfiguratorModuleConstants.CONFIGURATOR_MODULE_ID;
import static ru.rti.application.configurator.constants.ConfiguratorModuleConstants.HTTP_PATH;
import static ru.rti.application.metrics.configurator.PrometheusRegistryConfigurator.prometheusRegistryForApplication;

@Getter
public class ConfiguratorMetricsConfiguration extends MetricModuleDefaultConfiguration {
    private final String managementHttpPath = HTTP_PATH;
    private final PrometheusMeterRegistry prometheusMeterRegistry = prometheusRegistryForApplication(CONFIGURATOR_MODULE_ID);
}
