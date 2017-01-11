package com.vosmann.appconfig;

import java.io.InputStream;

public class Main {

    public static void main(final String[] args) {

        final InputStream appConfigFileStream = Main.class.getClassLoader().getResourceAsStream("appconfig.json");
        new Config(appConfigFileStream);

        // implement all interfaces

        // bind and expose all interfaces' implementations using guice.
    }

}
