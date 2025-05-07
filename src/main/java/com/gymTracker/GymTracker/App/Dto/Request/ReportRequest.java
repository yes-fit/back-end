package com.gymTracker.GymTracker.App.Dto.Request;

import java.time.LocalDateTime;

public class ReportRequest {
    private LocalDateTime time;

    private int hour;

    public ReportRequest(LocalDateTime time, int hour) {
        this.time = time;
        this.hour = hour;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }
}
