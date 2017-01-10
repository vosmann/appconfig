package com.vosmann.appconfig;

import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

public class ConfigExpectationFactory {

    public static Set<Field> full() {
        return fromPackage("");
    }

    public static Set<Field> fromPackage(String packageName) {
        Set<Field> allFields = findInterfaces(packageName).map(ConfigExpectationFactory::extractExpectedFields)
                                                          .flatMap(fields -> fields.stream())
                                                          .collect(toSet());
        return allFields;
    }

    private static Stream<Class<? extends Object>> findInterfaces(String packageName) {
        Reflections reflections = new Reflections(packageName);
        // todo works with Class<?> ?
        // Set<Class<? extends Object>> all = reflections.getSubTypesOf(Object.class);
        Set<Class<? extends Object>> all = reflections.getTypesAnnotatedWith(AppConfig.class);

        Set<Class<? extends Object>> interfaces = all.stream()
                                                     .filter(Class::isInterface)
                                                     .collect(toSet());
        return interfaces.stream();
    }

    private static Set<Field> extractExpectedFields(Class<? extends Object> i) {

        final String prefix = i.getAnnotation(AppConfig.class).prefix();
        final Set<Field> fields = new HashSet<>();

        for (Method method : i.getMethods()) {

            final String methodName = method.getName();

            if (!isNormalGetter(methodName) && !isBooleanGetter(methodName)) {
                throw new AppConfigException("Unexpected method in interface.");
            }

            final String fieldName = toFieldName(methodName);

            final String key = prefix + "." + fieldName;
            fields.add(new Field(key, method.getReturnType()));
        }

        return fields;
    }

    private static String toFieldName(String methodName) {
        if (isNormalGetter(methodName)) {
            return methodName.substring(3, 4).toLowerCase() + methodName.substring(4, methodName.length());
        } else {
            return methodName.substring(2, 3).toLowerCase() + methodName.substring(3, methodName.length());
        }
    }

    private static boolean isNormalGetter(String methodName) {
        return methodName.startsWith("get") && methodName.charAt(3) == methodName.toUpperCase().charAt(3);
    }

    private static boolean isBooleanGetter(String methodName) {
        return methodName.startsWith("is") && methodName.charAt(3) == methodName.toUpperCase().charAt(3);
    }

}
