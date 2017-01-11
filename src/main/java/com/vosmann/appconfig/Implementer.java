package com.vosmann.appconfig;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class Implementer {

    public <T> T implement(final Class<?> type, final Config totalConfig) {
        final EndpointConfig config = (EndpointConfig) Proxy.newProxyInstance(EndpointConfig.class.getClassLoader(),
                                                                              new Class[]{EndpointConfig.class},
                                                                              new Handler());
        return null;
    }

    public static class Handler implements InvocationHandler {

        private Config config;
        private String prefix;

        @Override
        public Object invoke(final Object proxy, final Method method, final Object[] args) {
            final String methodName = method.getName();
            final Class<?>[] classes = method.getParameterTypes();
            // int.class

            // if (methodName.equals("getHost")) {
            //     return config.get(prefix + "." + "host");
            // } else if (methodName.equals("getPort")) {
            //     return config.get(prefix + "." + "port");
            // }
            return null;
        }
    }

}
