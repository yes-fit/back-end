package com.gymTracker.GymTracker.App.Dto.Request;

import java.time.LocalDateTime;

public class ViewRequest {
    private String email;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public ViewRequest(String email, LocalDateTime startTime, LocalDateTime endTime) {
        this.email = email;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
