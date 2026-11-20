package io.github.equinoxelectronic.lullaby.mechanics;

public class TimeConstructor {
    private long timeInSeconds;

    public Time construct(){
        return new Time(timeInSeconds);
    }

    public TimeConstructor seconds(long timeInSeconds){
        this.timeInSeconds = timeInSeconds;
        return this;
    }

    public TimeConstructor minutes(long timeInSeconds){
        this.timeInSeconds = timeInSeconds * 60;
        return this;
    }

    public TimeConstructor hours(long timeInSeconds){
        this.timeInSeconds = timeInSeconds * 3600;
        return this;
    }

    public TimeConstructor days(long timeInSeconds){
        this.timeInSeconds = timeInSeconds * 86400;
        return this;
    }

}
