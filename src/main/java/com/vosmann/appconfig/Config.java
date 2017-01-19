package com.vosmann.appconfig;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.util.Optional;
import java.util.Set;

import static com.google.common.collect.Sets.difference;
import static com.vosmann.appconfig.AllowedType.getSimpleType;
import static com.vosmann.appconfig.ExpectedFieldScanner.scanExpectedFields;
import static java.util.stream.Collectors.toSet;

public class Config {

    private static final Logger LOG = LogManager.getLogger(Config.class);

    private final Set<ExpectedField> fields;
    private final JsonConfig jsonConfig;

    public Config(final InputStream appConfigFileStream) {
        fields = scanExpectedFields();
        jsonConfig = JsonConfig.from(appConfigFileStream);
        LOG.info(jsonConfig);
        LOG.info(fields);
        logUnexpectedConfigs();
        final Set<String> missingConfigs = findMissingConfigs();
        if (!missingConfigs.isEmpty()) {
            throw new AppConfigException("Missing or wrongly typed configs: " + missingConfigs);
        }
    }

    private Set<String> findMissingConfigs() {
        return fields.stream()
                     .filter(this::isConfigMissingOrWronglyTyped)
                     .map(ExpectedField::getKey)
                     .collect(toSet());
    }

    // todo this should be done on an interface-by-interface level. later.
    private boolean isConfigMissingOrWronglyTyped(final ExpectedField field) {

        if (!jsonConfig.contains(field.getKey())) {
            return true;
        }

        final Object value = jsonConfig.get(field.getKey());
        final Optional<Class<?>> valueType = getSimpleType(value);
        if (!valueType.isPresent()) {
            return true;
        }

        if (field.getType() != valueType.get()) {
            return true;
        }

        return false;
    }

    private void logUnexpectedConfigs() {
        final Set<String> all = jsonConfig.getKeys();
        final Set<String> expected = fields.stream()
                                           .map(ExpectedField::getKey)
                                           .collect(toSet());
        final Set<String> unexpected = difference(all, expected);
        LOG.warn("Unexpected configs found: {}.", unexpected);
    }

    public Object get(final String key) {
        return jsonConfig.get(key);
    }

}
