package com.vosmann.appconfig;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.google.common.collect.ImmutableList.of;

public enum AllowedType {

    BOOLEAN(boolean.class, of(boolean.class, Boolean.class)),

    INT(int.class, of(int.class, Integer.class)),

    DOUBLE(double.class, of(double.class, Double.class, float.class, Float.class)),

    STRING(String.class, of(String.class));

    private Class<?> expectedReturnType;
    private List<Class<?>> matchingTypes;

    AllowedType(final Class<?> expectedReturnType, final List<Class<?>> matchingTypes) {
        this.expectedReturnType = expectedReturnType;
        this.matchingTypes = matchingTypes;
    }

    public static boolean isAllowedReturnType(final Class<?> type) {
        for (final AllowedType allowedType : values()) {
            if (Objects.equals(type, allowedType.expectedReturnType)) {
                return true;
            }
        }
        return false;
    }

    public static Optional<Class<?>> getSimpleType(final Object object) {
        for (final AllowedType allowedType : values()) {
            for (final Class<?> matchingType : allowedType.matchingTypes) {
                if (matchingType.isInstance(object)) {
                    return Optional.of(allowedType.expectedReturnType);
                }
            }
        }

        return Optional.empty();
    }
}
