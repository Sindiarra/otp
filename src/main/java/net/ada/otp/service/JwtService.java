package net.ada.otp.service;

import net.ada.otp.entities.RefreshToken;
import net.ada.otp.entities.User;
import net.ada.otp.security.JwtUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletResponse;


@Service
public class JwtService {

    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    public JwtService(JwtUtil jwtUtil, RefreshTokenService refreshTokenService) {
        this.jwtUtil = jwtUtil;
        this.refreshTokenService = refreshTokenService;
    }

    public String generateAccessToken(User user) {
        return jwtUtil.generateToken(user.getEmail());
    }

    /**
     * Créé un refreshToken en base via RefreshTokenService et renvoie la valeur.
     * Met aussi le cookie HttpOnly dans la response si response != null.
     */
    public String generateRefreshToken(User user, HttpServletResponse response) {
        RefreshToken rt = refreshTokenService.createRefreshToken(user, response);
        return rt.getToken();
    }

    public boolean validateToken(String token) {
        return jwtUtil.isTokenValid(token);
    }

    /**
     * Ajoute header Authorization et cookie refreshToken sur la response.
     */
    public void attachTokensToResponse(User user, HttpServletResponse response) {
        String access = generateAccessToken(user);
        generateRefreshToken(user, response); // met déjà le cookie via refreshTokenService

        // Ajout de l'Authorization header pour commodité
        response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + access);
    }
}
