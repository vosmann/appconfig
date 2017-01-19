package com.vosmann.appconfig;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;

public class Main {

    private static final Logger LOG = LogManager.getLogger(Main.class);

    public static void main(final String[] args) {

        final InputStream appConfigFileStream = Main.class.getClassLoader().getResourceAsStream("appconfig.json");
        final Config config = new Config(appConfigFileStream);

        final Implementer implementer = new Implementer(config);
        final EndpointConfig endpoint = implementer.implement(EndpointConfig.class);
        LOG.info("Endpoint host: {}", endpoint.getHost());
        LOG.info("Endpoint port: {}", endpoint.getPort());

        // bind and expose all interfaces' implementations using guice.
    }

}
