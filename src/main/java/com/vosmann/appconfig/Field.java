package com.vosmann.appconfig;

import com.google.common.base.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Field field = (Field) o;
        return Objects.equal(key, field.key) &&
                Objects.equal(type, field.type);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(key, type);
    }

    @Override
    public String toString() {
        return "Field{" +
                "key='" + key + '\'' +
                ", type=" + type +
                '}';
    }

}
