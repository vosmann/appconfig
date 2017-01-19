package com.vosmann.appconfig;

import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static com.vosmann.appconfig.AllowedType.isAllowedReturnType;
import static com.vosmann.appconfig.FieldKey.isBooleanGetter;
import static com.vosmann.appconfig.FieldKey.isNormalGetter;
import static java.util.stream.Collectors.toSet;

public class ExpectedFieldScanner {

    private static final String ALL_PACKAGES_PREFIX = "";
    private static final Set<Class<?>> ALLOWED_CONFIG_TYPES;

    static {
        ALLOWED_CONFIG_TYPES = new HashSet<>();
        ALLOWED_CONFIG_TYPES.add(boolean.class);
        ALLOWED_CONFIG_TYPES.add(int.class);
        ALLOWED_CONFIG_TYPES.add(double.class);
        ALLOWED_CONFIG_TYPES.add(String.class);
    }

    public static Set<ExpectedField> scanExpectedFields() {
        final Set<ExpectedField> fields = scanAppConfigInterfaces().map(ExpectedFieldScanner::extractExpectedFields)
                                                                   .flatMap(fs -> fs.stream())
                                                                   .collect(toSet());
        return fields;
    }

    public static Stream<Class<?>> scanAppConfigInterfaces() {
        final Reflections reflections = new Reflections(ALL_PACKAGES_PREFIX);
        final Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(AppConfig.class);
        return annotated.stream()
                        .filter(Class::isInterface);
    }

    private static Set<ExpectedField> extractExpectedFields(final Class<? extends Object> i) {

        final String prefix = i.getAnnotation(AppConfig.class).prefix();
        final Set<ExpectedField> fields = new HashSet<>();

        for (final Method method : i.getMethods()) {

            final String methodName = method.getName();
            final Class<?> type = method.getReturnType();

            assertAllowed(methodName);
            assertAllowed(type);
            assertAllowed(method.getParameterCount());

            final FieldKey key = new FieldKey(prefix, methodName);

            fields.add(new ExpectedField(key, type));
        }

        return fields;
    }

    private static void assertAllowed(final String methodName) {
        if (!isNormalGetter(methodName) && !isBooleanGetter(methodName)) {
            throw new AppConfigException("Unexpected method name in interface: " + methodName);
        }
    }

    private static void assertAllowed(final Class<?> type) {
        if (!isAllowedReturnType(type)) {
            throw new AppConfigException("Unsupported return value on config method: " + type);
        }
    }

    private static void assertAllowed(final int parameterCount) {
        if (parameterCount > 0) {
            throw new AppConfigException("Config methods should not have any parameters.");
        }
    }

}
