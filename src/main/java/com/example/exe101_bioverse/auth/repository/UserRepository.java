package com.example.exe101_bioverse.auth.repository;

import com.example.exe101_bioverse.auth.entity.User;
import com.example.exe101_bioverse.auth.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(attributePaths = "role")
    Optional<User> findByEmail(String email);

    @EntityGraph(attributePaths = "role")
    @Override
    Optional<User> findById(Long id);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);

    boolean existsByPhoneAndIdNot(String phone, Long id);

    long countByRole_CodeAndStatus(String roleCode, UserStatus status);

    @EntityGraph(attributePaths = "role")
    @Query("""
            SELECT u FROM User u
            WHERE (:status IS NULL OR u.status = :status)
              AND (:roleCode IS NULL OR u.role.code = :roleCode)
              AND (
                    :q IS NULL
                    OR LOWER(u.email) LIKE LOWER(CONCAT('%', :q, '%'))
                    OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :q, '%'))
                  )
            """)
    Page<User> search(
            @Param("status") UserStatus status,
            @Param("roleCode") String roleCode,
            @Param("q") String q,
            Pageable pageable
    );
}
