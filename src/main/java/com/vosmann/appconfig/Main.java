package com.vosmann.appconfig;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.PrivateModule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;

import static com.vosmann.appconfig.ExpectedFieldScanner.scanAppConfigInterfaces;

public class Main {

    private static final Logger LOG = LogManager.getLogger(Main.class);

    public static void main(final String[] args) {

        final InputStream appConfigFileStream = Main.class.getClassLoader().getResourceAsStream("appconfig.json");
        final Config config = new Config(appConfigFileStream);
        final Implementer implementer = new Implementer(config);
        final EndpointConfig endpoint = (EndpointConfig) implementer.implement2(EndpointConfig.class);

        final Injector injector = Guice.createInjector(new PrivateModule() {
            @Override
            protected void configure() {
                final InputStream appConfigFileStream = Main.class.getClassLoader().getResourceAsStream("appconfig.json");
                final Config config = new Config(appConfigFileStream);
                final Implementer implementer = new Implementer(config);
                scanAppConfigInterfaces()
                        // .forEach(interfaceType -> bind(interfaceType).toInstance(implementer.implement
                        // (interfaceType)));
                        .forEach(interfaceType -> {
                            final Object obj = implementer.implement(interfaceType);
                            bind(interfaceType).toInstance(obj);
                        });
            }
        });

        // final EndpointConfig endpoint = injector.getInstance(EndpointConfig.class);

        LOG.info("Endpoint host: {}", endpoint.getHost());
        LOG.info("Endpoint port: {}", endpoint.getPort());
    }

}
