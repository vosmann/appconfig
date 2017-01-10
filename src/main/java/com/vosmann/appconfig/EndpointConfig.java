package com.vosmann.appconfig;

@AppConfig(prefix = "tracking.endpoint")
public interface EndpointConfig {

    // @DefaultString(prefix = "https")
    // String getScheme();

    String getHost();

    int getPort();

    // @DefaultLong(prefix = 1000)
    // long getTimeout();

}
