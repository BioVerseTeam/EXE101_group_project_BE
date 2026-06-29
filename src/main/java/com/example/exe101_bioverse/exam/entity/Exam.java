package com.example.exe101_bioverse.exam.entity;
import com.example.exe101_bioverse.exam.enums.ExamType;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
@Entity
@Table(name = "exam")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    @Enumerated(EnumType.STRING)
    private ExamType type;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    private String name;

    private String subjectName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExamQuestion> examQuestions;
}
