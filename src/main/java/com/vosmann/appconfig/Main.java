package com.vosmann.appconfig;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.vosmann.appconfig.samples.EndpointConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

    private static final Logger LOG = LogManager.getLogger(Main.class);

    public static void main(final String[] args) {

        final Injector injector = Guice.createInjector(new AppConfigModule());

        final EndpointConfig endpoint = injector.getInstance(EndpointConfig.class);

        LOG.info("Endpoint host: {}", endpoint.getHost());
        LOG.info("Endpoint port: {}", endpoint.getPort());
    }

}
