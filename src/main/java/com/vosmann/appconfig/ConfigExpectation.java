package com.vosmann.appconfig;

import com.google.common.collect.Maps;

import java.util.Map;

public class ConfigExpectation {

    private Map<String, Field> fields;

    private ConfigExpectation(Builder builder) {
        this.fields = builder.fields;
    }

    public static class Builder {

        private Map<String, Field> fields = Maps.newHashMap();

        public Builder add(String name, Class<?> type) {
            fields.put(name, new Field(name, type));
            return this;
        }

        public ConfigExpectation build() {
            return new ConfigExpectation(this);
        }
    }

}
