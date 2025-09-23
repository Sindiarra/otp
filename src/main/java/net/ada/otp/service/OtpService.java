package net.ada.otp.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import net.ada.otp.dtos.OtpRequest;
import net.ada.otp.entities.OtpToken;
import net.ada.otp.entities.User;
import net.ada.otp.repository.OtpTokenRepository;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class OtpService {

    private final OtpTokenRepository otpTokenRepository;
    private final JavaMailSender mailSender;
    private final UserService userService;

    private static final int EXPIRATION_MINUTES = 5;
    private static final int MAX_ATTEMPTS = 3;

    public OtpService(OtpTokenRepository otpTokenRepository, JavaMailSender mailSender, UserService userService) {
        this.otpTokenRepository = otpTokenRepository;
        this.mailSender = mailSender;
        this.userService = userService;
    }

    /**
     * Génère un OTP 6 chiffres, sauvegarde et envoie par email.
     */
    @Transactional
    public OtpToken generateOtp(User user) {
        String code = String.format("%06d", new Random().nextInt(1_000_000));

        OtpToken otp = new OtpToken(); // constructeur vide
        otp.setCode(code);
        otp.setExpirationDate(LocalDateTime.now().plusMinutes(EXPIRATION_MINUTES));
        otp.setUsed(false);
        otp.setAttempts(MAX_ATTEMPTS);
        otp.setUser(user);

        otp = otpTokenRepository.save(otp);
        sendOtpEmail(user, code);
        return otp;
    }

    /**
     * Envoi email simple (HTML possible)
     */
    public void sendOtpEmail(User user, String code) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
            helper.setTo(user.getEmail());
            helper.setSubject("Votre code OTP");
            String body = "Bonjour " + (user.getFirstName() != null ? user.getFirstName() : "") +
                    ",\n\nVotre code OTP est : " + code +
                    "\nIl expire dans " + EXPIRATION_MINUTES + " minutes.\n\nSi vous n'avez pas demandé ce code, ignorez cet email.";
            helper.setText(body);
            mailSender.send(message);
        } catch (MessagingException ex) {
            throw new RuntimeException("Impossible d'envoyer l'email OTP", ex);
        }
    }

    /**
     * Valide un OTPRequest (email + otp).
     * - Vérifie existence du dernier OTP non-used pour l'utilisateur
     * - Vérifie expiration
     * - Décrémente attempts si erreur
     * - Si succès : mark used, active user (enable)
     */
    @Transactional
    public void validateOtp(OtpRequest request) {
        User user = userService.findByEmail(request.getEmail());

        OtpToken otp = otpTokenRepository.findTopByUserOrderByExpirationDateDesc(user)
                .orElseThrow(() -> new RuntimeException("Aucun OTP généré pour cet utilisateur"));

        if (otp.isUsed()) {
            throw new RuntimeException("OTP déjà utilisé");
        }

        if (otp.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP expiré");
        }

        if (!otp.getCode().equals(request.getOtp())) {
            // erreur : décrémente attempts, persiste et throw
            int remaining = otp.getAttempts() - 1;
            otp.setAttempts(Math.max(0, remaining));
            otpTokenRepository.save(otp);

            if (remaining <= 0) {
                throw new RuntimeException("Nombre maximum d'essais atteint pour cet OTP");
            } else {
                throw new RuntimeException("OTP incorrect. Il vous reste " + remaining + " essai(s).");
            }
        }

        // succès
        otp.setUsed(true);
        otpTokenRepository.save(otp);

        // activer l'utilisateur (cas inscription / vérif login)
        userService.enableUser(user);
    }
}
