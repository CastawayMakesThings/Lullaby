package io.github.equinoxelectronic.lullaby.mechanics;

public class TimeConstructor {
    private long timeInSeconds;

    public Time construct() {
        return new Time(timeInSeconds);
    }

    public Time build() {
        return construct();
    }

    public TimeConstructor seconds(long timeInSeconds) {
        this.timeInSeconds += timeInSeconds;
        return this;
    }

    public TimeConstructor minutes(long minutes) {
        this.timeInSeconds += minutes * 60;
        return this;
    }

    public TimeConstructor hours(long hours) {
        this.timeInSeconds += hours * 3600;
        return this;
    }

    public TimeConstructor days(long days) {
        this.timeInSeconds += days * 86400;
        return this;
    }

    public TimeConstructor weeks(long weeks) {
        this.timeInSeconds += weeks * 604800;
        return this;
    }
}
