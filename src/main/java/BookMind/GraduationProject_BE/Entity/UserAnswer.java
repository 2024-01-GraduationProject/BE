package BookMind.GraduationProject_BE.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "user_answers")
@Getter
@Setter
@NoArgsConstructor
public class UserAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long bookId;

    @Lob
    private String question;

    @Lob
    private String answer;

    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;

    public UserAnswer(Long userId, Long bookId, String question, String answer) {
        this.userId = userId;
        this.bookId = bookId;
        this.question = question;
        this.answer = answer;
    }
}
