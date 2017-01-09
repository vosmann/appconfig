package com.vosmann.appconfig;

import com.vosmann.appconfig.defaults.DefaultLong;
import com.vosmann.appconfig.defaults.DefaultString;

@AppConfig(prefix = "tracking.endpoint")
public interface EndpointConfig {

    @DefaultString(value = "https")
    String getScheme();

    String getHost();

    int getPort();

    @DefaultLong(value = 1000)
    long getTimeout();

}
