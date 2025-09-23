package net.ada.otp.repository;

import net.ada.otp.entities.OtpToken;
import net.ada.otp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;

@Repository
public interface OtpTokenRepository extends JpaRepository<OtpToken, Long> {
    Optional<OtpToken> findByCodeAndUsedFalse(String code);
    List<OtpToken> findByUserAndUsedFalseOrderByExpirationDateDesc(User user);
    List<OtpToken> findByExpirationDateBefore(LocalDateTime now); // pour nettoyage
    Optional<OtpToken> findTopByUserOrderByExpirationDateDesc(User user); // dernière OTP
}
