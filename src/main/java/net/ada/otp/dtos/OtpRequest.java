package net.ada.otp.dtos;

public class OtpRequest {
    private String email;
    private String otp;

    // Constructeurs
    public OtpRequest() {}

    public OtpRequest(String email, String otp) {
        this.email = email;
        this.otp = otp;
    }

    // Getters
    public String getEmail() {
        return email;
    }

    public String getOtp() {
        return otp;
    }

    // Setters
    public void setEmail(String email) {
        this.email = email;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
