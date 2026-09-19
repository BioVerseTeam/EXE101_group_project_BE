package com.example.exe101_bioverse.exam.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Entity đại diện cho môn học theo từng học kỳ (Khoa học Tự nhiên, Sinh học, Hóa học, Vật lý...).
 * Quan hệ:
 * - Semester 1 - N Subject (Subject N - 1 Semester)
 * - Subject 1 - N Exam
 */
@Entity
@Table(name = "exam_subjects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name; // Ví dụ: "Khoa học Tự nhiên", "Sinh học", "Hóa học", "Vật lý"

    private String code; // Ví dụ: "KHTN6", "BIO7"

    @Column(columnDefinition = "TEXT")
    private String description;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "semester_id", nullable = false)
    private Semester semester;

    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Exam> exams;

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
