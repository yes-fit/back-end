package com.gymTracker.GymTracker.App.Controller;

import com.google.zxing.WriterException;
import com.gymTracker.GymTracker.App.Dto.Request.*;
import com.gymTracker.GymTracker.App.Dto.Response.*;
import com.gymTracker.GymTracker.Domain.Service.UserService;
import com.gymTracker.GymTracker.Infracstructure.Utils.QRCodeGenerator;
import jakarta.validation.Path;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
@EnableMethodSecurity
@Slf4j
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
    @GetMapping("/generate-qr")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<QrResponse> generateQrCode() {
        String hardcodedUrl = "http://localhost:8080/api/utilize/{sessionId}";

        try {
            String qrCodeBase64 = QRCodeGenerator.generateQRCodeBase64(hardcodedUrl, 300, 300);
            log.info("QR code generated successfully for {}", hardcodedUrl);

            QrResponse response = new QrResponse("00", qrCodeBase64);
            return ResponseEntity.ok(response);

        } catch (WriterException | IOException e) {
            log.error("Failed to generate QR code: {}", e.getMessage());

            QrResponse errorResponse = new QrResponse("99", "Failed to generate QR code.");
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
    @GetMapping("/utilize")
    //@PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<UtilizeResponse> utilizeSession() {
        UtilizeResponse response = userService.utilizeSession();
        return ResponseEntity.ok(response);
    }

    @PostMapping("workout")
    @PreAuthorize("hasAuthority('USER')")
    public WorkoutResponse createWorkout(@RequestBody @Valid WorkoutRequest workoutRequest){
        return userService.createWorkout(workoutRequest);
    }


}
