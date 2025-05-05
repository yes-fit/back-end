package com.gymTracker.GymTracker.App.Dto.Request;

import java.time.LocalDateTime;

public class EditRequest {
    private String sessionId;
    private LocalDateTime newTime;

    public EditRequest(String sessionId, LocalDateTime newTime) {
        this.sessionId = sessionId;
        this.newTime = newTime;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public LocalDateTime getNewTime() {
        return newTime;
    }

    public void setNewTime(LocalDateTime newTime) {
        this.newTime = newTime;
    }
}
