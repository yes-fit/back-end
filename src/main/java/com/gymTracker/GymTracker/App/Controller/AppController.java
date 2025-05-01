package com.gymTracker.GymTracker.App.Controller;

import com.gymTracker.GymTracker.App.Dto.Request.LoginRequest;
import com.gymTracker.GymTracker.App.Dto.Request.RegisterRequest;
import com.gymTracker.GymTracker.App.Dto.Request.SessionRequest;
import com.gymTracker.GymTracker.App.Dto.Request.ViewRequest;
import com.gymTracker.GymTracker.App.Dto.Response.LoginResponse;
import com.gymTracker.GymTracker.App.Dto.Response.RegistrationResponse;
import com.gymTracker.GymTracker.App.Dto.Response.SessionResponse;
import com.gymTracker.GymTracker.App.Dto.Response.ViewResponse;
import com.gymTracker.GymTracker.Domain.Service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class AppController {

    private final UserService userService;

    public AppController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")

    public RegistrationResponse register(@RequestBody RegisterRequest registerRequest){
        return userService.registerUser(registerRequest);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest){
        return userService.loginUser(loginRequest);
    }

    @GetMapping("/dummy")
    public String dummyEndPoint(){
        return "yay, welcome!";
    }

    @PostMapping("/bookSession")
    public SessionResponse bookSession(@RequestBody SessionRequest sessionRequest){
        return userService.bookSession(sessionRequest);
    }
    @GetMapping("/view")
    public ViewResponse viewSession(){
        return userService.viewSession();
    }
}
