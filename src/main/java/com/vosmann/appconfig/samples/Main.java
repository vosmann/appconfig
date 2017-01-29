package com.vosmann.appconfig.samples;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.PrivateModule;
import com.vosmann.appconfig.AppConfigModule;

public class Main {

    private static final String LOCATION_ON_FILESYSTEM = "/tmp/appconfig.json";
    private static final String LOCATION_ON_SERVER =
            "https://raw.githubusercontent.com/vosmann/appconfig/master/src/test/resources/appconfig.json";

    public static void main(final String[] args) {

        final Injector injector = Guice.createInjector(new AppConfigModule(LOCATION_ON_FILESYSTEM, LOCATION_ON_SERVER),
                                                       new PrivateModule() {
                                                           @Override
                                                           protected void configure() {
                                                               bind(ClassThatUsesAConfig.class);
                                                               expose(ClassThatUsesAConfig.class);
                                                           }
                                                       });
        final ClassThatUsesAConfig classThatUsesAConfig = injector.getInstance(ClassThatUsesAConfig.class);

        while (true) {
            try {
                Thread.sleep(100000000);
            } catch (final InterruptedException e) {
            }
        }
    }

}
