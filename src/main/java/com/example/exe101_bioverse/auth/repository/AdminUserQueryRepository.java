package com.example.exe101_bioverse.auth.repository;

import com.example.exe101_bioverse.auth.dto.response.UserResponse;
import com.example.exe101_bioverse.auth.enums.GenderType;
import com.example.exe101_bioverse.auth.enums.UserStatus;
import com.example.exe101_bioverse.common.response.PageResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Admin user list via JDBC + CAST(... AS varchar).
 * Avoids Hibernate named-parameter parsing of PostgreSQL {@code ::text} and enum/varchar JPQL mismatches.
 */
@Repository
public class AdminUserQueryRepository {

    private static final String FROM = """
            FROM users u
            LEFT JOIN roles r ON r.id = u.role_id
            """;

    private static final String SELECT = """
            SELECT u.id, u.email, u.full_name, u.phone, u.grade, u.avatar_url,
                   u.date_of_birth, CAST(u.gender AS varchar) AS gender,
                   r.code AS role, CAST(u.status AS varchar) AS status,
                   u.email_verified, u.last_login_at, u.created_at
            """ + FROM;

    private static final RowMapper<UserResponse> ROW_MAPPER = AdminUserQueryRepository::mapRow;

    private final JdbcTemplate jdbcTemplate;

    public AdminUserQueryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public PageResponse<UserResponse> search(String q, UserStatus status, String role, Pageable pageable) {
        StringBuilder where = new StringBuilder(" WHERE 1=1 ");
        List<Object> args = new ArrayList<>();

        if (status != null) {
            where.append(" AND CAST(u.status AS varchar) = ? ");
            args.add(status.name());
        }
        if (role != null && !role.isBlank()) {
            where.append(" AND r.code = ? ");
            args.add(role.trim());
        }
        if (q != null && !q.isBlank()) {
            where.append(" AND (LOWER(u.email) LIKE ? OR LOWER(u.full_name) LIKE ?) ");
            String like = "%" + q.trim().toLowerCase(Locale.ROOT) + "%";
            args.add(like);
            args.add(like);
        }

        Long total = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) " + FROM + where,
                Long.class,
                args.toArray()
        );
        long totalElements = total == null ? 0L : total;

        int size = Math.min(Math.max(pageable.getPageSize(), 1), 100);
        int page = Math.max(pageable.getPageNumber(), 0);
        long offset = (long) page * size;

        List<Object> pageArgs = new ArrayList<>(args);
        pageArgs.add(size);
        pageArgs.add(offset);

        List<UserResponse> items = totalElements == 0
                ? List.of()
                : jdbcTemplate.query(
                        SELECT + where + " ORDER BY u.created_at DESC, u.id DESC LIMIT ? OFFSET ?",
                        ROW_MAPPER,
                        pageArgs.toArray()
                );

        int totalPages = size == 0 ? 0 : (int) Math.ceil((double) totalElements / size);
        return PageResponse.<UserResponse>builder()
                .items(items)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .page(page)
                .size(size)
                .build();
    }

    private static UserResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserStatus status = parseEnum(rs.getString("status"), UserStatus.class);
        return UserResponse.builder()
                .id(rs.getLong("id"))
                .email(rs.getString("email"))
                .fullName(rs.getString("full_name"))
                .phone(rs.getString("phone"))
                .grade(rs.getObject("grade", Integer.class))
                .avatarUrl(rs.getString("avatar_url"))
                .dateOfBirth(rs.getObject("date_of_birth", LocalDate.class))
                .gender(parseEnum(rs.getString("gender"), GenderType.class))
                .role(rs.getString("role"))
                .status(status != null ? status : UserStatus.ACTIVE)
                .emailVerified(rs.getBoolean("email_verified"))
                .lastLoginAt(rs.getObject("last_login_at", LocalDateTime.class))
                .createdAt(rs.getObject("created_at", LocalDateTime.class))
                .build();
    }

    private static <E extends Enum<E>> E parseEnum(String value, Class<E> type) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return Enum.valueOf(type, value.trim());
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }
}
