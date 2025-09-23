package net.ada.otp.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import net.ada.otp.entities.RefreshToken;
import net.ada.otp.entities.User;
import net.ada.otp.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public RefreshToken createRefreshToken(User user, HttpServletResponse response) {
        RefreshToken refreshToken = new RefreshToken(); // constructeur vide
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpirationDate(LocalDateTime.now().plusDays(7)); // 7 jours
        refreshToken.setUser(user);

        refreshTokenRepository.save(refreshToken);

        Cookie cookie = new Cookie("refreshToken", refreshToken.getToken());
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // ⚠ nécessite HTTPS
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60);
        cookie.setAttribute("SameSite", "Strict");
        response.addCookie(cookie);

        return refreshToken;
    }


    public Optional<RefreshToken> validateRefreshToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .filter(rt -> rt.getExpirationDate().isAfter(LocalDateTime.now()));
    }

    public void deleteUserTokens(User user) {
        refreshTokenRepository.deleteByUser(user);
    }
}
