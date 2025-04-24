package com.gymTracker.GymTracker.App.Dto.Request;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class BookSessionRequest {
    private LocalDateTime time;
    private LocalDate date ;

    private boolean utilize;

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public boolean isUtilize() {
        return utilize;
    }

    public void setUtilize(boolean utilize) {
        this.utilize = utilize;
    }

}
