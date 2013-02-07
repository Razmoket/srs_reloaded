package fr.afnic.utils;

import java.util.concurrent.TimeUnit;

import com.google.common.base.Preconditions;

public class Delay {

    private int count = 0;
    private TimeUnit timeUnit;

    public Delay() {
    }

    public Delay(int delay, TimeUnit timeUnit) {
        super();
        this.count = delay;
        this.timeUnit = timeUnit;
    }

    public void sleep() {
        Preconditions.checkNotNull(this.timeUnit, "timeUnit");
        try {
            this.timeUnit.sleep(this.count);
        } catch (InterruptedException e) {
            throw new IllegalArgumentException("Sleep failed.");
        }
    }

    public long toMillis() {
        return this.timeUnit.toMillis(this.count);
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

}
