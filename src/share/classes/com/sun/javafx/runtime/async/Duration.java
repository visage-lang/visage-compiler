package com.sun.javafx.runtime.async;

import javax.swing.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Duration
 *
 * @author Brian Goetz
 */
public class Duration {
    private final DurationListener listener;
    private final int min, max, interval, increment;
    private ScheduledFuture<?> future;
    public static final int MIN_INTERVAL = 100;

    public Duration(DurationListener listener, int min, int max, int duration) {
        this.listener = listener;
        this.min = min;
        this.max = max;
        int interval = duration / (max - min);
        if (interval < MIN_INTERVAL) {
            increment = MIN_INTERVAL / interval;
            this.interval = interval * increment;
        }
        else {
            this.interval = interval;
            this.increment = 1;
        }
    }

    public void start() {
        class Task implements Runnable {
            private int next = min;

            public void run() {
                final int nextNumber = Math.min(next, max);
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        listener.onTick(nextNumber);
                    }
                });
                next += increment;
                if (next > max)
                    future.cancel(false);
            }
        }

        future = BackgroundExecutor.getTimer().scheduleWithFixedDelay(new Task(), 0, interval, TimeUnit.MILLISECONDS);
    }

    public void cancel() {
        if (future != null)
            future.cancel(false);
    }
}
