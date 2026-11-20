package io.github.equinoxelectronic.lullaby.mechanics;

import java.io.Serializable;

public class Time implements Serializable {
    private static final long serialVersionUID = 1L;

    private long timeInSeconds;

    public Time(long timeInSeconds) {
        this.timeInSeconds = timeInSeconds;
    }

    public static TimeConstructor construct(){
        return new TimeConstructor();
    }

    public long seconds() {
        return timeInSeconds;
    }

    public double minutesFloat() {
        return timeInSeconds / 60.0;
    }

    public long minutesLong() {
        return (long) timeInSeconds / 60;
    }

    public double hoursFloat() {
        return timeInSeconds / 3600.0;
    }

    public long hoursLong() {
        return (long) timeInSeconds / 3600;
    }

    public double daysFloat() {
        return timeInSeconds / 86400.0;
    }

    public long daysLong() {
        return (long) timeInSeconds / 86400;
    }

    public double weeksFloat() {
        return timeInSeconds / 604800.0;
    }

    public long weeksLong() {
        return (long) timeInSeconds / 604800;
    }

    public String getFormattedTime() {
        return "Day "+daysLong()+", "+hoursLong()+":"+minutesLong();
    }
}
