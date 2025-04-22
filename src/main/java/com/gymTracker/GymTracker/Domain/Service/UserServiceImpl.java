package com.gymTracker.GymTracker.Domain.Service;

import com.gymTracker.GymTracker.App.Dto.Request.LoginRequest;
import com.gymTracker.GymTracker.App.Dto.Request.RegisterRequest;
import com.gymTracker.GymTracker.App.Dto.Response.LoginResponse;
import com.gymTracker.GymTracker.App.Dto.Response.RegistrationResponse;
import com.gymTracker.GymTracker.Domain.Constants.Roles;
import com.gymTracker.GymTracker.Domain.Entity.User;
import com.gymTracker.GymTracker.Infracstructure.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
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
        return new LoginResponse("00", "Login Successful");
    }
}
