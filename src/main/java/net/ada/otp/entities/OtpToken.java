package net.ada.otp.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "otp_tokens")
public class OtpToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private LocalDateTime expirationDate;
    private boolean used;
    private int attempts;

    @ManyToOne
    private User user;

    public OtpToken() {}

    public OtpToken(Long id, String code, LocalDateTime expirationDate, boolean used, int attempts, User user) {
        this.id = id;
        this.code = code;
        this.expirationDate = expirationDate;
        this.used = used;
        this.attempts = attempts;
        this.user = user;
    }

    public Long getId() { return id; }
    public String getCode() { return code; }
    public LocalDateTime getExpirationDate() { return expirationDate; }
    public boolean isUsed() { return used; }
    public int getAttempts() { return attempts; }
    public User getUser() { return user; }

    public void setId(Long id) { this.id = id; }
    public void setCode(String code) { this.code = code; }
    public void setExpirationDate(LocalDateTime expirationDate) { this.expirationDate = expirationDate; }
    public void setUsed(boolean used) { this.used = used; }
    public void setAttempts(int attempts) { this.attempts = attempts; }
    public void setUser(User user) { this.user = user; }
}
