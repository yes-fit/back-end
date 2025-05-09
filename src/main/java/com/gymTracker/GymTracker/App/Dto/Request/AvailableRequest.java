package com.gymTracker.GymTracker.App.Dto.Request;

import java.time.LocalDate;

public class AvailableRequest {
    private LocalDate date;

    public AvailableRequest(LocalDate date) {
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
