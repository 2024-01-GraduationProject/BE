package BookMind.GraduationProject_BE.Repository;

import BookMind.GraduationProject_BE.Entity.BookCategoryConnection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookCategoryConnectionRepository extends JpaRepository<BookCategoryConnection, Long> {

    // bookId에 해당하는 모든 categoryId 리스트를 반환
    @Query("SELECT b.categoryId FROM BookCategoryConnection b WHERE b.bookId = :bookId")
    List<String> findCategoryIdsByBookId(@Param("bookId") Long bookId);
}

