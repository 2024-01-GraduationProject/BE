package BookMind.GraduationProject_BE.Repository;

import BookMind.GraduationProject_BE.DTO.BookDTO;
import BookMind.GraduationProject_BE.Entity.Book;
import BookMind.GraduationProject_BE.Entity.UserBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserBookRepository extends JpaRepository<UserBook, String> {
    Optional<UserBook> findByUserIdAndBookId(Long userId, Long bookId);

    List<UserBook> findAllByUserIdAndStatus(Long userId, UserBook.Status status);

    List<UserBook> findAllByUserIdAndFavoriteTrue(Long userId);

    // 여러 개의 userId를 통해서 UserBook 조회
    List<UserBook> findByUserIdIn(List<Long> userIds);

    // DB 상에 가장 많이 저장된 book_id를 조회, Native Query 사용
    @Query(value = "select book_id from user_book group by book_id order by count(*) desc limit 4", nativeQuery = true)
    List<Long> findMostReadBookIds();

    // user의 userBook 데이터를 userId를 통해서 조회
    List<UserBook> findByUserId(Long userId);
}
