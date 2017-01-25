package com.vosmann.appconfig;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Updater {

    private static final long INITIAL_DELAY_IN_SEC = 30;
    private static final long PERIOD_IN_SEC = 10;

    private final ScheduledExecutorService executor;

    public Updater() {
        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(this::run, INITIAL_DELAY_IN_SEC, PERIOD_IN_SEC, TimeUnit.SECONDS);
    }

    private void run() {
    }

}

