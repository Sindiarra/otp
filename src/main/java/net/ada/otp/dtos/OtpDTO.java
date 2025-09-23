package net.ada.otp.dtos;

import net.ada.otp.entities.OtpToken;
import java.time.LocalDateTime;

public class OtpDTO {
    private Long id;
    private String code;
    private LocalDateTime expirationDate;
    private boolean used;
    private Long userId;
    private String userEmail;

    public OtpDTO() {}

    public OtpDTO(Long id, String code, LocalDateTime expirationDate, boolean used, Long userId, String userEmail) {
        this.id = id;
        this.code = code;
        this.expirationDate = expirationDate;
        this.used = used;
        this.userId = userId;
        this.userEmail = userEmail;
    }

    public Long getId() { return id; }
    public String getCode() { return code; }
    public LocalDateTime getExpirationDate() { return expirationDate; }
    public boolean isUsed() { return used; }
    public Long getUserId() { return userId; }
    public String getUserEmail() { return userEmail; }

    public void setId(Long id) { this.id = id; }
    public void setCode(String code) { this.code = code; }
    public void setExpirationDate(LocalDateTime expirationDate) { this.expirationDate = expirationDate; }
    public void setUsed(boolean used) { this.used = used; }
    public void setUserId(Long userId) { this.userId = userId; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public static OtpDTO fromEntity(OtpToken otp) {
        if (otp == null) return null;
        return new OtpDTO(
                otp.getId(),
                otp.getCode(),
                otp.getExpirationDate(),
                otp.isUsed(),
                otp.getUser() != null ? otp.getUser().getId() : null,
                otp.getUser() != null ? otp.getUser().getEmail() : null
        );
    }
}
