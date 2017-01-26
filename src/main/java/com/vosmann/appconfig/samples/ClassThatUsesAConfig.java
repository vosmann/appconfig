package com.vosmann.appconfig.samples;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClassThatUsesAConfig {

    private static final Logger LOG = LogManager.getLogger(ClassThatUsesAConfig.class);

    private final EndpointConfig endpointConfig;
    private final ExecutorService executorService;

    @Inject
    public ClassThatUsesAConfig(final EndpointConfig endpointConfig) {
        this.endpointConfig = endpointConfig;
        this.executorService = Executors.newSingleThreadExecutor();
        this.executorService.submit(() -> {
            while (true) {
                LOG.info("Endpoint host: {}", endpointConfig.getHost());
                LOG.info("Endpoint port: {}", endpointConfig.getPort());
                try {
                    Thread.sleep(10 * 1000);
                } catch (final InterruptedException e) {
                }
            }
        });

    }

    public static void main(final String[] args) {

    }

}
