package com.gymTracker.GymTracker.App.Dto.Request;

import java.time.LocalDateTime;

public class SessionRequest {
    private LocalDateTime startTime;

    public SessionRequest(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
}
