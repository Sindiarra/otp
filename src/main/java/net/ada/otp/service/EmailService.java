package net.ada.otp.service;

import lombok.RequiredArgsConstructor;
import net.ada.otp.entities.User;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOtpEmail(User user, String otpCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Votre code OTP");
        message.setText("Bonjour " + user.getFirstName() + ",\n\n"
                + "Votre code OTP est : " + otpCode + "\n"
                + "Ce code expire dans 5 minutes.\n\n"
                + "Cordialement,\nL'équipe sécurité.");
        mailSender.send(message);
    }
}
