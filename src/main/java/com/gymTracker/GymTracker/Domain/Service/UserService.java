package com.gymTracker.GymTracker.Domain.Service;


import com.gymTracker.GymTracker.App.Dto.Request.*;
import com.gymTracker.GymTracker.App.Dto.Response.*;

import java.util.UUID;


public interface UserService {
    RegistrationResponse registerUser(RegisterRequest registerRequest);

    LoginResponse loginUser(LoginRequest loginRequest);

    SessionResponse bookSession(SessionRequest sessionRequest);

    ViewResponse viewSession ();
    EditResponse editSession(EditRequest editRequest);

    DeleteResponse deleteSession(DeleteRequest deleteRequest);

    ReportResponse findAllSessions (ReportRequest reportRequest);

    AvailableResponse findAllSessionsByDate(AvailableRequest availableRequest);

    UtilizeResponse utilizeSession();

    //UtilizeResponse utilizeSession(UUID sessionId);
}
