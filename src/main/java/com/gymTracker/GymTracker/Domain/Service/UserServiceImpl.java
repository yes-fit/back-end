package com.gymTracker.GymTracker.Domain.Service;

import com.gymTracker.GymTracker.App.Dto.Request.*;
import com.gymTracker.GymTracker.App.Dto.Response.*;
import com.gymTracker.GymTracker.Domain.Constants.Roles;
import com.gymTracker.GymTracker.Domain.Entity.Session;
import com.gymTracker.GymTracker.Domain.Entity.User;
import com.gymTracker.GymTracker.Infracstructure.Config.Jwt.JwtUtils;
import com.gymTracker.GymTracker.Infracstructure.Repository.SessionRepository;
import com.gymTracker.GymTracker.Infracstructure.Repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.gymTracker.GymTracker.Infracstructure.Config.SecurityUtils.getCurrentUserEmail;

@Service

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final SessionRepository sessionRepository;
    private final JwtUtils jwtUtils;

    public UserServiceImpl(UserRepository userRepository, SessionRepository sessionRepository, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public RegistrationResponse registerUser(RegisterRequest registerRequest)
    {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return new RegistrationResponse("01","User Already exists");
        }
        User user = new User();
        user.setUserName(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(registerRequest.getPassword());
        user.setFullName(registerRequest.getFullName());
        user.setRole(Roles.USER);
        user.setDob(registerRequest.getDob());
        user.setGender(registerRequest.getGender());

        userRepository.save(user);
        return new RegistrationResponse("00","Registration successful");
    }



    @Override
    public LoginResponse loginUser(LoginRequest loginRequest) {
        Optional<User> user = userRepository.findByEmail(loginRequest.getEmail());
        if (user.isEmpty()) {
            return new LoginResponse("01", "User does not exist");
        }
        if (!user.get().getPassword().equalsIgnoreCase(loginRequest.getPassword())) {
            return new LoginResponse("02", "Bad credentials/ Invalid password");
        }
        String token = jwtUtils.generateTokenFromEmail(user.get().getEmail());
        //System.out.println(token +  " -----this is the generated token");
        return new LoginResponse("00", "Login Successful", token);
    }

    @Override
    public SessionResponse bookSession(SessionRequest sessionRequest) {
        Session session = new Session();
        LocalDateTime startTime = sessionRequest.getStartTime();
        LocalDateTime endTime = sessionRequest.getStartTime().plusHours(1);
        LocalDateTime now = LocalDateTime.now();

        if (startTime.isBefore(now)) {
            return new SessionResponse("01", "Cannot book a session in the past.");
        }
        if (startTime.isBefore(now.plusHours(24))) {
            return new SessionResponse("02", "Session must be booked at least 24 hours earlier");
        }
        if (sessionRepository.findAllByStartTime(sessionRequest.getStartTime()).size() > 49){
            return new SessionResponse("03" , "maximum capacity filled");
        }
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            return new SessionResponse("04", "User not found");
        }

        User user = optionalUser.get();
        session.setUserId(String.valueOf(user.getId()));
        session.setStartTime(startTime);
        session.setEndTime(endTime);
        sessionRepository.save(session);
        return new SessionResponse("00" , "Booking Successful");
    }

    @Override
    public ViewResponse viewSession() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            return new ViewResponse("01", "User not found");
        }

        User user = optionalUser.get();
        Optional<List<Session>> sessionList  = sessionRepository.findByUserId(String.valueOf(user.getId()));

        if (sessionList.isEmpty()) {
            return new ViewResponse("01", "No sessions found for this user");
        }

        return new ViewResponse("00", "Successful", sessionList.get());
    }


    @Override
    public EditResponse editSession(EditRequest editRequest) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            return new EditResponse("01", "User not found");
        }

        User user = optionalUser.get();

        Optional<Session> optionalSession = sessionRepository.findById(UUID.fromString(editRequest.getSessionId()));

        if (optionalSession.isEmpty()) {
            return new EditResponse("02", "No session found for user");
        }

        Session session = optionalSession.get();

        LocalDateTime newTime = editRequest.getNewTime();
        LocalDateTime endTime = editRequest.getNewTime().plusHours(1);
        LocalDateTime now = LocalDateTime.now();

        if (newTime.isBefore(now)) {
            return new EditResponse("01", "Cannot book a session in the past.");
        }
        if (newTime.isBefore(now.plusHours(24))) {
            return new EditResponse("02", "Session must be booked at least 24 hours earlier");
        }
        if (sessionRepository.findAllByStartTime(editRequest.getNewTime()).size() > 49){
            return new EditResponse("03" , "maximum capacity filled");
        }
        session.setStartTime(newTime);
        session.setEndTime(endTime);

        sessionRepository.save(session);

        return new EditResponse("00" , "Session Updated successfully");
    }
}
