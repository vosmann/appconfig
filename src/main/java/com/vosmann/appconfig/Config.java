package com.vosmann.appconfig;

import java.io.InputStream;
import java.util.Set;

import static com.google.common.collect.Sets.difference;
import static com.vosmann.appconfig.ExpectedFieldScanner.scanExpectedFields;
import static java.util.stream.Collectors.toSet;

public class Config {

    private final Set<ExpectedField> fields;
    private final JsonConfig jsonConfig;

    public Config(final InputStream appConfigFileStream) {
        fields = scanExpectedFields();
        jsonConfig = JsonConfig.from(appConfigFileStream);
        System.out.println(jsonConfig);
        System.out.println(fields);
        logUnexpectedConfigs();
        final Set<String> missingConfigs = findMissingConfigs();
        if (!missingConfigs.isEmpty()) {
            throw new AppConfigException("Missing configs: " + missingConfigs);
        }
        // assert that configExpectation.isSatisfiedBy(config). otherwise throw
    }

    private Set<String> findMissingConfigs() {
        return fields.stream()
                     .filter(this::isConfigMissing)
                     .map(ExpectedField::getKey)
                     .collect(toSet());
    }

    private boolean isConfigMissing(final ExpectedField field) {

        final Object value = jsonConfig.get(field.getKey());
        if (value == null) {
            return true;
        }

        // Assert type correctness.

        return false;
    }

    private void logUnexpectedConfigs() {
        final Set<String> all = jsonConfig.getKeys();
        final Set<String> expected = fields.stream()
                                           .map(ExpectedField::getKey)
                                           .collect(toSet());
        final Set<String> unexpected = difference(all, expected);
        System.out.println("Unexpected configs found: " + unexpected);
    }

    // public <T> T get(final String key) {
    //     return (T) allConfigs.get(key);
    // }

}
