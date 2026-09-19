package com.example.exe101_bioverse.exam.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Entity đại diện cho khối lớp học (Lớp 6, 7, 8, 9).
 * Map với bảng "exam_classes".
 */
@Entity
@Table(name = "grades")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name; // Ví dụ: "Lớp 6", "Lớp 7", "Lớp 8", "Lớp 9"

    @Column(nullable = false)
    private Integer grade; // 6, 7, 8, 9

    @Column(columnDefinition = "TEXT")
    private String description;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    @OneToMany(mappedBy = "grade", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Semester> semesters;

    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
        updatedDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedDate = LocalDateTime.now();
    }
}
