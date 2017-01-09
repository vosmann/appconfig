package com.vosmann.appconfig;

import com.google.common.collect.Maps;

import java.util.Map;

public class Field {

    private final String key;
    private final Class<?> type;

    public Field(String key, Class<?> type) {
        this.key = key;
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public Class<?> getType() {
        return type;
    }

    public static class FieldStorage {
        private Map<String, Field> fields;

        private FieldStorage(Builder builder) {
            this.fields = builder.fields;
        }

        public static class Builder {
            private Map<String, Field> fields = Maps.newHashMap();
            public FieldStorage.Builder add(String name, Class<?> type) {
                fields.put(name, new Field(name, type));
                return this;
            }
        }

        // public FieldStorage build() {
        //     return new FieldStorage(this);
        // }
    }

}
