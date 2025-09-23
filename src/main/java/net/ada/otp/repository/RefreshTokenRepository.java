package net.ada.otp.repository;

import net.ada.otp.entities.RefreshToken;
import net.ada.otp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    List<RefreshToken> findByUser(User user);
    void deleteByUser(User user);
    List<RefreshToken> findByExpirationDateBefore(LocalDateTime now); // pour nettoyage
    boolean existsByToken(String token);
}
