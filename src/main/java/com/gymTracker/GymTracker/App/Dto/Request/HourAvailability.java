package com.gymTracker.GymTracker.App.Dto.Request;

public class HourAvailability {
    private int hour;
    private boolean available;

    public HourAvailability(int hour, boolean available) {
        this.hour = hour;
        this.available = available;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
