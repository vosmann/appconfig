package com.vosmann.appconfig.implementer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.google.common.collect.Sets.difference;
import static java.util.stream.Collectors.toSet;

public class Config {

    private static final Logger LOG = LogManager.getLogger(Config.class);

    private static final long INITIAL_DELAY_IN_SEC = 30;
    private static final long PERIOD_IN_SEC = 10;

    private final Set<ExpectedField> fields;
    private final String fileLocation;
    private final ScheduledExecutorService executor;

    private JsonConfig jsonConfig;

    public Config(final String fileLocation) {
        this.fields = ExpectedFieldScanner.scanExpectedFields();
        this.fileLocation = fileLocation;
        this.jsonConfig = loadJsonConfig();

        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> this.jsonConfig = loadJsonConfig(),
                                     INITIAL_DELAY_IN_SEC,
                                     PERIOD_IN_SEC,
                                     TimeUnit.SECONDS);
    }

    private JsonConfig loadJsonConfig() {
        final JsonConfig jsonConfig;
        try {
            jsonConfig = JsonConfig.from(fileLocation);
        } catch (final IOException e) {
            LOG.error("Could not load appconfig.json file.");
            throw new AppConfigException("Could not read file.", e);
        }

        LOG.info("Loaded a JSON config from {}. JSON config: {}.", fileLocation, jsonConfig);
        logUnexpectedConfigs(jsonConfig);
        final Set<String> missingConfigs = findMissingConfigs(jsonConfig);
        if (!missingConfigs.isEmpty()) {
            throw new AppConfigException("Missing or wrongly typed configs: " + missingConfigs);
        }

        return jsonConfig;
    }

    private Set<String> findMissingConfigs(final JsonConfig jsonConfig) {
        return fields.stream()
                     .filter(field -> isConfigMissingOrWronglyTyped(field, jsonConfig))
                     .map(ExpectedField::getKey)
                     .map(FieldKey::getKey)
                     .collect(toSet());
    }

    // todo fix the unwieldy .getKey().getKey() call.
    private boolean isConfigMissingOrWronglyTyped(final ExpectedField field, final JsonConfig jsonConfig) {

        if (!jsonConfig.contains(field.getKey().getKey())) {
            return true;
        }

        final Object value = jsonConfig.get(field.getKey().getKey());
        final Optional<Class<?>> valueType = AllowedType.getSimpleType(value);
        if (!valueType.isPresent()) {
            return true;
        }

        if (field.getType() != valueType.get()) {
            return true;
        }

        return false;
    }

    private void logUnexpectedConfigs(final JsonConfig jsonConfig) {
        final Set<String> all = jsonConfig.getKeys();
        final Set<String> expected = fields.stream()
                                           .map(ExpectedField::getKey)
                                           .map(FieldKey::getKey)
                                           .collect(toSet());
        final Set<String> unexpected = difference(all, expected);
        LOG.warn("Unexpected configs found: {}.", unexpected);
    }

    public Object get(final FieldKey key) {
        return jsonConfig.get(key.getKey());
    }

}
