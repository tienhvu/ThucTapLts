package com.example.demo.payload.request;

public class ResetPasswordRequest {
    private String email;
    private String verificationCode;
    private String newPassword;


    // Constructors
    public ResetPasswordRequest() {
    }

    public ResetPasswordRequest(String email, String verificationCode, String newPassword) {
        this.email = email;
        this.verificationCode = verificationCode;
        this.newPassword = newPassword;
    }

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
