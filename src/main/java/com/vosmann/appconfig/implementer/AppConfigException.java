package com.vosmann.appconfig.implementer;

public class AppConfigException extends RuntimeException {

    public AppConfigException(final String message) {
        super(message);
    }

    public AppConfigException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
