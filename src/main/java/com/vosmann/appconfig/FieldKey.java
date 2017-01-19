package com.vosmann.appconfig;

public class FieldKey {

    final String key;

    public FieldKey(final String prefix, final String methodName) {
        final String fieldName = toFieldName(methodName);
        key = prefix + "." + fieldName;
    }

    public String getKey() {
        return key;
    }

    private static String toFieldName(final String methodName) {
        if (isNormalGetter(methodName)) {
            return methodName.substring(3, 4).toLowerCase() + methodName.substring(4, methodName.length());
        } else {
            return methodName.substring(2, 3).toLowerCase() + methodName.substring(3, methodName.length());
        }
    }

    public static boolean isNormalGetter(final String methodName) {
        return methodName.startsWith("get") && methodName.charAt(3) == methodName.toUpperCase().charAt(3);
    }

    public static boolean isBooleanGetter(final String methodName) {
        return methodName.startsWith("is") && methodName.charAt(3) == methodName.toUpperCase().charAt(3);
    }

}
