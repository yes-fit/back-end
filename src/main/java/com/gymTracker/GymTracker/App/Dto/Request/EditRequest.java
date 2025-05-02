package com.gymTracker.GymTracker.App.Dto.Request;

import java.time.LocalDateTime;

public class EditRequest {
    private LocalDateTime newTime;

    public EditRequest(LocalDateTime newTime) {
        this.newTime = newTime;
    }

    public LocalDateTime getNewTime() {
        return newTime;
    }

    public void setNewTime(LocalDateTime newTime) {
        this.newTime = newTime;
    }
}
