package com.example.exe101_bioverse.auth.repository;

import com.example.exe101_bioverse.auth.entity.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserSessionRepository extends JpaRepository<UserSession, Long> {

    @Query("SELECT s FROM UserSession s JOIN FETCH s.user u JOIN FETCH u.role WHERE s.refreshToken = :refreshToken")
    Optional<UserSession> findByRefreshToken(@Param("refreshToken") String refreshToken);

    void deleteByRefreshToken(String refreshToken);

    @Modifying
    @Query("DELETE FROM UserSession s WHERE s.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);
}
