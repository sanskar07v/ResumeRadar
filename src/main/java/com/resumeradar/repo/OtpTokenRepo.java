package com.resumeradar.repo;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.resumeradar.entity.OtpToken;
import com.resumeradar.entity.User;

@Repository
public interface OtpTokenRepo extends JpaRepository<OtpToken, String>{

	Optional<OtpToken> findByUser(User user);

	Optional<OtpToken> findByUserAndExpiryAfterAndIsUsedFalse(User user, LocalDateTime now);

}
