package BookMind.GraduationProject_BE.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserAnswerDTO {

    private Long userId;
    private Long bookId;
    private String question;
    private String answer;
    private String createdAt;

    public UserAnswerDTO(Long userId, Long bookId, String question, String answer, String createdAt) {
        this.userId = userId;
        this.bookId = bookId;
        this.question = question;
        this.answer = answer;
        this.createdAt = createdAt;
    }
}
