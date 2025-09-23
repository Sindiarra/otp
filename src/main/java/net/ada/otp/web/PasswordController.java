package net.ada.otp.web;

import net.ada.otp.dtos.ForgotPasswordRequest;
import net.ada.otp.dtos.ResetPasswordRequest;
import net.ada.otp.service.PasswordResetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/password")
public class PasswordController {

    private final PasswordResetService passwordResetService;

    public PasswordController(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
    }

    /**
     * Étape 1 : Demande d'OTP pour réinitialisation
     * -> L'OTP est envoyé par email
     */
    @PostMapping("/forgot")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        passwordResetService.forgotPassword(request);
        return ResponseEntity.ok(new ApiMessage("OTP envoyé à l'email"));
    }

    /**
     * Étape 2 : Réinitialisation du mot de passe
     * -> Valide OTP + change le mot de passe
     */
    @PostMapping("/reset")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        passwordResetService.resetPassword(request);
        return ResponseEntity.ok(new ApiMessage("Mot de passe réinitialisé avec succès"));
    }

    // petite DTO interne pour message simple
    record ApiMessage(String message) {}
}
