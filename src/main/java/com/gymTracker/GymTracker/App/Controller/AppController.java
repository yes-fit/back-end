package com.gymTracker.GymTracker.App.Controller;

import com.gymTracker.GymTracker.App.Dto.Request.*;
import com.gymTracker.GymTracker.App.Dto.Response.*;
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

    @PutMapping("/edit")
    public EditResponse editSession(@RequestBody EditRequest editRequest){
        return userService.editSession(editRequest);
    }
    @DeleteMapping("/delete")
    public DeleteResponse deleteSession(@RequestBody DeleteRequest deleteRequest){
        return userService.deleteSession(deleteRequest);
    }
    @GetMapping("/report")
    public ReportResponse reportUsage(@RequestBody ReportRequest reportRequest){
        return userService.findAllSessions(reportRequest);
    }
}
