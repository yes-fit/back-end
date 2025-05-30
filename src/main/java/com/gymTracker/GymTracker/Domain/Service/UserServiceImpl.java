package com.gymTracker.GymTracker.Domain.Service;

import com.gymTracker.GymTracker.App.Dto.Request.*;
import com.gymTracker.GymTracker.App.Dto.Response.*;
import com.gymTracker.GymTracker.Domain.Constants.MailType;
import com.gymTracker.GymTracker.Domain.Constants.Roles;
import com.gymTracker.GymTracker.Domain.Entity.Session;
import com.gymTracker.GymTracker.Domain.Entity.User;
import com.gymTracker.GymTracker.Infracstructure.Config.Jwt.JwtUtils;
import com.gymTracker.GymTracker.Infracstructure.Repository.ReportRepository;
import com.gymTracker.GymTracker.Infracstructure.Repository.SessionRepository;
import com.gymTracker.GymTracker.Infracstructure.Repository.UserRepository;
import com.gymTracker.GymTracker.Infracstructure.Utils.SendMails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final SessionRepository sessionRepository;

    private final ReportRepository reportRepository;
    private final JwtUtils jwtUtils;

    private final SendMails sendMails;

    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, SessionRepository sessionRepository, ReportRepository reportRepository,
                           JwtUtils jwtUtils, SendMails sendMails, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.reportRepository = reportRepository;
        this.jwtUtils = jwtUtils;
        this.sendMails = sendMails;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public RegistrationResponse registerUser(RegisterRequest registerRequest)
    {
        log.info("Registration started for user {}", registerRequest.getEmail());
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return new RegistrationResponse("01","User Already exists");
        }
        User user = new User();
        user.setUserName(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setFullName(registerRequest.getFullName());
        user.setRole(Roles.USER);
        user.setDob(registerRequest.getDob());
        user.setGender(registerRequest.getGender());

        userRepository.save(user);
        sendMails.sendEmail(MailType.SESSION_REGISTRATION, registerRequest.getEmail());
        return new RegistrationResponse("00","Registration successful");
    }



    @Override
    public LoginResponse loginUser(LoginRequest loginRequest) {
        Optional<User> user = userRepository.findByEmail(loginRequest.getEmail());
        if (user.isEmpty()) {
            return new LoginResponse("01", "User does not exist");
        }
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.get().getPassword())) {
            return new LoginResponse("02", "Bad credentials/ Invalid password");
        }
        String token = jwtUtils.generateTokenFromEmail(user.get().getEmail());
        //System.out.println(token +  " -----this is the generated token");
        return new LoginResponse("00", "Login Successful", token);
    }

    @Override
    public SessionResponse bookSession(SessionRequest sessionRequest) {
        log.info("Attempting to book session at {}", sessionRequest.getStartTime());
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
        log.info("User {}  is attempting to book session at {}", email, session.getStartTime());

        if (optionalUser.isEmpty()) {
            return new SessionResponse("04", "User not found");
        }
        User user = optionalUser.get();
        Optional<Session> lastBookedSession = sessionRepository.findTopByUserIdOrderByStartTimeDesc(user.getId().toString());


        if (lastBookedSession.isPresent()) {
            Session lastSession = lastBookedSession.get();
            log.info("Last booked session for {} was at {}", email, lastSession.getStartTime());
            int lastHour  = lastSession.getStartTime().getHour();
            int requestedHour = sessionRequest.getStartTime().getHour();
            if (lastHour - 1 == requestedHour && sessionRequest.getStartTime().toLocalDate().equals(lastSession.getStartTime().toLocalDate())) {
                return new SessionResponse("07", "Cannot book a session before your last booked session on the same day.");
            }
            if (requestedHour - 1 == lastHour  && sessionRequest.getStartTime().toLocalDate().equals(lastSession.getStartTime().toLocalDate())) {
                return new SessionResponse("05" , "Concurrent Sessions cannot be booked.");
            }
        }

      //  log.info("No immediate previous session found for {}", email);


        int sessionsBookedToday = sessionRepository.countByUserIdAndStartTimeBetween(
                user.getId().toString(),
                startTime.toLocalDate().atStartOfDay(),
                startTime.toLocalDate().atTime(23, 59, 59)
        );

        if (sessionsBookedToday >= 2) {
            return new SessionResponse("06", "Maximum of 2 sessions per day exceeded.");
        }

        session.setUserId(String.valueOf(user.getId()));
        session.setStartTime(startTime);
        session.setEndTime(endTime);
        sessionRepository.save(session);
        log.info("Session assumed booked with details {}", session.toString());
        sendMails.sendEmail(MailType.SESSION_BOOKING, user.getEmail());

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
        sendMails.sendEmail(MailType.SESSION_EDITED, user.getEmail());
        return new EditResponse("00" , "Session Updated successfully");
    }

    @Override
    public DeleteResponse deleteSession(DeleteRequest deleteRequest) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("");
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            return new DeleteResponse("01", "User not found");
        }

        User user = optionalUser.get();
        Optional<Session> optionalSession = sessionRepository.findById(UUID.fromString(deleteRequest.getSessionId()));
        if (optionalSession.isEmpty()) {
            return new DeleteResponse("02", "No session found for user");
        }

        Session session= optionalSession.get();
        sessionRepository.delete(session);

        sendMails.sendEmail(MailType.SESSION_DELETION, user.getEmail());
        return new DeleteResponse("00" , "Session Deleted Successfully");
    }

    @Override
    public ReportResponse findAllSessions(ReportRequest reportRequest) {
        LocalDateTime requestTime = reportRequest.getTime() != null
                ? reportRequest.getTime()
                : LocalDateTime.now();


        int hour = requestTime.getHour();


        LocalDateTime fromTime = LocalDate.now().atTime(hour, 0);

        List<Session> sessions = sessionRepository.findAllByStartTimeGreaterThanEqual(fromTime);

        if (sessions.isEmpty()) {
            return new ReportResponse("01", "No sessions found");
        }

        return new ReportResponse("00", "Sessions Generated Successfully", sessions);
    }

    @Override
    public AvailableResponse findAllSessionsByDate(AvailableRequest availableRequest) {
        LocalDate date = availableRequest.getDate();

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);

        List<Session> sessions = sessionRepository.findAllByStartTimeBetween(startOfDay, endOfDay);


        List<HourAvailability> availabilityList = new ArrayList<>();

        for (int hour = 0; hour < 24; hour++) {
            final int currentHour = hour;

            long sessionCount = sessions.stream()
                    .filter(session -> session.getStartTime().getHour() == currentHour)
                    .count();

            boolean available = sessionCount < 50;

            availabilityList.add(new HourAvailability(currentHour, available));
        }

        return new AvailableResponse("00", "Sessions Generated Successfully", availabilityList);
    }


    public UtilizeResponse utilizeSession() {
        String email  = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Utilizing session for user: {}", email);
        return userRepository.findByEmail(email)
                .map(user -> {
                    String userId = String.valueOf(user.getId());
                    LocalDateTime now = LocalDateTime.now();
                    LocalDateTime startOfHour = now.withMinute(0).withSecond(0).withNano(0);
                    LocalDateTime endOfHour = now.withMinute(59).withSecond(59).withNano(999999999);


                    Optional<Session> sessionOpt = sessionRepository
                            .findTopByUserIdAndStartTimeBetweenAndActiveIsTrue(userId, startOfHour, endOfHour);

                    if (sessionOpt.isPresent()) {
                        Session session = sessionOpt.get();
                        if (!session.isUtilize()) {
                            session.setUtilize(true);
                            sessionRepository.save(session);
                            log.info("Session utilized for user: {}", email);
                            return new UtilizeResponse("00", "Session utilized successfully.");
                        } else {
                            log.warn("Session already utilized for user: {}", email);
                            return new UtilizeResponse("01", "Session already utilized.");
                        }
                    } else {
                        log.warn("No active session found for user: {}", email);
                        return new UtilizeResponse("02", "No active session found at this time.");
                    }

                })
                .orElseGet(() -> {
                    log.warn("User not found for email: {}", email);
                    return new UtilizeResponse("03", "User not found.");
                });
    }


}

