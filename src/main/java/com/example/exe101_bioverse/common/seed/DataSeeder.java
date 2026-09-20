package com.example.exe101_bioverse.common.seed;

import com.example.exe101_bioverse.auth.entity.Role;
import com.example.exe101_bioverse.auth.entity.User;
import com.example.exe101_bioverse.auth.enums.GenderType;
import com.example.exe101_bioverse.auth.enums.UserStatus;
import com.example.exe101_bioverse.auth.repository.RoleRepository;
import com.example.exe101_bioverse.auth.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Component
@ConditionalOnProperty(name = "bioverse.seed.enabled", havingValue = "true", matchIfMissing = true)
public class DataSeeder implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);
    private static final ZoneId VN_ZONE = ZoneId.of("Asia/Ho_Chi_Minh");

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        Role adminRole = requireRole("ADMIN");
        Role studentRole = requireRole("STUDENT");
        LocalDateTime now = LocalDateTime.now(VN_ZONE);

        List<SeedUser> seeds = List.of(
                new SeedUser(
                        "admin@bioverse.com",
                        "Admin@BioVerse!2026",
                        "Bioverse Admin",
                        "0900000001",
                        null,
                        LocalDate.of(1998, 1, 15),
                        GenderType.MALE,
                        adminRole
                ),
                new SeedUser(
                        "student@bioverse.com",
                        "Student@123456",
                        "Nguyen Van A",
                        "0900000002",
                        8,
                        LocalDate.of(2010, 5, 20),
                        GenderType.MALE,
                        studentRole
                ),
                new SeedUser(
                        "student2@bioverse.com",
                        "Student@123456",
                        "Tran Thi B",
                        "0900000003",
                        7,
                        LocalDate.of(2011, 8, 8),
                        GenderType.FEMALE,
                        studentRole
                )
        );

        for (SeedUser seed : seeds) {
            if (userRepository.existsByEmail(seed.email())) {
                log.info("Skip seed user, already exists: {}", seed.email());
                continue;
            }
            User user = User.builder()
                    .email(seed.email())
                    .passwordHash(passwordEncoder.encode(seed.password()))
                    .fullName(seed.fullName())
                    .phone(seed.phone())
                    .grade(seed.grade())
                    .dateOfBirth(seed.dateOfBirth())
                    .gender(seed.gender())
                    .role(seed.role())
                    .status(UserStatus.ACTIVE)
                    .emailVerified(true)
                    .createdAt(now)
                    .updatedAt(now)
                    .build();
            userRepository.save(user);
            log.info("Seeded user {} / {} ({})", seed.email(), seed.password(), seed.role().getCode());
        }
    }

    private Role requireRole(String code) {
        return roleRepository.findByCode(code)
                .orElseThrow(() -> new IllegalStateException("Missing role: " + code + ". Run Flyway migrations first."));
    }

    private record SeedUser(
            String email,
            String password,
            String fullName,
            String phone,
            Integer grade,
            LocalDate dateOfBirth,
            GenderType gender,
            Role role
    ) {
    }
}
