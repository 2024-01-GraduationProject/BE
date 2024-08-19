package BookMind.GraduationProject_BE.Repository;

import BookMind.GraduationProject_BE.Entity.UserBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserBookRepository extends JpaRepository<UserBook, String> {
    Optional<UserBook> findByUserIdAndBookId(Long userId, Long bookId);
}
