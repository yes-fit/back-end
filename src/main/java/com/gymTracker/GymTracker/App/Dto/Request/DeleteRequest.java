package com.gymTracker.GymTracker.App.Dto.Request;

public class DeleteRequest {
    private  String sessionId;

    public DeleteRequest(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
