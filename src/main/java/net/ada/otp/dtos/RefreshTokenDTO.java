package net.ada.otp.dtos;

import net.ada.otp.entities.RefreshToken;
import java.time.LocalDateTime;

public class RefreshTokenDTO {
    private Long id;
    private String token;
    private LocalDateTime expirationDate;
    private Long userId;
    private String userEmail;

    // --- Constructeurs ---
    public RefreshTokenDTO() {}

    public RefreshTokenDTO(Long id, String token, LocalDateTime expirationDate, Long userId, String userEmail) {
        this.id = id;
        this.token = token;
        this.expirationDate = expirationDate;
        this.userId = userId;
        this.userEmail = userEmail;
    }

    // --- Getters ---
    public Long getId() { return id; }
    public String getToken() { return token; }
    public LocalDateTime getExpirationDate() { return expirationDate; }
    public Long getUserId() { return userId; }
    public String getUserEmail() { return userEmail; }

    // --- Setters ---
    public void setId(Long id) { this.id = id; }
    public void setToken(String token) { this.token = token; }
    public void setExpirationDate(LocalDateTime expirationDate) { this.expirationDate = expirationDate; }
    public void setUserId(Long userId) { this.userId = userId; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    // --- Conversion entité -> DTO ---
    public static RefreshTokenDTO fromEntity(RefreshToken rt) {
        if (rt == null) return null;
        return new RefreshTokenDTO(
                rt.getId(),
                rt.getToken(),
                rt.getExpirationDate(),
                rt.getUser() != null ? rt.getUser().getId() : null,
                rt.getUser() != null ? rt.getUser().getEmail() : null
        );
    }
}
