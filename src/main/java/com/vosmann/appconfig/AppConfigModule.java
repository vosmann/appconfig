package com.vosmann.appconfig;

import com.google.inject.PrivateModule;
import com.vosmann.appconfig.implementer.Config;
import com.vosmann.appconfig.implementer.ExpectedFieldScanner;
import com.vosmann.appconfig.implementer.Implementer;

import java.io.InputStream;

public class AppConfigModule extends PrivateModule {

    private static final String DEFAULT_APP_CONFIG_FILE_LOCATION = "appconfig.json";

    private final String appConfigFileLocation;

    public AppConfigModule() {
        this.appConfigFileLocation = DEFAULT_APP_CONFIG_FILE_LOCATION;
    }

    public AppConfigModule(final String appConfigFileLocation) {
        this.appConfigFileLocation = appConfigFileLocation;
    }

    @Override
    protected void configure() {
        final InputStream fileStream = AppConfigModule.class.getClassLoader()
                                                            .getResourceAsStream(appConfigFileLocation);
        final Config config = new Config(fileStream);
        final Implementer implementer = new Implementer(config);
        ExpectedFieldScanner.scanAppConfigInterfaces()
                            .forEach(interfaceType -> {
                                bind(interfaceType).toInstance(implementer.implement(interfaceType));
                                expose(interfaceType);
                            });
    }

}
