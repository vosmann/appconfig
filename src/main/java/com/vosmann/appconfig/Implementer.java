package com.vosmann.appconfig;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

import static com.google.common.collect.ImmutableMap.of;

public class Implementer {

    private final Config config;

    public Implementer(final Config config) {
        this.config = config;
    }

    public <T> T implement(final Class<T> interfaceType) {
        // final Object interfaceImplementation = Proxy.newProxyInstance(input.getClass().getClassLoader(),
        //                                                               new Class[]{input.getClass()},
        //                                                               new InterfaceMethodHandler(config));
        final Object interfaceImplementation = Proxy.newProxyInstance(interfaceType.getClassLoader(),
                                                                      new Class[]{interfaceType},
                                                                      new InterfaceMethodHandler(config));
        final T implementation = (T) interfaceImplementation;
        return implementation;
    }

    public static class InterfaceMethodHandler implements InvocationHandler {

        private final Config config;
        private final Map<String, ExpectedField> expectedFields =
                of("getHost", new ExpectedField("tracking.endpoint.host", String.class),
                   "getPort", new ExpectedField("tracking.endpoint.port", int.class));

        public InterfaceMethodHandler(final Config config) {
            this.config = config;
        }

        @Override
        public Object invoke(final Object proxy, final Method method, final Object[] args) {
            final ExpectedField expectedField = expectedFields.get(method.getName());
            final Object value = config.get(expectedField.getKey());
            return value;
        }
    }

}
