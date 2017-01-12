package com.vosmann.appconfig;

import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static com.vosmann.appconfig.AllowedType.isAllowedReturnType;
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

    private static Stream<Class<?>> scanAppConfigInterfaces() {
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

            final String fieldName = toFieldName(methodName);
            final String key = prefix + "." + fieldName;

            fields.add(new ExpectedField(key, type));
        }

        return fields;
    }

    private static String toFieldName(final String methodName) {
        if (isNormalGetter(methodName)) {
            return methodName.substring(3, 4).toLowerCase() + methodName.substring(4, methodName.length());
        } else {
            return methodName.substring(2, 3).toLowerCase() + methodName.substring(3, methodName.length());
        }
    }

    private static boolean isNormalGetter(final String methodName) {
        return methodName.startsWith("get") && methodName.charAt(3) == methodName.toUpperCase().charAt(3);
    }

    private static boolean isBooleanGetter(final String methodName) {
        return methodName.startsWith("is") && methodName.charAt(3) == methodName.toUpperCase().charAt(3);
    }

    private static void assertAllowed(final String methodName) {
        if (!isNormalGetter(methodName) && !isBooleanGetter(methodName)) {
            throw new AppConfigException("Unexpected method name in interface: " + methodName);
        }
    }

    private static void assertAllowed(final Class<?> type) {
        if (!isAllowedReturnType(type)) {
            throw new AppConfigException("Unexpected config type in interface: " + type);
        }
    }

}
