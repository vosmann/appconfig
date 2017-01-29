package com.vosmann.appconfig;

import com.google.inject.PrivateModule;
import com.vosmann.appconfig.implementer.Config;
import com.vosmann.appconfig.implementer.ExpectedFieldScanner;
import com.vosmann.appconfig.implementer.Implementer;

import static java.util.Objects.requireNonNull;

public class AppConfigModule extends PrivateModule {

    private final String locationOnFileSystem;
    private final String locationOnServer;

    public AppConfigModule(final String appConfigLocationOnFileSystem, final String appConfigLocationOnServer) {
        this.locationOnFileSystem = requireNonNull(appConfigLocationOnFileSystem);
        this.locationOnServer = requireNonNull(appConfigLocationOnServer);
    }

    @Override
    protected void configure() {
        final Config config = new Config(locationOnFileSystem, locationOnServer);
        final Implementer implementer = new Implementer(config);
        ExpectedFieldScanner.scanAppConfigInterfaces()
                            .forEach(interfaceType -> {
                                bind(interfaceType).toInstance(implementer.implement(interfaceType));
                                expose(interfaceType);
                            });
    }

}
