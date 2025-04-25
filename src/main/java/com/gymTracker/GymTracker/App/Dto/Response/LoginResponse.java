package com.gymTracker.GymTracker.App.Dto.Response;

public class LoginResponse {
    private String responseCode;
    private String responseMessage;

    private String token;

    public LoginResponse(String responseCode, String responseMessage) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
    }

    public LoginResponse(String responseCode, String responseMessage, String token) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
        this.token = token;
    }

    public LoginResponse() {

    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
