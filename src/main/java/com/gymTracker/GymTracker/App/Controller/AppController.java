package com.gymTracker.GymTracker.App.Controller;

import com.gymTracker.GymTracker.App.Dto.Request.*;
import com.gymTracker.GymTracker.App.Dto.Response.*;
import com.gymTracker.GymTracker.Domain.Service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
@EnableMethodSecurity
public class AppController {

    private final UserService userService;

    public AppController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public RegistrationResponse register(@RequestBody @Valid RegisterRequest registerRequest){
        return userService.registerUser(registerRequest);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody @Valid LoginRequest loginRequest){
        return userService.loginUser(loginRequest);
    }

    @GetMapping("/dummy")
    public String dummyEndPoint(){
        return "yay, welcome!";
    }

    @PostMapping("/bookSession")
    @PreAuthorize("hasAuthority('USER')")
    public SessionResponse bookSession(@RequestBody @Valid SessionRequest sessionRequest){
        System.out.println("attempting to book session");
        //System.out.println("User details ::: " + authenticationPrincipal.getAuthentication().getAuthorities().toString());
        return userService.bookSession(sessionRequest);
    }
    @GetMapping("/view")
    @PreAuthorize("hasAuthority('USER')")
    public ViewResponse viewSession(){
        return userService.viewSession();
    }

    @PutMapping("/edit")
    @PreAuthorize("hasAuthority('USER')")
    public EditResponse editSession(@RequestBody @Valid EditRequest editRequest){
        return userService.editSession(editRequest);
    }
    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('USER')")
    public DeleteResponse deleteSession(@RequestBody @Valid DeleteRequest deleteRequest){
        return userService.deleteSession(deleteRequest);
    }
    @GetMapping("/report")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ReportResponse reportUsage(@RequestBody @Valid ReportRequest reportRequest){
        return userService.findAllSessions(reportRequest);
    }
    @PostMapping("available")
    @PreAuthorize("hasAuthority('USER')")
    public AvailableResponse availableSession(@RequestBody @Valid AvailableRequest availableRequest){
        return userService.findAllSessionsByDate(availableRequest);
    }
}
