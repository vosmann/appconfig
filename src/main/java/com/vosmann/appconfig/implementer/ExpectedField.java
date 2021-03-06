package com.vosmann.appconfig.implementer;

import com.google.common.base.Objects;

public class ExpectedField {

    private final FieldKey key;
    private final Class<?> type;

    public ExpectedField(final FieldKey key, final Class<?> type) {
        this.key = key;
        this.type = type;
    }

    public FieldKey getKey() {
        return key;
    }

    public Class<?> getType() {
        return type;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ExpectedField field = (ExpectedField) o;
        return Objects.equal(key, field.key) &&
                Objects.equal(type, field.type);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(key, type);
    }

    @Override
    public String toString() {
        return "ExpectedField{" +
                "key='" + key + '\'' +
                ", type=" + type +
                '}';
    }

}
