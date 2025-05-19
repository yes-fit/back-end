package com.gymTracker.GymTracker.Infracstructure.Task;

import com.gymTracker.GymTracker.Domain.Entity.Session;
import com.gymTracker.GymTracker.Infracstructure.Repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public class TaskScheduler {
    @Autowired
    private SessionRepository sessionRepository;

    public List<Session> SessionActive(){
        List<Session> sessions = sessionRepository.findAll();
        LocalDateTime now = LocalDateTime.now();
        for (Session session : sessions) {
            boolean isActive = now.isAfter(session.getStartTime()) && now.isBefore(session.getEndTime());
            session.setActive(isActive);
        }

        sessionRepository.saveAll(sessions);
        return sessions;
    }

}
