package net.ada.otp.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "otp_refresh_tokens")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private LocalDateTime expirationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    // --- Constructeurs ---
    public RefreshToken() {}

    public RefreshToken(Long id, String token, LocalDateTime expirationDate, User user) {
        this.id = id;
        this.token = token;
        this.expirationDate = expirationDate;
        this.user = user;
    }

    // --- Getters ---
    public Long getId() { return id; }
    public String getToken() { return token; }
    public LocalDateTime getExpirationDate() { return expirationDate; }
    public User getUser() { return user; }

    // --- Setters ---
    public void setId(Long id) { this.id = id; }
    public void setToken(String token) { this.token = token; }
    public void setExpirationDate(LocalDateTime expirationDate) { this.expirationDate = expirationDate; }
    public void setUser(User user) { this.user = user; }
}
