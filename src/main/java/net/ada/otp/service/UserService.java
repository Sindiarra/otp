package net.ada.otp.service;


import net.ada.otp.dtos.SignupRequest;
import net.ada.otp.dtos.UserDTO;
import net.ada.otp.entities.User;
import net.ada.otp.enums.Role;
import net.ada.otp.repository.UserRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final OtpService otpService;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       @Lazy OtpService otpService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.otpService = otpService;
    }

    @Transactional
    public User registerUser(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email déjà utilisé");
        }

        User user = new User(); // constructeur vide
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEnabled(false);
        user.setRole(Role.USER);

        user = userRepository.save(user);

        // génère et envoie OTP d'activation
        otpService.generateOtp(user);

        return user;
    }


    @Transactional
    public void enableUser(User user) {
        if (!user.isEnabled()) {
            user.setEnabled(true);
            userRepository.save(user);
        }
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
    }

    // --- CRUD supplémentaires ---

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
    }

    @Transactional
    public User updateUser(Long id, UserDTO dto) {
        User user = findById(id);
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setEmail(dto.getEmail());
        user.setEnabled(dto.isEnabled());
        //  gestion du rôle (facultatif si dto.role est non-null)
        if (dto.getRole() != null) {
            user.setRole(Role.valueOf(dto.getRole()));
        }
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Utilisateur introuvable");
        }
        userRepository.deleteById(id);
    }
}
