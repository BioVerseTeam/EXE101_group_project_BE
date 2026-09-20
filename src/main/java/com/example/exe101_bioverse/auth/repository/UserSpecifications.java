package com.example.exe101_bioverse.auth.repository;

import com.example.exe101_bioverse.auth.entity.User;
import com.example.exe101_bioverse.auth.enums.UserStatus;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class UserSpecifications {

    private UserSpecifications() {}

    public static Specification<User> adminList(String q, UserStatus status, String roleCode) {
        return (root, query, cb) -> {
            if (query != null
                    && query.getResultType() != null
                    && query.getResultType() != Long.class
                    && query.getResultType() != long.class) {
                root.fetch("role", JoinType.LEFT);
                query.distinct(true);
            }
            List<Predicate> predicates = new ArrayList<>();
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            if (roleCode != null && !roleCode.isBlank()) {
                predicates.add(cb.equal(root.get("role").get("code"), roleCode.trim()));
            }
            if (q != null && !q.isBlank()) {
                String like = "%" + q.trim().toLowerCase(Locale.ROOT) + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("email")), like),
                        cb.like(cb.lower(root.get("fullName")), like)
                ));
            }
            return predicates.isEmpty()
                    ? cb.conjunction()
                    : cb.and(predicates.toArray(Predicate[]::new));
        };
    }
}
