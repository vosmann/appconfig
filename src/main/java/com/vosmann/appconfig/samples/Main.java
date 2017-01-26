package com.vosmann.appconfig.samples;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.PrivateModule;
import com.vosmann.appconfig.AppConfigModule;

public class Main {

    public static void main(final String[] args) {

        final Injector injector = Guice.createInjector(new AppConfigModule("/tmp/appconfig.json"),
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
