package com.vosmann.appconfig;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class Implementer {

    public <T> T implement(Class<?> type, TotalConfig totalConfig) {
        EndpointConfig config = (EndpointConfig) Proxy.newProxyInstance(EndpointConfig.class.getClassLoader(),
                                                                        new Class[] { EndpointConfig.class },
                                                                        new Handler());
        return null;
    }

    public static class Handler implements InvocationHandler {

        private TotalConfig totalConfig;
        private String prefix;

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) {
            String methodName = method.getName();
            Class<?>[] classes = method.getParameterTypes();
            // int.class

            if (methodName.equals("getHost")) {
                return totalConfig.get(prefix + "." + "host");
            } else if (methodName.equals("getPort")) {
                return totalConfig.get(prefix + "." + "port");
            }
            return null;
        }
    }

}
