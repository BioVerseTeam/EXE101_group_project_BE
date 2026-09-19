package com.example.exe101_bioverse.auth.repository;

import com.example.exe101_bioverse.auth.entity.User;
import com.example.exe101_bioverse.auth.enums.UserStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    @EntityGraph(attributePaths = "role")
    Optional<User> findByEmail(String email);

    @EntityGraph(attributePaths = "role")
    @Override
    Optional<User> findById(Long id);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);

    boolean existsByPhoneAndIdNot(String phone, Long id);

    long countByRole_CodeAndStatus(String roleCode, UserStatus status);

    long countByRole_Code(String roleCode);

    long countByStatus(UserStatus status);

    long countByCreatedAtGreaterThanEqual(LocalDateTime from);

    @Query(value = """
            SELECT to_char(date_trunc('month', created_at), 'YYYY-MM') AS ym,
                   COUNT(*) AS total
            FROM users
            GROUP BY 1
            ORDER BY 1
            """, nativeQuery = true)
    List<Object[]> countCreatedByMonth();

    boolean existsByRole_Id(Long roleId);

    @Query("SELECT u.id FROM User u WHERE u.role.id = :roleId")
    List<Long> findIdsByRoleId(@Param("roleId") Long roleId);
}
