package net.ada.otp.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import net.ada.otp.dtos.LoginRequest;
import net.ada.otp.entities.User;
import net.ada.otp.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final OtpService otpService;

    public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, OtpService otpService, RefreshTokenService refreshTokenService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.otpService = otpService;
        this.refreshTokenService = refreshTokenService;
    }

    /**
     * Tentative de login :
     * - Vérifie credentials
     * - Si user.enabled == false -> génère OTP et retourne message "OTP envoyé"
     * - Si enabled == true -> génère tokens et attache à response
     */
    public Map<String, String> login(LoginRequest request, HttpServletResponse response) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        } catch (AuthenticationException ex) {
            throw new RuntimeException("Identifiants invalides");
        }

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();

        if (!user.isEnabled()) {
            // Génère OTP pour activation / login
            otpService.generateOtp(user);
            return Map.of("message", "OTP envoyé à l'email. Vérifiez et appelez /auth/verify-otp pour valider.");
        }

        // utilisateur activé -> générer tokens
        jwtService.attachTokensToResponse(user, response);
        return Map.of("message", "Connexion réussie");
    }

    public String refreshToken(String refreshToken) {
        // déléguer à refreshTokenService (déjà implémenté dans ton code précédent)
        return jwtService.generateAccessToken(
                refreshTokenService.validateRefreshToken(refreshToken)
                        .map(rt -> rt.getUser())
                        .orElseThrow(() -> new RuntimeException("Refresh token invalide"))
        );
    }

    public void logout(User user) {
        // supprimer refresh tokens de l'utilisateur
        refreshTokenService.deleteUserTokens(user);
    }

    // injection du service existant
    private final RefreshTokenService refreshTokenService;
}
