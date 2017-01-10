package com.vosmann.appconfig;

import java.util.Set;

public class Main {

    public static void main(String[] args) {

        // load json, flatten it into a TotalConfig
        TotalConfig totalConfig = TotalConfig.from(Main.class.getClassLoader().getResourceAsStream("appconfig.json"));
        System.out.println(totalConfig);

        // scan interfaces, build ConfigExpectation
        Set<Field> expectedFields = ConfigExpectationFactory.fromPackage("com.vosmann");
        System.out.println(expectedFields);

        // assert that configExpectation.isSatisfiedBy(totalConfig). otherwise throw.
        // implement all interfaces
        // bind and expose all interfaces' implementations using guice.
    }

}
