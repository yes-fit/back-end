package com.gymTracker.GymTracker.App.Dto.Response;

public class LoginResponse {
    private String LoginCode;
    private String LoginMessage;

    public LoginResponse(String loginCode, String loginMessage) {
        LoginCode = loginCode;
        LoginMessage = loginMessage;
    }

    public String getLoginCode() {
        return LoginCode;
    }

    public void setLoginCode(String loginCode) {
        LoginCode = loginCode;
    }

    public String getLoginMessage() {
        return LoginMessage;
    }

    public void setLoginMessage(String loginMessage) {
        LoginMessage = loginMessage;
    }
}
