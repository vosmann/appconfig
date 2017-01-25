package com.vosmann.appconfig.samples;

import com.vosmann.appconfig.annotations.AppConfig;

@AppConfig(prefix = "tracking.endpoint")
public interface EndpointConfig {

    // @DefaultString(prefix = "https")
    // String getScheme();

    String getHost();

    int getPort();

    // @DefaultLong(prefix = 1000)
    // long getTimeout();

}
