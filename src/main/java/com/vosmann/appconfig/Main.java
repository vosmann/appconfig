package com.vosmann.appconfig;

import java.util.Set;

public class Main {

    public static void main(final String[] args) {

        // load json, flatten it into a TotalConfig
        final TotalConfig totalConfig = TotalConfig.from(Main.class.getClassLoader().getResourceAsStream("appconfig.json"));
        System.out.println(totalConfig);

        // scan interfaces, build ConfigExpectation
        final Set<ExpectedField> expectedFields = ExpectedFieldFinder.findExpectedFields();
        System.out.println(expectedFields);

        // assert that configExpectation.isSatisfiedBy(totalConfig). otherwise throw. warn about redundant configs in
        // file.

        // implement all interfaces

        // bind and expose all interfaces' implementations using guice.
    }

}
