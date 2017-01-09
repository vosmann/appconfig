package com.vosmann.appconfig;

import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TotalConfigTest {

    @Test
    public void testString() throws IOException {
        TotalConfig config = TotalConfig.fromDefaultLocation();
        String host = config.get("tracking.endpoint.host");
        assertThat(host, is("an.example.com"));
    }

    @Test
    public void testInt() throws IOException {
        TotalConfig config = TotalConfig.fromDefaultLocation();
        Integer port = config.get("tracking.endpoint.port");
        assertThat(port, is(10000));
    }
}