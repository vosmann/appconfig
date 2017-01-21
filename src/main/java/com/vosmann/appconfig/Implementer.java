package com.vosmann.appconfig;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class Implementer {

    private final Config config;

    public Implementer(final Config config) {
        this.config = config;
    }

    public <T> T implement(final Class<?> interfaceType) {
        final String prefix = interfaceType.getAnnotation(AppConfig.class).prefix();
        final Object interfaceImplementation = Proxy.newProxyInstance(interfaceType.getClassLoader(),
                                                                      new Class[]{interfaceType},
                                                                      new InterfaceMethodHandler(prefix, config));

        final T implementation = (T) interfaceImplementation;
        return implementation;
    }

    public Object implement2(final Class<?> interfaceType) {
        final String prefix = interfaceType.getAnnotation(AppConfig.class).prefix();
        final Object interfaceImplementation = Proxy.newProxyInstance(interfaceType.getClassLoader(),
                                                                      new Class[]{interfaceType},
                                                                      new InterfaceMethodHandler(prefix, config));

        final Object implementation = interfaceType.cast(interfaceImplementation);
        return implementation;
    }

    public static class InterfaceMethodHandler implements InvocationHandler {

        private final String prefix;
        private final Config config;

        public InterfaceMethodHandler(final String prefix, final Config config) {
            this.prefix = prefix;
            this.config = config;
        }

        @Override
        public Object invoke(final Object proxy, final Method method, final Object[] args) {
            final Object value = config.get(new FieldKey(prefix, method.getName()));
            return value;
        }
    }

}
