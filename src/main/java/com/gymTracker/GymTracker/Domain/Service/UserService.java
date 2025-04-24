package com.gymTracker.GymTracker.Domain.Service;


import com.gymTracker.GymTracker.App.Dto.Request.SessionRequest;
import com.gymTracker.GymTracker.App.Dto.Request.LoginRequest;
import com.gymTracker.GymTracker.App.Dto.Request.RegisterRequest;
import com.gymTracker.GymTracker.App.Dto.Response.SessionResponse;
import com.gymTracker.GymTracker.App.Dto.Response.LoginResponse;
import com.gymTracker.GymTracker.App.Dto.Response.RegistrationResponse;


public interface UserService {
    RegistrationResponse registerUser(RegisterRequest registerRequest);

    LoginResponse loginUser(LoginRequest loginRequest);

    SessionResponse bookSession(SessionRequest sessionRequest);

}
