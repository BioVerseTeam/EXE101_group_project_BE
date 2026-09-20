package com.example.exe101_bioverse.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnTransformer;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bio_models")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BioModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lesson_id")
    private Long lessonId;

    @Column(nullable = false)
    private String name;

    @Column(name = "name_en")
    private String nameEn;

    @Column(name = "scientific_name")
    private String scientificName;

    @Column(length = 100)
    private String category;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String habitat;

    @Column(columnDefinition = "TEXT")
    private String characteristics;

    @Column(columnDefinition = "jsonb")
    @ColumnTransformer(read = "cast(classification as text)", write = "?::jsonb")
    private String classification;

    @Column(name = "fun_facts", columnDefinition = "jsonb")
    @ColumnTransformer(read = "cast(fun_facts as text)", write = "?::jsonb")
    private String funFacts;

    // 3D asset files
    @Column(name = "model_url", length = 500)
    private String modelUrl;

    @Column(name = "thumbnail_url", length = 500)
    private String thumbnailUrl;

    @Column(name = "model_format", length = 20)
    @Builder.Default
    private String modelFormat = "glb";

    @Column(name = "model_size_bytes")
    private Long modelSizeBytes;

    // 3D display settings
    @Column(name = "default_scale", precision = 6, scale = 3)
    @Builder.Default
    private BigDecimal defaultScale = BigDecimal.ONE;

    @Column(name = "default_rotation", columnDefinition = "jsonb")
    @ColumnTransformer(read = "cast(default_rotation as text)", write = "?::jsonb")
    private String defaultRotation;

    @Column(name = "camera_position", columnDefinition = "jsonb")
    @ColumnTransformer(read = "cast(camera_position as text)", write = "?::jsonb")
    private String cameraPosition;

    @Column(columnDefinition = "jsonb")
    @ColumnTransformer(read = "cast(annotations as text)", write = "?::jsonb")
    private String annotations;

    // V14 fields
    @Column(unique = true)
    private String slug;

    @Column(name = "is_featured", nullable = false)
    @Builder.Default
    private Boolean isFeatured = false;

    @Column
    private Integer grade;

    @Column(length = 50)
    @Builder.Default
    private String subject = "BIOLOGY";

    @Column(name = "badge_text", length = 100)
    private String badgeText;

    @Column(name = "action_text", length = 100)
    @Builder.Default
    private String actionText = "Khám phá ngay";

    @Column(name = "action_icon", length = 50)
    @Builder.Default
    private String actionIcon = "3d_rotation";

    @Column(name = "target_mode", length = 100)
    private String targetMode;

    @Column(name = "views_count", nullable = false)
    @Builder.Default
    private Long viewsCount = 0L;

    // Common fields
    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "sort_order", nullable = false)
    @Builder.Default
    private Integer sortOrder = 0;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
