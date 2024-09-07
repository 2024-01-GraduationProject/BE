package BookMind.GraduationProject_BE.Repository;

import BookMind.GraduationProject_BE.Entity.UserAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAnswerRepository extends JpaRepository<UserAnswer, Long> {
    List<UserAnswer> findAllByUserIdAndBookId(Long userId, Long bookId);
}
