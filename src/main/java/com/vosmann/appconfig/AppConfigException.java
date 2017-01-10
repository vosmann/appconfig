package com.vosmann.appconfig;

public class AppConfigException extends RuntimeException {

    public AppConfigException(String message) {
        super(message);
    }

    public AppConfigException(String message, Throwable cause) {
        super(message, cause);
    }

}
