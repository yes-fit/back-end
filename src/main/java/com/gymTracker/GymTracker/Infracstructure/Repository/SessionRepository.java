package com.gymTracker.GymTracker.Infracstructure.Repository;

import com.gymTracker.GymTracker.Domain.Entity.Session;

import com.gymTracker.GymTracker.Domain.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

    List<Session> findAllByStartTime(LocalDateTime startTime);

    Optional<Session> findByUserId(String userId);
}
