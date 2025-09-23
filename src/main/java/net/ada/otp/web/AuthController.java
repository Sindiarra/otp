package net.ada.otp.web;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.ada.otp.dtos.*;
import net.ada.otp.entities.User;
import net.ada.otp.service.AuthService;
import net.ada.otp.service.UserService;
import net.ada.otp.service.OtpService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final OtpService otpService;
    private final AuthService authService;

    public AuthController(UserService userService, OtpService otpService, AuthService authService) {
        this.userService = userService;
        this.otpService = otpService;
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
        userService.registerUser(request);
        return ResponseEntity.ok().body(new ApiMessage("Utilisateur créé. Vérifiez votre email pour l'OTP."));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody OtpRequest request, HttpServletResponse response) {
        otpService.validateOtp(request);

        // après validation on peut aussi directement créer tokens (optionnel)
        User user = userService.findByEmail(request.getEmail());
        // Génère et attache tokens
        // jwtService.attachTokensToResponse(user, response); // si tu veux login automatique après verification

        return ResponseEntity.ok().body(new ApiMessage("OTP validé. Compte activé."));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        var result = authService.login(request, response);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request) {
        String refreshToken = null;
        if (request.getCookies() != null) {
            for (Cookie c : request.getCookies()) {
                if ("refreshToken".equals(c.getName())) {
                    refreshToken = c.getValue();
                }
            }
        }
        if (refreshToken == null) {
            return ResponseEntity.badRequest().body(new ApiMessage("Aucun refresh token trouvé"));
        }

        String newAccess = authService.refreshToken(refreshToken);
        return ResponseEntity.ok().body(Map.of("accessToken", newAccess));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        var principal = request.getUserPrincipal();
        if (principal == null) {
            return ResponseEntity.badRequest().body(new ApiMessage("Utilisateur non authentifié"));
        }
        // récupérer user par email
        String email = principal.getName();
        User user = userService.findByEmail(email);
        authService.logout(user);
        return ResponseEntity.ok(new ApiMessage("Déconnexion réussie"));
    }

    // petite DTO interne de message simple
    record ApiMessage(String message) {}
}
