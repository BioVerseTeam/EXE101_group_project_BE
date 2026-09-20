package com.example.exe101_bioverse.exam.entity;
import com.example.exe101_bioverse.exam.enums.AnswerType;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
@Entity
@Table(name = "answer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private AnswerType type;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    @Column(name = "explanation", columnDefinition = "TEXT")
    private String explain;

    @Column(columnDefinition = "TEXT")
    private String description;

    private boolean isCorrect;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    @OneToMany(mappedBy = "answer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AnswerImage> answerImages;
}
