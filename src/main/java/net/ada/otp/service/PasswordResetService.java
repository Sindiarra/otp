package net.ada.otp.service;

import lombok.RequiredArgsConstructor;
import net.ada.otp.dtos.ForgotPasswordRequest;
import net.ada.otp.dtos.ResetPasswordRequest;
import net.ada.otp.entities.User;
import net.ada.otp.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PasswordResetService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final OtpService otpService;
    private final PasswordEncoder passwordEncoder;

    public PasswordResetService(UserRepository userRepository, UserService userService, OtpService otpService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.otpService = otpService;
        this.passwordEncoder = passwordEncoder;
    }

    public void forgotPassword(ForgotPasswordRequest request) {
        User user = userService.findByEmail(request.getEmail());
        // génère OTP (même mécanisme que pour inscription)
        otpService.generateOtp(user);
    }

    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        // Valide OTP (cela active user si pas déjà), mais ici on veut juste valider l'OTP
        // On utilisera la même validateOtp : elle active le compte et marque OTP utilisé
        otpService.validateOtp(new net.ada.otp.dtos.OtpRequest(request.getEmail(), request.getOtp()));

        User user = userService.findByEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}
