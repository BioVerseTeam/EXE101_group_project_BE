package com.example.exe101_bioverse.auth.repository;

import com.example.exe101_bioverse.auth.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByCode(String code);

    boolean existsByCode(String code);

    boolean existsByCodeAndIdNot(String code, Long id);

    @Query("""
            SELECT r FROM Role r
            WHERE (
                    :q IS NULL
                    OR LOWER(r.code) LIKE LOWER(CONCAT('%', :q, '%'))
                    OR LOWER(r.name) LIKE LOWER(CONCAT('%', :q, '%'))
                  )
            """)
    Page<Role> search(@Param("q") String q, Pageable pageable);
}
