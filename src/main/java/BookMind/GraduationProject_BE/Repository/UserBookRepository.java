package BookMind.GraduationProject_BE.Repository;

import BookMind.GraduationProject_BE.Entity.UserBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBookRepository extends JpaRepository<UserBook, Long> {
}
