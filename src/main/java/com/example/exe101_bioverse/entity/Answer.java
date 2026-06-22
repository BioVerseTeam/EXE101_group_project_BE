package com.example.exe101_bioverse.entity;
import com.example.exe101_bioverse.enums.AnswerType;
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

    @Column(name = "explanation")
    private String explain;

    private String description;

    private boolean isCorrect;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    @OneToMany(mappedBy = "answer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AnswerImage> answerImages;
}
