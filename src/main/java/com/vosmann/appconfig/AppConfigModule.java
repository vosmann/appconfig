package com.vosmann.appconfig;

import com.google.inject.PrivateModule;

import java.io.InputStream;

import static com.vosmann.appconfig.ExpectedFieldScanner.scanAppConfigInterfaces;

public class AppConfigModule extends PrivateModule {

    @Override
    protected void configure() {
        final InputStream fileStream = AppConfigModule.class.getClassLoader()
                                                            .getResourceAsStream("appconfig.json");
        final Config config = new Config(fileStream);
        final Implementer implementer = new Implementer(config);
        scanAppConfigInterfaces()
                .forEach(interfaceType -> {
                    bind(interfaceType).toInstance(implementer.implement(interfaceType));
                    expose(interfaceType);
                });
    }

}
